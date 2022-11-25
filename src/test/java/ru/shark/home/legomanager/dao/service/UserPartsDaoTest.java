package ru.shark.home.legomanager.dao.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.common.RequestSearch;
import ru.shark.home.legomanager.dao.dto.UserPartListDto;
import ru.shark.home.legomanager.dao.dto.request.UserPartListRequest;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.UserEntity;
import ru.shark.home.legomanager.dao.entity.UserPartEntity;
import ru.shark.home.legomanager.enums.UserPartRequestType;
import ru.shark.home.legomanager.util.DbTest;

import java.text.MessageFormat;
import java.util.List;

import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_NOT_FOUND_BY_ID;

public class UserPartsDaoTest extends DbTest {
    public static final String USER = "Максим";

    @Autowired
    private UserPartsDao userPartsDao;

    @BeforeAll
    public void init() {
        loadSeries("UserPartsDaoTest/series.json");
        loadColors("UserPartsDaoTest/colors.json");
        loadPartCategories("UserPartsDaoTest/partCats.json");
        loadParts("UserPartsDaoTest/parts.json");
        loadSets("UserPartsDaoTest/sets.json");
        loadUsers("UserPartsDaoTest/users.json");
    }

    @Test
    public void getList() {
        // GIVEN
        Long userId = entityFinder.findUserId(USER);
        RequestCriteria requestCriteria = new RequestCriteria(0, 3);
        UserPartListRequest requestDto = new UserPartListRequest(UserPartRequestType.ALL, userId);

        // WHEN
        PageableList<UserPartListDto> list = userPartsDao.getList(requestDto, requestCriteria);

        // THEN
        checkPagingDtoList(list, 3, 3L);
        List<UserPartListDto> data = list.getData();
        for (UserPartListDto dto : data) {
            Assertions.assertNotNull(dto.getColorName());
            Assertions.assertNotNull(dto.getUserCount());
            Assertions.assertNotNull(dto.getUserId());
            Assertions.assertNotNull(dto.getColorNumber());
            Assertions.assertNotNull(dto.getSetsCount());
            Assertions.assertNotNull(dto.getCategoryName());
            Assertions.assertNotNull(dto.getNumber());
            Assertions.assertNotNull(dto.getPartName());
            Assertions.assertNotNull(dto.getCategoryId());
            Assertions.assertNotNull(dto.getColorId());
            Assertions.assertNotNull(dto.getPartColorId());
        }

        Assertions.assertTrue(data.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("112231") &&
                item.getUserCount() == 25 && item.getSetsCount() == 10));
        Assertions.assertTrue(data.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("55531") &&
                item.getUserCount() == 3 && item.getSetsCount() == 3));
        Assertions.assertTrue(data.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("55521") &&
                item.getUserCount() == 5 && item.getSetsCount() == 0));
    }

    @Test
    public void getListWithOnlyAdded() {
        // GIVEN
        Long userId = entityFinder.findUserId(USER);
        RequestCriteria requestCriteria = new RequestCriteria(0, 3);
        UserPartListRequest requestDto = new UserPartListRequest(UserPartRequestType.ONLY_ADDED, userId);

        // WHEN
        PageableList<UserPartListDto> list = userPartsDao.getList(requestDto, requestCriteria);

        // THEN
        checkPagingDtoList(list, 2, 2L);
        List<UserPartListDto> data = list.getData();
        Assertions.assertTrue(data.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("112231") &&
                item.getUserCount() == 25 && item.getSetsCount() == 10));
        Assertions.assertTrue(data.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("55521") &&
                item.getUserCount() == 5 && item.getSetsCount() == 0));
    }

    @Test
    public void getListWithOnlyNotAdded() {
        // GIVEN
        Long userId = entityFinder.findUserId(USER);
        RequestCriteria requestCriteria = new RequestCriteria(0, 3);
        UserPartListRequest requestDto = new UserPartListRequest(UserPartRequestType.ONLY_NOT_ADDED, userId);

        // WHEN
        PageableList<UserPartListDto> list = userPartsDao.getList(requestDto, requestCriteria);

        // THEN
        checkPagingDtoList(list, 1, 1L);
        List<UserPartListDto> data = list.getData();
        Assertions.assertTrue(data.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("55531") &&
                item.getUserCount() == 3 && item.getSetsCount() == 3));
    }

    @Test
    public void getListWithSearch() {
        // GIVEN
        Long userId = entityFinder.findUserId(USER);
        RequestCriteria requestCriteria = new RequestCriteria(0, 3);
        requestCriteria.setSearch(new RequestSearch("112231", false));
        UserPartListRequest requestDto = new UserPartListRequest(UserPartRequestType.ALL, userId);

        // WHEN
        PageableList<UserPartListDto> list = userPartsDao.getList(requestDto, requestCriteria);

        // THEN
        checkPagingDtoList(list, 1, 1L);
        List<UserPartListDto> data = list.getData();
        Assertions.assertTrue(data.stream().anyMatch(item -> item.getUserId().equals(userId) &&
                item.getColorNumber().equalsIgnoreCase("112231") &&
                item.getUserCount() == 25 && item.getSetsCount() == 10));
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        Long userId = entityFinder.findUserId(USER);
        Long partColorId = entityFinder.findPartColorId("55531");
        UserPartEntity entity = prepareEntity(userId, partColorId);
        Integer expectedCount = 2;

        // WHEN
        UserPartEntity saved = userPartsDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(userId, saved.getUser().getId());
        Assertions.assertEquals(partColorId, saved.getPartColor().getId());
        Assertions.assertEquals(expectedCount, saved.getCount());
    }

    @Test
    public void saveWithCreateAndNotInSets() {
        // GIVEN
        Long userId = entityFinder.findUserId(USER);
        Long partColorId = entityFinder.findPartColorId("55511");
        UserPartEntity entity = prepareEntity(userId, partColorId);

        // WHEN
        UserPartEntity saved = userPartsDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(userId, saved.getUser().getId());
        Assertions.assertEquals(partColorId, saved.getPartColor().getId());
        Assertions.assertEquals(entity.getCount(), saved.getCount());
    }

    @Test
    public void saveWithCreateAndCountEqualsToSetCount() {
        // GIVEN
        Long userId = entityFinder.findUserId(USER);
        Long partColorId = entityFinder.findPartColorId("55531");
        UserPartEntity entity = prepareEntity(userId, partColorId);
        entity.setCount(3);
        Integer expectedCount = 0;

        // WHEN
        UserPartEntity saved = userPartsDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(expectedCount, saved.getCount());
    }

    @Test()
    public void saveWithCreateAndExists() {
        // GIVEN
        Long userId = entityFinder.findUserId(USER);
        Long partColorId = entityFinder.findPartColorId("112231");
        UserPartEntity entity = prepareEntity(userId, partColorId);

        // WHEN
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> userPartsDao.save(entity));

        // THEN
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                UserPartEntity.getDescription(), "112231"), exception.getMessage());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        Long userPartId = entityFinder.findUserPartId(USER, "112231");
        Long userId = entityFinder.findUserId(USER);
        Long partColorId = entityFinder.findPartColorId("112231");
        UserPartEntity entity = prepareEntity(userId, partColorId);
        entity.setCount(1);
        entity.setId(userPartId);

        // WHEN
        UserPartEntity saved = userPartsDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(userPartId, saved.getId());
        Assertions.assertEquals(entity.getCount(), saved.getCount());
    }

    @Test
    public void saveWithUpdateAndEqualToSetCount() {
        // GIVEN
        Long userPartId = entityFinder.findUserPartId(USER, "112231");
        Long userId = entityFinder.findUserId(USER);
        Long partColorId = entityFinder.findPartColorId("112231");
        UserPartEntity entity = prepareEntity(userId, partColorId);
        entity.setCount(10);
        entity.setId(userPartId);
        Integer expectedCount = 0;

        // WHEN
        UserPartEntity saved = userPartsDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(expectedCount, saved.getCount());
    }

    @Test
    public void saveWithUpdateAndExists() {
        // GIVEN
        Long userId = entityFinder.findUserId(USER);
        Long partColorId = entityFinder.findPartColorId("112231");
        UserPartEntity entity = prepareEntity(userId, partColorId);

        // WHEN
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> userPartsDao.save(entity));

        // THEN
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                UserPartEntity.getDescription(), "112231"), exception.getMessage());
    }

    @Test
    public void deleteById() {
        // GIVEN
        Long userPartId = entityFinder.findUserPartId(USER, "112231");

        // WHEN
        userPartsDao.deleteById(userPartId);

        // THEN
        isDeleted(userPartId, UserPartEntity.class);
    }

    @Test
    public void deleteByIdWithNotFound() {
        // GIVEN
        Long userPartId = 999L;

        // WHEN
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> userPartsDao.deleteById(userPartId));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                UserPartEntity.getDescription(), userPartId), exception.getMessage());
    }


    private UserPartEntity prepareEntity(Long userId, Long partColorId) {
        UserPartEntity entity = new UserPartEntity();
        entity.setUser(new UserEntity());
        entity.getUser().setId(userId);
        entity.setPartColor(new PartColorEntity());
        entity.getPartColor().setId(partColorId);
        entity.setCount(5);

        return entity;
    }
}
