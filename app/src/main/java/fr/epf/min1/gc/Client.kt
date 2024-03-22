package fr.epf.min1.gc

enum class Gender {
    MAN,WOMAN
}
data class Client(
    val lastname: String,
    val firstname: String,
    val gender: Gender
){
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