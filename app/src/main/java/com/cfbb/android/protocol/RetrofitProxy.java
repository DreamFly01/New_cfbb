package com.cfbb.android.protocol;

import android.content.Context;

import com.cfbb.android.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * @author MrChang
 *         created  at  2016/2/24.
 * @description kernel congfig
 */
public class RetrofitProxy {

    private static APIService apiService = null;
    private static Retrofit retrofit = null;
    private static OkHttpClient mOkHttpClient = null;


    /**
     * initialize
     */
    public static void init(final Context context) {

        Executor executor = Executors.newCachedThreadPool();
        Gson gson = new GsonBuilder().create();

        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(5, TimeUnit.MINUTES);
        mOkHttpClient.setWriteTimeout(1, TimeUnit.MINUTES);
        mOkHttpClient.setReadTimeout(1, TimeUnit.MINUTES);

        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        if(BuildConfig.DEBUG)
        {
            mOkHttpClient.networkInterceptors().add(new LogInterceptor());
        }

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))// 使用Gson作为数据转换器
                .baseUrl(APIService.HOST)
                .callbackExecutor(executor)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 使用RxJava作为回调适配器
                .build();

        //connect with requestApi
        apiService = retrofit.create(APIService.class);
    }


    public static Retrofit getRetrofit(Context context) {
        if (retrofit != null) return retrofit;
        init(context);
        return getRetrofit(context);
    }

    public static APIService getApiService(Context context) {
        if (apiService != null) return apiService;
        init(context);
        return getApiService(context);
    }
}
