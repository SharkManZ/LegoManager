package ru.shark.home.legomanager.dao.repository;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.shark.home.legomanager.dao.entity.PartColorNumberEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
public class PartColorNumberRepositoryTest extends DbTest {
    @Autowired
    private PartColorNumberRepository partColorNumberRepository;

    @BeforeAll
    public void init() {
        loadPartCategories("PartColorNumberRepositoryTest/partCats.json");
        loadColors("PartColorNumberRepositoryTest/colors.json");
        loadParts("PartColorNumberRepositoryTest/parts.json");
    }

    @Test
    public void getPartColorNumbersByPartColorId() {
        // GIVEN
        Long partColorId = entityFinder.findPartColorId("112231");
        List<String> numbers = Arrays.asList("112231", "898", "899");

        // WHEN
        List<PartColorNumberEntity> list = partColorNumberRepository.getPartColorNumbersByPartColorId(partColorId);

        // WHEN
        Assertions.assertEquals(3, list.size());
        Assertions.assertTrue(list.stream().map(PartColorNumberEntity::getNumber).collect(Collectors.toList()).containsAll(numbers));
    }

    @Test
    public void getPartColorNumbersByPartColorIds() {
        // GIVEN
        Set<Long> ids = Sets.newHashSet(entityFinder.findPartColorId("112231"), entityFinder.findPartColorId("332221"));
        List<String> numbers = Arrays.asList("112231", "898", "899", "332221");

        // WHEN
        List<PartColorNumberEntity> list = partColorNumberRepository.getPartColorNumbersByPartColorIds(ids);

        // THEN
        Assertions.assertEquals(4, list.size());
        Assertions.assertTrue(list.stream().map(PartColorNumberEntity::getNumber).collect(Collectors.toList()).containsAll(numbers));
    }
}
