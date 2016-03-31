package org.elasticsearch.index.analysis;

import org.elasticsearch.common.inject.AbstractModule;

/**
 * @author Baymax
 */
public class IdCardAnalysisModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IdCardAnalysisIndicesComponent.class).asEagerSingleton();
    }
}
