package fr.phpierre.axelordevtools.objects

class DomainName(val packageName: String, val className: String) {

    fun name(): String = "$packageName.$className";
}