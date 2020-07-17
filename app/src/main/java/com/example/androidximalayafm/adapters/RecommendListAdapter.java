package com.example.androidximalayafm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidximalayafm.R;
import com.example.androidximalayafm.utils.LogUtil;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * RecommendListAdapter
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/7/8 10:40 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {
    private static final String TAG = "RecommendListAdapter";
    private List<Album> mData = new ArrayList<>();
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);
        LogUtil.d(TAG, "onCreateViewHolder");
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        // 返回要显示的个数
        if (mData!=null) {
            return mData.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
//        Optional.of(albumList).ifPresent(mData->{
//            mData.clear();
//            mData.addAll(albumList);
//        });
        if (albumList!=null) {
            mData.clear();
            mData.addAll(albumList);
        }
        LogUtil.d("setData", mData.toString());
        // 通知界面更新
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder{
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            ImageView albumCoverIv = itemView.findViewById(R.id.album_cover);
            TextView albumTitleTv = itemView.findViewById(R.id.album_title_tv);
            TextView descIv = itemView.findViewById(R.id.album_description_tv);
            TextView albumPlayCountTv = itemView.findViewById(R.id.album_play_count);
            TextView albumContentCountTv = itemView.findViewById(R.id.album_content_size);
            albumTitleTv.setText(album.getAlbumTitle());
            descIv.setText(album.getAlbumIntro());
            albumPlayCountTv.setText(album.getPlayCount()+"");
            albumContentCountTv.setText(album.getIncludeTrackCount()+"");
            Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCoverIv);
        }
    }
}
