package com.zee.log;

/**
 * Created by Administrator on 2017-04-19.
 */

interface ILog {

    void v(Object message);

    void v(String tag, Object message);

    void w(Object message);

    void w(String tag, Object message);

    void i(Object message);

    void i(String tag, Object message);

    void d(Object message);

    void d(String tag, Object message);

    void e(Object message);

    void e(String tag, Object message);
}
