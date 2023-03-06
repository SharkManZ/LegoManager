package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.load.PartLoadSkipEntity;
import ru.shark.home.legomanager.util.DbTest;

import java.util.List;

public class PartLoadSkipRepositoryTest extends DbTest {

    @Autowired
    private PartLoadSkipRepository partLoadSkipRepository;

    @BeforeAll
    public void init() {
        loadPartSkip("PartLoadSkipRepositoryTest/partLoadSkip.json");
    }

    @Test
    public void getAllPartSkip() {
        // WHEN
        List<PartLoadSkipEntity> list = partLoadSkipRepository.getAllPartSkip();

        // THEN
        Assertions.assertEquals(2L, list.size());
    }
}
