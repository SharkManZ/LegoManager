package ru.shark.home.legomanager.datamanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.services.dto.Search;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.services.dto.SearchDto;
import ru.shark.home.legomanager.util.DbTest;

import java.util.List;

public class PartColorDataManagerTest extends DbTest {
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
        List<PartColorDto> list = partColorDataManager.getPartColorListByPartId(partId, null);

        // THEN
        Assertions.assertEquals(2, list.size());
        for (PartColorDto dto : list) {
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getPart());
            Assertions.assertNotNull(dto.getPart().getId());
            Assertions.assertNotNull(dto.getColor());
            Assertions.assertNotNull(dto.getColor().getId());
            Assertions.assertNotNull(dto.getAlternateNumber());
        }
    }

    @Test
    public void getPartColorListByPartIdWithEqualsSearch() {
        // GIVEN
        Long partId = entityFinder.findPartId("3010");
        Search search = new Search();
        search.setEquals(true);
        search.setValue("black");

        // WHEN
        List<PartColorDto> list = partColorDataManager.getPartColorListByPartId(partId, search);

        // THEN
        Assertions.assertEquals(1, list.size());
        for (PartColorDto dto : list) {
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getPart());
            Assertions.assertNotNull(dto.getPart().getId());
            Assertions.assertNotNull(dto.getColor());
            Assertions.assertNotNull(dto.getColor().getId());
            Assertions.assertNotNull(dto.getAlternateNumber());
        }
    }

    @Test
    public void getPartColorListByPartIdWithNotEqualsSearch() {
        // GIVEN
        Long partId = entityFinder.findPartId("3010");
        Search search = new Search();
        search.setValue("bla");

        // WHEN
        List<PartColorDto> list = partColorDataManager.getPartColorListByPartId(partId, search);

        // THEN
        Assertions.assertEquals(1, list.size());
        for (PartColorDto dto : list) {
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getPart());
            Assertions.assertNotNull(dto.getPart().getId());
            Assertions.assertNotNull(dto.getColor());
            Assertions.assertNotNull(dto.getColor().getId());
            Assertions.assertNotNull(dto.getAlternateNumber());
        }
    }

    @Test
    public void search() {
        // GIVEN
        SearchDto dto = new SearchDto();
        dto.setSearchValue("112231");

        // WHEN
        PartColorDto search = partColorDataManager.search(dto);

        // THEN
        Assertions.assertNotNull(search);
        Assertions.assertEquals(search.getNumber(), dto.getSearchValue());
    }
}
