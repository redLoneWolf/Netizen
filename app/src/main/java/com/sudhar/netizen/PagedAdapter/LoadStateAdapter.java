package com.sudhar.netizen.PagedAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.RecyclerView;

import com.sudhar.netizen.R;

import org.jetbrains.annotations.NotNull;

public class LoadStateAdapter extends androidx.paging.LoadStateAdapter<LoadStateAdapter.LoadStateViewHolder> {
    private static final String TAG = "LoadStateAdapter";

    View.OnClickListener onClickListener;

    public LoadStateAdapter(@NonNull View.OnClickListener retryCallback) {
        Log.d(TAG, "LoadStateAdapter: ");
        this.onClickListener = retryCallback;
    }

    @Override
    public void onBindViewHolder(@NotNull LoadStateViewHolder loadStateViewHolder, @NotNull LoadState loadState) {

        loadStateViewHolder.mProgressBar.setVisibility(loadState instanceof LoadState.Loading ? View.VISIBLE : View.GONE);

        if (loadState instanceof LoadState.Error) {
            loadStateViewHolder.mRetry.setVisibility(View.VISIBLE);
            loadStateViewHolder.mErrorMsg.setVisibility(View.VISIBLE);

            loadStateViewHolder.mErrorMsg.setText(((LoadState.Error) loadState).getError().getLocalizedMessage());
        }


        Log.d(TAG, "onBindViewHolder: " + loadState.toString());

    }

    @NotNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, @NotNull LoadState loadState) {

        return new LoadStateViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading, viewGroup, false));
    }

    class LoadStateViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        private TextView mErrorMsg;
        private Button mRetry;


        public LoadStateViewHolder(@NonNull View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.load_state_progress);
            mErrorMsg = itemView.findViewById(R.id.load_state_errorMessage);
            mRetry = itemView.findViewById(R.id.load_state_retry);
            mRetry.setOnClickListener(onClickListener);
//            mProgressBar.setVisibility(View.GONE);
//            mErrorMsg.setVisibility(View.GONE);
//            mRetry.setVisibility(View.GONE);

        }
    }


}
