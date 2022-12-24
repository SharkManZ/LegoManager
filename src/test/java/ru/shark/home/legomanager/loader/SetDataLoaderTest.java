package ru.shark.home.legomanager.loader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shark.home.legomanager.dao.dto.load.RemoteSetPartsDto;
import ru.shark.home.legomanager.util.DbTest;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.shark.home.legomanager.common.ErrorConstants.*;

public class SetDataLoaderTest extends DbTest {

    @Autowired
    private SetDataLoader setDataLoader;

    @BeforeAll
    public void init() {
        loadSeries("SetDataLoaderTest/series.json");
        loadColors("SetDataLoaderTest/colors.json");
        loadPartCategories("SetDataLoaderTest/partCats.json");
        loadParts("SetDataLoaderTest/parts.json");
        loadSets("SetDataLoaderTest/sets.json");
        loadUsers("SetDataLoaderTest/users.json");
    }

    @Test
    public void loadSetParts() {
        // GIVEN
        String setNum = "60296";
        List<RemoteSetPartsDto> list = Arrays.asList(prepareRemoteDto("3010", "112231", 10),
                prepareRemoteDto("555", "55531", 5));
        Long partColorId1 = entityFinder.findPartColorId("112231");
        Long partColorId2 = entityFinder.findPartColorId("55531");
        Long setId = entityFinder.findSetId(setNum);

        // WHEN
        setDataLoader.loadSetParts(setNum, list);

        // THEN
        Long setPartId1 = entityFinder.findSetPartId(setId, partColorId1);
        Long setPartId2 = entityFinder.findSetPartId(setId, partColorId2);
        Assertions.assertNotNull(setPartId1);
        Assertions.assertNotNull(setPartId2);
    }

    @Test
    public void loadSetPartsWithValidateErrors() {
        // GIVEN
        String setNum = "60296";
        List<RemoteSetPartsDto> list = Arrays.asList(prepareRemoteDto("3010", "112231", 10),
                prepareRemoteDto("555", "55531", 5));
        // WHEN
        loadWithError(null, list, EMPTY_SET_NUMBER);
        loadWithError(setNum, Collections.emptyList(), EMPTY_IMPORT_PARTS);
        loadWithError("99999", list, MessageFormat.format(SET_NOT_FOUND, "99999"));
        loadWithError("42082", list, SET_MUST_BE_EMPTY);

        // THEN
    }

    @Test
    public void loadSetPartsWithNotFound() {
        // GIVEN
        String setNum = "60296";
        List<RemoteSetPartsDto> list = Arrays.asList(prepareRemoteDto("3010", "112231", 10),
                prepareRemoteDto("555", "55532", 5));
        Long partColorId1 = entityFinder.findPartColorId("112231");
        Long partColorId2 = entityFinder.findPartColorId("55531");
        Long setId = entityFinder.findSetId(setNum);

        // WHEN
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> setDataLoader.loadSetParts(setNum, list));


        // THEN
        Assertions.assertNotNull(validationException);
        Assertions.assertEquals("Не найдена деталь с номером 555 и номером цвета 55532", validationException.getMessage());
    }

    private void loadWithError(String setNum, List<RemoteSetPartsDto> list, String err) {
        try {
            setDataLoader.loadSetParts(setNum, list);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals(err, e.getMessage());
        }
    }

    private RemoteSetPartsDto prepareRemoteDto(String number, String colorNumber, Integer count) {
        RemoteSetPartsDto dto = new RemoteSetPartsDto();
        dto.setNumber(number);
        dto.setColorNumber(colorNumber);
        dto.setCount(count);
        dto.setName(number + " title");
        return dto;
    }
}
