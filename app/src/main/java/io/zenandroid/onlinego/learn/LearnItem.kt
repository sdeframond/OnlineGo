package io.zenandroid.onlinego.learn

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.zenandroid.onlinego.R
import kotlinx.android.synthetic.main.item_learn.*

class LearnItem(
        private val title: String,
        private val subtitle: String,
        @DrawableRes private val icon: Int,
        private val ctaText: String
) : Item() {
    override fun bind(holder: GroupieViewHolder, position: Int) {
        holder.apply {
            titleView.text = title
            subtitleView.text = subtitle
            iconView.setImageResource(icon)
            ctaView.text = ctaText
        }
    }

    override fun getLayout(): Int = R.layout.item_learn
}