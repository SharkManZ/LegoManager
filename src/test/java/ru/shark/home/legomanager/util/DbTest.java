package ru.shark.home.legomanager.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.dto.Dto;
import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class DbTest {
    @Autowired
    private TestDataLoader testDataLoader;

    @Autowired
    protected TestEntityFinder entityFinder;

    @PersistenceContext
    protected EntityManager em;

    protected <T extends BaseEntity> void checkPagingList(PageableList<T> list, Integer count, Long total) {
        Assertions.assertNotNull(list);
        Assertions.assertNotNull(list.getData());
        Assertions.assertEquals(count, list.getData().size());
        Assertions.assertEquals(list.getTotalCount(), total);
    }

    protected <T extends Dto> void checkPagingDtoList(PageableList<T> list, Integer count, Long total) {
        Assertions.assertNotNull(list);
        Assertions.assertNotNull(list.getData());
        Assertions.assertEquals(count, list.getData().size());
        Assertions.assertEquals(list.getTotalCount(), total);
    }

    protected void loadSeries(String... files) {
        testDataLoader.loadSeries(files);
    }

    protected void loadSets(String... files) {
        testDataLoader.loadSets(files);
    }

    protected void loadColors(String... files) {
        testDataLoader.loadColors(files);
    }

    protected void loadPartCategories(String... files) {
        testDataLoader.loadPartCategories(files);
    }

    protected void loadParts(String... files) {
        testDataLoader.loadParts(files);
    }

    protected void loadUsers(String... files) {
        testDataLoader.loadUsers(files);
    }

    protected void loadPartSkip(String... files) {
        testDataLoader.loadPartSkip(files);
    }

    protected <E extends BaseEntity> boolean isDeleted(E entity) {
        Long count = (Long) em.createQuery("select count(e.id) from " + entity.getClass().getSimpleName() + " e " +
                        "where e.id = " +
                        entity.getId())
                .getSingleResult();
        return count == 0;
    }

    protected <E extends BaseEntity> boolean isDeleted(Long id, Class<E> entityClass) {
        Long count = (Long) em.createQuery("select count(e.id) from " + entityClass.getSimpleName() + " e " +
                        "where e.id = " +
                        id)
                .getSingleResult();
        return count == 0;
    }

    @AfterAll
    public void cleanUp() {
        testDataLoader.cleanUp();
    }

    protected EntityManager getEm() {
        return em;
    }
}
