package com.example.oneread.ui.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Chapter;

import java.util.List;

public class DownloadChapterAdapter extends RecyclerView.Adapter<DownloadChapterAdapter.ViewHolder>{
    private List<Chapter> chapters;
    private Context context;
    private List<Boolean> listBookDownloaded;
    private List<Boolean> listCheck;
    private Callback callback;

    //todo: truyen vao list chapter da download de an button check


    public interface Callback {
        void onChapterItemClick(int position, boolean isChecked);
    }

    public DownloadChapterAdapter() {
    }

    public DownloadChapterAdapter(Context context, List<Chapter> chapters, List<Boolean> listBookDownloaded, List<Boolean> listCheck) {
        this.context = context;
        this.chapters = chapters;
        this.listBookDownloaded = listBookDownloaded;
        this.listCheck = listCheck;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.chapter_download_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.chapter_title.setText(chapters.get(position).getTitle());
        holder.chapter_uploaded.setText(chapters.get(position).getTime().split(" ")[0]);
        if (!listBookDownloaded.get(position)) {
            if (listCheck.get(position)) {
                holder.btn_check.setImageResource(R.drawable.ic_check);
                holder.btn_check.setTag(R.drawable.ic_check);
            } else {
                holder.btn_check.setImageResource(R.drawable.ic_uncheck);
                holder.btn_check.setTag(R.drawable.ic_uncheck);
            }
        } else {
            holder.btn_check.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chapter_title, chapter_uploaded;
        ImageView btn_check;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chapter_title = itemView.findViewById(R.id.chapter_title);
            chapter_uploaded = itemView.findViewById(R.id.chapter_uploaded);
            btn_check = itemView.findViewById(R.id.btn_check);
            btn_check.setTag(R.drawable.ic_uncheck);

            int uncheck_id = (int) btn_check.getTag();

            itemView.setOnClickListener(v -> {
                if (btn_check.getVisibility() == View.INVISIBLE) {
                    return;
                }
                if(((int) btn_check.getTag()) == uncheck_id){
                    btn_check.setImageResource(R.drawable.ic_check);
                    btn_check.setTag(R.drawable.ic_check);
                    callback.onChapterItemClick(getAdapterPosition(), true);
                }else{
                    btn_check.setImageResource(R.drawable.ic_uncheck);
                    btn_check.setTag(R.drawable.ic_uncheck);
                    callback.onChapterItemClick(getAdapterPosition(), false);
                }
            });
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
