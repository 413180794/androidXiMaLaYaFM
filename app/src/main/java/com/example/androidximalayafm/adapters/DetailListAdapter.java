package com.example.androidximalayafm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidximalayafm.R;
import com.example.androidximalayafm.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DetailListAdapter
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/8/13 10:19 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {
    private List<Track> mDetailData = new ArrayList<>();
    // 格式化时间
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat durationFormat = new SimpleDateFormat("mm:ss");
    private ItemClickListener itemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(mDetailData, position);
    }

    @Override
    public int getItemCount() {
        return mDetailData.size();
    }

    public void setData(List<Track> tracks) {
        // 更新数据 清楚原来的数据
        mDetailData.clear();
        // 添加新的数据
        mDetailData.addAll(tracks);
        // 更新 UI
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(List<Track> detailData, int position) {
            Track track = detailData.get(position);
            TextView orderText = itemView.findViewById(R.id.order_text);
            TextView detailTitle = itemView.findViewById(R.id.detail_title);
            TextView amountOfPlay = itemView.findViewById(R.id.amount_of_play);
            TextView detailTime = itemView.findViewById(R.id.detail_time);
            TextView detailDate = itemView.findViewById(R.id.detail_date);

            orderText.setText((Integer) itemView.getTag() + "");
            detailTitle.setText(track.getTrackTitle());
            amountOfPlay.setText(track.getPlayCount() + "");
            int durationMI = track.getDuration() * 1000;
            detailTime.setText(durationFormat.format(durationMI) + "");
            String updateTimeText = mSimpleDateFormat.format(track.getUpdatedAt());
            detailDate.setText(updateTimeText);

            itemView.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "", Toast.LENGTH_SHORT).show();
                Optional.ofNullable(itemClickListener).ifPresent(i-> this.accept(i, position));
            });
        }

        private void accept(ItemClickListener i, int position) {
            i.onItemClick(mDetailData, position);
        }

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(List<Track> detailData, int position);
    }
}
