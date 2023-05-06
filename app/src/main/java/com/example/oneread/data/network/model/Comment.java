package com.example.oneread.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Comment implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("book_endpoint")
    @Expose
    private String bookEndpoint;
    @SerializedName("id_root")
    @Expose
    private Integer idRoot;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("files")
    @Expose
    private List<String> files;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("replies")
    @Expose
    private List<Comment> replies;

    public Comment() {
        files = new ArrayList<>();
        user = new User();
        replies = new ArrayList<>();
        id = -1;
        idRoot = -1;
    }

    public Comment(Integer id, String bookEndpoint, Integer idRoot, String content, String time, List<String> files, User user, List<Comment> replies) {
        this.id = id;
        this.bookEndpoint = bookEndpoint;
        this.idRoot = idRoot;
        this.content = content;
        this.time = time;
        this.files = files;
        this.user = user;
        this.replies = replies;
    }

    public Comment(Comment comment) {
        this.id = comment.getId();
        this.bookEndpoint = comment.getBookEndpoint();
        this.idRoot = comment.getIdRoot();
        this.content = comment.getContent();
        this.time = comment.getTime();
        this.files = comment.getFiles();
        this.user = comment.getUser();
        this.replies = comment.getReplies();
    }

    protected Comment(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        bookEndpoint = in.readString();
        if (in.readByte() == 0) {
            idRoot = null;
        } else {
            idRoot = in.readInt();
        }
        content = in.readString();
        time = in.readString();
        files = in.createStringArrayList();
        user = in.readParcelable(User.class.getClassLoader());
        replies = in.createTypedArrayList(Comment.CREATOR);
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public Comment clone() {
        return new Comment(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookEndpoint() {
        return bookEndpoint;
    }

    public void setBookEndpoint(String endpoint) {
        this.bookEndpoint = endpoint;
    }

    public Integer getIdRoot() {
        return idRoot;
    }

    public void setIdRoot(Integer idRoot) {
        this.idRoot = idRoot;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(bookEndpoint);
        if (idRoot == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(idRoot);
        }
        parcel.writeString(content);
        parcel.writeString(time);
        parcel.writeStringList(files);
        parcel.writeParcelable(user, i);
        parcel.writeTypedList(replies);
    }
}
