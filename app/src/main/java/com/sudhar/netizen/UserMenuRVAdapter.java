package com.sudhar.netizen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.Arrays;
import java.util.List;


public class UserMenuRVAdapter extends RecyclerView.Adapter<UserMenuRVAdapter.ViewHolder> {

    List<String> mMenuItems;
    Context context;
    private OnItemSelected mOnItemSelected;

    public interface OnItemSelected {
        void onOptionSelected(int position);
    }

    public void setOnItemSelected(OnItemSelected mOnItemSelected) {
        this.mOnItemSelected = mOnItemSelected;
    }

    public UserMenuRVAdapter(Context context) {

        mMenuItems = Arrays.asList(context.getResources().getStringArray(R.array.userMenu));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.textView.setText(mMenuItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelected.onOptionSelected(getLayoutPosition());
                }
            });
            textView = itemView.findViewById(R.id.content);
//            textView.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                             mOnItemSelected.onOptionSelected(getLayoutPosition());
//                                            }
//                                        });
        }
    }
}