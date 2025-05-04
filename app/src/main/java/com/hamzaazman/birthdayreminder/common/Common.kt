package com.hamzaazman.birthdayreminder.common

import android.view.View

fun View.setVisibleIf(condition: Boolean) {
    if (condition && visibility != View.VISIBLE) visibility = View.VISIBLE
    else if (!condition && visibility != View.GONE) visibility = View.GONE
}
fun View.updateVisibilityIfNeeded(show: Boolean) {
    val shouldChange = (show && visibility != View.VISIBLE) || (!show && visibility != View.GONE)
    if (shouldChange) {
        visibility = if (show) View.VISIBLE else View.GONE
    }
}
