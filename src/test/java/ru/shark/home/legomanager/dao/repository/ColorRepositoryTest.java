package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

public class ColorRepositoryTest extends DaoServiceTest {
    @Autowired
    private ColorRepository colorRepository;

    @BeforeAll
    public void init() {
        loadColors("ColorRepositoryTest/colors.json");
    }

    @Test
    public void findColorByName() {
        // GIVEN
        ColorEntity expected = entityFinder.findColor("Black");

        // WHEN
        ColorEntity black = colorRepository.findColorByName("Black");

        // THEN
        Assertions.assertNotNull(black);
        Assertions.assertEquals(expected.getId(), black.getId());
    }
}
