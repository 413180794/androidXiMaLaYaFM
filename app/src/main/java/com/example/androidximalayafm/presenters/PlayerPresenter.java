package com.example.androidximalayafm.presenters;

import com.example.androidximalayafm.base.BaseApplication;
import com.example.androidximalayafm.interfaces.IPlayerCallback;
import com.example.androidximalayafm.interfaces.IPlayerPresenter;
import com.example.androidximalayafm.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PlayPresenter
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/28 10:07 AM
 * @since 1.0.0
 * <p>
 * Description:
 * 可以看出来，presenter 层主要负责逻辑的处理，通过 UI 注册的回调，对 UI 层处理。
 */
public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private List<IPlayerCallback> mIPlayerCallbacks = new ArrayList<>();
    private static final String TAG = "PlayerPresenter";
    private XmPlayerManager mXmPlayerManager;
    private Track mCurrentTrack;
    private int mCurrentIndex = 0;


    private PlayerPresenter() {
        mXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        // 广告相关的接口
        Optional.ofNullable(mXmPlayerManager).ifPresent(i -> {
            i.addAdsStatusListener(this);
            i.addPlayerStatusListener(this);

        });
        // 注册播放器状态相关的接口

    }

    /*=================广告相关的回调方法===================*/
    @Override
    public void onStartGetAdsInfo() {
        LogUtil.d(TAG, " onStartGetAdsInfo");
    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {

        LogUtil.d(TAG, "  onGetAdsInfo ");
    }

    @Override
    public void onAdsStartBuffering() {

        LogUtil.d(TAG, " onAdsStartBuffering");
    }

    @Override
    public void onAdsStopBuffering() {

        LogUtil.d(TAG, " onAdsStopBuffering");
    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {

        LogUtil.d(TAG, " onStartPlayAds");
    }

    @Override
    public void onCompletePlayAds() {

        LogUtil.d(TAG, " onCompletePlayAds");
    }

    @Override
    public void onError(int what, int extra) {

        LogUtil.d(TAG, " onError what =>" + what + " extra => " + extra);
    }


    /*=================广告相关的回调方法===================*/

    // ----------------播放器状态相关的回调方法---------------
    @Override
    public void onPlayStart() {
        LogUtil.d(TAG, "onPlayStart");
        mIPlayerCallbacks.forEach(IPlayerCallback::onPlayStart);
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG, "");

        mIPlayerCallbacks.forEach(IPlayerCallback::onPlayPause);
    }

    @Override
    public void onPlayStop() {

        LogUtil.d(TAG, "");
        mIPlayerCallbacks.forEach(IPlayerCallback::onPlayStop);
    }

    @Override
    public void onSoundPlayComplete() {

        LogUtil.d(TAG, "");
    }

    @Override
    public void onSoundPrepared() {

        LogUtil.d(TAG, "");
        if (mXmPlayerManager.getPlayerStatus() == PlayerConstants.STATE_STARTED) {
            // 播放器准备完成，可以播放
            mXmPlayerManager.play();
        }
    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        // 代表的是当前播放的内容， 如果通过 getKind 方法获取他是什么类型
        // track 表示的是 track 类型
        // 不推荐
//        if ("track".equals(curModel.getKind())) {
//            Track currentTrack = (Track) curModel;
//            LogUtil.d(TAG, "title == > " + currentTrack.getTrackTitle());
//        }
        if (curModel instanceof Track) {
            mCurrentTrack = (Track) curModel;
            mCurrentIndex = mXmPlayerManager.getCurrentIndex();
            mIPlayerCallbacks.forEach(iPlayerCallback -> {
                iPlayerCallback.onTrackUpdate(mCurrentTrack, mCurrentIndex);
            });
        }

    }

    @Override
    public void onBufferingStart() {

        LogUtil.d(TAG, "");
    }

    @Override
    public void onBufferingStop() {

        LogUtil.d(TAG, "");
    }

    @Override
    public void onBufferProgress(int i) {

        LogUtil.d(TAG, "");
    }

    @Override
    public void onPlayProgress(int currentPosition, int duration) {
        // 单位毫秒,
        // 回调界面中响应进度条的操作
        mIPlayerCallbacks.forEach(iPlayerCallback -> iPlayerCallback.onProgressChange(currentPosition, duration));
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(TAG, "");
        return false;
    }

    // ----------------播放器状态相关的回调方法---------------
    private static class PlayPresenterHolder {
        private static PlayerPresenter INSTANCE = new PlayerPresenter();
    }

    private boolean isPlayListSet = false;

    public void setPlayList(List<Track> list, int playIndex) {

        Optional.ofNullable(mXmPlayerManager).ifPresent(i -> {
            i.setPlayList(list, playIndex);
            isPlayListSet = true;
        });
        mCurrentTrack = list.get(playIndex);
        mCurrentIndex = playIndex;
        if (!Optional.ofNullable(mXmPlayerManager).isPresent()) {
            LogUtil.d(TAG, "mPlayerManager is null");
        }
    }

    public static PlayerPresenter getInstance() {
        return PlayPresenterHolder.INSTANCE;
    }

    @Override
    public void play() {
        if (isPlayListSet) {
            Optional.ofNullable(mXmPlayerManager).ifPresent(XmPlayerManager::play);
        }

    }

    @Override
    public void pause() {
        Optional.ofNullable(mXmPlayerManager).ifPresent(XmPlayerManager::pause);
    }

    @Override
    public void stop() {
        Optional.ofNullable(mXmPlayerManager).ifPresent(XmPlayerManager::stop);
    }

    @Override
    public void playPre() {
        Optional.ofNullable(mXmPlayerManager).ifPresent(XmPlayerManager::playPre);
    }

    @Override
    public void playNext() {
        Optional.ofNullable(mXmPlayerManager).ifPresent(XmPlayerManager::playNext);
    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {
        Optional.ofNullable(mXmPlayerManager).ifPresent(xmPlayerManager -> {
            xmPlayerManager.setPlayMode(mode);
        });
    }

    @Override
    public void getPlayList() {
        mXmPlayerManager.getPlayList();
        Optional.ofNullable(mXmPlayerManager).ifPresent(xmPlayerManager -> {
            List<Track> playList = xmPlayerManager.getPlayList();
            mIPlayerCallbacks.forEach(iPlayerCallback -> {
                iPlayerCallback.onListLoaded(playList);
            });
        });
    }

    @Override
    public void playByIndex(int index) {
        // 切换播放器到第 index 的位置进行播放
        Optional.ofNullable(mXmPlayerManager).ifPresent(i -> i.play(index));
    }

    @Override
    public void seekTo(int progress) {
        Optional.ofNullable(mXmPlayerManager).ifPresent(i -> i.seekTo(progress));
    }

    @Override
    public boolean isPlay() {
        // 返回当前是否正在播放
        return Optional.ofNullable(mXmPlayerManager).map(XmPlayerManager::isPlaying).orElse(Boolean.FALSE);
    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        iPlayerCallback.onTrackUpdate(mCurrentTrack, mCurrentIndex);
        if (!mIPlayerCallbacks.contains(iPlayerCallback)) {
            mIPlayerCallbacks.add(iPlayerCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayerCallback iPlayerCallback) {

    }
}
