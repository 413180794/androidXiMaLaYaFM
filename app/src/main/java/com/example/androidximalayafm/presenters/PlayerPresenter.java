package com.example.androidximalayafm.presenters;

import com.example.androidximalayafm.base.BaseApplication;
import com.example.androidximalayafm.interfaces.IPlayerCallback;
import com.example.androidximalayafm.interfaces.IPlayerPresenter;
import com.example.androidximalayafm.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

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
public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener {

    private static final String TAG = "PlayerPresenter";
    private XmPlayerManager mPlayerManager;

    private PlayerPresenter() {
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        mPlayerManager.addAdsStatusListener(this);
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
