import org.apache.lucene.analysis.*
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter
import org.apache.lucene.analysis.snowball.SnowballFilter
import org.apache.lucene.analysis.standard.ClassicTokenizer
import org.tartarus.snowball.ext.RomanianStemmer
import java.io.File


class AnalyzerPipeline : Analyzer() {
    //https://github.com/stopwords-iso/stopwords-ro
    private val stopWords = StopFilter.makeStopSet(File("stopwords-ro.txt").readLines())
    override fun createComponents(fieldName: String): TokenStreamComponents {
        val source: Tokenizer = ClassicTokenizer()
        var tokenStream: TokenStream = LowerCaseFilter(source)
        tokenStream = ASCIIFoldingFilter(tokenStream)
        tokenStream = StopFilter(tokenStream, stopWords)
        tokenStream = SnowballFilter(tokenStream, RomanianStemmer())
        return TokenStreamComponents(source, tokenStream)
    }
}