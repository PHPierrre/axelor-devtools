package fr.phpierre.axelordevtools.indexes

import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.util.indexing.*
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import com.intellij.util.io.VoidDataExternalizer
import fr.phpierre.axelordevtools.util.AxelorFile
import fr.phpierre.axelordevtools.util.XmlUtil
import gnu.trove.THashMap

class EnumNameIndex : FileBasedIndexExtension<String, Void?>() {

    companion object {
        val KEY =
                ID.create<String, Void?>("axelor.enum.name")
    }

    override fun getName(): ID<String, Void?> {
        return KEY
    }

    override fun getIndexer(): DataIndexer<String, Void?, FileContent> {
        return DataIndexer { inputData: FileContent ->
            val psiFile = inputData.psiFile
            val map: MutableMap<String, Void?> = THashMap()
            val sets: Set<String> = XmlUtil.indexAxelorEnumName(psiFile)
            for (name in sets) {
                map[name] = null
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
            AxelorFile.isDomain(virtualFile)
        }
    }

    override fun dependsOnFileContent(): Boolean {
        return true
    }
}