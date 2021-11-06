import org.apache.tika.Tika
import ListOfFiles
import java.io.File


object TypeDetection {
    @JvmStatic
    fun main(args: Array<String>) {
        val files = ListOfFiles()
        val file_paths: Array<out File>? = files.getDocs()
        //Instantiating tika facade class
        val tika = Tika()

        //detecting the file type using detect method
        if (file_paths != null) {
            for (file_path in file_paths) {
                val filetype = tika.detect(file_path)
                println(filetype)
            }
        }
    }
}