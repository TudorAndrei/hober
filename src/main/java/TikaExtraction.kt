import org.apache.tika.Tika
import org.apache.tika.exception.TikaException
import org.apache.tika.io.TikaInputStream
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.sax.BodyContentHandler
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object TikaExtraction {
    @Throws(IOException::class, TikaException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        //Assume sample.txt is in your current directory
        val files = ListOfFiles()
        val file_paths: Array<out File>? = files.getDocs()
        //Instantiating tika facade class
        val handler = BodyContentHandler(-1)
        val parser = AutoDetectParser()
        val metadata = Metadata()
//        val tika = Tika()

        //detecting the file type using detect method
        var count_valid = 0
        if (file_paths != null) {
            for (file_path in file_paths) {
//                val stream = FileInputStream(file_path)
////                val file = File(file_path)
                val stream = TikaInputStream.get(file_path)
                try {
                    parser.parse(
                        stream,
                        handler,
                        metadata
                    )
//                    println(handler.toString())
                    count_valid = count_valid +  1
                } finally {
                    stream.close()
                }
            }
        }
        if (file_paths != null) {
            print(file_paths.size == count_valid)
        }
    }
}