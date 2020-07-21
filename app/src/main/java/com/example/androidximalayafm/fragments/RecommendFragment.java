package com.example.androidximalayafm.fragments;

import android.graphics.Rect;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment  extends BaseFragment {
    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mRecommendRv;
    private RecommendListAdapter mRecommendListAdapter;
    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        // View 加载完成
        View mRootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_recommend, container, false);
        // 获recycleview
        mRecommendRv = mRootView.findViewById(R.id.recommend_list);
        // 加载布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendRv.setLayoutManager(linearLayoutManager);
        mRecommendRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);

            }
        });
        // 设置适配器
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRv.setAdapter(mRecommendListAdapter);
        // 设置数据
        getRecommendData();
        return mRootView;
    }

    /**
     * 获取推荐内容，其实就是猜你喜欢
     */
    private void getRecommendData() {

        Map<String, String> map = new HashMap<>();
        // 这个参数表示一夜数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    if (albumList != null) {
                        setRecycleViewData(albumList);
                    }
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

    private void setRecycleViewData(List<Album> albumList) {
        mRecommendListAdapter.setData(albumList);
    }

}
