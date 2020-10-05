package com.sudhar.netizen.PagedAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.sudhar.netizen.R;
import com.sudhar.netizen.RoomDB.Entities.Template;
import com.sudhar.netizen.SharedPreferenceHelper;
import com.sudhar.netizen.models.TemplateDetailModel;

public class PagedTemplateAdapter extends PagingDataAdapter<Template, PagedTemplateAdapter.TempelateVH> {

    private Context context;

    private final String TAG = "PagedTemplateAdapter";
    RequestOptions options;
    SharedPreferenceHelper sharedPreferenceHelper;


    public PagedTemplateAdapter(Context context) {
        super(Template.CALLBACK);
        this.context = context;


        options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        sharedPreferenceHelper = new SharedPreferenceHelper(context);
    }




    @NonNull
    @Override
    public TempelateVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TempelateVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TempelateVH holder, int position) {


        Template item = getItem(position);


        if (item != null) {
            if (!item.getImages().isEmpty()) {
                Glide.with(context).load(getItem(position).getImages().get(0).getImageUrl())
                        .apply(options)
                        .into(holder.img);

                if (item.getImages().size() > 1) {
                    holder.mViewAllBtn.setEnabled(true);
                    holder.mViewAllBtn.setVisibility(View.VISIBLE);
                    String temp = "+" + (item.getImages().size() - 1);
                    holder.mViewAllBtn.setText(temp);
                    holder.mViewAllBtn.setOnClickListener(holder.listener);
                } else {
                    holder.mViewAllBtn.setEnabled(false);
                    holder.mViewAllBtn.setVisibility(View.GONE);
                }
            }


        }



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


//            listener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getItem(getLayoutPosition());
//                    TemplateFragmentDirections.ActionTemplateFragmentToViewerFragment action = TemplateFragmentDirections.actionTemplateFragmentToViewerFragment(getItem(getAdapterPosition()).getId(), ContentType.TEMPLATE);
//
//                    Navigation.findNavController(v).navigate(action);
//                }
//            };
//
//
//            img.setOnClickListener(listener);


        }
    }


}

