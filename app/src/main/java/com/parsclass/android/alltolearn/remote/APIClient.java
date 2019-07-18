package com.parsclass.android.alltolearn.remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static final String BASE_URL ="http://api.sarvandishan.com/";
    public static final String BASE_URL2 ="https://newsapi.org/v2/";
    private static Retrofit retrofit=null;
    private static Retrofit retrofit2=null;

    public static Retrofit getClient(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient2(){
        if(retrofit2==null){
            retrofit2=new Retrofit.Builder()
                    .baseUrl(BASE_URL2)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }
}
