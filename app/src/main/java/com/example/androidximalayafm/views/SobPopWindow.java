package com.example.androidximalayafm.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.example.androidximalayafm.R;
import com.example.androidximalayafm.base.BaseApplication;

/**
 * SobPopWindow
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/10/12 11:22 PM
 * @since 1.0.0
 * <p>
 * Description:
 */
public class SobPopWindow extends PopupWindow {
    public SobPopWindow() {
        // 设置宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 载入 view
        View popView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null);
        // 设置内容
        setContentView(popView);
    }
}
