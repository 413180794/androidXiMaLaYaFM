package com.example.androidximalayafm.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidximalayafm.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {
    private List<Album> albumList = new ArrayList<>();
    private View mView;
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend,parent,false);
        return new InnerHolder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        Album album = albumList.get(position);
        holder.itemView.setTag(position);
        holder.setData(album);
    }

    @Override
    public int getItemCount() {
        if (albumList != null) {
            return albumList.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
        if (albumList != null) {
            this.albumList.clear();
            this.albumList.addAll(albumList);
        }
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            ImageView imageView = itemView.findViewById(R.id.album_cover);
            TextView albumPlayCount = itemView.findViewById(R.id.album_play_count);
            TextView albumContentSize = itemView.findViewById(R.id.album_content_size);
            TextView albumDescTv = itemView.findViewById(R.id.album_description_tv);
            TextView albumTitleTv = itemView.findViewById(R.id.album_title_tv);

            albumPlayCount.setText(album.getPlayCount()+"");
            albumContentSize.setText(album.getIncludeTrackCount()+"");
            albumDescTv.setText(album.getAlbumIntro());
            albumTitleTv.setText(album.getAlbumTitle());
            Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(imageView);
        }
    }
}