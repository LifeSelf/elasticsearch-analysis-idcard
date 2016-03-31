package org.lavenderx.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.common.lucene.Lucene;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.StringReader;

@RunWith(JUnit4.class)
public class IdCardAnalyzerTest {

    @Test
    public void test() throws IOException {
        IdCardAnalyzer analyzer = new IdCardAnalyzer(Lucene.ANALYZER_VERSION);
        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader("511062199010202689"));
        tokenStream.reset();
        tokenStream.addAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) {
            CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
            System.out.println(termAttribute.toString());
        }
    }
}
