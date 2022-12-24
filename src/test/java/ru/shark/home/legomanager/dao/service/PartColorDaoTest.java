package ru.shark.home.legomanager.dao.service;

import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.ColorDto;
import ru.shark.home.legomanager.dao.dto.PartColorDto;
import ru.shark.home.legomanager.dao.dto.PartDto;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.PartColorNumberEntity;
import ru.shark.home.legomanager.dao.entity.PartEntity;
import ru.shark.home.legomanager.services.dto.SearchDto;
import ru.shark.home.legomanager.util.DbTest;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.List;

import static ru.shark.home.common.common.ErrorConstants.*;
import static ru.shark.home.legomanager.common.ErrorConstants.MORE_THAN_ONE_PART_COLOR;

public class PartColorDaoTest extends DbTest {
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
                String color1 = partColorEntity.getNumbers().stream().filter(PartColorNumberEntity::getMain)
                        .findFirst().orElseThrow(() -> new ValidationException("Не найден главный цвет детали"))
                        .getNumber();
                String color2 = t1.getNumbers().stream().filter(PartColorNumberEntity::getMain)
                        .findFirst().orElseThrow(() -> new ValidationException("Не найден главный цвет детали"))
                        .getNumber();
                return color1.compareTo(color2);
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
        PartColorDto dto = prepareDto();

        // WHEN
        PartColorDto saved = partColorDao.savePartColor(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(dto.getNumber(), saved.getNumber());
        Assertions.assertEquals(dto.getPart().getId(), saved.getPart().getId());
        Assertions.assertEquals(dto.getColor().getId(), saved.getColor().getId());
        Assertions.assertEquals(dto.getAlternateNumber(), saved.getAlternateNumber());
    }

    @Test
    public void saveWithCreateExistsByPartAndColor() {
        // GIVEN
        PartColorDto dto = prepareDto();
        dto.getPart().setId(entityFinder.findPartId("3010"));
        dto.getColor().setId(entityFinder.findColorId("Red"));

        // WHEN
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> partColorDao.savePartColor(dto));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartColorEntity.getDescription(),
                dto.getPart().getId() + " " + dto.getColor().getId()), exception.getMessage());
    }

    @Test
    public void saveWithUpdate() {
        // GIVEN
        PartColorDto dto = prepareDto();
        dto.setId(entityFinder.findPartColorId("098765"));

        // WHEN
        PartColorDto saved = partColorDao.savePartColor(dto);

        // THEN
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(dto.getId(), saved.getId());
        Assertions.assertEquals(dto.getNumber(), saved.getNumber());
        Assertions.assertEquals(dto.getPart().getId(), saved.getPart().getId());
        Assertions.assertEquals(dto.getColor().getId(), saved.getColor().getId());
        Assertions.assertEquals(dto.getAlternateNumber(), saved.getAlternateNumber());
    }

    @Test
    public void saveWithUpdateExistsByPartAndColor() {
        // GIVEN
        PartColorDto dto = prepareDto();
        dto.setId(entityFinder.findPartColorId("112231", "3010"));
        dto.getPart().setId(entityFinder.findPartId("3010"));
        dto.getColor().setId(entityFinder.findColorId("Red"));

        // WHEN
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> partColorDao.savePartColor(dto));

        // THEN
        Assertions.assertEquals(MessageFormat.format(ENTITY_ALREADY_EXISTS, PartColorEntity.getDescription(),
                dto.getPart().getId() + " " + dto.getColor().getId()), exception.getMessage());
    }

    @Test
    public void saveWithValidate() {
        // GIVEN
        PartColorDto noNumber = prepareDto();
        noNumber.setNumber(null);
        PartColorDto noPart = prepareDto();
        noPart.setPart(null);
        PartColorDto noPartId = prepareDto();
        noPartId.getPart().setId(null);
        PartColorDto partNotFound = prepareDto();
        partNotFound.getPart().setId(999L);
        PartColorDto noColor = prepareDto();
        noColor.setColor(null);
        PartColorDto noColorId = prepareDto();
        noColorId.getColor().setId(null);
        PartColorDto colorNotFound = prepareDto();
        colorNotFound.getColor().setId(999L);

        // WHEN
        ValidationException noNumberException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.savePartColor(noNumber));
        ValidationException noPartException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.savePartColor(noPart));
        ValidationException noPartIdException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.savePartColor(noPartId));
        ValidationException partNotFoundException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.savePartColor(partNotFound));
        ValidationException noColorException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.savePartColor(noColor));
        ValidationException noColorIdException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.savePartColor(noColorId));
        ValidationException colorNotFoundException = Assertions.assertThrows(ValidationException.class, () -> partColorDao.savePartColor(colorNotFound));

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
        Assertions.assertTrue(entity.getNumbers().stream().anyMatch(item -> item.getNumber().equalsIgnoreCase(dto.getSearchValue())));
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
        Assertions.assertTrue(entity.getNumbers().stream().anyMatch(item -> item.getNumber().equalsIgnoreCase("112231")));
        Assertions.assertTrue(entity.getPart().getNumbers().stream().anyMatch(item -> item.getNumber().equalsIgnoreCase("3010")));
    }

    private PartColorDto prepareDto() {
        PartColorDto dto = new PartColorDto();
        dto.setNumber("5555");
        dto.setPart(new PartDto());
        dto.getPart().setId(entityFinder.findPartId("3001"));
        dto.setColor(new ColorDto());
        dto.getColor().setId(entityFinder.findColorId("Red"));
        dto.setAlternateNumber("111");

        return dto;
    }
}
