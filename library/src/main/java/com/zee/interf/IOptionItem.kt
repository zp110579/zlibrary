package com.zee.interf

/**
 *created by zee on 2020/9/4.
 *
 */
interface IOptionItem {
    /**
     * 返回显示的内容
     */
    fun getTitle(): String

    /**
     * 是否有对应的2级菜单,没有返回null即可
     */
    fun getList(): ArrayList<IOptionItem>?
}