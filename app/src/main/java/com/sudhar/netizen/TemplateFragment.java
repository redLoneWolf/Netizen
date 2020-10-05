package com.sudhar.netizen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.PagedAdapter.LoadStateAdapter;
import com.sudhar.netizen.PagedAdapter.PagedTemplateAdapter;
import com.sudhar.netizen.RoomDB.Entities.Meme;
import com.sudhar.netizen.RoomDB.Entities.Template;
import com.sudhar.netizen.ViewModels.TemplateViewModel;
import com.sudhar.netizen.models.TemplateDetailModel;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static autodispose2.AutoDispose.autoDisposable;

public class TemplateFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = "TemplateFragment";

    private RecyclerView mTemplateRecyclerView;


    TemplateViewModel templateViewModel;


    private RestInterface restInterface;
    PagedTemplateAdapter pagingAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    public TemplateFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_template, container, false);

        mTemplateRecyclerView = view.findViewById(R.id.TemplateCardRv);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        templateViewModel = new ViewModelProvider(this).get(TemplateViewModel.class);      /// dont change
        pagingAdapter = new PagedTemplateAdapter(getContext());                                       /// dont change
    }

    private void initRV() {




        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        };

        mTemplateRecyclerView.setAdapter(pagingAdapter.withLoadStateFooter(new LoadStateAdapter(listener)));
        GridLayoutManager llmTools = new GridLayoutManager(getContext(), 2);

        mTemplateRecyclerView.setLayoutManager(llmTools);
        templateViewModel.getFlowablePagedData()
                .subscribeOn(Schedulers.newThread())
                .onErrorReturn(new Function<Throwable, PagingData<Template>>() {
                    @Override
                    public PagingData<Template> apply(Throwable throwable) throws Throwable {
                        Log.d(TAG, "apply: " + throwable.getStackTrace().toString());
                        return null;
                    }
                }).to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(this::pagingSubscriber, Throwable::printStackTrace);


    }

    private void pagingSubscriber(PagingData<Template> pagingData) {

        pagingAdapter.submitData(getViewLifecycleOwner().getLifecycle(), pagingData);
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void retry(){
        pagingAdapter.retry();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        restInterface = RetrofitClient.getClient(getContext()).create(RestInterface.class);
        initRV();
    }


    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: Tempalte");
        pagingAdapter.refresh();
    }
}