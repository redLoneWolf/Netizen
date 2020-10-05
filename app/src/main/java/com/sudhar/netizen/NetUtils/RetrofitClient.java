package com.sudhar.netizen.NetUtils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sudhar.netizen.SharedPreferenceHelper;
import com.sudhar.netizen.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.OffsetDateTime;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    static SharedPreferenceHelper sharedPreferenceHelper;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Retrofit getClient(Context context) {


        sharedPreferenceHelper = new SharedPreferenceHelper(context);


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gSonInstance = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(OffsetDateTime.class, new Utils.OffsetDateTimeDeserializer())
                .registerTypeAdapter(OffsetDateTime.class, new Utils.OffsetDateTimeSerializer())
                .create();

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .authenticator(new TokenAuthenticator(context, RestInterface.BASE_URL))
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {


                        Request newRequest = chain.request().newBuilder()
                                .header("Authorization", "Bearer " + sharedPreferenceHelper.getAccessToken())
                                .build();


                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gSonInstance))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(httpClient)
                .build();
        return retrofit;
    }


}
