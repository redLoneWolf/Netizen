package com.sudhar.netizen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.models.User;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserMenuFragment extends Fragment implements ProgressRequestBody.UploadCallbacks {
    private final String TAG = "UserMenuFragment";

    private UserMenuRVAdapter.OnItemSelected mOnItemSelected;

    TextInputLayout mUsernameFieldLayout, mAboutFieldLayout;
    TextInputEditText mUsernameField, mAboutField;
    RestInterface restInterface;
    MaterialButton mDeleteAccountBtn, mChangePasswordBtn;
    ShapeableImageView mProfilePic;
    private static final int PICK_REQUEST = 10;
    private User user;
    ProgressDialog pDialog;

    public UserMenuFragment() {
    }


    public static UserMenuFragment newInstance() {
        UserMenuFragment fragment = new UserMenuFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restInterface = RetrofitClient.getClient(getContext()).create(RestInterface.class);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_menu_list, container, false);
        FragmentManager fm = getChildFragmentManager();

        mProfilePic = view.findViewById(R.id.profile_pic);

        mUsernameFieldLayout = view.findViewById(R.id.UsernameFieldLayout);
        mUsernameField = view.findViewById(R.id.UsernameField);

        mAboutFieldLayout = view.findViewById(R.id.AboutFieldLayout);
        mAboutField = view.findViewById(R.id.AboutField);

        mDeleteAccountBtn = view.findViewById(R.id.DeleteAccountBtn);
        mChangePasswordBtn = view.findViewById(R.id.ChangePasswordBtn);

        mUsernameField.setEnabled(false);
        mAboutField.setEnabled(false);

        mUsernameFieldLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mUsernameField.setEnabled(true);
                ChangeDialogFragment mChangeDialogFragment = ChangeDialogFragment.newInstance();
                mChangeDialogFragment.setOnChangeListener(new OnchangeListener());
                mChangeDialogFragment.setUsername(mUsernameField.getText().toString());
                mChangeDialogFragment.show(fm, mChangeDialogFragment.getTag());


            }
        });

        mAboutFieldLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDialogFragment mChangeDialogFragment = ChangeDialogFragment.newInstance();
                mChangeDialogFragment.setOnChangeListener(new OnchangeListener());

                mChangeDialogFragment.setAbout(mAboutField.getText().toString());
                mChangeDialogFragment.show(fm, mChangeDialogFragment.getTag());
            }
        });


        mChangePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_userMenuFragment_to_changePasswordFragment);
            }
        });

        mDeleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccountDialogFragment mDeleteAccountDialogFragment = DeleteAccountDialogFragment.newInstance();

                mDeleteAccountDialogFragment.show(fm, mDeleteAccountDialogFragment.getTag());
            }
        });

        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(getContext())
                        .setPositiveButton("Open gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getImage();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        return view;
    }

    private void getImage() {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(Intent.createChooser(chooserIntent, "Select Picture"), PICK_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case PICK_REQUEST:
                    Uri uri = data.getData();
//                    mProfilePic.setImageURI(uri);
                    uploadProfilePicture(uri);
                    Toast.makeText(getContext(), "uploaded", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private class OnchangeListener implements ChangeDialogFragment.OnChangeListener {


        @Override
        public void onChanged(String username, String about) {
            mUsernameField.setText(username);
            mUsernameField.setEnabled(false);
            mAboutField.setText(about);
            mAboutField.setEnabled(false);
        }
    }

    void getData() {

        Call<User> call = restInterface.getMe();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");

                    return;
                }
                user = response.body();
                setData();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    private void setData() {
        mUsernameField.setText(user.getUsername());
        mAboutField.setText(user.getAbout());
    }

    private void uploadProfilePicture(Uri fileUri) {
        pDialog = new ProgressDialog(getContext());
        showProgress("uploading");
        String realPath = ImagePath.getPath(getContext(), fileUri);
        File file = new File(realPath);

        ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
//        RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/jpg"));

        MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), fileBody);


        Call<JsonObject> call = restInterface.uploadProfilePic(body);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "profile pic upload Error");

                    return;
                }
                JsonObject memeDetailModel = response.body();

                JsonObject jsonObject = response.body();

                String profile_pic_url = jsonObject.get("profile_pic").getAsString();

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round);
                Glide.with(getContext()).load(profile_pic_url).apply(options).into(mProfilePic);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "Failure" + t.getMessage());

                Toast.makeText(getContext(), "upload Failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateProgress(int val, String title, String msg) {
        pDialog.setTitle(title);
        pDialog.setMessage(msg);
        pDialog.setProgress(val);
    }

    public void showProgress(String str) {
        try {
            pDialog.setCancelable(false);
            pDialog.setTitle("Please wait");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setMax(100); // Progress Dialog Max Value
            pDialog.setMessage(str);
            if (pDialog.isShowing())
                pDialog.dismiss();
            pDialog.show();
        } catch (Exception e) {

        }
    }

    public void hideProgress() {
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception e) {

        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        updateProgress(percentage, "uploding", "hello");
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        hideProgress();
    }
}