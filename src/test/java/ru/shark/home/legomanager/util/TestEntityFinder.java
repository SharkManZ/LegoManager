package ru.shark.home.legomanager.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;

import javax.persistence.EntityManager;

@Component
public class TestEntityFinder {
    @Autowired
    private EntityManager em;

    public SeriesEntity findSeries(String name) {
        return (SeriesEntity) em.createQuery("select s from SeriesEntity s where lower(s.name ) = lower(:name)")
                .setParameter("name", name)
                .getSingleResult();
    }

    public Long findSeriesId(String name) {
        return (Long) em.createQuery("select s.id from SeriesEntity s where lower(s.name ) = lower(:name)")
                .setParameter("name", name)
                .getSingleResult();
    }
}
