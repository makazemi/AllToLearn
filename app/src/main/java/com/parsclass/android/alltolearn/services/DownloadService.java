package com.parsclass.android.alltolearn.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.remote.APIInterface;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.CHANNEL_ID_DOWNLOAD;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.KEY_DOWNLOAD_ID_DOC;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.KEY_FINE_NAME_DOC;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.KEY_URL_DOC;

public class DownloadService extends IntentService {



    public DownloadService(Context context)
    {
        super("Download Service");
        this.context=context;
    }

    public DownloadService() {
        super("Download Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;
    private String url;
    private String fileName;
    private String downloadId;
    private Download download;
    private Context context;
    public static final String TAG="DownloadService";

    @Override
    protected void onHandleIntent(Intent intent) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID_DOWNLOAD)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());


        url=intent.getStringExtra(KEY_URL_DOC);
        fileName=intent.getStringExtra(KEY_FINE_NAME_DOC);
        downloadId=intent.getStringExtra(KEY_DOWNLOAD_ID_DOC);

        download=new Download();
        download.setDownloadId(downloadId);
        initDownload();

    }

    private void initDownload(){

        APIInterface retrofitInterface=null;
        Retrofit retrofit;
        Call<ResponseBody> request=null;
        if(url!=null){

             retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .build();
             retrofitInterface = retrofit.create(APIInterface.class);
            request = retrofitInterface.downloadFile();
        }


       // APIInterface retrofitInterface = retrofit.create(APIInterface.class);

       // Call<ResponseBody> request = retrofitInterface.downloadFile();

//        request.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.e(TAG,"onResponse: ");
//                if(response.body()!=null)
//                Log.e(TAG,"onResponse: "+response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e(TAG,"DownloadFailed");
//            }
//        });
        try {
            if(request!= null) {
                downloadFile(request.execute().body());
            }

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.e(TAG,"DownloadFailed: "+e.getMessage());
            download.setFailDownload(true);
            sendNotification(download);

        }
    }

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);

//        int index=path.indexOf('.');
//        String extension=path.substring(index);

       // File outputFile =  new File(context.getExternalFilesDir(null), DOWNLOAD_DOCUMENT_CONTENT_DIRECTORY);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
//        File path=MyApplication.getInstance().getDownloadDirectory();
//        File outputFile=new File(path,fileName);

        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            //Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }

    private void sendNotification(Download download){

        sendIntent(download);
        if(download.isFailDownload()){
            notificationBuilder.setContentText(getString(R.string.failed_doc_download));
        }else {
            notificationBuilder.setProgress(100, download.getProgress(), false);
            notificationBuilder.setContentText("Downloading file " + download.getCurrentFileSize() + "/" + totalFileSize + " MB");
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download){

        Intent intent = new Intent(ConstantUtil.MESSAGE_PROGRESS);
        intent.putExtra("download",download);
        Log.e(TAG,"progerss: "+download.getProgress());
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(){

        //Download download = new Download();
        download.setProgress(100);
        sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("File Downloaded");
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}

