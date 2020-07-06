package com.example.androidximalayafm.fragments;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.example.androidximalayafm.R;
import com.example.androidximalayafm.base.BaseFragment;
import com.example.androidximalayafm.utils.Constants;
import com.example.androidximalayafm.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RecommendFragment  extends BaseFragment {
    private static final String TAG = "RecommendFragment";

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        View rootView = layoutInflater.inflate(R.layout.fragment_recommend, container, false);
        
        
        getRecommendData();
        return rootView;
    }

    /**
     * 获取推荐内容，其实就是猜你喜欢
     */
    private void getRecommendData() {

        Map<String,String> map = new HashMap<>();
        // 这个参数表示一夜数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT +"");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                // 数据获取成功
                Optional.ofNullable(gussLikeAlbumList)
                        .flatMap(s -> Optional.ofNullable(s.getAlbumList()))
                        .ifPresent((albumList -> {
                        LogUtil.d(TAG, "size --> " + albumList);
                }));
            }

            @Override
            public void onError(int i, String s) {
                // 获取获取失败
                LogUtil.d(TAG, "error --> " + i);
                LogUtil.d(TAG, "errorMsg -- >" + s);
            }
        });
    }
}
