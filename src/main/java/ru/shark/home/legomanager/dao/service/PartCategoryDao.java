package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.dao.util.SpecificationUtils;
import ru.shark.home.legomanager.dao.entity.PartCategoryEntity;
import ru.shark.home.legomanager.dao.repository.PartCategoryRepository;
import ru.shark.home.legomanager.dao.repository.PartRepository;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import static ru.shark.home.common.common.ErrorConstants.EMPTY_ENTITY;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_ALREADY_EXISTS;
import static ru.shark.home.common.common.ErrorConstants.ENTITY_NOT_FOUND_BY_ID;
import static ru.shark.home.legomanager.common.ErrorConstants.PART_CATEGORY_DELETE_WITH_SETS;

@Component
public class PartCategoryDao extends BaseDao<PartCategoryEntity> {
    private static String NAME_FIELD = "name";

    private PartCategoryRepository partCategoryRepository;
    private PartRepository partRepository;

    public PartCategoryDao() {
        super(PartCategoryEntity.class);
    }

    public PageableList<PartCategoryEntity> getWithPagination(RequestCriteria request) {
        Specification<PartCategoryEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(),
                NAME_FIELD);
        return partCategoryRepository.getWithPagination(request, searchSpec, NAME_FIELD);
    }

    public List<PartCategoryEntity> getAllCategories() {
        return partCategoryRepository.getAllCategories();
    }

    @Override
    public PartCategoryEntity save(PartCategoryEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(EMPTY_ENTITY, PartCategoryEntity.getDescription()));
        }

        PartCategoryEntity byName = partCategoryRepository.findPartCategoryByName(entity.getName());
        if (byName != null && (entity.getId() == null || !entity.getId().equals(byName.getId()))) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                    PartCategoryEntity.getDescription(), entity.getName()));
        }
        entity.setName(entity.getName().trim());
        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        PartCategoryEntity entity = findById(id);

        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                    PartCategoryEntity.getDescription(), id));
        }

        if (partRepository.getPartCountByCategory(id) > 0) {
            throw new ValidationException(PART_CATEGORY_DELETE_WITH_SETS);
        }

        super.deleteById(id);
    }

    public List<PartCategoryEntity> getListBySetId(Long setId) {
        if (setId == null) {
            return Collections.emptyList();
        }

        return partCategoryRepository.getCategoriesBySetId(setId);
    }

    @Autowired
    public void setPartCategoryRepository(PartCategoryRepository partCategoryRepository) {
        this.partCategoryRepository = partCategoryRepository;
    }

    @Autowired
    public void setPartRepository(PartRepository partRepository) {
        this.partRepository = partRepository;
    }
}
