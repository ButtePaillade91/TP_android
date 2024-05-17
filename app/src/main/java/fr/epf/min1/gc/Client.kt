package fr.epf.min1.gc

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

enum class Gender {
    MAN,WOMAN
}

@Parcelize
data class Client(
    val lastname: String,
    val firstname: String,
    val gender: Gender
) : Parcelable{
    companion object {

        fun generateClients(size: Int = 20) =
             (1 .. size).map {
                Client(
                    "Nom${it}",
                    "Prénom${it}",
                    if(it %3 == 0) Gender.MAN else Gender.WOMAN
                )
            }


    }
}