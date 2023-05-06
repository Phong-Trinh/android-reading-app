package com.example.oneread.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.oneread.R;
import com.example.oneread.data.network.model.HistoryRead;
import com.example.oneread.ui.detail.DetailActivity;
import com.example.oneread.utils.MODE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryReadAdapter extends RecyclerView.Adapter<HistoryReadAdapter.ViewHolder> {

    private Context context;
    private List<HistoryRead> historyReads;

    public HistoryReadAdapter(Context context, List<HistoryRead> historyReads) {
        this.context = context;
        this.historyReads = historyReads;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_read_item, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.title.setText(historyReads.get(position).getBook().getTitle());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        try {
            Date d = format.parse(historyReads.get(position).getTime());
            viewHolder.time.setText(DateUtils.getRelativeTimeSpanString(d.getTime(),
                    Calendar.getInstance().getTimeInMillis(), DateUtils.FORMAT_ABBREV_RELATIVE).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        viewHolder.chapter_title.setText(historyReads.get(position).getChapter().getTitle());
        Glide.with(context).asBitmap().load(historyReads.get(position).getBook().getThumb())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_err)
                .into(viewHolder.thumb);
    }

    @Override
    public int getItemCount() {
        return historyReads.size();
    }

    public List<HistoryRead> getHistoryReads() {
        return historyReads;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView title, time, chapter_title;

        public ViewHolder(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            chapter_title = itemView.findViewById(R.id.chapter_title);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("endpoint", historyReads.get(getAdapterPosition()).getBook().getEndpoint());
                intent.putExtra("mode", MODE.ONLINE);
                context.startActivity(intent);
            });
        }
    }

}