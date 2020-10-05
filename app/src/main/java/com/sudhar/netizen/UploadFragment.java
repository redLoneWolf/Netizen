package com.sudhar.netizen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.models.MemeDetailModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFragment extends Fragment implements ProgressRequestBody.UploadCallbacks {

    private static final String TAG = "UploadFragment";
    private static final int REQUEST_CODE = 101;
    private ContentType contentType;
    private ImageView mImagePreview, mProfilePic;
    private TextView mUsername;
    private TextInputEditText mDescription;
    private MaterialButton mCancelBtn, mPostBtn, mAddMoreBtn;

    private RestInterface restInterface;
    List<Uri> mImagesList = new ArrayList<>();
    ProgressDialog pDialog;
    private RecyclerView rv;
    private ImageAdapter imageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);


        mUsername = view.findViewById(R.id.Username);
        mProfilePic = view.findViewById(R.id.ProfilePic);
        mDescription = view.findViewById(R.id.Description);
        mCancelBtn = view.findViewById(R.id.CancelBtn);
        mPostBtn = view.findViewById(R.id.PostBtn);
        mAddMoreBtn = view.findViewById(R.id.AddMoreBtn);
        rv = view.findViewById(R.id.ImagePreviewRV);

        mAddMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                startActivityForResult(Intent.createChooser(chooserIntent, "Select images"), REQUEST_CODE);
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        UploadFragmentArgs args = UploadFragmentArgs.fromBundle(getArguments());

        restInterface = RetrofitClient.getClient(getContext()).create(RestInterface.class);

        contentType = args.getContentType();
        List<Uri> temp = new ArrayList<>();

        for (Uri imageUri : Arrays.asList(args.getImageUris())) {
            if (imageUri != null) {
                temp.add(imageUri);
            }
        }


        imageAdapter = new ImageAdapter();
        imageAdapter.addAll(temp);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rv.setLayoutManager(layoutManager);
        rv.hasFixedSize();
        rv.setAdapter(imageAdapter);


        switch (contentType) {
            case MEME:
                mPostBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.hideSoftKeyboard(getActivity());


                        uploadMemes(imageAdapter.getImageList(), mDescription.getText().toString());

                    }
                });
                break;
            case TEMPLATE:
                mPostBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.hideSoftKeyboard(getActivity());
//                        uploadTemplate(images, mDescription.getText().toString());
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Uri> temp = new ArrayList<>();


        if (resultCode == Activity.RESULT_OK && data != null && requestCode == REQUEST_CODE) {

            ClipData clipData = data.getClipData();

            if (clipData != null) {
                int count = clipData.getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    temp.add(imageUri);
                }

            } else {

                temp.add(data.getData());

            }

            imageAdapter.addAll(temp);
        }

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


    private MultipartBody.Part prepareFilePart(String name, Uri fileUri) {

        String realPath = ImagePath.getPath(getContext(), fileUri);
        File file = new File(realPath);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/jpg"));

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(name, file.getName(), requestFile);
    }

    private void uploadMemes(List<Uri> fileUris, String Description) {

        List<MultipartBody.Part> list = new ArrayList<>();

        for (Uri fileUri : fileUris) {


            //very important files[]
            MultipartBody.Part imageRequest = prepareFilePart("images", fileUri);
            list.add(imageRequest);
        }

        RequestBody description = RequestBody.create(Description, MediaType.parse("text/plain"));

        Call<MemeDetailModel> call = restInterface.uploadMeme(description, list);

        call.enqueue(new Callback<MemeDetailModel>() {
            @Override
            public void onResponse(Call<MemeDetailModel> call, Response<MemeDetailModel> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");
                    Log.d("id", response.errorBody().toString());
                    Log.d("id", response.headers().toString());

                    Log.d("call", call.request().body().toString());
                    return;
                }
                MemeDetailModel body = response.body();


                UploadFragmentDirections.ActionUploadFragmentToViewerFragment action = UploadFragmentDirections.actionUploadFragmentToViewerFragment(body.getId(), ContentType.MEME);
                Navigation.findNavController(getView()).navigate(action);
            }

            @Override
            public void onFailure(Call<MemeDetailModel> call, Throwable t) {
                Log.d(TAG, "Failure" + t.getMessage());

                Toast.makeText(getContext(), "upload Failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ImageAdapter extends RecyclerView.Adapter {


        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ImageVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_grid_for_upload, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Uri item = mImagesList.get(position);
            ImageVH imageVH = (ImageVH) holder;
            imageVH.mImageView.setImageURI(item);
        }

        @Override
        public int getItemCount() {
            return mImagesList.size();
        }

        private void add(Uri uri) {
            mImagesList.add(uri);
            notifyDataSetChanged();
        }

        private void remove(Uri uri) {
            mImagesList.remove(uri);
            notifyDataSetChanged();
        }

        private void append(Uri uri) {
            mImagesList.add(getItemCount(), uri);
            notifyItemInserted(getItemCount());
        }

        private List<Uri> getImageList() {
            return mImagesList;
        }

        private void addAll(List<Uri> uris) {
            for (Uri uri : uris) {
                add(uri);
            }

            Log.d(TAG, "addAll: " + mImagesList.size());
        }

        private class ImageVH extends RecyclerView.ViewHolder {
            ImageView mImageView;
            ImageButton mRemoveButton;

            public ImageVH(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.imageViewtemp);
                mRemoveButton = itemView.findViewById(R.id.RemoveBtn);
                mRemoveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(mImagesList.get(getLayoutPosition()));
                    }
                });
            }
        }
    }

}
