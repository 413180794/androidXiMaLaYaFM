package com.example.androidximalayafm.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidximalayafm.DetailActivity;
import com.example.androidximalayafm.R;
import com.example.androidximalayafm.adapters.RecommendListAdapter;
import com.example.androidximalayafm.base.BaseFragment;
import com.example.androidximalayafm.interfaces.IRecommendViewCallback;
import com.example.androidximalayafm.presenters.AlbumDetailPresenter;
import com.example.androidximalayafm.presenters.RecommendPresenter;
import com.example.androidximalayafm.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RecommendFragment  extends BaseFragment implements IRecommendViewCallback, UILoader.OnRetryClickListener, RecommendListAdapter.OnRecommendItemClickListener {
    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mRecommendRv;
    private RecommendListAdapter mRecommendListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUiLoader;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        mUiLoader = new UILoader(Objects.requireNonNull(getContext())) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return createSuccessView(layoutInflater,container );
            }
        };

        // 获取逻辑层的注册
        mRecommendPresenter = RecommendPresenter.getInstance();
        // 先要设置通知接口的注册,可以看到所谓的注册callback 就是让对方实现接口，并且将其放入到数组中等待后续的调用
        mRecommendPresenter.registerViewCallback(this);
        // 获取推荐列表
        mRecommendPresenter.getRecommendList();;
        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
            
        }
        // 所谓的注册监听也是让对方实现接口
        mUiLoader.setOnRetryClickListener(this);
        return mUiLoader;
    }

    private View createSuccessView(LayoutInflater layoutInflater, ViewGroup container) {
        // View 加载完成
        mRootView =layoutInflater.inflate(R.layout.fragment_recommend, container, false);
        // 获recycleview
        mRecommendRv = mRootView.findViewById(R.id.recommend_list);
        // 加载布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendRv.setLayoutManager(linearLayoutManager);
        mRecommendRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);

            }
        });
        // 设置适配器
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRv.setAdapter(mRecommendListAdapter);
        mRecommendListAdapter.setOnRecommendItemClickListener(this);
        return mRootView;
    }


    @Override
    public void onRecommendListLoaded(List<Album> result) {
        // 当我们获取到推荐内容的时候，这个方法就会被调用（成功了)
        // 数据回来以后 ，就会更新 UI
        Log.d(TAG, "更新 UI");
        Log.d(TAG, result.toString());
        mRecommendListAdapter.setData(result);
        mUiLoader.updateStatus(UILoader.UIStatue.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        mUiLoader.updateStatus(UILoader.UIStatue.NETWORK_ERROR);
    }

    @Override
    public void onEmpty() {

        mUiLoader.updateStatus(UILoader.UIStatue.EMPTY);
    }

    @Override
    public void onLoading() {

        mUiLoader.updateStatus(UILoader.UIStatue.LOADING);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消接口的注册， 避免内存泄露
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void onRetryClick() {
        // 表示网络不佳的时候，用户点击了重试
        // 重新获取数据即可
        Optional.ofNullable(mRecommendPresenter).ifPresent(RecommendPresenter::getRecommendList);
    }

    @Override
    public void onItemClick(int position, Album album) {
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        // 根据位置拿到 数据
        // item 被点击了, 跳转到详情
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);


    }
}
