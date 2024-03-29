package ru.shark.home.legomanager.dao.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.export.*;
import ru.shark.home.legomanager.util.DbTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.ObjectUtils.isEmpty;

public class ExportDaoTest extends DbTest {

    @BeforeAll
    public void init() {
        loadColors("ExportDaoTest/colors.json");
        loadPartCategories("ExportDaoTest/partCats.json");
        loadParts("ExportDaoTest/parts.json");
        loadSeries("ExportDaoTest/series.json");
        loadSets("ExportDaoTest/sets.json");
        loadUsers("ExportDaoTest/users.json");
        loadPartSkip("ExportDaoTest/partLoadSkip.json");
        loadPartLoadComparison("ExportDaoTest/loadComparison.json");
    }

    @Autowired
    private ExportDao exportDao;

    @Test
    public void exportColors() {
        // WHEN
        List<ColorDictionaryDto> list = exportDao.exportColors();

        // THEN
        Assertions.assertEquals(list.size(), 2);
        Assertions.assertTrue(list.stream().allMatch(item -> !isBlank(item.getName()) && !isBlank(item.getHexColor())));
    }

    @Test
    public void exportPartCategories() {
        // WHEN
        List<PartCategoryDictionaryDto> list = exportDao.exportPartCategories();

        // THEN
        Assertions.assertEquals(list.size(), 2);
        boolean emptyCatChecked = false;
        boolean partsCatChecked = false;
        boolean emptyPartChecked = false;
        boolean colorsPartChecked = false;
        for (PartCategoryDictionaryDto category : list) {
            Assertions.assertNotNull(category.getName());
            if ("tile".equalsIgnoreCase(category.getName())) {
                emptyCatChecked = true;
            } else if ("brick".equalsIgnoreCase(category.getName())) {
                partsCatChecked = true;
                for (PartDictionaryDto part : category.getParts()) {
                    Assertions.assertNotNull(part.getNumbers());
                    Assertions.assertNotNull(part.getName());
                    List<String> numbers = part.getNumbers().stream().map(item -> item.getNumber()).collect(Collectors.toList());
                    if (numbers.contains("3001")) {
                        emptyPartChecked = true;
                    } else if (numbers.contains("3010")) {
                        colorsPartChecked = true;
                        Assertions.assertEquals(part.getColors().size(), 2);
                    }
                }
            }
        }

        Assertions.assertTrue(emptyCatChecked);
        Assertions.assertTrue(emptyPartChecked);
        Assertions.assertTrue(partsCatChecked);
        Assertions.assertTrue(colorsPartChecked);
    }

    @Test
    public void exportSeries() {
        // WHEN
        List<SeriesDictionaryDto> list = exportDao.exportSeries();

        // THEN
        Assertions.assertEquals(2, list.size());
        boolean setsChecked = false;
        boolean partsChecked = false;
        for (SeriesDictionaryDto seriesDto : list) {
            Assertions.assertNotNull(seriesDto.getName());
            for (SetDictionaryDto setDto : seriesDto.getSets()) {
                setsChecked = true;
                Assertions.assertNotNull(setDto.getName());
                Assertions.assertNotNull(setDto.getNumber());
                Assertions.assertNotNull(setDto.getYear());
                for (SetPartDictionaryDto partDto : setDto.getParts()) {
                    partsChecked = true;
                    Assertions.assertNotNull(partDto.getCount());
                    Assertions.assertNotNull(partDto.getPartColorNumber());
                }
            }
        }
        Assertions.assertTrue(setsChecked);
        Assertions.assertTrue(partsChecked);
    }

    @Test
    public void exportUsers() {
        // WHEN
        List<UserDictionaryDto> list = exportDao.exportUsers();

        // THEN
        Assertions.assertEquals(2, list.size());

        boolean setsChecked = false;
        boolean partsChecked = false;
        for (UserDictionaryDto dto : list) {
            Assertions.assertNotNull(dto.getName());
            if (!isEmpty(dto.getSets())) {
                setsChecked = true;
                for (UserSetDictionaryDto setDto : dto.getSets()) {
                    Assertions.assertNotNull(setDto.getNumber());
                    Assertions.assertNotNull(setDto.getCount());
                }
            }
            if (!isEmpty(dto.getParts())) {
                partsChecked = true;
                Assertions.assertTrue(dto.getParts().stream()
                        .allMatch(item -> !isBlank(item.getPartNumber()) &&
                                !isBlank(item.getPartColorNumber()) &&
                                item.getCount() != 0));
            }

        }
        Assertions.assertTrue(setsChecked);
        Assertions.assertTrue(partsChecked);
    }

    @Test
    public void exportPartLoadSkip() {
        // WHEN
        List<PartLoadSkipDto> list = exportDao.exportPartLoadSkip();

        // THEN
        Assertions.assertEquals(2L, list.size());
        Assertions.assertTrue(list.stream().allMatch(item -> !isBlank(item.getPtn())));
    }

    @Test
    public void exportPartLoadComparison() {
        // WHEN
        List<PartLoadComparisonDto> list = exportDao.exportPartLoadComparison();

        // THEN
        Assertions.assertEquals(2L, list.size());
        Assertions.assertTrue(list.stream().allMatch(item -> !isBlank(item.getNumber()) && !isBlank(item.getName()) &&
                !isBlank(item.getPartNumber()) && !isBlank(item.getPartColorNumber())));
    }
}
