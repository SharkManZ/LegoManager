package ru.shark.home.legomanager.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.shark.home.common.dao.common.PageableList;
import ru.shark.home.common.dao.common.RequestCriteria;
import ru.shark.home.common.dao.entity.BaseEntity;
import ru.shark.home.common.dao.service.BaseDao;
import ru.shark.home.common.dao.util.SpecificationUtils;
import ru.shark.home.legomanager.dao.dto.SeriesFullDto;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;
import ru.shark.home.legomanager.dao.repository.SeriesRepository;
import ru.shark.home.legomanager.dao.repository.SetRepository;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.shark.home.common.common.ErrorConstants.*;
import static ru.shark.home.legomanager.common.ErrorConstants.SERIES_DELETE_WITH_SETS;

@Component
public class SeriesDao extends BaseDao<SeriesEntity> {
    private static final String NAME_FIELD = "name";

    private SeriesRepository seriesRepository;
    private SetRepository setRepository;

    protected SeriesDao() {
        super(SeriesEntity.class);
    }

    public PageableList<SeriesFullDto> getWithPagination(RequestCriteria request) {
        Specification<SeriesEntity> searchSpec = SpecificationUtils.searchSpecification(request.getSearch(), NAME_FIELD);
        return getListWithAdditionalFields(seriesRepository.getWithPagination(request, searchSpec, NAME_FIELD));
    }

    private PageableList<SeriesFullDto> getListWithAdditionalFields(PageableList<SeriesEntity> list) {
        List<Map<String, Object>> counts = seriesRepository.getSeriesSetsCountByIds(list.getData()
                .stream()
                .map(SeriesEntity::getId).collect(Collectors.toList()));
        List<SeriesFullDto> result = new ArrayList<>();
        list.getData().forEach(entity -> {
            SeriesFullDto dto = new SeriesFullDto();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            Map<String, Object> map = counts.stream()
                    .filter(mapItem -> mapItem.get("id").equals(entity.getId()))
                    .findFirst().orElse(null);
            dto.setSetsCount((Long) map.get("cnt"));
            dto.setImgName(entity.getName().toLowerCase()
                    .replaceAll(" ", "_")
                    .replaceAll("-", "_"));
            result.add(dto);
        });
        return new PageableList<>(result, list.getTotalCount());
    }

    public List<SeriesEntity> getAllSeries() {
        return seriesRepository.getAllSeries();
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
        if (setRepository.getSetCountBySeries(id) > 0) {
            throw new ValidationException(SERIES_DELETE_WITH_SETS);
        }

        super.deleteById(id);
    }

    @Autowired
    public void setSeriesRepository(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    @Autowired
    public void setSetRepository(SetRepository setRepository) {
        this.setRepository = setRepository;
    }
}
