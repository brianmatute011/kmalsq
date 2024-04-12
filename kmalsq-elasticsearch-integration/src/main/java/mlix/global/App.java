package mlix.global;
import mlix.global.build.ElasticSearchKmalBuildQuery;
import org.elasticsearch.index.query.QueryBuilder;

public class App {
    public static void main( String[] args ) {
        ElasticSearchKmalBuildQuery elasticSearchKmalBuildQuery = new ElasticSearchKmalBuildQuery();
        String dslPrompt = "NOT ([a] == 15 OR [b] == 10);";
        QueryBuilder queryBuilder = elasticSearchKmalBuildQuery.query(dslPrompt);
        System.out.println(queryBuilder.toString());
    }
}
