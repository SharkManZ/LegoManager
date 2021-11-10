package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.SeriesEntity;

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
}
