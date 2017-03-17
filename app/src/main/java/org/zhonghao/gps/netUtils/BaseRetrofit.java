package org.zhonghao.gps.netUtils;

import org.zhonghao.gps.utils.Urls;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2017/3/15.
 */
//封装成单例模式，网络请求
public class BaseRetrofit {
    private static BaseRetrofit baseRetrofit=null;
    private static Retrofit retrofit=null;
    public static Retrofit getInstance(){
            return retrofit;

    }
    public static BaseRetrofit init(){
        if(baseRetrofit==null){
            baseRetrofit=new BaseRetrofit();
        }
        return baseRetrofit;
    }
    private BaseRetrofit(){
        retrofit=new Retrofit
                .Builder()
                .baseUrl(Urls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
