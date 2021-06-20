package org.greenrobot.eventbus.adapter;

import com.zee.log.ZLog;

import org.greenrobot.eventbus.listener.EventBusPostListener;

import java.lang.reflect.Method;

public abstract class EventBusPostAdapter implements EventBusPostListener {
    @Override
    public void onPostStart() {
        ZLog.i("EventBus 【Post】 onPostStart()");
    }

    @Override
    public void onPostProcess(Object object, Method paMethod, int index) {
        StringBuilder loStringBuilder = new StringBuilder();
        loStringBuilder.append("object: " + object.getClass().getSimpleName());
        loStringBuilder.append(",method=" + paMethod.getName());
        loStringBuilder.append(",index: " + index);
        ZLog.i("EventBus 【Post】 onPostProcess(" + loStringBuilder.toString() + ")");
    }

    @Override
    public void onPostNoFound() {
        ZLog.i("EventBus 【Post】 onPostNoFound()");
    }

    @Override
    public void onPostError(Exception paE) {
        ZLog.e("EventBus【Post】 onPostError(" + paE.getMessage() + ")");
    }

    @Override
    public void onPostEnd() {
        ZLog.i("EventBus 【Post】 onPostEnd()");
    }
}
