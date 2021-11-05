import kotlin.jvm.JvmStatic
import org.apache.tika.Tika
import java.io.File

internal class ListOfFiles {
    fun get_docs(): Array<String> {
        //Creating a File object for directory
        val directoryPath = File("./documents")
        //List of all files and directories
        return directoryPath.list()
    }
}

object Typedetection {
    @JvmStatic
    fun main(args: Array<String>) {

        //assume example.mp3 is in your current directory
//        File file = new File("./documents/descarca-camil-petrescu-ultima-noapte-de-d - Unknown.docx");//
        val files = ListOfFiles()
        val file_paths: Array<String> = files.get_docs()
        //Instantiating tika facade class
        val tika = Tika()

        //detecting the file type using detect method
        for (file_path in file_paths) {
            val filetype = tika.detect(file_path)
            println(filetype)
        }
    }
}