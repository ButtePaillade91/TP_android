package fr.epf.min1.gc

import android.util.Log
import android.view.View

fun Client.getImage()  =
    when(this.gender) {
        Gender.MAN -> R.drawable.man
        Gender.WOMAN -> R.drawable.woman
    }

val Client.name : String
    get() = "${firstname} ${lastname}"

fun View.click(action: (View) -> Unit){
    Log.d("EPF", "click !!")
    this.setOnClickListener(action)
}
