package com.zee.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.annotation.*
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.zee.adapter.OnItemClickListener
import com.zee.adapter.OnItemViewClickListener
import com.zee.bean.RecyclerViewHolder
import com.zee.utils.UIUtils

abstract class Z1RecyclerAdapter<E>(@LayoutRes layoutID: Int) : BaseRVAdapter<E>() {
    private var mItemClickListener: OnItemClickListener<E>? = null
    private var mOnItemViewClickListener: OnItemViewClickListener<E>? = null

    init {
        setLayoutResID(layoutID)
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener<E>?) {
        mItemClickListener = itemClickListener
    }

    protected fun addOnItemViewClickListener(@IdRes id: Int) {
        val loView = findViewById<View>(id)
        setOnItemClickListener(loView)
    }

    protected fun setOnItemClickListener(view: View) {
        val index = curIndex
        val bean = get(curIndex)
        view.setOnClickListener(object : OnNoDoubleClickListener() {
            override fun onNoDoubleClick(v: View) {
                if (mOnItemViewClickListener != null) {
                    mOnItemViewClickListener!!.onItemClick(bean, v, index)
                }
            }
        })
    }

    fun setOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener<E>) {
        mOnItemViewClickListener = onItemViewClickListener
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        super.onBindViewHolder(viewHolder, position)
        val bean = mList[position]

        initViews(viewHolder.convertView, position, bean)
        mItemClickListener?.apply {
            viewHolder.convertView.setOnClickListener(object : OnNoDoubleClickListener() {
                override fun onNoDoubleClick(v: View) {
                    onItemClick(bean, position)
                }
            })
        }
    }

     abstract fun initViews(parentView: View, location: Int, bean: E)

}