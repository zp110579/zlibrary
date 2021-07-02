package org.greenrobot.eventbus.listener;

/**
 * 如何对象实现该接口，EventBus注销的时候会自动调用该接口
 */
public interface EventBusUnregisterListener {

    void onEventBusUnRegister();
}
