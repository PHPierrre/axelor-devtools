package fr.phpierre.axelordevtools.objects.dom

import com.intellij.util.xml.DomFileDescription
import org.jetbrains.annotations.NonNls

class DomainFileDescription : DomFileDescription<DomainModels>(DomainModels::class.java, rootDomain) {
    companion object {
        const val rootDomain = "domain-models"
    }
}