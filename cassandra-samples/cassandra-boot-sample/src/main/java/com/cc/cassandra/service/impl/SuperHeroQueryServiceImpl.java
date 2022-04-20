package com.cc.cassandra.service.impl;

import com.cc.cassandra.model.SuperHero;
import com.cc.cassandra.repository.SuperHeroQueryRepository;
import com.cc.cassandra.service.SuperHeroQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperHeroQueryServiceImpl implements SuperHeroQueryService {

    @Autowired
    private SuperHeroQueryRepository superHeroQueryRepository;

    @Override
    public List<SuperHero> save() {
        return superHeroQueryRepository.save();
    }

    @Override
    public List<SuperHero> getAll() {
        return superHeroQueryRepository.getAll();
    }

    @Override
    public List<SuperHero> getSuperHeroByName(String name) {
        return superHeroQueryRepository.getSuperHeroByName(name);
    }

    @Override
    public SuperHero getOneSuperHeroByName(String name) {
        return superHeroQueryRepository.getOneSuperHeroByName(name);
    }

    @Override
    public List<SuperHero> getSuperHeroByNameLike(String name) {
        return superHeroQueryRepository.getSuperHeroByNameLike(name);
    }

    @Override
    public SuperHero getSingleSuperHeroBySuperName(String superName) {
        return superHeroQueryRepository.getSingleSuperHeroBySuperName(superName);
    }

    @Override
    public List<SuperHero> getSuperHeroByAgeGreaterThan(int age) {
        return superHeroQueryRepository.getSuperHeroByAgeGreaterThan(age);
    }

    @Override
    public List<SuperHero> getSuperHeroWhoCanFly(boolean canFly) {
        return superHeroQueryRepository.getSuperHeroWhoCanFly(canFly);
    }
}
