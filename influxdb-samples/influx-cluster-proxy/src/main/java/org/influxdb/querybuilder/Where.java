package org.influxdb.querybuilder;

import org.influxdb.querybuilder.clauses.Clause;
import org.influxdb.querybuilder.clauses.ConjunctionClause;

import java.util.List;

public interface Where {

  <T extends Where> T and(final Clause clause);

  <T extends Where> T or(final Clause clause);

  List<ConjunctionClause> getClauses();

  WhereNested andNested();

  WhereNested orNested();

  <T extends Select> T orderBy(final Ordering orderings);

  <T extends Select> T groupBy(final Object... columns);

  <T extends Select> T limit(final int limit);

  <T extends Select> T limit(final int limit, final long offSet);
}
