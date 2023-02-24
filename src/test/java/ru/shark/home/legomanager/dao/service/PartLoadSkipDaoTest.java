package ru.shark.home.legomanager.dao.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.entity.load.PartLoadSkipEntity;
import ru.shark.home.legomanager.util.DbTest;

public class PartLoadSkipDaoTest extends DbTest {

    @Autowired
    private PartLoadSkipDao partLoadSkipDao;

    @BeforeAll
    public void init() {
        loadPartSkip("PartLoadSkipDaoTest/partLoadSkip.json");
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        PartLoadSkipEntity entity = new PartLoadSkipEntity();
        entity.setPattern("test ptn");

        // WHEN
        PartLoadSkipEntity saved = partLoadSkipDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(entity.getPattern(), saved.getPattern());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        PartLoadSkipEntity entity = entityFinder.findPartLoadSkip("ptn1");
        entity.setPattern("test ptn");

        // WHEN
        PartLoadSkipEntity saved = partLoadSkipDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(entity.getId(), saved.getId());
        Assertions.assertEquals(entity.getPattern(), saved.getPattern());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findPartLoadSkipId("ptn1");

        // WHEN
        partLoadSkipDao.deleteById(id);

        // WHEN
        Assertions.assertTrue(isDeleted(id, PartLoadSkipEntity.class));
    }

    @Test
    public void getWithPagination() {
        // GIVEN
        RequestCriteria requestCriteria = new RequestCriteria(0, 10);

        // WHEN
        PageableList<PartLoadSkipEntity> list = partLoadSkipDao.getWithPagination(requestCriteria);

        // THEN
        checkPagingList(list, 2, 2L);
    }
}
