package com.example.oneread.ui.main.notify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Notify;
import com.example.oneread.utils.MODE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private Callback callback;
    private List<Notify> notifications;

    public interface Callback {

        void onNotificationClick(int position);

    }


    public NotificationAdapter() {
    }

    public NotificationAdapter(Context context, List<Notify> notifications, Callback callback) {
        this.context = context;
        this.notifications = notifications;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.content.setText(notifications.get(position).getContent());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        try {
            Date d = format.parse(notifications.get(position).getTime());
            holder.time.setText(DateUtils.getRelativeTimeSpanString(d.getTime(),
                    Calendar.getInstance().getTimeInMillis(), DateUtils.FORMAT_ABBREV_RELATIVE).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.root.setBackgroundResource(notifications.get(position).getStatus() == 0 ? R.color.card : R.color.primary);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public List<Notify> getNotifications() {
        return notifications;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content, time;
        LinearLayout root;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            root = itemView.findViewById(R.id.root);

            itemView.setOnClickListener(v -> {
                callback.onNotificationClick(getAdapterPosition());
            });
        }
    }
}
