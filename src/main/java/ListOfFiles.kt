import java.io.File
import java.io.FilenameFilter
import java.io.IOException

class ListOfFiles(private val documentsPath: String = "./documents/") {
    @Throws(IOException::class)
    fun getDocs(): Array<out File>? {
        //Creating a File object for directory
        val directoryPath = File(documentsPath)
        val textFileFilter = FilenameFilter { _, name ->
            val lowercaseName = name.lowercase()
            lowercaseName.endsWith(".txt") ||
                    lowercaseName.endsWith(".pdf") ||
                    lowercaseName.endsWith(".doc") ||
                    lowercaseName.endsWith(".docx")
        }
        return directoryPath.listFiles(textFileFilter)
    }
}