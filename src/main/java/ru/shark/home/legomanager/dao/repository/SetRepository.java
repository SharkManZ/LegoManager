package ru.shark.home.legomanager.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shark.home.common.dao.repository.BaseRepository;
import ru.shark.home.legomanager.dao.entity.SetEntity;

public interface SetRepository extends BaseRepository<SetEntity> {
    /**
     * Возвращает набор по номеру.
     *
     * @param number номер.
     * @return сущность набора.
     */
    @Query(name = "findSetByNumber")
    SetEntity findSetByNumber(@Param("number") String number);
}
