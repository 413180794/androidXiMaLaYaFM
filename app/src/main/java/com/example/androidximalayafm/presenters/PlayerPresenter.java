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
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.lang.annotation.Target;
import java.util.List;

/**
 * PlayPresenter
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/28 10:07 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private static final String TAG = "PlayerPresenter";
    private XmPlayerManager mPlayerManager;

    private PlayerPresenter() {
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        // 广告相关的接口
        mPlayerManager.addAdsStatusListener(this);
        // 注册播放器状态相关的接口
        mPlayerManager.addPlayerStatusListener(this);

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
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG, "");
    }

    @Override
    public void onPlayStop() {

        LogUtil.d(TAG, "");
    }

    @Override
    public void onSoundPlayComplete() {

        LogUtil.d(TAG, "");
    }

    @Override
    public void onSoundPrepared() {

        LogUtil.d(TAG, "");
    }

    @Override
    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {

        LogUtil.d(TAG, "");
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
    public void onPlayProgress(int i, int i1) {

        LogUtil.d(TAG, "");
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

        if (mPlayerManager != null) {
            mPlayerManager.setPlayList(list,playIndex);
            isPlayListSet = true;
        } else {
            LogUtil.d(TAG, "mPlayerManager is null");
        }
    }

    public static PlayerPresenter getInstance() {
        return PlayPresenterHolder.INSTANCE;
    }

    @Override
    public void play() {
        if (isPlayListSet) {
            mPlayerManager.play();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {

    }

    @Override
    public void playNext() {

    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {

    }

    @Override
    public void playByIndex(int index) {

    }

    @Override
    public void seekTo(int progress) {

    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {

    }

    @Override
    public void unRegisterViewCallback(IPlayerCallback iPlayerCallback) {

    }
}
