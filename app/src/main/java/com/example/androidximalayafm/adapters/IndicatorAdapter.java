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
        // ����view
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        // ����һ����ɫΪ��ɫ
        colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
        // ����ѡ�������Ϊ��ɫ
        colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
        // ��λsp
        colorTransitionPagerTitleView.setTextSize(18);
        // ����Ҫ��ʾ������
        colorTransitionPagerTitleView.setText(mTitles[index]);
        // ����title�ĵ���¼�������Ļ�����������title����ô�ͻ�ѡ�������viewPager����Ӧ��index����ȥ
        // Ҳ����˵�������ǵ����title��ʱ�������viewpager���Ӧ��index�����л�����
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
