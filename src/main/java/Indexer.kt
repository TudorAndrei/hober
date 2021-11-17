@file:Suppress("NAME_SHADOWING")

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.sax.BodyContentHandler
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

@Suppress("DEPRECATION")
object Indexer {
    private val documentsPath = Paths.get("./documents/")
    private val indexPath = Paths.get("./index/")
    private val analyzer: Analyzer = AnalyzerPipeline()
    private val file_paths: Array<out File>? = ListOfFiles(documentsPath.toString()).getDocs()
    private val handler: BodyContentHandler = BodyContentHandler(-1)
    private val parser: AutoDetectParser = AutoDetectParser()
    private val metadata: Metadata = Metadata()

    private fun createDir(path: Path) {
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path)
            } catch (e: IOException) {
                e.printStackTrace()
                exitProcess(1)
            }
        }
    }

    private fun createIndex() {
        createDir(documentsPath)
        createDir(indexPath)
        try {
            val config = IndexWriterConfig(analyzer)
            config.openMode = IndexWriterConfig.OpenMode.CREATE
            val dir: Directory = FSDirectory.open(indexPath)
            val writer = IndexWriter(dir, config)
            indexDocuments(writer)
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Logger.getGlobal().log(Level.SEVERE, "Creating the Index failed: $e")
            exitProcess(1)
        }
    }

    private fun indexDocuments(writer: IndexWriter) {
        if (file_paths != null) {
            for (file_path in file_paths) {
                val stream = FileInputStream(file_path)
                stream.use { stream ->
                    parser.parse(
                        stream,
                        handler,
                        metadata
                    )
                    //                    println(handler.toString())
                    val doc = Document()
                    doc.add(TextField("content", handler.toString(), Field.Store.YES))
                    doc.add(StringField("path", file_path.toString(), Field.Store.YES))
                    try {
                        writer.addDocument(doc)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Logger.getGlobal().log(Level.SEVERE, "IndexWriter unable to add document: $e")
                        exitProcess(1)
                    }
                }
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        createIndex()
    }
}