package com.example.androidximalayafm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.androidximalayafm.adapters.PlayerTrackPagerAdapter;
import com.example.androidximalayafm.interfaces.IPlayerCallback;
import com.example.androidximalayafm.presenters.PlayerPresenter;
import com.example.androidximalayafm.utils.LogUtil;
import com.example.androidximalayafm.views.SobPopWindow;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    private ImageView mPlayModeSwitchBtn;
    private XmPlayListControl.PlayMode mCurrentMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
    private static Map<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode> sPlayModeRule = new HashMap<>();

    static {
        sPlayModeRule.put(XmPlayListControl.PlayMode.PLAY_MODEL_LIST, XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP);
        sPlayModeRule.put(XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP, XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM);
        sPlayModeRule.put(XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM, XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP);
        sPlayModeRule.put(XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP, XmPlayListControl.PlayMode.PLAY_MODEL_LIST);
    }

    private ImageView mPlayListBtn;
    private SobPopWindow mSobPopWindow;
    private View mPlayListBtn1;

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
    @SuppressLint("ClickableViewAccessibility")
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
                if (action == MotionEvent.ACTION_DOWN) {
                    LogUtil.d(TAG, "ACTION DOWN");
                    mIsUserTouchProgressBar = true;
                }
                return false;

            });
        });


        Optional.ofNullable(mPlayModeSwitchBtn).ifPresent(playModeSwitch -> {
            playModeSwitch.setOnClickListener(v -> {
                // 根据当前的 mode 获取到下一个 mode / 注意这里使用一个 map 来实现了循环
                XmPlayListControl.PlayMode playMode = sPlayModeRule.get(mCurrentMode);
                Optional.ofNullable(mPlayerPresenter).ifPresent(playerPresenter -> {
                    playerPresenter.switchPlayMode(playMode);
                });
            });
        });
        Optional.ofNullable(mPlayListBtn).ifPresent(playListBtn->{
            playListBtn.setOnClickListener(v->{
                // 展示播放列表
                mSobPopWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                // 处理一下背景，有点透明度
                updateBgAlpha(0.8f);
            });
        });
        mSobPopWindow.setOnDismissListener(() -> {
            // pop 窗体消失以后，恢复透明度
            updateBgAlpha(1.0f);
        });
    }

    public void updateBgAlpha(float alpha) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = alpha;
        window.setAttributes(attributes);
    }


    /**
     * 根据当前的状态，更新播放模式的图标
     *
     * @param playMode
     */
    private void updatePlayModeBtnImg(XmPlayListControl.PlayMode playMode) {
        int resId = R.drawable.selector_player_mode_list_order;
        switch (playMode) {
            case PLAY_MODEL_LIST:
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.drawable.selector_play_mode_random;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_player_mode_list_looper;
                break;
            case PLAY_MODEL_SINGLE:
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.drawable.selector_play_mode_single_loop;
                break;
        }
        mPlayModeSwitchBtn.setImageResource(resId);
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
        // 播放列表
        mPlayListBtn = this.findViewById(R.id.play_list);
        // 创建适配器
        mTrackPagerAdapter = new PlayerTrackPagerAdapter();
        // 设置适配器
        mTrackPageView.setAdapter(mTrackPagerAdapter);
        mPlayModeSwitchBtn = this.findViewById(R.id.player_mode_switch_btn);

        mSobPopWindow = new SobPopWindow();
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
        updatePlayModeBtnImg(playMode);
        mCurrentMode = playMode;
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
