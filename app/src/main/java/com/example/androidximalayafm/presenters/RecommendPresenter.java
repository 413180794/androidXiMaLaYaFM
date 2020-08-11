package com.example.androidximalayafm.presenters;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.androidximalayafm.interfaces.IRecommendPresenter;
import com.example.androidximalayafm.interfaces.IRecommendViewCallback;
import com.example.androidximalayafm.utils.Constants;
import com.example.androidximalayafm.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecommendPresenter
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/7/23 9:48 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public class RecommendPresenter implements IRecommendPresenter {
    private static final String TAG = "RecommendPresenter";
    // 单例
    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();

    private RecommendPresenter() {}

    private static RecommendPresenter sInstance = null;

    /**
     * 获取单例对象
     * @return
     */
    public static RecommendPresenter getInstance() {
        if (sInstance == null) {
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }
    @Override
    public void getRecommendList() {
        // 获取推荐内容
        updateLoading();
        Map<String, String> map = new HashMap<>();
        // 这个参数表示一夜数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.COUNT_RECOMMEND + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                Log.d(TAG, "success");
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    if (albumList != null) {
                        handlerRecommendResult(albumList);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                // 获取获取失败
                LogUtil.d(TAG, "error --> " + i);
                LogUtil.d(TAG, "errorMsg -- >" + s);
                handlerError();
            }
        });
    }

    private void handlerError() {
        if (mCallbacks != null) {
            mCallbacks.forEach(IRecommendViewCallback::onNetworkError);
        }
    }

    private void handlerRecommendResult(List<Album> albumList) {
        if (albumList != null) {
            if (albumList.size() == 0) {
                mCallbacks.forEach(IRecommendViewCallback::onEmpty);
            } else {
                mCallbacks.forEach(callback ->
                        callback.onRecommendListLoaded(albumList));
            }
        }
        // 通知 UI 更新
    }
    private void updateLoading() {
        mCallbacks.forEach(IRecommendViewCallback::onLoading);
    }
    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }
}
