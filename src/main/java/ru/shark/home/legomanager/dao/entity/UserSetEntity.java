package ru.shark.home.legomanager.dao.entity;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LEGO_USER_SETS")
public class UserSetEntity extends BaseEntity {
    private static final String DESCRIPTION = "Наборы владельца";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "UserSetsGenerator", sequenceName = "LEGO_USER_SETS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserSetsGenerator")
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
    @JoinColumn(name = "LEGO_SET_ID", nullable = false)
    private SetEntity set;

    /**
     * Количество экземпляров набора во владении.
     */
    @Column(name = "LEGO_COUNT", nullable = false)
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

    public SetEntity getSet() {
        return set;
    }

    public void setSet(SetEntity set) {
        this.set = set;
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
