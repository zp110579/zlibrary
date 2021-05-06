/*
 * Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.greenrobot.eventbus.meta;

import org.greenrobot.eventbus.DispenseOrder;
import org.greenrobot.eventbus.ThreadMode;

public class SubscriberMethodInfo {
    final String methodName;
    final ThreadMode threadMode;
    final Class<?> eventType;
    final int priority;
    final boolean sticky;
    final String tag;
    final boolean isFinish;
    final boolean mlifo;
    final boolean ignoredClassTag;


    public SubscriberMethodInfo(String methodName, Class<?> eventType, ThreadMode threadMode,
                                int priority, boolean sticky, String tag, boolean isFinish, boolean lifo, boolean ignoredClassTag) {
        this.methodName = methodName;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.priority = priority;
        this.sticky = sticky;
        this.tag = tag;
        this.isFinish = isFinish;
        this.mlifo = lifo;
        this.ignoredClassTag = ignoredClassTag;
    }

    public SubscriberMethodInfo(String methodName, Class<?> eventType, ThreadMode threadMode,
                                int priority, boolean sticky) {
        this(methodName, eventType, threadMode, priority, sticky, "", false, false, false);
    }

    public String getMethodName() {
        return methodName;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isSticky() {
        return sticky;
    }

    public String getTag() {
        return tag;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public boolean getMlifo() {
        return mlifo;
    }

    public boolean isIgnoredClassTag() {
        return ignoredClassTag;
    }

    public SubscriberMethodInfo(String methodName, Class<?> eventType) {
        this(methodName, eventType, ThreadMode.POSTING, 0, false, "", false, false, false);
    }

    public SubscriberMethodInfo(String methodName, Class<?> eventType, ThreadMode threadMode) {
        this(methodName, eventType, threadMode, 0, false, "", false, false, false);
    }

    public SubscriberMethodInfo(String methodName, Class<?> eventType, ThreadMode threadMode, String tag) {
        this(methodName, eventType, threadMode, 0, false, tag, true, true, false);
    }

    public SubscriberMethodInfo(String methodName, Class<?> eventType, int priority, boolean sticky, String tag) {
        this(methodName, eventType, priority, sticky, tag, false);
    }


    public SubscriberMethodInfo(String methodName, Class<?> eventType, int priority, boolean sticky, String tag, boolean isFinish) {
        this(methodName, eventType, ThreadMode.MAIN, priority, sticky, tag, isFinish, false, false);
    }

    //SubscribeMainThread 专有
    public SubscriberMethodInfo(String methodName, Class<?> eventType, int priority, boolean sticky, String tag, boolean isFinish, boolean lifo) {
        this(methodName, eventType, ThreadMode.MAIN, priority, sticky, tag, isFinish, lifo, false);
    }

    //SubscribeMainThread ignoredClassTag 为真的时候用此方法
    public SubscriberMethodInfo(String methodName, Class<?> eventType, int priority, boolean sticky, String tag, boolean isFinish, boolean lifo, boolean ignoredClassTag) {
        this(methodName, eventType, ThreadMode.MAIN, priority, sticky, tag, isFinish, lifo, ignoredClassTag);
    }

    public SubscriberMethodInfo(String methodName, Class<?> eventType,  String tag) {
        this(methodName, eventType, ThreadMode.MAIN, 0, false, tag, false, false, false);
    }

}