package com.example.oneread.ui.bookcase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.ui.detail.DetailActivity;
import com.example.oneread.utils.MODE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BookCaseAdapter extends RecyclerView.Adapter<BookCaseAdapter.ViewHolder>{

    Context context;
    List<Book> books = new ArrayList<>();
    RecyclerListener listener;

    public BookCaseAdapter() {
    }

    public BookCaseAdapter(Context context, List<Book> books, RecyclerListener listener) {
        this.context = context;
        this.books = books;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.book_item_rect, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        File thumb = new File(books.get(position).getThumb());
        Glide.with(context).load(thumb).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(holder.thumb);
        holder.title_comic.setText(books.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumb;
        TextView title_comic;
        public ViewHolder( View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title_comic = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("endpoint", books.get(getAdapterPosition()).getEndpoint());
                intent.putExtra("mode", MODE.OFFLINE);
                context.startActivity(intent);
//                listener.onDeleteItemClick(getAdapterPosition());
            });
        }
    }
}
