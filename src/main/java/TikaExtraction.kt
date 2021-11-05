import kotlin.Throws
import java.io.IOException
import org.apache.tika.exception.TikaException
import kotlin.jvm.JvmStatic
import org.apache.tika.Tika
import java.io.File

object TikaExtraction {
    @Throws(IOException::class, TikaException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        //Assume sample.txt is in your current directory
        val file = File("./documents/sample.txt")

        //Instantiating Tika facade class
        val tika = Tika()
        val filecontent = tika.parseToString(file)
        println("Extracted Content: $filecontent")
    }
}