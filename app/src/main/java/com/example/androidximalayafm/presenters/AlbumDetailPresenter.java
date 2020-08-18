package com.example.androidximalayafm.presenters;

import androidx.annotation.Nullable;

import com.example.androidximalayafm.interfaces.IAlbumDetailPresenter;
import com.example.androidximalayafm.interfaces.IAlbumDetailViewCallback;
import com.example.androidximalayafm.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.CommonTrackList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.androidximalayafm.utils.Constants.COUNT_DEFAULT;

/**
 * AlbumDetailPresenter
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/3 10:24 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public class AlbumDetailPresenter implements IAlbumDetailPresenter {
    private static final String TAG = "AlbumDetailPresenter";
    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();
    // 要求单例
    private Album mTargetAlbum = null;

    private AlbumDetailPresenter() {
    }

    private static AlbumDetailPresenter sInstance = null;

    public static AlbumDetailPresenter getInstance() {
        if (sInstance == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (sInstance == null) {
                    sInstance = new AlbumDetailPresenter();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int albumId, int page) {
        // 根据页码和专辑id 获取列表
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, albumId + "");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, page + "");
        map.put(DTransferConstants.PAGE_SIZE, COUNT_DEFAULT+"");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(@Nullable TrackList trackList) {
                // 将 trace的数据显示在 recycleView 上，分别是序号、集数、traceTitle、时间、播放数
                // 这一部分的显示自己写
                LogUtil.d(TAG, "current Thread -- > " + Thread.currentThread().getName());
                Optional.ofNullable(trackList).ifPresent(i -> {
                    LogUtil.d(TAG, "tracks size --> " + i.getTracks().size());
                    handlerAlbumDetailResult(i.getTracks());
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "errorCode --> " + errorCode);
                LogUtil.d(TAG, "errorMsg --> " + errorMsg);
            }
        });
    }

    private void handlerAlbumDetailResult(List<Track> tracks) {
        mCallbacks.forEach(mCallback->{
            mCallback.onDetailListLoaded(tracks);
        });
    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback albumDetailViewCallback) {
        if (!mCallbacks.contains(albumDetailViewCallback)) {
            mCallbacks.add(albumDetailViewCallback);
            if (mTargetAlbum != null) {
                albumDetailViewCallback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unRegisterViewCallback(IAlbumDetailViewCallback albumDetailViewCallback) {
        mCallbacks.remove(albumDetailViewCallback);
    }


    public void setTargetAlbum(Album mTargetAlbum) {
        this.mTargetAlbum = mTargetAlbum;
    }
}
