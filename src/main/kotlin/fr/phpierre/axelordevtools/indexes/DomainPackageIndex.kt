package fr.phpierre.axelordevtools.indexes

import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.*
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import com.intellij.util.io.VoidDataExternalizer
import fr.phpierre.axelordevtools.util.XmlUtil.Companion.getPackageNameOfDomain
import gnu.trove.THashMap

class DomainPackageIndex : FileBasedIndexExtension<String, Void?>() {
    override fun getName(): ID<String, Void?> {
        return KEY
    }

    override fun getIndexer(): DataIndexer<String, Void?, FileContent> {
        return DataIndexer { inputData: FileContent ->
            val psiFile = inputData.psiFile
            val map: MutableMap<String, Void?> = THashMap()
            val domainName =
                getPackageNameOfDomain(psiFile)
            if (domainName != null) {
                map[domainName] = null
            }
            map
        }
    }

    override fun getKeyDescriptor(): KeyDescriptor<String> {
        return EnumeratorStringDescriptor()
    }

    override fun getValueExternalizer(): DataExternalizer<Void?> {
        return VoidDataExternalizer.INSTANCE
    }

    override fun getVersion(): Int {
        return 0
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return FileBasedIndex.InputFilter { virtualFile: VirtualFile ->
            virtualFile.fileType === XmlFileType.INSTANCE && virtualFile.parent.name == "domains"
        }
    }

    override fun dependsOnFileContent(): Boolean {
        return true
    }

    companion object {
        val KEY =
            ID.create<String, Void?>("axelor.domains.name")
    }
}