package org.elasticsearch.plugin;

import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.plugin.analysis.idcard.IdCardAnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.test.ESIntegTestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.test.ESIntegTestCase.Scope.TEST;
import static org.hamcrest.core.Is.is;

@ESIntegTestCase.ClusterScope(scope = TEST, maxNumDataNodes = 1)
public class IdCardPluginIntegrationTest extends ESIntegTestCase {

    static {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        createIndex("test");
        ensureGreen("test");
        final XContentBuilder mapping = jsonBuilder().startObject()
                .startObject("type")
                .startObject("properties")
                .startObject("foo")
                .field("type", "string")
                .field("analyzer", "idcard")
                .endObject()
                .endObject()
                .endObject()
                .endObject();

        client().admin().indices().preparePutMapping("test").setType("type").setSource(mapping).get();
        ensureGreen("test");
        Locale.setDefault(new Locale("en_US"));
    }

    @Override
    protected Settings nodeSettings(int nodeOrdinal) {
        Settings.Builder builder = Settings.builder()
                .put(super.nodeSettings(nodeOrdinal))
                .put("node.mode", "network")
                .put("plugin.types", IdCardAnalysisPlugin.class.getName());
        return builder.build();
    }

    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return pluginList(IdCardAnalysisPlugin.class);
    }

    @Test
    public void testPluginIsLoaded() {
        NodesInfoResponse infos = client().admin().cluster().prepareNodesInfo().setPlugins(true).execute().actionGet();
        assertThat(infos.getNodes()[0].getPlugins().getPluginInfos().get(0).getName(), is("analysis-idcard"));
    }

    @Test
    public void testIdCardAnalyzer() throws InterruptedException, ExecutionException, IOException {
        assertIncludes("511062199010202689",
                Arrays.asList("511062199010202689", "511062", "5110621990", "51106219901020", "2689"));
    }


    private void assertIncludes(String originalString, List<String> expectedTokens) throws ExecutionException, InterruptedException, IOException {
        AnalyzeResponse response = client().admin().indices().prepareAnalyze(originalString).setField("foo").setIndex("test").execute().get();
        index("test", "type", "1", "foo", originalString);


        // Verify all the expected tokens
        List<String> tokens = new ArrayList<>();
        for (AnalyzeResponse.AnalyzeToken token : response.getTokens()) {
            assertFalse(Objects.isNull(token.getTerm()) || token.getTerm().isEmpty());
            tokens.add(token.getTerm());
            System.out.println(token.getTerm());
        }

        flush();
        refresh();

        for (String expectedToken : expectedTokens) {
            assertTrue(tokens.contains(expectedToken));
            SearchResponse sr = client().prepareSearch("test").setQuery(QueryBuilders.termQuery("foo", expectedToken)).execute().actionGet();
            assertThat(sr.getHits().getTotalHits(), is(1L));
            sr = client().prepareSearch("test").setQuery(QueryBuilders.termQuery("foo", "idcard_search_term")).execute().actionGet();
            assertThat(sr.getHits().getTotalHits(), is(0L));
        }
    }
}
