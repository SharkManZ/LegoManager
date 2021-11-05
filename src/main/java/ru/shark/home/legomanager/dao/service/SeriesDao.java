package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.entity.BaseEntity;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.dao.util.SpecificationUtils;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.dao.repository.SeriesRepository;

import java.text.MessageFormat;

import static ru.shark.home.common.common.ErrorConstants.*;

@Component
public class SeriesDao extends BaseDao<SeriesEntity> {
    private static final String NAME_FIELD = "name";

    private SeriesRepository seriesRepository;

    protected SeriesDao() {
        super(SeriesEntity.class);
    }

    public PageableList<SeriesEntity> getWithPagination(RequestCriteria request) {
        Specification<BaseEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(), NAME_FIELD);
        return seriesRepository.getWithPagination(request, searchSpec, NAME_FIELD);
    }

    @Override
    public SeriesEntity save(SeriesEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(EMPTY_ENTITY, SeriesEntity.getDescription()));
        }

        SeriesEntity byName = seriesRepository.findSeriesByName(entity.getName());
        if (byName != null && (entity.getId() == null || !entity.getId().equals(byName.getId()))) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_ALREADY_EXISTS,
                    SeriesEntity.getDescription(), entity.getName()));
        }

        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        SeriesEntity entity = findById(id);

        if (entity == null) {
            throw new IllegalArgumentException(MessageFormat.format(ENTITY_NOT_FOUND_BY_ID,
                    SeriesEntity.getDescription(), id));
        }

        super.deleteById(id);
    }

    @Autowired
    public void setSeriesRepository(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }
}
