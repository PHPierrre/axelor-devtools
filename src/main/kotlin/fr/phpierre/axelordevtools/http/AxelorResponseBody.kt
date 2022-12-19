package fr.phpierre.axelordevtools.http

data class AxelorResponseBody<T>(
    var status: Int = 0,
    val data: T?,
    val offset: Int = 0,
    val total: Int = 0,
)