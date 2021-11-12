import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;

public class TextExtractionSimple {

    public static void main(final String[] args) throws TikaException, IOException {

        //Assume sample.txt is in your current directory
        File file = new File("./documents/Amurgul gindurilor - Emil Cioran.pdf");
        Tika tika = new Tika();
        String text = tika.parseToString(file);
        System.out.print(text);

    }

}
