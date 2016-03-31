package org.lavenderx.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.Version;

/**
 * @author Baymax
 */
public final class IdCardAnalyzer extends Analyzer {

    public IdCardAnalyzer(Version version) {
        super();
        setVersion(version);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new IdCardTokenizer();

        return new TokenStreamComponents(source);
    }
}
