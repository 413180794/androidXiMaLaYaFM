package com.example.androidximalayafm.fragments;

import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidximalayafm.R;
import com.example.androidximalayafm.adapters.RecommendListAdapter;
import com.example.androidximalayafm.base.BaseFragment;
import com.example.androidximalayafm.interfaces.IRecommendViewCallback;
import com.example.androidximalayafm.presenters.RecommendPresenter;
import com.example.androidximalayafm.utils.Constants;
import com.example.androidximalayafm.utils.LogUtil;
import com.example.androidximalayafm.views.UILoader;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class RecommendFragment  extends BaseFragment implements IRecommendViewCallback, UILoader.OnRetryClickListener {
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
        // 先要设置通知接口的注册
        mRecommendPresenter.registerViewCallback(this);
        // 获取推荐列表
        mRecommendPresenter.getRecommendList();;
        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
            
        }
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
}
