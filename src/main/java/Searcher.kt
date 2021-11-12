import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.ParseException
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.FSDirectory
import org.apache.tika.exception.TikaException
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

object Searcher {
    private val analyzer = AnalyzerPipeline()
    private val indexPath = Paths.get("./index/")
    private val queryPath = Paths.get("./query/query.txt")
    private var searcher = readIndex()

    private fun readIndex(): IndexSearcher {
        try {
            val reader = DirectoryReader.open(FSDirectory.open(indexPath))
            return IndexSearcher(reader)
        } catch (e: IOException) {
            e.printStackTrace()
            Logger.getGlobal().log(Level.SEVERE, "Unable to read index")
            exitProcess(1)
        }
    }

    @Throws(IOException::class)
    private fun readQueries(): ArrayList<String> {
        return try {
            ArrayList<String>(File(queryPath.toString()).readLines())
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.getGlobal().log(Level.SEVERE, "Unable to read query file")
            exitProcess(1)
        }
    }

    private fun search(queryString: String, topHits: Int): ArrayList<String> {
        val parser = QueryParser("content", analyzer)

        try {
            val query = parser.parse(queryString)
            val hits = searcher.search(query, topHits).scoreDocs
            val docPaths = ArrayList<String>()
            for (hit in hits) {
                val doc = searcher.doc(hit.doc)
                val path = doc.get("path")
                docPaths.add(path)
            }
            return docPaths
        } catch (e: ParseException) {
            e.printStackTrace()
            Logger.getGlobal().log(Level.SEVERE, "Cannot parse the query")
            exitProcess(1)
        } catch (ex: IOException) {
            ex.printStackTrace()
            exitProcess(1)
        }
    }

    @Throws(IOException::class, TikaException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        var hits = 5
        try {
            hits = args[0].toInt()
        } catch (e: IndexOutOfBoundsException) {
        }
        println(hits)
        val queries: ArrayList<String> = readQueries()
        for (q in queries) {
            print("For Query $q: ")
            val docPaths = search(q, hits)
            if (docPaths.size > 0) {
                println("Found in the document(s): ")
                for (docPath in docPaths) {
                    println(docPath)
                }
            } else {
                println("No document found")
            }
        }
    }
}