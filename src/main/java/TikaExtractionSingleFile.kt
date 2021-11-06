import org.apache.tika.Tika
import org.apache.tika.exception.TikaException
import org.apache.tika.io.TikaInputStream
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.sax.BodyContentHandler
import java.io.File
import java.io.IOException

object TikaExtractionSingleFile {
    @Throws(IOException::class, TikaException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        //Assume sample.txt is in your current directory
        val files = ListOfFiles()
        files.getDocs()
        //Instantiating tika facade class
        val handler = BodyContentHandler(-1)
        val parser = AutoDetectParser()
//        val parser = TXTParser()dd
        val metadata = Metadata()
        val tika = Tika()

        //detecting the file type using detect method
        val file_path = File("/home/tudor/proj/hober/documents/Amurgul gindurilor - Emil Cioran.docx")
//        val result = tika.parseToString(file_path)
//        println(tika.detect(file_path))
//        val stream: FileInputStream = FileInputStream(file_path)
//        val file_: File = File(file_path)
        val stream = TikaInputStream.get(file_path)

        try {
            parser.parse(
                stream,
                handler,
                metadata
            )
            println(handler.toString())
        } finally {
            stream.close()
        }
    }
}