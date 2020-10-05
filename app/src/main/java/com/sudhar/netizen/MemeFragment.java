package com.sudhar.netizen;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.PagedAdapter.LoadStateAdapter;
import com.sudhar.netizen.PagedAdapter.PagedMemeAdapter;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Entities.CommentWithReplies;
import com.sudhar.netizen.RoomDB.Entities.Meme;
import com.sudhar.netizen.RoomDB.db.AppDatabase;
import com.sudhar.netizen.ViewModels.MemeViewModel;
import com.sudhar.netizen.models.MemeDetailModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static autodispose2.AutoDispose.autoDisposable;


public class MemeFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener,  SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MemeFragment";

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;

    private boolean mIsAvatarShown = true;
    private int mMaxScrollSize;
    private static final int MEME_PICK_REQUEST = 10;
    private static final int TEMPLATE_PICK_REQUEST = 20;

    RecyclerView mMemeRecyclerView;

    private ImageView mLogo;

    MemeViewModel memeViewModel;

    SwipeRefreshLayout swipeRefreshLayout;

    private RestInterface restInterface;
    PagedMemeAdapter pagingAdapter;


    public MemeFragment() {

    }

    public static MemeFragment newInstance(String text, int number) {
        MemeFragment fragment = new MemeFragment();
        return fragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        restInterface = RetrofitClient.getClient(getContext()).create(RestInterface.class);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meme, container, false);


        AppBarLayout appbarLayout = (AppBarLayout) view.findViewById(R.id.MemeAppBarLayout);
        mLogo = (ImageView) view.findViewById(R.id.NetizenLogo);
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        ImageButton searchBtn = view.findViewById(R.id.SearchBtn);

//        Button mMemeUploadBtn = view.findViewById(R.id.MemeUploadBtn);
//        Button mTemplateUploadBtn = view.findViewById(R.id.TemplateUploadBtn);
        mMemeRecyclerView = view.findViewById(R.id.cardRv);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

//        mMemeUploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getImage(ContentType.MEME);
//            }
//        });
//
//        mTemplateUploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getImage(ContentType.TEMPLATE);
//            }
//        });
        initRV();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memeViewModel = new ViewModelProvider(this).get(MemeViewModel.class);         /// dont change
        pagingAdapter = new PagedMemeAdapter(getContext());                              /// dont change

        pagingAdapter.setMemeShareAndReportListener(memeShareAndReportListener);
    }

    private void initRV() {




        LinearLayoutManager llmTools = new LinearLayoutManager(getContext());
        mMemeRecyclerView.setLayoutManager(llmTools);


        View.OnClickListener listener = v -> retry();

        mMemeRecyclerView.setAdapter(pagingAdapter.withLoadStateFooter(new LoadStateAdapter(listener)));


        memeViewModel.getFlowablePagedData()
                .subscribeOn(Schedulers.newThread())
                .onErrorReturn(new Function<Throwable, PagingData<Meme>>() {
                    @Override
                    public PagingData<Meme> apply(Throwable throwable) throws Throwable {
                        Log.d(TAG, "apply: " + throwable.getStackTrace().toString());
                        return null;
                    }
                })
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(this::pagingSubscriber, Throwable::printStackTrace);


    }

    List<CommentWithReplies> commentWithReplies = new ArrayList<>();
    private void pagingSubscriber(PagingData<Meme> pagingData) {

        pagingAdapter.submitData(getViewLifecycleOwner().getLifecycle(), pagingData);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

//        MainDao mainDao = AppDatabase.getInstance(getContext()).mainDao();
//
//
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                commentWithReplies = mainDao.getc();
//                Log.d(TAG, "run: ");
//            }
//        });

    }

    private void retry() {
        pagingAdapter.retry();
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "Refreshing", Toast.LENGTH_SHORT).show();
        pagingAdapter.refresh();
    }


    @SuppressLint("IntentReset")
    private void getImage(ContentType contentType) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
//        getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});


        int REQUEST_CODE;
        String title = "";

        switch (contentType) {
            case TEMPLATE:
                REQUEST_CODE = TEMPLATE_PICK_REQUEST;
                title = "Meme";
                break;
            case MEME:
                REQUEST_CODE = MEME_PICK_REQUEST;
                title = "Template";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + contentType);
        }


        startActivityForResult(Intent.createChooser(chooserIntent, "Select " + title), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MemeFragmentDirections.ActionMemeFragmentToUploadFragment action = null;

        if (resultCode == Activity.RESULT_OK && data != null) {
            List<Uri> imagelist = new ArrayList<>();

            ClipData clipData = data.getClipData();

            if (clipData != null) {
                int count = clipData.getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    imagelist.add(imageUri);
                }
            } else {
                Uri imagePath = data.getData();
                imagelist.add(imagePath);
            }
            Uri[] images = new Uri[5];

            imagelist.toArray(images);
            switch (requestCode) {

                case MEME_PICK_REQUEST:
                    action = MemeFragmentDirections.actionMemeFragmentToUploadFragment(images, ContentType.MEME);
                    break;
                case TEMPLATE_PICK_REQUEST:
                    action = MemeFragmentDirections.actionMemeFragmentToUploadFragment(images, ContentType.TEMPLATE);
                    break;
            }
            Navigation.findNavController(getView()).navigate(action);
        }


    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mLogo.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mLogo.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }


    PagedMemeAdapter.MemeShareAndReportListener memeShareAndReportListener = new PagedMemeAdapter.MemeShareAndReportListener() {
        @Override
        public void OnReportButtonClick(Meme meme) {
            ReportDialogFragment reportDialogFragment = ReportDialogFragment.newInstance();
            reportDialogFragment.setMemeId(meme.getMeme().getId());
            reportDialogFragment.show(getChildFragmentManager(), reportDialogFragment.getTag());
        }

        @Override
        public void OnShareButtonClick(Meme meme) {
            share(meme);
        }
    };

    private void share(Meme meme) {


        Glide.with(this)
                .asBitmap()
                .load(meme.getImages().get(0).getImageUrl())
                .into((new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                File f = new File(Environment.getExternalStorageDirectory()
                                        + File.separator + "temporary_file.jpg");
                                try {
                                    f.createNewFile();
                                    FileOutputStream fo = new FileOutputStream(f);
                                    fo.write(bytes.toByteArray());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                share.putExtra(Intent.EXTRA_STREAM,
                                        Uri.parse(f.getPath()));
                                startActivity(Intent.createChooser(share, "Share Image"));
                                share.putExtra(Intent.EXTRA_TEXT, "image from netizen");
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        })

                );


    }


}