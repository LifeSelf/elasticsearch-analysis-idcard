package org.elasticsearch.plugin.analysis.idcard;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.IdCardAnalysisModule;
import org.elasticsearch.plugins.Plugin;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Baymax
 */
public class IdCardAnalysisPlugin extends Plugin {

    @Override
    public String name() {
        return "analysis-idcard";
    }

    @Override
    public String description() {
        return "ID card analyzer plugin for elasticsearch";
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.singletonList(new IdCardAnalysisModule());
    }
}
