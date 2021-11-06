import java.io.File

class ListOfFiles (val documentsPath: String = "./documents/"){
    fun getDocs(): Array<String> {
        //Creating a File object for directory
        val directoryPath = File(documentsPath)
        //List of all files and directories
        return directoryPath.list()
    }
}