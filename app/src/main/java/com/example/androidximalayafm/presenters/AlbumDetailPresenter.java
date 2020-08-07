package com.example.androidximalayafm.presenters;

import com.example.androidximalayafm.interfaces.IAlbumDetailPresenter;
import com.example.androidximalayafm.interfaces.IAlbumDetailViewCallback;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

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
    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();
    // 要求单例
    private Album mTargetAlbum = null;

    private AlbumDetailPresenter() {}

    private static AlbumDetailPresenter sInstance = null;

    public static AlbumDetailPresenter getInstance() {
        if (sInstance == null ){
            synchronized (AlbumDetailPresenter.class) {
                if ( sInstance == null) {
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
