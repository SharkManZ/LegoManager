package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shark.home.legomanager.dao.dto.export.*;
import ru.shark.home.legomanager.dao.entity.*;
import ru.shark.home.legomanager.dao.repository.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class ExportDao {
    private ColorRepository colorRepository;
    private PartCategoryRepository partCategoryRepository;
    private PartRepository partRepository;
    private PartColorRepository partColorRepository;
    private SeriesRepository seriesRepository;
    private SetRepository setRepository;
    private SetPartRepository setPartRepository;
    private UsersRepository usersRepository;
    private UserSetsRepository userSetsRepository;
    private UserPartsRepository userPartsRepository;

    /**
     * Экспорт цветов.
     *
     * @return список цветов
     */
    public List<ColorDictionaryDto> exportColors() {
        List<ColorEntity> allColors = colorRepository.getAllColors();
        return allColors.stream().map(this::colorEntityToDictionary).collect(Collectors.toList());
    }

    /**
     * Экспорт категорий, их деталей и цветов деталей.
     *
     * @return дерево категории - детали - цвета
     */
    public List<PartCategoryDictionaryDto> exportPartCategories() {
        List<PartCategoryEntity> allCategories = partCategoryRepository.getAllCategories();
        return allCategories.stream().map(this::categoryEntityToDictionary).collect(Collectors.toList());
    }

    /**
     * Экспорт серий, их наборов и деталей наборов.
     *
     * @return дерево серий - наборов - деталей
     */
    public List<SeriesDictionaryDto> exportSeries() {
        List<SeriesEntity> list = seriesRepository.getAllSeries();
        return list.stream().map(this::seriesEntityToDictionary).collect(Collectors.toList());
    }

    /**
     * Экспорт владельцев.
     *
     * @return дерево владельцев
     */
    public List<UserDictionaryDto> exportUsers() {
        List<UserEntity> list = usersRepository.getAllUsers();
        return list.stream().map(this::userEntityToDictionary).collect(Collectors.toList());
    }

    private UserDictionaryDto userEntityToDictionary(UserEntity entity) {
        UserDictionaryDto dto = new UserDictionaryDto();
        dto.setName(entity.getName());

        List<UserSetEntity> sets = userSetsRepository.getUserSetsByUser(entity.getId());
        if (!isEmpty(sets)) {
            dto.setSets(sets.stream().map(this::userSetEntityToDictionary).collect(Collectors.toList()));
        }

        List<UserPartEntity> parts = userPartsRepository.getAllUserPartsByUser(entity.getId());
        if (!isEmpty(parts)) {
            dto.setParts(parts.stream().map(this::userPartEntityToDictionary).collect(Collectors.toList()));
        }
        return dto;
    }

    private UserSetDictionaryDto userSetEntityToDictionary(UserSetEntity entity) {
        UserSetDictionaryDto dto = new UserSetDictionaryDto();
        dto.setNumber(entity.getSet().getNumber());
        dto.setCount(entity.getCount());

        return dto;
    }

    private UserPartDictionaryDto userPartEntityToDictionary(UserPartEntity entity) {
        UserPartDictionaryDto dto = new UserPartDictionaryDto();
        dto.setPartNumber(entity.getPartColor().getPart().getNumbers()
                .stream().filter(PartNumberEntity::getMain).findFirst().orElseThrow(() -> new ValidationException("Не найден главный номер детали"))
                .getNumber());
        dto.setPartColorNumber(entity.getPartColor().getNumbers().stream().filter(PartColorNumberEntity::getMain)
                .findFirst().orElseThrow(() -> new ValidationException("Не найлен главный номер цвета детали")).getNumber());
        dto.setCount(entity.getCount());

        return dto;
    }

    private SeriesDictionaryDto seriesEntityToDictionary(SeriesEntity entity) {
        SeriesDictionaryDto dto = new SeriesDictionaryDto();
        dto.setName(entity.getName());
        dto.setSets(setRepository.findBySeriesId(entity.getId())
                .stream()
                .map(this::setEntityToDictionary)
                .collect(Collectors.toList()));

        return dto;
    }

    private SetDictionaryDto setEntityToDictionary(SetEntity entity) {
        SetDictionaryDto dto = new SetDictionaryDto();
        dto.setNumber(entity.getNumber());
        dto.setName(entity.getName());
        dto.setYear(entity.getYear());
        dto.setParts(setPartRepository.findBySetId(entity.getId())
                .stream()
                .map(this::setPartEntityToDictionary)
                .collect(Collectors.toList()));

        return dto;
    }

    private SetPartDictionaryDto setPartEntityToDictionary(SetPartEntity entity) {
        SetPartDictionaryDto dto = new SetPartDictionaryDto();
        dto.setPartColorNumber(entity.getPartColor().getNumbers().stream().filter(PartColorNumberEntity::getMain)
                .findFirst().orElseThrow(() -> new ValidationException("Не найлен главный номер цвета детали")).getNumber());
        dto.setCount(entity.getCount());

        return dto;
    }

    private PartCategoryDictionaryDto categoryEntityToDictionary(PartCategoryEntity entity) {
        PartCategoryDictionaryDto dto = new PartCategoryDictionaryDto();
        dto.setName(entity.getName());
        List<PartEntity> partList = partRepository.findByCategoryId(entity.getId());
        dto.setParts(partList.stream().map(this::partEntityToDictionary).collect(Collectors.toList()));

        return dto;
    }

    private PartDictionaryDto partEntityToDictionary(PartEntity entity) {
        PartDictionaryDto dto = new PartDictionaryDto();
        dto.setName(entity.getName());
        dto.setColors(partColorRepository.getPartColorsByPartId(entity.getId())
                .stream()
                .map(item -> item.getColor().getName())
                .collect(Collectors.toList()));
        dto.setNumbers(entity.getNumbers().stream().map(item -> new NumberDictionaryDto(item.getNumber(), item.getMain()))
                .collect(Collectors.toList()));
        return dto;
    }

    private ColorDictionaryDto colorEntityToDictionary(ColorEntity entity) {
        ColorDictionaryDto dto = new ColorDictionaryDto();
        dto.setName(entity.getName());
        dto.setHexColor(entity.getHexColor());

        return dto;
    }

    @Autowired
    public void setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Autowired
    public void setPartCategoryRepository(PartCategoryRepository partCategoryRepository) {
        this.partCategoryRepository = partCategoryRepository;
    }

    @Autowired
    public void setPartRepository(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Autowired
    public void setPartColorRepository(PartColorRepository partColorRepository) {
        this.partColorRepository = partColorRepository;
    }

    @Autowired
    public void setSeriesRepository(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    @Autowired
    public void setSetRepository(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    @Autowired
    public void setSetPartRepository(SetPartRepository setPartRepository) {
        this.setPartRepository = setPartRepository;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setUserSetsRepository(UserSetsRepository userSetsRepository) {
        this.userSetsRepository = userSetsRepository;
    }

    @Autowired
    public void setUserPartsRepository(UserPartsRepository userPartsRepository) {
        this.userPartsRepository = userPartsRepository;
    }
}
