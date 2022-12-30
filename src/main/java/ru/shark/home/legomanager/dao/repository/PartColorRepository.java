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
     * Возвращает список цветов детали по набору идентификаторов деталей с сортировкой по идентификатору детали.
     *
     * @param partIds идентификаторы деталей
     * @return список цветов деталей
     */
    @Query(name = "getPartColorsByPartIds")
    List<PartColorEntity> getPartColorsByPartIds(@Param("partIds") List<Long> partIds);

    /**
     * Возвращает список цветов детали по идентификатору детали и поиском по названию цвета по строгому соответствию.
     *
     * @param partId идентификатор детали
     * @param search строка поиска
     * @return список цветов деталей
     */
    @Query(name = "getPartColorsByPartIdAndEqualsSearch")
    List<PartColorEntity> getPartColorsByPartIdAndEqualsSearch(@Param("partId") Long partId,
                                                               @Param("search") String search);

    /**
     * Возвращает список цветов детали по идентификатору детали и поиском по названию цвета по не строгому соответствию.
     *
     * @param partId идентификатор детали
     * @param search строка поиска
     * @return список цветов деталей
     */
    @Query(name = "getPartColorsByPartIdAndNotEqualsSearch")
    List<PartColorEntity> getPartColorsByPartIdAndNotEqualsSearch(@Param("partId") Long partId,
                                                                  @Param("search") String search);

    /**
     * Возвращает количество цветов детали по номеру.
     *
     * @param number номер цвета детали
     * @return количество
     */
    @Query(name = "getPartColorCountByNumber")
    Long getPartColorCountByNumber(@Param("number") String number);

    /**
     * Возвращает цвет детали по номеру.
     *
     * @param number номер цвета детали
     * @return цвет детали
     */
    @Query(name = "getPartColorByNumber")
    PartColorEntity getPartColorByNumber(@Param("number") String number);

    /**
     * Возвращает цвет детали по номеру цвета и номеру детали.
     *
     * @param number     номер цвета детали
     * @param partNumber номер детали
     * @return цвет детали
     */
    @Query(name = "getPartColorByNumberPartNumber")
    List<PartColorEntity> getPartColorByNumberPartNumber(@Param("number") String number, @Param("partNumber") String partNumber);

    /**
     * Возвращает цвет детали по детали и цвету
     *
     * @param partId  деталь
     * @param colorId цвет
     * @return цвет детали
     */
    @Query(name = "getPartColorByPartAndColor")
    PartColorEntity getPartColorByPartAndColor(@Param("partId") Long partId, @Param("colorId") Long colorId);

    /**
     * Возвращает идентификатор цвета детали по номеру цвета детали и номеру детали.
     *
     * @param partNumber  номер детали
     * @param colorNumber номер цвета детали
     * @return идентификатор
     */
    @Query(name = "getPartColorIdByPartColorNumberAndPartNumber")
    Long getPartColorIdByPartColorNumberAndPartNumber(@Param("partNumber") String partNumber,
                                                      @Param("colorNumber") String colorNumber);

    /**
     * Возвращает сколько раз цвет детали встречается в наборах.
     *
     * @param partColorId идентификатор цвета детали
     * @return кол-во
     */
    @Query(name = "getPartColorCountInSets")
    Long getPartColorCountInSets(@Param("partColorId") Long partColorId);

    /**
     * Возвращает сколько раз цвет детали встречается в деталях владельцев.
     *
     * @param partColorId идентификатор цвета детали
     * @return кол-во
     */
    @Query(name = "getPartColorCountInUserParts")
    Long getPartColorCountInUserParts(@Param("partColorId") Long partColorId);
}
