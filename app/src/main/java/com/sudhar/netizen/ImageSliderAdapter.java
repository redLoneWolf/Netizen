package com.sudhar.netizen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Entities.ImageModelEntity;
import com.sudhar.netizen.RoomDB.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.SliderVH> {
    List<ImageModelEntity> images = new ArrayList<>();
    RequestOptions options;
    Context context;
    AppDatabase appDatabase;
    MainDao mainDao;
    private View.OnClickListener onClickListener;


    public ImageSliderAdapter(List<ImageModelEntity> images, Context context) {

        this.context = context;

        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.mipmap.ic_launcher_round);

        this.images = images;




    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public SliderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderVH holder, int position) {
        ImageModelEntity item = images.get(position);

        Glide.with(context).load(item.getImageUrl()).apply(options).into(holder.mMemeImageView);


    }

    public void remove(ImageModelEntity image) {
        int position = images.indexOf(image);
        if (position > -1) {
            images.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ImageModelEntity getItem(int position) {
        return images.get(position);
    }


    public void clear() {

        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    @Override
    public int getItemCount() {

        return images.size();

    }

    public class SliderVH extends RecyclerView.ViewHolder {
        ImageView mMemeImageView;

        public SliderVH(@NonNull View itemView) {
            super(itemView);
            mMemeImageView = itemView.findViewById(R.id.MemeImageView);
            if (onClickListener != null) {
                mMemeImageView.setOnClickListener(onClickListener);
            }

        }
    }
}
