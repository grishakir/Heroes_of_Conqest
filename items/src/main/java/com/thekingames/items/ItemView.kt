package com.thekingames.items

import android.content.Context
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.item.view.*

val qualitiesColorId: Array<Int> = arrayOf(
        R.color.color_quality_1,
        R.color.color_quality_2,
        R.color.color_quality_3,
        R.color.color_quality_4,
        R.color.color_quality_5,
        R.color.color_quality_6,
        R.color.color_quality_7
)

val qualitiesNameId: Array<Int> = arrayOf(
        R.string.quality_1,
        R.string.quality_2,
        R.string.quality_3,
        R.string.quality_4,
        R.string.quality_5,
        R.string.quality_6,
        R.string.quality_7
)

open class ItemView(var context: Context, idSrc: Int, var count: Int) {
    var quality: Int = 0
        set(value) {
            if (value < qualitiesColorId.size && value >= 0) {
                field = value
                view.item_root.setBackgroundResource(qualitiesColorId[value])
            }
        }
    val view: View = View.inflate(context, R.layout.item, null)

    val image: ImageView = view.findViewById(R.id.item_icon)

    var qualityName: String = context.getString(qualitiesNameId[quality])
        get() {
            return context.getString(qualitiesNameId[quality])
        }

    init {
        if (count > 1) {
            view.count.setText(count.toString())
        }
        image.setImageResource(idSrc)
    }
}