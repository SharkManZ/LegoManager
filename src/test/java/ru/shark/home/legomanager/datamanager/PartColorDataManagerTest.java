package ru.shark.home.legomanager.datamanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.services.dto.PartColorSearchDto;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.List;

public class PartColorDataManagerTest extends DaoServiceTest {
    @Autowired
    private PartColorDataManager partColorDataManager;

    @BeforeAll
    public void init() {
        loadPartCategories("PartColorDataManagerTest/partCats.json");
        loadColors("PartColorDataManagerTest/colors.json");
        loadParts("PartColorDataManagerTest/parts.json");
    }

    @Test
    public void getPartColorListByPartId() {
        // GIVEN
        Long partId = entityFinder.findPartId("3010");

        // WHEN
        List<PartColorDto> list = partColorDataManager.getPartColorListByPartId(partId);

        // THEN
        Assertions.assertEquals(2, list.size());
        for (PartColorDto dto : list) {
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getPart());
            Assertions.assertNotNull(dto.getPart().getId());
            Assertions.assertNotNull(dto.getColor());
            Assertions.assertNotNull(dto.getColor().getId());
        }
    }

    @Test
    public void search() {
        // GIVEN
        PartColorSearchDto dto = new PartColorSearchDto();
        dto.setSearchValue("112231");

        // WHEN
        PartColorDto search = partColorDataManager.search(dto);

        // THEN
        Assertions.assertNotNull(search);
        Assertions.assertEquals(search.getNumber(), dto.getSearchValue());
    }
}
