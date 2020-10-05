package com.sudhar.netizen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class UserMentionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DataModel> mUserList = new ArrayList<>();

    private UsernameSelectListener usernameSelectListener;

    public void setUsernameSelectListener(UsernameSelectListener usernameSelectListener) {
        this.usernameSelectListener = usernameSelectListener;
    }

    public interface UsernameSelectListener {
        void OnUserSelect(String Username);
    }

    public UserMentionAdapter() {
//        mUserList.add(new DataModel("siFDgvg","sud"));
//        mUserList.add(new DataModel("siFDgvg","sud"));
//        mUserList.add(new DataModel("siFDgvg","sud"));
//        mUserList.add(new DataModel("siFDgvg","sud"));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_mention_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataModel item = mUserList.get(position);
        VH vh = (VH) holder;
        vh.mUsername.setText(item.getUsername());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class DataModel {
        private String Username;
        private String profilePicUrl;

        public DataModel(String username, String profilePicUrl) {
            Username = username;
            this.profilePicUrl = profilePicUrl;
        }

        public String getUsername() {
            return Username;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }
    }

    private void add(DataModel data) {
        mUserList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<DataModel> datas) {
        for (DataModel data : datas) {
            add(data);
        }
    }

    public DataModel getItem(int position) {
        return mUserList.get(position);
    }

    public void remove(DataModel template) {
        int position = mUserList.indexOf(template);
        if (position > -1) {
            mUserList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {

        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView mUsername;
        ShapeableImageView mProfilePic;

        public VH(@NonNull View itemView) {
            super(itemView);

            mUsername = itemView.findViewById(R.id.Username);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usernameSelectListener.OnUserSelect(mUserList.get(getLayoutPosition()).getUsername());
                }
            });

        }
    }
}
