package com.zee.extendobject

import android.support.annotation.IdRes
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zee.view.ZxRecyclerView

fun ViewGroup.texViewById(@IdRes id: Int): TextView {
    return findViewById(id)
}

fun ViewGroup.editTextById(@IdRes id: Int): EditText {
    return findViewById(id)
}

fun ViewGroup.imageViewById(@IdRes id: Int): ImageView {
    return findViewById(id)
}

fun ViewGroup.viewById(@IdRes id: Int): View {
    return findViewById(id)
}

fun ViewGroup.linearLayoutById(@IdRes id: Int): LinearLayout {
    return findViewById(id)
}

fun ViewGroup.relativeLayoutById(@IdRes id: Int): RelativeLayout {
    return findViewById(id)
}

fun ViewGroup.recyclerViewById(@IdRes id: Int): ZxRecyclerView {
    return findViewById(id)
}



