package com.example.androidximalayafm;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.androidximalayafm.interfaces.IPlayerCallback;
import com.example.androidximalayafm.presenters.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

/**
 * PlayerActivity
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/24 10:02 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public class PlayerActivity extends BaseActivity implements IPlayerCallback {

    private Optional<ImageView> mOptionalControlBtn;
    private Optional<PlayerPresenter> mOptionalPlayerPresenter;
    private SimpleDateFormat mMinFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourFormat = new SimpleDateFormat("hh:mm:ss");
    private Optional<TextView> mOptionalTotalDuration;
    private Optional<TextView> mOptionalCurrentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        // TODO: 测试一下播放
        mOptionalPlayerPresenter = Optional.ofNullable(PlayerPresenter.getInstance());
        mOptionalPlayerPresenter.ifPresent(i -> i.registerViewCallback(this));
        initView();
        initEvent();
        startPlay();
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        mOptionalPlayerPresenter.ifPresent(PlayerPresenter::play);
    }

    /**
     * 给控件设置相关的事件
     */
    private void initEvent() {
        mOptionalControlBtn.ifPresent(i -> i.setOnClickListener(v -> {
            // 如果现在的状态是正在播放的，那么久暂停
            // 如果现在的状态是非播放的，那么我们就让播放器播放节目
            mOptionalPlayerPresenter.ifPresent(playerPresenter -> {
                if (playerPresenter.isPlay()) {
                    playerPresenter.pause();
                } else {
                    playerPresenter.play();
                }
            });
        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOptionalPlayerPresenter.ifPresent(i -> {
            i.unRegisterViewCallback(this);
            mOptionalPlayerPresenter = Optional.empty();
        });
    }

    /**
     * 找到各个控件
     */
    private void initView() {
        mOptionalControlBtn = Optional.ofNullable(this.findViewById(R.id.play_or_pause_btn));
        mOptionalTotalDuration = Optional.ofNullable(this.findViewById(R.id.track_duration));
        mOptionalCurrentPosition = Optional.ofNullable(this.findViewById(R.id.current_position));

    }

    @Override
    public void onPlayStart() {
        // 开始播放，修改 UI 成暂停的按钮
        mOptionalControlBtn.ifPresent(i -> i.setImageResource(R.mipmap.stop));
    }

    @Override
    public void onPlayPause() {
        mOptionalControlBtn.ifPresent(i -> i.setImageResource(R.mipmap.play));
    }

    @Override
    public void onPlayStop() {

    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void onNextPlay(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> list) {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(long currentProgress, long totalProgress) {
        // 更新播放进度，更新进度条
        String totalDuration;
        String currentPosition;
        if (totalProgress > 1000 * 60 * 60) {
            totalDuration = mHourFormat.format(totalProgress);
            currentPosition = mHourFormat.format(currentProgress);
        } else {
            totalDuration = mMinFormat.format(totalProgress);
            currentPosition = mMinFormat.format(currentProgress);
        }
        mOptionalTotalDuration.ifPresent(t->t.setText(totalDuration));
        mOptionalCurrentPosition.ifPresent(c->c.setText(currentPosition));

    }


    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }
}
