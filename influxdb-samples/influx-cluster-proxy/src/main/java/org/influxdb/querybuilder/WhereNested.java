package org.influxdb.querybuilder;

import org.influxdb.querybuilder.clauses.*;

import java.util.ArrayList;
import java.util.List;

public class WhereNested<T extends Where> {

  private final List<ConjunctionClause> clauses = new ArrayList<>();
  private final boolean orConjunction;
  private final T where;

  WhereNested(final T where, final boolean orConjunction) {
    this.where = where;
    this.orConjunction = orConjunction;
  }

  public WhereNested<T> and(final Clause clause) {
    clauses.add(new AndConjunction(clause));
    return this;
  }

  public WhereNested<T> or(final Clause clause) {
    clauses.add(new OrConjunction(clause));
    return this;
  }

  public T close() {
    if (orConjunction) {
      where.or(new NestedClause(clauses));
    } else {
      where.and(new NestedClause(clauses));
    }

    return where;
  }
}
