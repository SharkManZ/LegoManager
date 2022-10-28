package ru.shark.home.legomanager.dao.entity;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.*;

/**
 * Детали владельца.
 */
@Entity
@Table(name = "LEGO_USER_PARTS")
public class UserPartEntity extends BaseEntity {

    private static final String DESCRIPTION = "Детали владельца";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "UserPartsGenerator", sequenceName = "LEGO_USER_PART_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserPartsGenerator")
    private Long id;

    /**
     * Ссылка на владельца.
     */
    @ManyToOne
    @JoinColumn(name = "LEGO_USER_ID", nullable = false)
    private UserEntity user;

    /**
     * Ссылка на набор.
     */
    @ManyToOne
    @JoinColumn(name = "LEGO_PART_COLOR_ID", nullable = false)
    private PartColorEntity partColor;

    /**
     * Количество деталей без учета их количества в наборах (т.е. хранятся только детали которых больше или меньше
     * чем в наборах)
     */
    @Column(name = "LEGO_COUNT")
    private Integer count;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PartColorEntity getPartColor() {
        return partColor;
    }

    public void setPartColor(PartColorEntity partColor) {
        this.partColor = partColor;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }
}
