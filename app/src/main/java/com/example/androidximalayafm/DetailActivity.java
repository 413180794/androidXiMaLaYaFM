package com.example.androidximalayafm;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidximalayafm.adapters.DetailListAdapter;
import com.example.androidximalayafm.interfaces.IAlbumDetailViewCallback;
import com.example.androidximalayafm.presenters.AlbumDetailPresenter;
import com.example.androidximalayafm.utils.ImageBlur;
import com.example.androidximalayafm.utils.LogUtil;
import com.example.androidximalayafm.views.RoundRectImageView;
import com.example.androidximalayafm.views.UILoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * DetailActivity
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/1 11:20 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, UILoader.OnRetryClickListener {


    private RoundRectImageView mSmallCover;
    private ImageView mLargeCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private String TAG = "DetailActivity";
    private int mCurrentPage = 1;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;
    private FrameLayout mDetailListContainer;
    private UILoader mUiLoader;
    private int mCurrentAlbumId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);

        // 获取专辑的详情内容
    }

    private void initView() {
        mDetailListContainer = this.findViewById(R.id.detail_list_container);
        // 创建 UILoader
        mUiLoader = Optional.ofNullable(mUiLoader).orElse(new UILoader(this) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return createSuccessView(container);
            }
        });
        mUiLoader.setOnRetryClickListener(this);
        mDetailListContainer.removeAllViews();
        mDetailListContainer.addView(mUiLoader);
        mLargeCover = this.findViewById(R.id.iv_large_cover);
        mSmallCover = this.findViewById(R.id.viv_small_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        mAlbumAuthor = this.findViewById(R.id.tv_ablum_author);

        // recyclerview 的使用步骤


    }

    private View createSuccessView(ViewGroup container) {
        View detailListView = LayoutInflater.from(this).inflate(R.layout.item_detail_list, container, false);
        mDetailList = detailListView.findViewById(R.id.album_detail_list);
        // 第一步： 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDetailList.setLayoutManager(layoutManager);
        // 第二步， 设置适配器
        mDetailListAdapter = new DetailListAdapter();
        mDetailList.setAdapter(mDetailListAdapter);
        // 设置 item 的间距
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 2);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 2);
                outRect.left = UIUtil.dip2px(view.getContext(), 2);
                outRect.right = UIUtil.dip2px(view.getContext(), 2);
            }
        });
        return detailListView;
    }


    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        // 判断数据结果，根据结果控制 UI 显示
        Optional.ofNullable(tracks).ifPresent(t -> {
            Optional<UILoader> optionalUILoader = Optional.ofNullable(mUiLoader);
            if (t.size() == 0) {
                optionalUILoader.ifPresent(uiLoader -> uiLoader.updateStatus(UILoader.UIStatue.EMPTY));
            } else {
                optionalUILoader.ifPresent(uiLoader -> uiLoader.updateStatus(UILoader.UIStatue.SUCCESS));
            }
        });
        // 更新或设置
        mDetailListAdapter.setData(tracks);
    }

    @Override
    public void onAlbumLoaded(Album album) {
        mCurrentAlbumId = (int) album.getId();
        mAlbumDetailPresenter.getAlbumDetail(mCurrentAlbumId, mCurrentPage);
        // 拿数据，显示 Loading 状态
        Optional.ofNullable(mUiLoader).ifPresent(uiLoader -> uiLoader.updateStatus(UILoader.UIStatue.LOADING));
        Optional.ofNullable(mAlbumTitle).ifPresent(albumTitle -> albumTitle.setText(album.getAlbumTitle()));
        Optional.ofNullable(mAlbumAuthor).ifPresent(albumAuthor -> albumAuthor.setText(album.getAnnouncer().getNickname()));
        Optional.ofNullable(mLargeCover).ifPresent(largeCover -> Picasso.with(this).load(album.getCoverUrlLarge()).into(mLargeCover, new Callback() {
            @Override
            public void onSuccess() {
                Drawable drawable = mLargeCover.getDrawable();
                if (drawable != null) {
                    ImageBlur.makeBlur(mLargeCover, DetailActivity.this);
                }
            }

            @Override
            public void onError() {
                LogUtil.d(TAG, "onerror");
            }
        }));
        Optional.ofNullable(mSmallCover).ifPresent(smallCover -> Picasso.with(this).load(album.getCoverUrlLarge()).into(mSmallCover));
    }

    @Override
    public void onNetworkError(int errorCode, String errorMsg) {
        // 请求 发生错误，显示网络异常状态
        Optional.ofNullable(mUiLoader).ifPresent(uiLoader -> uiLoader.updateStatus(UILoader.UIStatue.NETWORK_ERROR));
    }

    @Override
    public void onRetryClick() {
       Optional.ofNullable(mAlbumDetailPresenter).ifPresent(albumDetailPresenter -> {
           albumDetailPresenter.getAlbumDetail(mCurrentAlbumId, mCurrentPage);
       });
    }
}
