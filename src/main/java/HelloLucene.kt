import kotlin.Throws
import java.io.IOException
import kotlin.jvm.JvmStatic
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.store.ByteBuffersDirectory
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexReader
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TopDocs
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.ParseException
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.store.Directory

object HelloLucene {
    @Throws(IOException::class, ParseException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        val analyzer = StandardAnalyzer()

        // 1. create the index
        val index: Directory = ByteBuffersDirectory()
        val config = IndexWriterConfig(analyzer)
        val w = IndexWriter(index, config)
        addDoc(w, "Lucene in Action", "193398817")
        addDoc(w, "Lucene for Dummies", "55320055Z")
        addDoc(w, "Managing Gigabytes", "55063554A")
        addDoc(w, "The Art of Computer Science", "9900333X")
        w.close()

        // 2. query
        val querystring = if (args.size > 0) args[0] else "lucene"

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        val q = QueryParser("title", analyzer).parse(querystring)

        // 3. search
        val hitsPerPage = 10
        val reader: IndexReader = DirectoryReader.open(index)
        val searcher = IndexSearcher(reader)
        val docs = searcher.search(q, hitsPerPage)
        val hits = docs.scoreDocs

        // 4. display results
        println("Found " + hits.size + " hits.")
        for (i in hits.indices) {
            val docId = hits[i].doc
            val d = searcher.doc(docId)
            println((i + 1).toString() + ". " + d["isbn"] + "\t" + d["title"])
        }

        // reader can only be closed when there
        // is no need to access the documents anymore.
        reader.close()
    }

    @Throws(IOException::class)
    private fun addDoc(w: IndexWriter, title: String, isbn: String) {
        val doc = Document()
        doc.add(TextField("title", title, Field.Store.YES))

        // use a string field for isbn because we don't want it tokenized
        doc.add(StringField("isbn", isbn, Field.Store.YES))
        w.addDocument(doc)
    }
}