package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;
import ru.shark.home.legomanager.dao.entity.load.PartLoadComparisonEntity;

public interface PartLoadComparisonRepository extends BaseRepository<PartLoadComparisonEntity> {

    /**
     * Возвращает цвет детали из данных сопоствления деталей загрузки по номеру и названию.
     *
     * @param number номер детали
     * @param name   название детали
     * @return цвет детали из справочника
     */
    @Query(name = "findPartColorByLoadPartComparisonByNumberAndName")
    PartColorEntity findPartColorByLoadPartComparisonByNumberAndName(@Param("number") String number,
                                                                     @Param("name") String name);
}
