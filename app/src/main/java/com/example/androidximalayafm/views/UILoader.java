package com.example.androidximalayafm.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidximalayafm.R;
import com.example.androidximalayafm.base.BaseApplication;
import com.example.androidximalayafm.utils.LogUtil;

import java.util.Optional;

/**
 * UILoader
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/7/26 9:07 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public abstract class UILoader extends FrameLayout {

    private View mLoadingView;
    private View mSuccessView;
    private View mNetWorkErrorView;
    private View mEmptyView;
    private OnRetryClickListener mOnRetrClickListener = null;

    public enum UIStatue {
        LOADING, SUCCESS, NETWORK_ERROR, EMPTY, NONE
    }

    public UIStatue mCurrentStatus = UIStatue.NONE;

    public UILoader(@NonNull Context context) {
        this(context, null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 保证了唯一个入口
        init();
    }


    public void updateStatus(UIStatue statue) {
        mCurrentStatus = statue;
        // 更新 UI -- 一定要在主线程上
        BaseApplication.getHandlerOptional().ifPresent(sHandler -> sHandler.post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        }));
    }

    /**
     * 初始化 UI
     */
    private void init() {
        switchUIByCurrentStatus();
    }


    private void switchUIByCurrentStatus() {
        //加载中
        // 根据状态设置是否课件

        mLoadingView = Optional.ofNullable(mLoadingView).orElseGet(() -> {
            mLoadingView = getLoadingView();
            addView(mLoadingView);
            return mLoadingView;
        });
        mLoadingView.setVisibility(mCurrentStatus == UIStatue.LOADING ? VISIBLE : GONE);

        LogUtil.d("mLoadingView", mCurrentStatus+"");
        mSuccessView = Optional.ofNullable(mSuccessView).orElseGet(() -> {
            mSuccessView = getSuccessView(this);
            addView(mSuccessView);
            return mSuccessView;
        });
        mSuccessView.setVisibility(mCurrentStatus == UIStatue.SUCCESS ? VISIBLE : GONE);

        LogUtil.d("mSuccessView", mCurrentStatus+"");
        mNetWorkErrorView = Optional.ofNullable(mNetWorkErrorView).orElseGet(() -> {
            mNetWorkErrorView = getNetworkErrorView();
            addView(mNetWorkErrorView);
            return mNetWorkErrorView;
        });
        mNetWorkErrorView.setVisibility(mCurrentStatus == UIStatue.NETWORK_ERROR ? VISIBLE : GONE);

        LogUtil.d("mNetWorkErrorView", mCurrentStatus+"");
        mEmptyView = Optional.ofNullable(mEmptyView).orElseGet(() -> {
            mEmptyView = getEmptyView();
            addView(mEmptyView);
            return mEmptyView;
        });
        mEmptyView.setVisibility(mCurrentStatus == UIStatue.EMPTY ? VISIBLE : GONE);

        LogUtil.d("mEnptyView", mCurrentStatus+"");

    }

    private View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragement_empty_view, this, false);
    }

    private View getNetworkErrorView() {
        View networkErrorView = LayoutInflater.from(getContext()).inflate(R.layout.fragement_error_view, this, false);
        networkErrorView.findViewById(R.id.network_error_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 刷新 TODO ： 重新获取数据
                Optional.ofNullable(mOnRetrClickListener).ifPresent(OnRetryClickListener::onRetryClick);
            }
        });
        return networkErrorView;
    }

    protected abstract View getSuccessView(ViewGroup container);

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragement_loading_view, this, false);
    }
    public void setOnRetryClickListener(OnRetryClickListener listener) {
        this.mOnRetrClickListener = listener;

    }
    public interface OnRetryClickListener {
        void onRetryClick();
    }
}
