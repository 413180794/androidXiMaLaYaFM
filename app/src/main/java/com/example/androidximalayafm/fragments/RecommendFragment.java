package com.example.androidximalayafm.fragments;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidximalayafm.R;
import com.example.androidximalayafm.adapters.RecommendListAdapter;
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
    private View mRootView;
    private RecyclerView mRecommendRv;
    private RecommendListAdapter mRecommendListAdapter;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        // View 加载完成
        mRootView = layoutInflater.inflate(R.layout.fragment_recommend, container, false);
        // 找到控件
        mRecommendRv = mRootView.findViewById(R.id.recommend_list);
        // 设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendRv.setLayoutManager(linearLayoutManager);
        // 设置适配器
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRv.setAdapter(mRecommendListAdapter);
        // 去拿数据回来
        getRecommendData();
        // 返回 view 给界面显示
        /**
         * 1. 布局管理器的设置
         * 2. 适配器的设置
         * 3. 数据的设置
         */
        return mRootView;
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
                if (gussLikeAlbumList != null) {
                    List<Album> albums = gussLikeAlbumList.getAlbumList();
                    updateRecommendUI(albums);
                }
            }

            @Override
            public void onError(int i, String s) {
                // 获取获取失败
                LogUtil.d(TAG, "error --> " + i);
                LogUtil.d(TAG, "errorMsg -- >" + s);
            }
        });
    }

    private void updateRecommendUI(List<Album> albumList) {
        // 把数据设置给适配器，并且更新 UI
        LogUtil.d(TAG, "update"+albumList);
        mRecommendListAdapter.setData(albumList);
    }
}
