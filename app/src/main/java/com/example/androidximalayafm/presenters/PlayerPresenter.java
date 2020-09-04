package com.example.androidximalayafm.presenters;

import com.example.androidximalayafm.base.BaseApplication;
import com.example.androidximalayafm.interfaces.IPlayerCallback;
import com.example.androidximalayafm.interfaces.IPlayerPresenter;
import com.example.androidximalayafm.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
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
public class PlayerPresenter implements IPlayerPresenter {

    private static final String TAG = "PlayerPresenter";
    private XmPlayerManager mPlayerManager;

    private PlayerPresenter() {
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
    }

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
