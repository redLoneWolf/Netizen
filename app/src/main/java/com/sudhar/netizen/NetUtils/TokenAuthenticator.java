package com.sudhar.netizen.NetUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sudhar.netizen.SharedPreferenceHelper;
import com.sudhar.netizen.models.TokenResponse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class TokenAuthenticator implements Authenticator {
    private static String URL;
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final int REFRESH_TOKEN_FAIL = 401;
    private RestInterface restInterface;
    private Context context;
    static SharedPreferenceHelper sharedPreferenceHelper;

    public TokenAuthenticator(Context context, String URL) {
        this.context = context;
        this.URL = URL;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {

        sharedPreferenceHelper = new SharedPreferenceHelper(context);

        if (response.request().header(HEADER_AUTHORIZATION) != null) {


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            restInterface = retrofit.create(RestInterface.class);
            Log.d("HTTP FAILED", "intercept: " + response.code() + " " + response.message());

            Call<TokenResponse> call = restInterface.refresh(sharedPreferenceHelper.getRefreshToken());


            String token = sharedPreferenceHelper.getAccessToken();


            try {
                retrofit2.Response<TokenResponse> tokenResponseResponse = call.execute();
                TokenResponse tokenResponse = tokenResponseResponse.body();


                sharedPreferenceHelper.setAccessToken(tokenResponse.getAccessToken());

                token = tokenResponse.getAccessToken();
            } catch (IOException e) {
                Toast.makeText(context, "network failure :(", Toast.LENGTH_SHORT).show();
            }


            return response.request()
                    .newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();


        }

        return null;

    }


}
