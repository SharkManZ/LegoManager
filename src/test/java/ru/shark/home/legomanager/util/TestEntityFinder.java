package ru.shark.home.legomanager.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.dao.entity.*;

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

    public SetEntity findSet(String number) {
        return (SetEntity) em.createQuery("select s from SetEntity s where lower(s.number) = :number")
                .setParameter("number", number.toLowerCase())
                .getSingleResult();
    }

    public Long findSetId(String number) {
        return (Long) em.createQuery("select s.id from SetEntity s where lower(s.number) = :number")
                .setParameter("number", number.toLowerCase())
                .getSingleResult();
    }

    public ColorEntity findColor(String name) {
        return (ColorEntity) em.createQuery("select c from ColorEntity c where lower(c.name) = :name")
                .setParameter("name", name.toLowerCase())
                .getSingleResult();
    }

    public Long findColorId(String name) {
        return (Long) em.createQuery("select c.id from ColorEntity c where lower(c.name) = :name")
                .setParameter("name", name.toLowerCase())
                .getSingleResult();
    }

    public PartCategoryEntity findPartCategory(String name) {
        return (PartCategoryEntity) em.createQuery("select p from PartCategoryEntity p where lower(p.name) = :name")
                .setParameter("name", name.toLowerCase())
                .getSingleResult();
    }

    public Long findPartCategoryId(String name) {
        return (Long) em.createQuery("select p.id from PartCategoryEntity p where lower(p.name) = :name")
                .setParameter("name", name.toLowerCase())
                .getSingleResult();
    }

    public PartEntity findPart(String number) {
        return (PartEntity) em.createQuery("select p from PartEntity p where lower(p.number) = :number")
                .setParameter("number", number.toLowerCase())
                .getSingleResult();
    }

    public Long findPartId(String number) {
        return (Long) em.createQuery("select p.id from PartEntity p where lower(p.number) = :number")
                .setParameter("number", number.toLowerCase())
                .getSingleResult();
    }

    public PartColorEntity findPartColor(String number) {
        return (PartColorEntity) em.createQuery("select p from PartColorEntity p where lower(p.number) = :number")
                .setParameter("number", number.toLowerCase())
                .getSingleResult();
    }

    public Long findPartColorId(String number) {
        return (Long) em.createQuery("select p.id from PartColorEntity p where lower(p.number) = :number")
                .setParameter("number", number.toLowerCase())
                .getSingleResult();
    }
}
