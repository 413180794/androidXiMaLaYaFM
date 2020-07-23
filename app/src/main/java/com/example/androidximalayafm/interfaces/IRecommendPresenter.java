package com.example.androidximalayafm.interfaces;

/**
 * IRecommendPresenter
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/7/23 9:43 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public interface IRecommendPresenter {
    /**
     *  获取推荐内容
     */
    void getRecommendList();

    /**
     * 下拉刷新更多内容
     */
    void pull2RefreshMore();

    /**
     * 上拉加载更多
     */

    void loadMore();


    /**
     * 这个方法用于注册 ui 的回调
     * @param callback
     */
    void registerViewCallback(IRecommendViewCallback callback);

    /**
     * 取消 ui 的回调注册
     * @param callback
     */
    void unRegisterViewCallback(IRecommendViewCallback callback);
}
