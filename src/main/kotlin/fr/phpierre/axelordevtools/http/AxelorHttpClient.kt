package fr.phpierre.axelordevtools.http

abstract class AxelorHttpClient {
    //HttpClients.createDefault()

    companion object {
        var token: String = ""
    }

    init {
        //Chaque requete va devoir vérifier l'auth pour savoir si elle doit demander ou non un token.
    }

    fun auth() : AxelorHttpResponse {
        TODO()
    }

    abstract fun request() : AxelorHttpResponse
}