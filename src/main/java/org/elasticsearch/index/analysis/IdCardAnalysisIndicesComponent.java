package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.lavenderx.analysis.IdCardAnalyzer;
import org.lavenderx.analysis.IdCardTokenizer;

/**
 * @author Baymax
 */
public class IdCardAnalysisIndicesComponent extends AbstractComponent {

    @Inject
    public IdCardAnalysisIndicesComponent(Settings settings, IndicesAnalysisService indicesAnalysisService,
                                          Environment env) {
        super(settings);

        indicesAnalysisService.analyzerProviderFactories().put("idcard",
                new PreBuiltAnalyzerProviderFactory("idcard",
                        AnalyzerScope.GLOBAL, new IdCardAnalyzer(Lucene.ANALYZER_VERSION))
        );

        indicesAnalysisService.tokenizerFactories().put("idcard",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "idcard";
                    }

                    @Override
                    public Tokenizer create() {
                        return new IdCardTokenizer();
                    }
                }));
    }
}
