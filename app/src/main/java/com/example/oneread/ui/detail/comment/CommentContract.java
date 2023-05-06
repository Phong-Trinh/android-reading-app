package com.example.oneread.ui.detail.comment;

import com.example.oneread.data.network.model.Comment;
import com.example.oneread.ui.base.BaseContract;
import okhttp3.MultipartBody;

import java.util.List;

public interface CommentContract {
    interface View extends BaseContract.View {

        void setComments(List<Comment> comments);

        void setReplies(List<Comment> replies, int adapterPosition);

        void addComment(Comment comment);

        void addReply(Comment comment, int position);

    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void loadComments(String endpoint);

        void loadReplies(int id, int adapterPosition);

        void addComment(String bookEndpoint, MultipartBody.Part[] body);

        void addReply(String bookEndpoint, MultipartBody.Part[] body, int position);

    }
}
