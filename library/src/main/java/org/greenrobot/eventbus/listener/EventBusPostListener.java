package org.greenrobot.eventbus.listener;


import java.lang.reflect.Method;

public interface EventBusPostListener {

    void onPostStart();

    void onPostProcess(Object object, Method paMethod, int index);

    void onPostNoFound();

    void onPostError(Exception paE);

    void onPostEnd();
}
