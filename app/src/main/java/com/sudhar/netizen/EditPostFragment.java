package com.sudhar.netizen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.models.ImageModel;
import com.sudhar.netizen.models.MemeDetailModel;
import com.sudhar.netizen.models.TemplateDetailModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostFragment extends Fragment {
    private static final String TAG = "EditPostFragment";
    private ContentType contentType;
    private ImageView mProfilePic;
    private TextView mUsername;
    private TextInputEditText mDescription;
    private MaterialButton mCancelBtn, mPostBtn;

    private RestInterface restInterface;

    ProgressDialog pDialog;

    private MemeDetailModel memeDetailModel;
    private TemplateDetailModel templateDetailModel;
    private RecyclerView rv;
    private ImageAdapter imageAdapter;
    RequestOptions options;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);


        mUsername = view.findViewById(R.id.Username);
        mProfilePic = view.findViewById(R.id.ProfilePic);
        mDescription = view.findViewById(R.id.Description);
        mCancelBtn = view.findViewById(R.id.CancelBtn);
        mPostBtn = view.findViewById(R.id.PostBtn);
        rv = view.findViewById(R.id.ImagePreviewRV);
        MaterialButton mAddMoreBtn = view.findViewById(R.id.AddMoreBtn);
        mAddMoreBtn.setEnabled(false);
        mAddMoreBtn.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        restInterface = RetrofitClient.getClient(getContext()).create(RestInterface.class);

        EditPostFragmentArgs args = EditPostFragmentArgs.fromBundle(getArguments());
        contentType = args.getContentType();

        switch (contentType) {
            case MEME:
                getMeme(args.getId());
                mPostBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.hideSoftKeyboard(getActivity());

                        patchMeme(args.getId(), mDescription.getText().toString());
                    }
                });
                break;
            case TEMPLATE:
                getTemplate(args.getId());
                mPostBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.hideSoftKeyboard(getActivity());
                        patchTemplate(args.getId(), mDescription.getText().toString());
                    }
                });
                break;

        }

    }

    private void setData(String Description, List<ImageModel> Images) {

        mDescription.setText(Description);
        mDescription.requestFocus();
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        imageAdapter = new ImageAdapter(Images);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rv.setLayoutManager(layoutManager);
        rv.hasFixedSize();
        rv.setAdapter(imageAdapter);

    }

    private void getMeme(String id) {
        Call<MemeDetailModel> call = restInterface.getMeme(id);

        call.enqueue(new Callback<MemeDetailModel>() {
            @Override
            public void onResponse(Call<MemeDetailModel> call, Response<MemeDetailModel> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");
                    return;
                }

                memeDetailModel = response.body();
                setData(memeDetailModel.getDescription(), memeDetailModel.getImages());

            }

            @Override
            public void onFailure(Call<MemeDetailModel> call, Throwable t) {
                Log.d(TAG, "Failure");
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTemplate(String id) {
        Call<TemplateDetailModel> call = restInterface.getTemplate(id);

        call.enqueue(new Callback<TemplateDetailModel>() {
            @Override
            public void onResponse(Call<TemplateDetailModel> call, Response<TemplateDetailModel> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");
                    return;
                }

                templateDetailModel = response.body();
                setData(templateDetailModel.getDescription(), templateDetailModel.getImages());
            }

            @Override
            public void onFailure(Call<TemplateDetailModel> call, Throwable t) {
                Log.d(TAG, "Failure");
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void patchMeme(String id, String Description) {
        JsonObject body = new JsonObject();

        body.addProperty("description", Description);

        Call<JsonObject> call = restInterface.patchMeme(id, body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (!response.isSuccessful()) {
                    Log.d(TAG, "patch Error");

                    return;
                }
                JsonObject jsonObject = response.body();
                Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
                String id = jsonObject.get("id").getAsString();
                String responseDescription = jsonObject.get("description").getAsString();
                if (responseDescription.equals(Description)) {

                    EditPostFragmentDirections.ActionEditPostFragmentToViewerFragment action = EditPostFragmentDirections.actionEditPostFragmentToViewerFragment(id, ContentType.MEME);
                    Navigation.findNavController(getView()).navigate(action);
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void patchTemplate(String id, String Description) {
        JsonObject body = new JsonObject();

        body.addProperty("description", Description);

        Call<JsonObject> call = restInterface.patchTemplate(id, body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (!response.isSuccessful()) {
                    Log.d(TAG, "patch Error");

                    return;
                }
                JsonObject jsonObject = response.body();
                Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
                String id = jsonObject.get("id").getAsString();
                String responseDescription = jsonObject.get("description").getAsString();
                if (responseDescription.equals(Description)) {
                    EditPostFragmentDirections.ActionEditPostFragmentToViewerFragment action = EditPostFragmentDirections.actionEditPostFragmentToViewerFragment(id, ContentType.TEMPLATE);
                    Navigation.findNavController(getView()).navigate(action);
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private class ImageAdapter extends RecyclerView.Adapter {
        List<ImageModel> mImagesList;


        public ImageAdapter(List<ImageModel> mImagesList) {
            this.mImagesList = mImagesList;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ImageAdapter.ImageVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_grid, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ImageModel item = mImagesList.get(position);
            ImageVH imageVH = (ImageVH) holder;

            Glide.with(getContext()).load(mImagesList.get(position).getImageUrl()).apply(options).into(imageVH.mImageView);
        }

        @Override
        public int getItemCount() {
            return mImagesList.size();
        }


        private class ImageVH extends RecyclerView.ViewHolder {
            ImageView mImageView;
            MaterialButton mViewAllBtn;

            public ImageVH(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.imageViewtemp);

                mViewAllBtn = itemView.findViewById(R.id.ViewAllBtn);
                mViewAllBtn.setVisibility(View.GONE);
                mViewAllBtn.setEnabled(false);

            }
        }
    }
}
