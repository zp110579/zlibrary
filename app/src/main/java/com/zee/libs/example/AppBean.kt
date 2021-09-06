package com.zee.libs.example

import com.zee.bean.ITitle


class TabBean(var name:String) : ITitle {
    override fun getTitle(): String {
        return name
    }
}
