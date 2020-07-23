package com.example.androidximalayafm.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * IRecommendViewCallback
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/7/23 9:45 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public interface IRecommendViewCallback {
    // 逻辑层通知 UI

    /**
     * 获取推荐内容的结果
     * @param result
     */
    void onRecommendListLoaded(List<Album> result);

    /**
     * 加载更多的结果
     *
     * @param result
     */
    void onLoaderMore(List<Album> result);

    /**
     * 下拉加载更多的结果
     * @param result
     */
    void onRefreshMore(List<Album> result);
}
