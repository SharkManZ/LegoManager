package ru.shark.home.legomanager.dao.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.NumberDto;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.dao.entity.PartNumberEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.Arrays;
import java.util.List;

public class PartNumberDaoTest extends DbTest {
    @Autowired
    private PartNumberDao partNumberDao;

    @BeforeAll
    public void init() {
        loadPartCategories("PartNumberDaoTest/partCats.json");
        loadColors("PartNumberDaoTest/colors.json");
        loadParts("PartNumberDaoTest/parts.json");
    }

    @Test
    public void savePartNumbers() {
        // GIVEN
        PartEntity part = entityFinder.findPart("3010");
        List<NumberDto> dtoList = Arrays.asList(new NumberDto("3010", false), new NumberDto("4000", true));

        // WHEN
        List<PartNumberEntity> list = partNumberDao.savePartNumbers(part, dtoList);

        // THEN
        Assertions.assertEquals(2, list.size());
        Assertions.assertTrue(list.stream().anyMatch(item -> item.getNumber().equalsIgnoreCase("3010") && !item.getMain()));
        Assertions.assertTrue(list.stream().anyMatch(item -> item.getNumber().equalsIgnoreCase("4000") && item.getMain()));
    }

    @Test
    public void savePartNumbersWithNewPart() {
        // GIVEN
        List<NumberDto> dtoList = Arrays.asList(new NumberDto("3010", false), new NumberDto("4000", true));
        PartEntity partEntity = (PartEntity) em.createQuery("select p from PartEntity p where p.name = :name")
                .setParameter("name", "Brick 2x8")
                .getSingleResult();

        // WHEN
        List<PartNumberEntity> list = partNumberDao.savePartNumbers(partEntity, dtoList);

        // THEN
        Assertions.assertEquals(2, list.size());
        Assertions.assertTrue(list.stream().anyMatch(item -> item.getNumber().equalsIgnoreCase("3010") && !item.getMain()));
        Assertions.assertTrue(list.stream().anyMatch(item -> item.getNumber().equalsIgnoreCase("4000") && item.getMain()));
    }
}
