package com.example.androidximalayafm;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.androidximalayafm.presenters.PlayerPresenter;

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
public class PlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        // TODO: 测试一下播放
        PlayerPresenter playerPresenter = PlayerPresenter.getInstance();
        playerPresenter.play();
    }

}
