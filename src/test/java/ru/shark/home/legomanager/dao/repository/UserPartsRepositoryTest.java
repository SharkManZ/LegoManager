package ru.shark.home.legomanager.dao.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.UserPartListDto;
import ru.shark.home.legomanager.dao.entity.UserPartEntity;
import ru.shark.home.legomanager.util.DaoServiceTest;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class UserPartsRepositoryTest extends DaoServiceTest {

    @Autowired
    private UserPartsRepository userPartsRepository;

    @BeforeAll
    public void init() {
        loadSeries("UserPartsRepositoryTest/series.json");
        loadColors("UserPartsRepositoryTest/colors.json");
        loadPartCategories("UserPartsRepositoryTest/partCats.json");
        loadParts("UserPartsRepositoryTest/parts.json");
        loadSets("UserPartsRepositoryTest/sets.json");
        loadUsers("UserPartsRepositoryTest/users.json");
    }

    @Test
    public void getUserPartsByUser() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");

        // WHEN
        List<UserPartListDto> list = userPartsRepository.getUserPartsByUser(userId);

        // THEN
        Assertions.assertEquals(4, list.size());
        Assertions.assertTrue(list.stream().allMatch(this::checkListDto));
        Assertions.assertTrue(list.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("112231") &&
                item.getUserCount() == 25 && item.getSetsCount() == 10));
        Assertions.assertTrue(list.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("55531") &&
                item.getUserCount() == 3 && item.getSetsCount() == 3));
        Assertions.assertTrue(list.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("55521") &&
                item.getUserCount() == 5 && item.getSetsCount() == 0));
    }

    @Test
    public void findUserPartByUserAndPartColor() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");
        Long partColorId = entityFinder.findPartColorId("112231");

        // WHEN
        UserPartEntity userPart = userPartsRepository.findUserPartByUserAndPartColor(partColorId, userId);

        // THEN
        Assertions.assertNotNull(userPart);
        Assertions.assertEquals(userId, userPart.getUser().getId());
        Assertions.assertEquals(partColorId, userPart.getPartColor().getId());
    }

    @Test
    public void getPartCountInUserSets() {
        // GIVEN
        Long userId = entityFinder.findUserId("Максим");
        Long partColorId = entityFinder.findPartColorId("112231");
        Long expected = 10L;

        // WHEN
        Long count = userPartsRepository.getPartCountInUserSets(userId, partColorId);

        // THEN
        Assertions.assertEquals(expected, count);
    }

    private boolean checkListDto(UserPartListDto dto) {
        return !isBlank(dto.getPartName()) && !isBlank(dto.getCategoryName()) && !isBlank(dto.getColorNumber()) &&
                !isBlank(dto.getNumber()) && dto.getUserCount() != null && dto.getSetsCount() != null;
    }
}
