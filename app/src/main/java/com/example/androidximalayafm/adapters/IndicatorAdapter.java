package com.example.androidximalayafm.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.example.androidximalayafm.MainActivity;
import com.example.androidximalayafm.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

public class IndicatorAdapter extends CommonNavigatorAdapter {

    private final String[] mTitles;

    public IndicatorAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.indicator_title);
    }

    @Override
    public int getCount() {
        if (mTitles != null) {
            return mTitles.length;
        }
        return 0;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        // 创建view
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        // 设置一般颜色为灰色
        colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
        // 设置选中情况下为黑色
        colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
        // 单位sp
        colorTransitionPagerTitleView.setTextSize(18);
        // 设置要显示的内容
        colorTransitionPagerTitleView.setText(mTitles[index]);
        // 设置title的点击事件，这里的话，如果点击了title，那么就会选中下面的viewPager到对应的index里面去
        // 也就是说，当我们点击了title的时候，下面的viewpager会对应着index进行切换内容
        colorTransitionPagerTitleView.setOnClickListener(view -> {

        });
        return colorTransitionPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
        linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        linePagerIndicator.setColors(Color.WHITE);
        return linePagerIndicator;
    }
}
