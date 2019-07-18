package com.parsclass.android.alltolearn.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.LocalManager;
import com.parsclass.android.alltolearn.adapter.StartVPAdapter;
import com.parsclass.android.alltolearn.base.BaseActivity;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.model.VPItem;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import me.relex.circleindicator.CircleIndicator;

public class StartActivity extends BaseActivity {

    private static final String TAG ="StartActivity" ;
    private ArrayList<VPItem> items;
    private ViewPager viewPager;
    private StartVPAdapter adapter;
    private CircleIndicator circleIndicator;
    private Button btnBrowse,btnSign;
    private ImageView imgLang;
    private TextView txtLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MyApplication.prefHelper.isUserLogin()) {
            launchHomeScreen();
            finish();
        }
        setContentView(R.layout.activity_start);

        btnBrowse=findViewById(R.id.btnBrowse);
        btnSign=findViewById(R.id.btnSing);

        items=new ArrayList<>();
        viewPager=findViewById(R.id.view_pager);
        adapter=new StartVPAdapter(items);
        viewPager.setAdapter(adapter);
        circleIndicator =findViewById(R.id.Indicator);
        circleIndicator.setViewPager(viewPager);
        adapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        items.add(new VPItem(R.drawable.img_background_intro1,getString(R.string.title_item_1),getString(R.string.desc_item_1)));
        items.add(new VPItem(R.drawable.img_background_intro2,getString(R.string.title_item_1),getString(R.string.desc_item_1)));
        items.add(new VPItem(R.drawable.img_background_intro3,getString(R.string.title_item_1),getString(R.string.desc_item_1)));
        items.add(new VPItem(R.drawable.img_background_intro4,getString(R.string.title_item_1),getString(R.string.desc_item_1)));

        adapter.notifyDataSetChanged();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark2));
        }

        imgLang=findViewById(R.id.select_lang);

    }

    public void browse(View view){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void sign(View view){
        Intent intent=new Intent(this,SigningActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchHomeScreen(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void changeLang(View view){
        //Context context=ViewPumpContextWrapper.wrap( super.getBaseContext());

        MyApplication.getInstance().chooseDialog(this);
    }


    @Override
    public void onBackPressed() {
        Log.e(TAG,"onbackpress");
        finish();
        super.onBackPressed();
    }


}
