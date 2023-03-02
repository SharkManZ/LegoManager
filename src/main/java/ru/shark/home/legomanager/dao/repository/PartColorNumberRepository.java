package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.PartColorNumberEntity;

import java.util.List;
import java.util.Set;

public interface PartColorNumberRepository extends BaseRepository<PartColorNumberEntity> {

    /**
     * Возвращает номера цвета детали по ее идентификатору.
     *
     * @param partColorId идентификатор цвета детали
     * @return список номеров
     */
    @Query(value = "select pcn from PartColorNumberEntity pcn where pcn.partColor.id = :partColorId")
    List<PartColorNumberEntity> getPartColorNumbersByPartColorId(@Param("partColorId") Long partColorId);

    /**
     * Возвращает номера цветов деталей по их идентификаторам.
     *
     * @param ids идентификаторы цветов деталей
     * @return список номеров
     */
    @Query(value = "select pcn from PartColorNumberEntity pcn where pcn.partColor.id in (:ids)")
    List<PartColorNumberEntity> getPartColorNumbersByPartColorIds(@Param("ids") Set<Long> ids);

    /**
     * Возвращает основные номера цветов детали по основному номеру детали
     *
     * @param partNumber основное номер детали
     * @return основные номера цветов
     */
    @Query(value = "select pcn.number from PartColorNumberEntity pcn join pcn.partColor pc join pc.part p " +
            "join p.numbers pn where lower(pn.number) = lower(:partNumber) and pn.main = true and pcn.main = true")
    List<String> getMainPartColorNumbersByMainPartNumber(@Param("partNumber") String partNumber);
}
