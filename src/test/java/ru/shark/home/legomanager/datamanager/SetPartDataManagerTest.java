package ru.shark.home.legomanager.datamanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.dao.dto.SetDto;
import ru.shark.home.legomanager.dao.dto.SetPartDto;
import ru.shark.home.legomanager.dao.dto.SetPartFullDto;
import ru.shark.home.legomanager.util.DbTest;

public class SetPartDataManagerTest extends DbTest {
    @Autowired
    private SetPartDataManager setPartDataManager;

    @BeforeAll
    public void init() {
        loadSeries("SetPartDaoTest/series.json");
        loadColors("SetPartDaoTest/colors.json");
        loadPartCategories("SetPartDaoTest/partCats.json");
        loadParts("SetPartDaoTest/parts.json");
        loadSets("SetPartDaoTest/sets.json");
    }

    @Test
    public void getPartsBySetId() {
        // GIVEN
        Long setId = entityFinder.findSetId("42082");

        // WHEN
        PageableList<SetPartFullDto> list = setPartDataManager.getPartsBySetId(setId, new RequestCriteria(0, 10));

        // THEN
        Assertions.assertEquals(2, list.getData().size());
        for (SetPartFullDto dto : list.getData()) {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getSetId());
            Assertions.assertNotNull(dto.getPartColorId());
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getColorNumber());
            Assertions.assertNotNull(dto.getHexColor());
            Assertions.assertNotNull(dto.getCount());
        }
    }

    @Test
    public void save() {
        // GIVEN
        SetPartDto dto = new SetPartDto();
        dto.setCount(2);
        dto.setSet(new SetDto());
        dto.getSet().setId(entityFinder.findSetId("42100"));
        dto.setPartColor(new PartColorDto());
        dto.getPartColor().setId(entityFinder.findPartColorId("112231"));

        // WHEN
        SetPartDto saved = setPartDataManager.save(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(dto.getCount(), saved.getCount());
        Assertions.assertEquals(dto.getSet().getId(), saved.getSet().getId());
        Assertions.assertEquals(dto.getPartColor().getId(), saved.getPartColor().getId());
    }
}
