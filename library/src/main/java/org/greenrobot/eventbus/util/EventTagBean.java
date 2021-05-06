package org.greenrobot.eventbus.util;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class EventTagBean {
    Object event;
    int type;

    public EventTagBean(Object event, int type) {
        this.event = event;
        this.type = type;
    }

}
