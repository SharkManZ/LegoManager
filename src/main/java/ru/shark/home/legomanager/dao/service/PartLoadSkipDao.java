package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.dao.util.SpecificationUtils;
import ru.shark.home.legomanager.dao.entity.load.PartLoadSkipEntity;
import ru.shark.home.legomanager.dao.repository.PartLoadSkipRepository;

@Component
public class PartLoadSkipDao extends BaseDao<PartLoadSkipEntity> {
    private static final String PATTERN_FIELD = "pattern";

    private PartLoadSkipRepository partLoadSkipRepository;

    public PartLoadSkipDao() {
        super(PartLoadSkipEntity.class);
    }

    public PageableList<PartLoadSkipEntity> getWithPagination(RequestCriteria request) {
        Specification<PartLoadSkipEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(),
                PATTERN_FIELD);
        return partLoadSkipRepository.getWithPagination(request, searchSpec, PATTERN_FIELD);
    }

    @Autowired
    public void setPartLoadSkipRepository(PartLoadSkipRepository partLoadSkipRepository) {
        this.partLoadSkipRepository = partLoadSkipRepository;
    }
}
