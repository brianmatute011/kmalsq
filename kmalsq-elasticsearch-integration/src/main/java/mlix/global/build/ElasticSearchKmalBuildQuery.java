package mlix.global.build;

import mlix.global.executor.ElasticSearchKmalOperatorExecutor;
import mlix.global.kmal.executor.QueryExecutor;
import org.elasticsearch.index.query.QueryBuilder;

public class ElasticSearchKmalBuildQuery extends ElasticSearchKmalOperatorExecutor implements QueryExecutor<QueryBuilder> {
    @Override
    public QueryBuilder query(String criteria) {
        return this.parseQuery(criteria);
    }
}
