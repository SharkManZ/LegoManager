package ru.shark.home.legomanager.dao.entity.load;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "LEGO_PART_LOAD_SKIP")
public class PartLoadSkipEntity extends BaseEntity {
    private static final String DESCRIPTION = "Шаблоны пропуска деталей импорта";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "PartLoadSkipGenerator", sequenceName = "LEGO_PART_LOAD_SKIP_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PartLoadSkipGenerator")
    private Long id;

    @Column(name = "LEGO_PATTERN", nullable = false)
    private String pattern;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
