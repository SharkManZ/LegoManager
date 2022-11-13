package ru.shark.home.legomanager.loader;

import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.dao.dto.load.RemoteSetPartsDto;
import ru.shark.home.legomanager.dao.service.PartColorDao;
import ru.shark.home.legomanager.dao.service.SetDao;
import ru.shark.home.legomanager.dao.service.SetPartDao;

import java.util.List;

@Component
public class SetDataLoader {

    private SetDao setDao;
    private SetPartDao setPartDao;
    private PartColorDao partColorDao;

    public void loadSetParts(String setNumber, List<RemoteSetPartsDto> setParts) {

    }
}
