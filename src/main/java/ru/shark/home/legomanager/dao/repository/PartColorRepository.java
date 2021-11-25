package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;

import java.util.List;

public interface PartColorRepository extends BaseRepository<PartColorEntity> {

    /**
     * Возвращает список цветов детали по идентификатору детали.
     *
     * @param partId идентификатор детали
     * @return список цветов деталей
     */
    @Query(name = "getPartColorsByPartId")
    List<PartColorEntity> getPartColorsByPartId(@Param("partId") Long partId);

    /**
     * Возвращает цвет детали по номеру.
     *
     * @param number номер цвета детали
     * @return цвет детали
     */
    @Query(name = "getPartColorByNumber")
    PartColorEntity getPartColorByNumber(@Param("number") String number);

    /**
     * Возвращает цвет детали по детали и цвету
     *
     * @param partId  деталь
     * @param colorId цвет
     * @return цвет детали
     */
    @Query(name = "getPartColorByPartAndColor")
    PartColorEntity getPartColorByPartAndColor(@Param("partId") Long partId, @Param("colorId") Long colorId);
}
