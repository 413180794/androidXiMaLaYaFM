package com.example.androidximalayafm.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * IAlbumDetailViewCallback
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/5 11:08 AM
 * @since 1.0.0
 * <p>
 * Description:
 */


public interface IAlbumDetailViewCallback {
    /**
     * 专辑详情内容加载出来
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);

    /**
     * 把 album 传给 ui
     * @param album
     */
    void onAlbumLoaded(Album album);

    /**
     * 网络错误
     */
    void onNetworkError(int errorCode, String errorMsg);
}
