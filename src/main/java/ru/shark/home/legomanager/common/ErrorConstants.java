package ru.shark.home.legomanager.common;

/**
 * Константы ошибок.
 */
public class ErrorConstants {
    public static final String SERIES_DELETE_WITH_SETS = "Нельзя удалять серию при наличии в ней наборов";
    public static final String PART_CATEGORY_DELETE_WITH_SETS = "Нельзя удалять категорию деталей при наличии в ней деталей";
    public static final String MORE_THAN_ONE_PART_COLOR = "Найдено {0} цветов деталей по номеру {1}. " +
            "Воспользуйтесь поиском по 2м номерам (цвет детали и деталь)";
    public static final String PART_COLOR_DELETE_WITH_SET_PARTS = "Нельзя удалить цвет детали, если он используется в наборах";
    public static final String PART_COLOR_DELETE_WITH_USER_PARTS = "Нельзя удалить цвет детали, если по нему внесены данные в деталях владельцев";

    // loader constants
    public static final String EMPTY_SET_NUMBER = "Не указан номер набора для импорта!";
    public static final String EMPTY_IMPORT_PARTS = "Не указаны детали для импорта";
    public static final String SET_NOT_FOUND = "Не найден набор с номером {0}";
    public static final String SET_MUST_BE_EMPTY = "Импорт разрешен только в пустой набор!";
    public static final String PART_NOT_FOUND = "Не найдена деталь с номером {0} и номером цвета {1}";
}
