package com.cc.cassandra.repository;

import com.cc.cassandra.model.SuperHero;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperHeroRepository extends CassandraRepository<SuperHero, Long> {
}
