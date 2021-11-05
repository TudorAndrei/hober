import org.apache.tika.Tika;

import java.io.File;

class ListOfFiles {
    public String[] get_docs(){
        //Creating a File object for directory
        File directoryPath = new File("./documents");
        //List of all files and directories
        return directoryPath.list();
    }

}
public class Typedetection {

    public static void main(String[] args) {

        //assume example.mp3 is in your current directory
//        File file = new File("./documents/descarca-camil-petrescu-ultima-noapte-de-d - Unknown.docx");//

        ListOfFiles files = new ListOfFiles();
        String[] file_paths;
        file_paths =  files.get_docs();
        //Instantiating tika facade class
        Tika tika = new Tika();

        //detecting the file type using detect method
        for (String file_path : file_paths) {
            String filetype = tika.detect(file_path);
            System.out.println(filetype);
        }
    }
}