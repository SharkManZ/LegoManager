package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;

import java.util.List;
import java.util.Map;

public interface SeriesRepository extends BaseRepository<SeriesEntity> {
    /**
     * Возвращает серию набора по названию.
     *
     * @param name название.
     * @return сущность серии.
     */
    @Query(name = "findSeriesByName")
    SeriesEntity findSeriesByName(@Param("name") String name);

    /**
     * Возвращает обхее количество серий наборов.
     */
    @Query(name = "getSeriesCount")
    Long getCount();

    /**
     * Возвращает количество наборов для переданных серий.
     *
     * @param ids идентификаторы серий
     * @return количество наборов по сериям
     */
    @Query(name = "getSeriesSetsCountByIds")
    List<Map<String, Object>> getSeriesSetsCountByIds(@Param("ids") List<Long> ids);

    /**
     * Возвращает все серии с сортировкой по названию.
     */
    @Query(name = "getAllSeries")
    List<SeriesEntity> getAllSeries();
}
