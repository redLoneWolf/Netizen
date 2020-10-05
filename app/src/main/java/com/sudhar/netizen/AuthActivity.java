package com.sudhar.netizen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.models.TokenResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener {
    final String TAG = "AuthActivity";
    private RestInterface restInterface;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        preferences = getSharedPreferences("Netizen", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        preferences = getSharedPreferences("Netizen", Context.MODE_PRIVATE);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()

                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        restInterface = retrofit.create(RestInterface.class);
    }

    private void login(String email, String password) {
        Call<TokenResponse> call = restInterface.login(email, password);

        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (!response.isSuccessful()) {

                    Toast.makeText(AuthActivity.this, "Error response" + response.code(), Toast.LENGTH_SHORT).show();
                    return;

                }

                TokenResponse loginResponse = response.body();


                assert loginResponse != null;
                preferences.edit().putString("Access", loginResponse.getAccessToken()).apply();
                preferences.edit().putString("Refresh", loginResponse.getRefreshToken()).apply();

                Toast.makeText(AuthActivity.this, "Access granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

                Toast.makeText(AuthActivity.this, "Login Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onLoginFragmentInteraction(String email, String password) {
        login(email, password);


    }

    private void signUp(String email, String email2, String username, String password) {
        Call<TokenResponse> call = restInterface.signUp(email, email2, username, password);

        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (!response.isSuccessful()) {
//                    Log.e(Tag,"Code" + response.code());
                    Toast.makeText(AuthActivity.this, "Error response" + response.code(), Toast.LENGTH_SHORT).show();
                    return;

                }
                TokenResponse loginResponse = response.body();


                preferences.edit().putString("Access", loginResponse.getAccessToken()).apply();
                preferences.edit().putString("Refresh", loginResponse.getRefreshToken()).apply();

                Toast.makeText(AuthActivity.this, "Access granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

                Toast.makeText(AuthActivity.this, "signup Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onSignUpFragmentInteraction(String email, String email2, String username, String password) {
        signUp(email, email2, username, password);
    }
}