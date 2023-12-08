package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.load.PartLoadComparisonEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.List;

public class PartLoadComparisonRepositoryTest extends DbTest {

    @Autowired
    private PartLoadComparisonRepository repository;

    @BeforeAll
    public void init() {
        loadColors("PartLoadComparisonRepositoryTest/colors.json");
        loadPartCategories("PartLoadComparisonRepositoryTest/partCats.json");
        loadParts("PartLoadComparisonRepositoryTest/parts.json");
        loadPartLoadComparison("PartLoadComparisonRepositoryTest/loadComparison.json");
    }

    @Test
    public void findPartColorByLoadPartComparisonByNumberAndName() {
        // GIVEN
        String number = "3010a";
        String name = "black_name";
        PartColorEntity expectedColor = entityFinder.findPartColor("112231");

        // WHEN
        PartColorEntity partColor = repository.findPartColorByLoadPartComparisonByNumberAndName(number, name);

        // THEN
        Assertions.assertEquals(expectedColor.getId(), partColor.getId());
    }

    @Test
    public void getAllPartLoadComparison() {
        // WHEN
        List<PartLoadComparisonEntity> all = repository.getAllPartLoadComparison();

        // THEN
        Assertions.assertEquals(2, all.size());
    }
}
