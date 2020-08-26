package com.example.androidximalayafm.interfaces;

import com.example.androidximalayafm.base.IBasePresenter;

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
public interface IRecommendPresenter extends IBasePresenter<IRecommendViewCallback> {
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


}
