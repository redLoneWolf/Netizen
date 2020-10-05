package com.sudhar.netizen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sudhar.netizen.models.ContentList;

import java.util.ArrayList;
import java.util.List;

public class UserContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ContentList.UserContentModel> mContentList = new ArrayList<>();
    Context context;
    RequestOptions glideOptions;
    boolean isLoaderVisible = false;
    private boolean isLoadingAdded = false;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private ContentType contentType;

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public UserContentAdapter(Context context) {
        this.context = context;
        glideOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.layout_card_grid, parent, false);

        switch (viewType) {
            case VIEW_TYPE_NORMAL:

                return new UserMemeVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_grid, parent, false));
            case VIEW_TYPE_LOADING:
                return new LoadingVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false));
            default:
                return null;
        }

//        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        UserContentModel item = mMemeList.get(position);


//        Glide.with(context).load(item.getImageUrl()).apply(glideOptions).into(holder.imageView);
        switch (getItemViewType(position)) {
            case VIEW_TYPE_NORMAL:
                ContentList.UserContentModel item = mContentList.get(position);
                UserMemeVH memeVH = (UserMemeVH) holder;
                Glide.with(context).load(item.getImageUrl()).apply(glideOptions).into(memeVH.imageView);

                break;
            case VIEW_TYPE_LOADING:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mContentList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    private void add(ContentList.UserContentModel content) {
        mContentList.add(content);
        notifyDataSetChanged();
    }

    public void addAll(List<ContentList.UserContentModel> contents) {
        for (ContentList.UserContentModel content : contents) {
            add(content);
        }
    }

    public void remove(ContentList.UserContentModel content) {
        int position = mContentList.indexOf(content);
        if (position > -1) {
            mContentList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public ContentList.UserContentModel getItem(int position) {
        return mContentList.get(position);
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

    public void addLoading() {
        isLoaderVisible = true;
        mContentList.add(new ContentList.UserContentModel());
        notifyItemInserted(mContentList.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mContentList.size() - 1;
        ContentList.UserContentModel item = getItem(position);
        if (item != null) {
            mContentList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class UserMemeVH extends RecyclerView.ViewHolder {
        ImageView imageView;


        public UserMemeVH(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewtemp);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragmentDirections.ActionProfileFragmentToViewerFragment action = ProfileFragmentDirections.actionProfileFragmentToViewerFragment(mContentList.get(getLayoutPosition()).getId(), contentType);

                    Navigation.findNavController(v).navigate(action);
                }
            });


        }
    }
}
