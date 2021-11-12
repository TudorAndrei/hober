import org.apache.lucene.analysis.*
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter
import org.apache.lucene.analysis.ro.RomanianAnalyzer
import org.apache.lucene.analysis.snowball.SnowballFilter
import org.apache.lucene.analysis.standard.StandardTokenizer
import org.tartarus.snowball.ext.RomanianStemmer


class AnalyzerPipeline : Analyzer() {
    override fun createComponents(fieldName: String): TokenStreamComponents {
        val source: Tokenizer = StandardTokenizer()
        var tokenStream: TokenStream = LowerCaseFilter(source)
        // not sure about this one
        tokenStream = ASCIIFoldingFilter(tokenStream)
        tokenStream = StopFilter(tokenStream, RomanianAnalyzer.getDefaultStopSet())
        tokenStream = SnowballFilter(tokenStream, RomanianStemmer())
        return TokenStreamComponents(source, tokenStream)
    }
}