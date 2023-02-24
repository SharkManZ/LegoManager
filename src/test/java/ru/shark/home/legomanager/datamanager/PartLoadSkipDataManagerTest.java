package ru.shark.home.legomanager.datamanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.dto.load.PartLoadSkipDto;
import ru.shark.home.legomanager.dao.entity.load.PartLoadSkipEntity;
import ru.shark.home.legomanager.util.DbTest;

public class PartLoadSkipDataManagerTest extends DbTest {

    @Autowired
    private PartLoadSkipDataManager partLoadSkipDataManager;

    @BeforeAll
    public void init() {
        loadPartSkip("PartLoadSkipDataManagerTest/partLoadSkip.json");
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        PartLoadSkipDto dto = new PartLoadSkipDto();
        dto.setPattern("test ptn");

        // WHEN
        PartLoadSkipDto saved = partLoadSkipDataManager.save(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(dto.getPattern(), saved.getPattern());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        PartLoadSkipDto dto = new PartLoadSkipDto();
        dto.setId(entityFinder.findPartLoadSkipId("ptn1"));
        dto.setPattern("test ptn");

        // WHEN
        PartLoadSkipDto saved = partLoadSkipDataManager.save(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(dto.getId(), saved.getId());
        Assertions.assertEquals(dto.getPattern(), saved.getPattern());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long id = entityFinder.findPartLoadSkipId("ptn1");

        // WHEN
        partLoadSkipDataManager.deleteById(id);

        // WHEN
        Assertions.assertTrue(isDeleted(id, PartLoadSkipEntity.class));
    }

    @Test
    public void getWithPagination() {
        // WHEN
        PageableList<PartLoadSkipDto> list = partLoadSkipDataManager.getWithPagination(new RequestCriteria(0, 10));

        // THEN
        checkPagingDtoList(list, 2, 2L);
    }
}
