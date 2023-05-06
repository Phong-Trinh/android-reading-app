package com.example.oneread.ui.main.notify;

import com.example.oneread.data.network.model.Notify;
import com.example.oneread.ui.base.BaseContract;

import java.util.List;

public interface NotificationContract {

    interface View extends BaseContract.View {

        void setNotifications(List<Notify> notifications);

        void removeReadNotification(List<Notify> notifications);

        void updateNotification(Notify notify);

    }

    interface Presenter <V extends View> extends BaseContract.Presenter <V> {

        void getAllNotifications();

        void deleteReadNotification();

        void readNotification(String endpoint);

    }

}
