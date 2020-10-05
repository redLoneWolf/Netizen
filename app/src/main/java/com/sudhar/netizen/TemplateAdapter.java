package com.sudhar.netizen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.models.TemplateDetailModel;

import java.util.ArrayList;
import java.util.List;

public class TemplateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TemplateAdapter";
    private List<TemplateDetailModel> mTemplateList = new ArrayList<>();
    private Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    boolean isLoaderVisible = false;

    private boolean isLoadingAdded = false;
    RequestOptions options;
    private RestInterface restInterface;

    public TemplateAdapter(Context context) {
        this.context = context;

        this.restInterface = restInterface;

        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new TempelateVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_grid, parent, false));
            case VIEW_TYPE_LOADING:
                return new LoadingVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_NORMAL:
                TemplateDetailModel item = mTemplateList.get(position);

                TempelateVH tempelateVH = (TempelateVH) holder;

                if (!item.getImages().isEmpty()) {
                    Glide.with(context).load(item.getImages().get(0).getImageUrl()).apply(options).into(tempelateVH.img);
                    if (item.getImages().size() > 1) {
                        tempelateVH.mViewAllBtn.setEnabled(true);
                        tempelateVH.mViewAllBtn.setVisibility(View.VISIBLE);
                        String temp = "+" + (item.getImages().size() - 1);
                        tempelateVH.mViewAllBtn.setText(temp);
                        tempelateVH.mViewAllBtn.setOnClickListener(tempelateVH.listener);
                    } else {
                        tempelateVH.mViewAllBtn.setEnabled(false);
                        tempelateVH.mViewAllBtn.setVisibility(View.GONE);
                    }
                }

                break;

            case VIEW_TYPE_LOADING:
                break;


        }


    }

    @Override
    public int getItemCount() {

        Log.d(TAG, "getItemCount: " + mTemplateList.size());
        return mTemplateList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return mTemplateList.get(position).getId() == null ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        mTemplateList.add(new TemplateDetailModel());
        notifyItemInserted(mTemplateList.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mTemplateList.size() - 1;
        TemplateDetailModel item = getItem(position);
        if (item != null) {
            mTemplateList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private void add(TemplateDetailModel template) {
        mTemplateList.add(template);
        notifyDataSetChanged();
    }

    public void addAll(List<TemplateDetailModel> templates) {
        for (TemplateDetailModel template : templates) {
            add(template);
        }
    }

    public void remove(TemplateDetailModel template) {
        int position = mTemplateList.indexOf(template);
        if (position > -1) {
            mTemplateList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public TemplateDetailModel getItem(int position) {
        return mTemplateList.get(position);
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public class TempelateVH extends RecyclerView.ViewHolder {
        ImageView img;
        MaterialButton mViewAllBtn;
        View.OnClickListener listener;

        public TempelateVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageViewtemp);
            mViewAllBtn = itemView.findViewById(R.id.ViewAllBtn);
            mViewAllBtn.setVisibility(View.GONE);
            mViewAllBtn.setEnabled(false);
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = mTemplateList.get(getLayoutPosition()).getId();
                    String contentType = mTemplateList.get(getLayoutPosition()).getContentType();
                    TemplateFragmentDirections.ActionTemplateFragmentToViewerFragment action = TemplateFragmentDirections.actionTemplateFragmentToViewerFragment(id, ContentType.TEMPLATE);

                    Navigation.findNavController(v).navigate(action);
                }
            };


            img.setOnClickListener(listener);


        }
    }

    public class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }

}
