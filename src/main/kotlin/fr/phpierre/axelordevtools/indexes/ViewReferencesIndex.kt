package fr.phpierre.axelordevtools.indexes

import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.util.indexing.*
import com.intellij.util.io.*
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.util.XmlUtil
import gnu.trove.THashMap

class ViewReferencesIndex : FileBasedIndexExtension<String, Int>() {

    companion object {
        val KEY =
            ID.create<String, Int>("axelor.views.references")
    }

    override fun getName(): ID<String, Int> {
        return KEY
    }

    override fun getIndexer(): DataIndexer<String, Int, FileContent> {
        return DataIndexer { inputData: FileContent ->
            val psiFile = inputData.psiFile
            val map: MutableMap<String, Int> = THashMap()
            val refs: Set<MetaReference> =
                XmlUtil.referenceAxelorViews(psiFile)
            for (ref in refs) {
                map[ref.name] = ref.position
            }
            map
        }
    }

    override fun getKeyDescriptor(): KeyDescriptor<String> {
        return EnumeratorStringDescriptor()
    }

    override fun getValueExternalizer(): DataExternalizer<Int> {
        return EnumeratorIntegerDescriptor.INSTANCE
    }

    override fun getVersion(): Int {
        return 4
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return FileBasedIndex.InputFilter { virtualFile: VirtualFile ->
            virtualFile.fileType === XmlFileType.INSTANCE && virtualFile.parent.name == "views"
        }
    }

    override fun dependsOnFileContent(): Boolean {
        return true
    }
}