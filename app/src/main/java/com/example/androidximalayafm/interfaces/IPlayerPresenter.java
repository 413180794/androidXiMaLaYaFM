package com.example.androidximalayafm.interfaces;

import com.example.androidximalayafm.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

/**
 * IPlayerPresenter
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/26 10:04 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public interface  IPlayerPresenter extends IBasePresenter<IPlayerCallback> {
    // 播放
    void play();
    // 暂停
    void pause();
    // 停止
    void stop();
    // 上一首
    void playPre();
    // 下一首
    void playNext();

    // 切换播放模式
    void switchPlayMode(XmPlayListControl.PlayMode mode);

    // 获取播放列表
    void getPlayList();

    // 根据节目的位置进行播放
    void playByIndex(int index);

    // 切换播放进度
    void seekTo(int progress);

    // 判断播放器是否在播放
    boolean isPlay();

}
