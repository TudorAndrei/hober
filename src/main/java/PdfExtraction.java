import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfExtraction {
    public static void main(final String[] args) throws IOException, TikaException {

        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        FileInputStream input_stream = new FileInputStream(new File("./documents/Amurgul gindurilor - Emil Cioran.pdf"));
        ParseContext context = new ParseContext();

        //parsing the document using PDF parsero

        PDFParser pdfparser = new PDFParser();
        try {
            pdfparser.parse(input_stream, handler, metadata, context);
        } catch (SAXException e) {
            e.printStackTrace();
        }

        //getting the content of the document
//        System.out.println("Contents of the PDF :" + handler.toString());

        //getting metadata of the document
        System.out.println("Metadata of the PDF:");
        String[] metadataNames = metadata.names();

        for (String name : metadataNames) {
            System.out.println(name + " : " + metadata.get(name));
        }
    }
}