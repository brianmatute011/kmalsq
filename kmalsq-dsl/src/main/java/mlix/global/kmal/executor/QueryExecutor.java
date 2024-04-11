package mlix.global.kmal.executor;

/**
 * Represents a query executor.
 * @param <T> the type of the result of the query.
 */
public interface QueryExecutor<T> {
    /**
     * Query the data source.
     * @param criteria the criteria to query.
     * @return the result of the query.
     */
    T query(String criteria);
}
