package com.thekingames.tasks

import android.content.Context
import android.view.View
import android.widget.ImageView

class TaskView(context: Context,idSrc:Int) {
    val view: View = View.inflate(context, R.layout.task, null)
    val image: ImageView = view.findViewById(R.id.icon)

    init {
        image.setImageResource(idSrc)
    }
}