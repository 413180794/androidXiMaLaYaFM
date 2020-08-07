package com.example.androidximalayafm.interfaces;

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
public interface IAlbumDetailPresenter {
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
     * @param albumId
     * @param page
     */
    void getAlbumDetail(int albumId, int page);

    /**
     * 注册 UI 通知接口
     * @param detailPresenter
     */
    void registerViewCallback(IAlbumDetailViewCallback albumDetailViewCallback);


    void unRegisterViewCallback(IAlbumDetailViewCallback albumDetailViewCallback);
}
