package fr.epf.min1.gc

import android.util.Log
import android.view.View

fun View.click(action: (View) -> Unit) {
    Log.d("EPF", "click !!")
    this.setOnClickListener(action)
}