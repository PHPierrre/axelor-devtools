package fr.phpierre.axelordevtools.http

import fr.phpierre.axelordevtools.http.model.AxelorModel

data class AxelorRequestBody(
        val model: String,
        val action: String,
        val data: MutableMap<String, Any> = mutableMapOf(),

        val records: MutableList<AxelorModel> = mutableListOf(),
        val fields: MutableList<String> = mutableListOf(),
        val limit: Int = 100_000,
        val offset: Int = 0,

) {
    fun addInContext(key: String, value: Any) {
        createContextIfnotExist();
        (data["context"] as MutableMap<String, Any>).put(key, value)
    }

    private fun createContextIfnotExist() {
        if(!data.containsKey("context")) {
            val emptyContext: MutableMap<String, Any> = mutableMapOf()
            data.put("context", emptyContext)
        }
    }
}