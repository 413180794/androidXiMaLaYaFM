package com.example.androidximalayafm;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.androidximalayafm.adapters.PlayerTrackPagerAdapter;
import com.example.androidximalayafm.interfaces.IPlayerCallback;
import com.example.androidximalayafm.presenters.PlayerPresenter;
import com.example.androidximalayafm.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
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
public class PlayerActivity extends BaseActivity implements IPlayerCallback, ViewPager.OnPageChangeListener {

    private static final String TAG = "PlayerActivity";
    private SimpleDateFormat mMinFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);
    private SimpleDateFormat mHourFormat = new SimpleDateFormat("hh:mm:ss", Locale.CHINA);

    private ImageView mControlBtn;
    private PlayerPresenter mPlayerPresenter;
    private TextView mTotalDuration;
    private TextView mCurrentPosition;
    private SeekBar mDurationBar;

    private int mCurrentProgress = 0;
    private boolean mIsUserTouchProgressBar = false;
    private ImageView mPlayNext;
    private ImageView mPlayPre;
    private TextView mTrackTitle;
    private ViewPager mTrackPageView;
    private PlayerTrackPagerAdapter mTrackPagerAdapter;
    private boolean mIsUserSlidePager = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        // TODO: 测试一下播放
        mPlayerPresenter = PlayerPresenter.getInstance();
        initView();
        initEvent();
        Optional.ofNullable(mPlayerPresenter).ifPresent(i -> {
            i.registerViewCallback(this);
            i.getPlayList();
        });
    }

    /**
     * 给控件设置相关的事件
     */
    private void initEvent() {
        Optional.ofNullable(mControlBtn).ifPresent(i -> i.setOnClickListener(v -> {
            // 如果现在的状态是正在播放的，那么久暂停
            // 如果现在的状态是非播放的，那么我们就让播放器播放节目
            Optional.ofNullable(mPlayerPresenter).ifPresent(playerPresenter -> {
                if (playerPresenter.isPlay()) {
                    playerPresenter.pause();
                } else {
                    playerPresenter.play();
                }
            });
        }));
        Optional.ofNullable(mDurationBar).ifPresent(i -> i.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = false;
                // 手离开进度条的时候
                mPlayerPresenter.seekTo(mCurrentProgress);
            }
        }));

        Optional.ofNullable(mPlayPre).ifPresent(i -> i.setOnClickListener(v -> {
            // todo 播放前一个节目
            Optional.ofNullable(mPlayerPresenter).ifPresent(PlayerPresenter::playPre);
        }));
        Optional.ofNullable(mPlayNext).ifPresent(i -> i.setOnClickListener(v -> {
            // todo 播放后一个节目
            Optional.ofNullable(mPlayerPresenter).ifPresent(PlayerPresenter::playNext);
        }));
        Optional.ofNullable(mTrackPageView).ifPresent(trackPageView -> {
            trackPageView.addOnPageChangeListener(this);
            trackPageView.setOnTouchListener((view, motionEvent) -> {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        LogUtil.d(TAG, "ACTION DOWN");
                        mIsUserTouchProgressBar = true;
                        break;
                }
                return false;

            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Optional.ofNullable(mPlayerPresenter).ifPresent(i -> {
            i.unRegisterViewCallback(this);
            mPlayerPresenter = null;
        });
    }

    /**
     * 找到各个控件
     */
    private void initView() {
        mControlBtn = this.findViewById(R.id.play_or_pause_btn);
        mTotalDuration = this.findViewById(R.id.track_duration);
        mCurrentPosition = this.findViewById(R.id.current_position);
        mDurationBar = this.findViewById(R.id.track_seek_bar);
        mPlayNext = this.findViewById(R.id.play_next);
        mPlayPre = this.findViewById(R.id.play_pre);
        mTrackTitle = this.findViewById(R.id.track_title);
        mTrackPageView = this.findViewById(R.id.track_pager_view);
        // 创建适配器
        mTrackPagerAdapter = new PlayerTrackPagerAdapter();
        // 设置适配器
        mTrackPageView.setAdapter(mTrackPagerAdapter);
    }

    @Override
    public void onPlayStart() {
        // 开始播放，修改 UI 成暂停的按钮
        Optional.ofNullable(mControlBtn).ifPresent(i -> i.setImageResource(R.drawable.selector_player_stop));
    }

    @Override
    public void onPlayPause() {
        Optional.ofNullable(mControlBtn).ifPresent(i -> i.setImageResource(R.drawable.selector_player_play));
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
//        LogUtil.d(TAG, "lsit " + list);
        // 把数据设置到适配器中
        Optional.ofNullable(mTrackPagerAdapter).ifPresent(i -> i.setData(list));
    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(int currentProgress, int totalProgress) {
        // 更新播放进度，更新进度条
        Optional.ofNullable(mDurationBar).ifPresent(i -> i.setMax(totalProgress));
        String totalDuration;
        String currentPosition;
        if (totalProgress > 1000 * 60 * 60) {
            totalDuration = mHourFormat.format(totalProgress);
            currentPosition = mHourFormat.format(currentProgress);
        } else {
            totalDuration = mMinFormat.format(totalProgress);
            currentPosition = mMinFormat.format(currentProgress);
        }
        Optional.ofNullable(mTotalDuration).ifPresent(t -> t.setText(totalDuration));
        Optional.ofNullable(mCurrentPosition).ifPresent(c -> c.setText(currentPosition));
        if (!mIsUserTouchProgressBar) {
            // 计算当前的进度
            Optional.ofNullable(mDurationBar).ifPresent(i -> i.setProgress(currentProgress));
        }
    }


    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {

        Optional.ofNullable(mTrackTitle).ifPresent(i -> i.setText(track.getTrackTitle()));
        Optional.ofNullable(mTrackPageView).ifPresent(i -> i.setCurrentItem(playIndex, true));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.d(TAG, "mIsUserTouchProgressBar = " + mIsUserTouchProgressBar);
        // 当页面选中的时候，就去切换播放的内容
        Optional.ofNullable(mPlayerPresenter).ifPresent(playerPresenter -> {
            LogUtil.d(TAG, "mIsUserTouchProgressBar = " + mIsUserTouchProgressBar);
            if (mIsUserTouchProgressBar) {
                playerPresenter.playByIndex(position);
            }
            mIsUserTouchProgressBar = false;
        });
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
