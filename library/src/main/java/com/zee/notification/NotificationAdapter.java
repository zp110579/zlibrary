package com.zee.notification;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.widget.RemoteViews;

import com.zee.utils.UIUtils;

public abstract class NotificationAdapter {
    private RemoteViews mRemoteViews;

    public abstract @LayoutRes
    int getLayoutResID();

    RemoteViews getRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(UIUtils.getApplication().getPackageName(), getLayoutResID());
        mRemoteViews = remoteViews;
        initViews(remoteViews);
        return mRemoteViews;
    }

    protected abstract void initViews(RemoteViews parentView);

    public void setOnClickActivity(int viewId, Class<?> cls) {
        Intent intent = new Intent(UIUtils.getCurActivity(), cls);
        setOnClickIntent(viewId, intent);
    }

    public void setOnClickIntent(int viewId, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(UIUtils.getCurActivity(), 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        setOnClickPendingIntent(viewId, pendingIntent);
    }

    public void setOnClickPendingIntent(int viewId, PendingIntent pendingIntent) {
        mRemoteViews.setOnClickPendingIntent(viewId, pendingIntent);
    }

    public void setText(int viewId, CharSequence text) {
        mRemoteViews.setTextViewText(viewId, text);
    }
}
