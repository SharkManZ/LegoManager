package ru.shark.home.legomanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ru.shark.home.common.services.dto.response.BaseResponse;
import ru.shark.home.legomanager.dao.repository.PartColorNumberRepository;
import ru.shark.home.legomanager.services.dto.PartColorRenameDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.shark.home.common.common.ErrorConstants.ERR_500;

@Component
public class RepairService {
    private final String partImagesPath =
            "H:\\Projects\\js\\LegoManagerUI\\lego-manager-ui\\public\\lego-images\\parts_old\\";
    private PartColorNumberRepository partColorNumberRepository;

    public BaseResponse renamePartColor(PartColorRenameDto request) {
        List<String> numbers = partColorNumberRepository.getMainPartColorNumbersByMainPartNumber(request.getPartNumber().trim());
        if (isEmpty(numbers)) {
            return BaseResponse.buildError(ERR_500, "Не найдены цвета детали " + request.getPartNumber().trim());
        }
        List<String> missingImages = new ArrayList<>();
        for (String number : numbers) {
            Path path = Paths.get(partImagesPath + number + ".png");
            if (!Files.exists(path)) {
                missingImages.add(number);
            }
        }
        BaseResponse response = new BaseResponse();
        if (!isEmpty(missingImages)) {
            return BaseResponse.buildError(ERR_500, "Не найдены картинки для номеров " + missingImages);
        }
        for (String number : numbers) {
            Path path = Paths.get(partImagesPath + number + ".png");
            Path targetPath = Paths.get(partImagesPath + request.getPartNumber().trim() + "_" + number + ".png");
            try {
                Files.move(path, targetPath);
            } catch (IOException e) {
                return BaseResponse.buildError(ERR_500, "Ошибка переименования цвета детали " + number);
            }
        }
        response = new BaseResponse();
        response.setBody("Успешно переименованы цвета " + numbers);
        response.setSuccess(true);
        return response;
    }

    @Autowired
    public void setPartColorNumberRepository(PartColorNumberRepository partColorNumberRepository) {
        this.partColorNumberRepository = partColorNumberRepository;
    }
}
