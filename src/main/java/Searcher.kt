import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.ParseException
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TotalHitCountCollector
import org.apache.lucene.store.FSDirectory
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

    private fun readQueries(): ArrayList<String> {
        return try {
            ArrayList(File(queryPath.toString()).readLines())
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.getGlobal().log(Level.SEVERE, "Unable to read query file")
            exitProcess(1)
        }
    }

    private fun search(queryString: String): ArrayList<String> {
        val parser = QueryParser("content", analyzer)

        try {
            val collector = TotalHitCountCollector()
            val query = parser.parse(queryString)
            searcher.search(query, collector)
            val hits = searcher.search(query, 1.coerceAtLeast(collector.totalHits)).scoreDocs
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

    @JvmStatic
    fun main(args: Array<String>) {
        val queries: ArrayList<String> = readQueries()
        for (q in queries) {
            print("For Query $q: ")
            val docPaths = search(q)
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