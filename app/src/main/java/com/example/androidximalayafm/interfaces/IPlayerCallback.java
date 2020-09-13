package com.example.androidximalayafm.interfaces;


import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
 * IPlayerCallback
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/26 10:05 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public interface IPlayerCallback{
    /**
     * 开始播放
     */
    void onPlayStart();

    /**
     * 播放暂停
     */
    void onPlayPause();


    /**
     * 播放停止
     */
    void onPlayStop();

    /**
     * 缓冲播放错误
     */
    void onPlayError();

    /**
     * 下一首
     */
    void onNextPlay(Track track);

    /**
     * 上一首
     */
    void onPrePlay(Track track);


    // 播放列表数据加载完成
    void onListLoaded(List<Track> list);

    // 播放模式改变了
    void onPlayModeChange(XmPlayListControl.PlayMode playMode);

    // 进度条的改变
    void onProgressChange(int currentProgress, int totalProgress);

    // 广告正在加载
    void onAdLoading();

    // 广告加载完成
    void onAdFinished();

    /*
    更新当前节目
     */
    void onTrackUpdate (Track track, int playIndex);
}
