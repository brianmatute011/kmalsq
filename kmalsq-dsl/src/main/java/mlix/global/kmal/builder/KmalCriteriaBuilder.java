package mlix.global.kmal.builder;

import mlix.global.kmal.executor.OperatorExecutor;
import mlix.global.kmal.executor.QueryExecutor;

/*
 * Represents a criteria builder.
 */
public final class KmalCriteriaBuilder<E, T extends OperatorExecutor<E> & QueryExecutor<E>> {
    T tKmal;
    String query;

    KmalCriteriaBuilder(T tKmal){
        this.tKmal = tKmal;
    }
    public KmalCriteriaBuilder<E, T> fromQuery(String tKmal) {
        this.query = this.sanatizeQuery(tKmal);
        return this;
    }

    public KmalCriteriaBuilder<E, T> andQuery(String tKmal) {
        this.query += " AND ( " + this.sanatizeQuery(tKmal) + " )";
        return this;
    }

    public KmalCriteriaBuilder<E, T> orQuery(String tKmal) {
        this.query += " OR ( " + this.sanatizeQuery(tKmal) + " )";
        return this;
    }

    public E build() {
        return this.tKmal.query(this.query);
    }

    private String sanatizeQuery(String query) {
        return query.endsWith(";") ? query.substring(0, query.length() - 1) : query;
    }

}
