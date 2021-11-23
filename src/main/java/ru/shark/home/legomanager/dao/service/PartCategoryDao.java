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

import java.text.MessageFormat;

import static ru.shark.home.common.common.ErrorConstants.*;

@Component
public class PartCategoryDao extends BaseDao<PartCategoryEntity> {
    private static String NAME_FIELD = "name";

    private PartCategoryRepository partCategoryRepository;

    public PartCategoryDao() {
        super(PartCategoryEntity.class);
    }

    public PageableList<PartCategoryEntity> getWithPagination(RequestCriteria request) {
        Specification<PartCategoryEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(),
                NAME_FIELD);
        return partCategoryRepository.getWithPagination(request, searchSpec, NAME_FIELD);
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

        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        PartCategoryEntity entity = findById(id);

        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                    PartCategoryEntity.getDescription(), id));
        }

        super.deleteById(id);
    }

    @Autowired
    public void setPartCategoryRepository(PartCategoryRepository partCategoryRepository) {
        this.partCategoryRepository = partCategoryRepository;
    }
}
