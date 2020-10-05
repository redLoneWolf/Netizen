package com.sudhar.netizen;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.db.AppDatabase;
import com.sudhar.netizen.models.Comment;
import com.sudhar.netizen.models.PagedResponse;
import com.sudhar.netizen.models.User;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";


    SharedPreferences preferences;
    NavController navController;
    SharedPreferenceHelper sharedPreferenceHelper;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("Netizen", MODE_PRIVATE);
        sharedPreferenceHelper = new SharedPreferenceHelper(this);

        if (!preferences.contains("Access") || !preferences.contains("Refresh")) {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_main);


//For First Start-----------------------------------

            boolean firstStart = preferences.getBoolean("firstStart", true);
            if (firstStart) {
                copyAsset("stickers");
                Toast.makeText(MainActivity.this, "Copying Assets", Toast.LENGTH_SHORT).show();
                File dir = new File(Environment.getExternalStorageDirectory(), "font");
                dir.mkdirs();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstStart", false);
                editor.apply();
            }

//--------------------------------------------------

            final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);


            navController = Navigation.findNavController(this, R.id.nav_host_fragment);

            NavigationUI.setupWithNavController(bottomNavigationView, navController);


            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


            getMe();

        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getMe() {
        Call<User> call = RetrofitClient.getClient(this).create(RestInterface.class).getMe();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");

                    return;
                }
                User user = response.body();
                sharedPreferenceHelper.setUsername(user.getUsername());
                sharedPreferenceHelper.setUserId(user.getId());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure login", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String permissions[], @NotNull int[] grantResults) {
        switch (requestCode) {

            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void copyAsset(String path) {
        AssetManager manager = getAssets();

        // If we have a directory, we make it and recurse. If a file, we copy its
        // contents.
        try {
            String[] contents = manager.list(path);

            // The documentation suggests that list throws an IOException, but doesn't
            // say under what conditions. It'd be nice if it did so when the path was
            // to a file. That doesn't appear to be the case. If the returned array is
            // null or has 0 length, we assume the path is to a file. This means empty
            // directories will get turned into files.
            if (contents == null || contents.length == 0)
                throw new IOException();

            // Make the directory.
            File dir = new File(Environment.getExternalStorageDirectory(), path);
            dir.mkdirs();

            // Recurse on the contents.
            for (String entry : contents) {
                copyAsset(path + "/" + entry);
            }
        } catch (IOException e) {
            copyFileAsset(path);
        }
    }

    /**
     * Copy the asset file specified by path to app's data directory. Assumes
     * parent directories have already been created.
     *
     * @param path Path to asset, relative to app's assets directory.
     */
    private void copyFileAsset(String path) {
        File file = new File(Environment.getExternalStorageDirectory(), path);
        try {
            InputStream in = getAssets().open(path);
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
            out.close();
            in.close();
        } catch (IOException e) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                final MainDao userDao = appDatabase.mainDao();

                appDatabase.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        userDao.deleteImages();
                        userDao.deleteMemes();
                        userDao.deleteComments();

//                        Glide.get(getApplicationContext()).clearDiskCache();
                    }
                });

            }
        });
    }
}




