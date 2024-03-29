package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.dao.util.SpecificationUtils;
import ru.shark.home.legomanager.dao.entity.ColorEntity;
import ru.shark.home.legomanager.dao.repository.ColorRepository;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import static ru.shark.home.common.common.ErrorConstants.EMPTY_ENTITY;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_NOT_FOUND_BY_ID;

@Component
public class ColorDao extends BaseDao<ColorEntity> {
    private static String NAME_FIELD = "name";

    private ColorRepository colorRepository;

    public ColorDao() {
        super(ColorEntity.class);
    }

    public PageableList<ColorEntity> getWithPagination(RequestCriteria request) {
        Specification<ColorEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(), NAME_FIELD);
        return colorRepository.getWithPagination(request, searchSpec, NAME_FIELD);
    }

    public List<ColorEntity> getAllColors() {
        return colorRepository.getAllColors();
    }

    @Override
    public ColorEntity save(ColorEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(EMPTY_ENTITY, ColorEntity.getDescription()));
        }

        ColorEntity byName = colorRepository.findColorByName(entity.getName());
        if (byName != null && (entity.getId() == null || !entity.getId().equals(byName.getId()))) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                    ColorEntity.getDescription(), entity.getName()));
        }
        entity.setName(entity.getName().trim());
        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        ColorEntity entity = findById(id);

        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                    ColorEntity.getDescription(), id));
        }

        super.deleteById(id);
    }

    public List<ColorEntity> getListBySetId(Long setId) {
        if (setId == null) {
            return Collections.emptyList();
        }
        return colorRepository.getColorsBySetId(setId);
    }

    public List<ColorEntity> getPartNotExistsColors(Long partId) {
        return colorRepository.getNotAddedPartColorsByPartId(partId);
    }

    @Autowired
    public void setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }
}
