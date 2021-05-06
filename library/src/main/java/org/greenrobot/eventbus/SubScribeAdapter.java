package org.greenrobot.eventbus;

import org.greenrobot.eventbus.listener.EventBusPostListener;

import java.lang.reflect.Method;

class SubScribeAdapter implements EventBusPostListener {
    private int time = 0;
    private EventBusPostListener mEventBusPostListener;

    SubScribeAdapter(EventBusPostListener paEventBusPostListener) {
        mEventBusPostListener = paEventBusPostListener;
    }

    void onProcess(Object object, Method methodName) {
        if (mEventBusPostListener != null) {
            mEventBusPostListener.onPostProcess(object, methodName, time);
        }
        time++;
    }

    @Override
    public void onPostStart() {
        if (mEventBusPostListener != null) {
            mEventBusPostListener.onPostStart();
        }
    }

    @Override
    public void onPostProcess(Object object, Method paMethod, int index) {

    }

    @Override
    public void onPostNoFound() {
        if (mEventBusPostListener != null) {
            mEventBusPostListener.onPostNoFound();
        }
    }

    @Override
    public void onPostError(Exception paE) {
        if (mEventBusPostListener != null) {
            mEventBusPostListener.onPostError(paE);
        }
    }

    @Override
    public void onPostEnd() {
        if (mEventBusPostListener != null) {
            mEventBusPostListener.onPostEnd();
        }
    }
}
