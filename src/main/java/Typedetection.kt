import org.apache.tika.Tika
import ListOfFiles


object Typedetection {
    @JvmStatic
    fun main(args: Array<String>) {

        //assume example.mp3 is in your current directory
//        File file = new File("./documents/descarca-camil-petrescu-ultima-noapte-de-d - Unknown.docx");//
        val files = ListOfFiles()
        val file_paths: Array<String> = files.getDocs()
        //Instantiating tika facade class
        val tika = Tika()

        //detecting the file type using detect method
        for (file_path in file_paths) {
            val filetype = tika.detect(file_path)
            println(filetype)
        }
    }
}