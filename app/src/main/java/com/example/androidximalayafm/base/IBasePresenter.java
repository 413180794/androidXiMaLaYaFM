package com.example.androidximalayafm.base;

/**
 * IBasePresenter
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/26 9:47 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public interface IBasePresenter<T> {
    // 注册 UI 的回调接口
    void registerViewCallback(T t);
    // 取消注册
    void unRegisterViewCallback(T t);
}
