package com.example.androidximalayafm.interfaces;

import com.example.androidximalayafm.base.IBasePresenter;

/**
 * IAlbumDetailPresenter
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/3 10:19 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailViewCallback> {
    /**
     * 下拉刷新更多内容
     */
    void pull2RefreshMore();

    /**
     * 上拉加载更多
     */

    void loadMore();

    /**
     * 获取专辑详情
     *
     * @param albumId
     * @param page
     */
    void getAlbumDetail(int albumId, int page);
}
