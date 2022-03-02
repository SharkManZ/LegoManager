package ru.shark.home.legomanager.dao.service;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.services.dto.SearchDto;
import ru.shark.home.legomanager.util.DaoServiceTest;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;

import static ru.shark.home.common.common.ErrorConstants.*;
import static ru.shark.home.legomanager.common.ErrorConstants.MORE_THAN_ONE_PART_COLOR;

public class PartColorDaoTest extends DaoServiceTest {
    @Autowired
    private PartColorDao partColorDao;

    @BeforeAll
    public void init() {
        loadPartCategories("PartColorDaoTest/partCats.json");
        loadColors("PartColorDaoTest/colors.json");
        loadParts("PartColorDaoTest/parts.json");
    }

    @Test
    public void getPartColorListByPartId() {
        Long partId = entityFinder.findPartId("3010");
        // GIVEN
        Ordering<PartColorEntity> ordering = new Ordering<PartColorEntity>() {
            @Override
            public int compare(@Nullable PartColorEntity partColorEntity, @Nullable PartColorEntity t1) {
                return Comparator.comparing(PartColorEntity::getNumber)
                        .compare(partColorEntity, t1);
            }
        };

        // WHEN
        List<PartColorEntity> list = partColorDao.getPartColorListByPartId(partId, null);

        // THEN
        Assertions.assertEquals(3, list.size());
        Assertions.assertTrue(ordering.isOrdered(list));
    }

    @Test
    public void saveWithCreate() {
        // GIVEN
        PartColorEntity entity = prepareEntity();

        // WHEN
        PartColorEntity saved = partColorDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(entity.getNumber(), saved.getNumber());
        Assertions.assertEquals(entity.getPart().getId(), saved.getPart().getId());
        Assertions.assertEquals(entity.getColor().getId(), saved.getColor().getId());
        Assertions.assertEquals(entity.getAlternateNumber(), saved.getAlternateNumber());
    }

    @Test
    public void saveWithCreateExistsByPartAndColor() {
        // GIVEN
        PartColorEntity entity = prepareEntity();
        entity.getPart().setId(entityFinder.findPartId("3010"));
        entity.getColor().setId(entityFinder.findColorId("Red"));

        // WHEN
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> partColorDao.save(entity));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartColorEntity.getDescription(),
                entity.getPart().getId() + " " + entity.getColor().getId()), exception.getMessage());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        PartColorEntity entity = prepareEntity();
        entity.setId(entityFinder.findPartColorId("098765"));

        // WHEN
        PartColorEntity saved = partColorDao.save(entity);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(entity.getId(), saved.getId());
        Assertions.assertEquals(entity.getNumber(), saved.getNumber());
        Assertions.assertEquals(entity.getPart().getId(), saved.getPart().getId());
        Assertions.assertEquals(entity.getColor().getId(), saved.getColor().getId());
        Assertions.assertEquals(entity.getAlternateNumber(), saved.getAlternateNumber());
    }

    @Test
    public void saveWithUpdateExistsByPartAndColor() {
        // GIVEN
        PartColorEntity entity = prepareEntity();
        entity.setId(entityFinder.findPartColorId("112231", "3010"));
        entity.getPart().setId(entityFinder.findPartId("3010"));
        entity.getColor().setId(entityFinder.findColorId("Red"));

        // WHEN
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> partColorDao.save(entity));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartColorEntity.getDescription(),
                entity.getPart().getId() + " " + entity.getColor().getId()), exception.getMessage());
    }

    @Test
    public void saveWithValidate() {
        // GIVEN
        PartColorEntity noNumber = prepareEntity();
        noNumber.setNumber(null);
        PartColorEntity noPart = prepareEntity();
        noPart.setPart(null);
        PartColorEntity noPartId = prepareEntity();
        noPartId.getPart().setId(null);
        PartColorEntity partNotFound = prepareEntity();
        partNotFound.getPart().setId(999L);
        PartColorEntity noColor = prepareEntity();
        noColor.setColor(null);
        PartColorEntity noColorId = prepareEntity();
        noColorId.getColor().setId(null);
        PartColorEntity colorNotFound = prepareEntity();
        colorNotFound.getColor().setId(999L);

        // WHEN
        ValidationException noNumberException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.save(noNumber));
        ValidationException noPartException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.save(noPart));
        ValidationException noPartIdException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.save(noPartId));
        ValidationException partNotFoundException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.save(partNotFound));
        ValidationException noColorException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.save(noColor));
        ValidationException noColorIdException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.save(noColorId));
        ValidationException colorNotFoundException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.save(colorNotFound));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "number",
                PartColorEntity.getDescription()), noNumberException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "part",
                PartColorEntity.getDescription()), noPartException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "part",
                PartColorEntity.getDescription()), noPartIdException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, PartEntity.getDescription(),
                partNotFound.getPart().getId()), partNotFoundException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "color",
                PartColorEntity.getDescription()), noColorException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_EMPTY_FIELD, "color",
                PartColorEntity.getDescription()), noColorIdException.getMessage());
        Assertions.assertEquals(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID, ColorEntity.getDescription(),
                colorNotFound.getColor().getId()), colorNotFoundException.getMessage());
    }

    @Test
    public void search() {
        // GIVEN
        SearchDto dto = new SearchDto();
        dto.setSearchValue("098765");

        // WHEN
        PartColorEntity entity = partColorDao.search(dto);

        // THEN
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(entity.getNumber(), dto.getSearchValue());
    }

    @Test
    public void searchWithMoreThanOneResult() {
        // GIVEN
        SearchDto dto = new SearchDto();
        dto.setSearchValue("112231");
        String expected = MessageFormat.format(MORE_THAN_ONE_PART_COLOR, 2, dto.getSearchValue());

        // WHEN
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> partColorDao.search(dto));

        // THEN
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void searchByColorAndPartNumbers() {
        // GIVEN
        SearchDto dto = new SearchDto();
        dto.setSearchValue("112231 3010");

        // WHEN
        PartColorEntity entity = partColorDao.search(dto);

        // THEN
        Assertions.assertNotNull(entity);
        Assertions.assertEquals("112231", entity.getNumber());
        Assertions.assertEquals("3010", entity.getPart().getNumber());
    }

    private PartColorEntity prepareEntity() {
        PartColorEntity entity = new PartColorEntity();
        entity.setNumber("5555");
        entity.setPart(new PartEntity());
        entity.getPart().setId(entityFinder.findPartId("3001"));
        entity.setColor(new ColorEntity());
        entity.getColor().setId(entityFinder.findColorId("Red"));
        entity.setAlternateNumber("111");

        return entity;
    }
}
