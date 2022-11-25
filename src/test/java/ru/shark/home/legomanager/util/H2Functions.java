package ru.shark.home.legomanager.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Функции POSTGRESQL отсутствующие в H2.
 */
public class H2Functions {
    /**
     * Преобразует строку в массив используя переданный разделитель
     *
     * @param source    исходная строка
     * @param delimiter разделитель
     * @return массив элементов
     */
    public static String[] stringToArray(String source, String delimiter) {
        return source.split(delimiter);
    }

    /**
     * Возвращает позицию элемента в массиве. Позиция начинается с 1.
     *
     * @param array   исходный массив
     * @param element элемент для поиска
     * @return позиция в массиве
     */
    public static int arrayPosition(String[] array, String element) {
        if (ObjectUtils.isEmpty(array) || StringUtils.isBlank(element)) {
            return 0;
        }

        return Arrays.asList(array).stream().map(item -> item.trim()).collect(Collectors.toList()).indexOf(element) + 1;
    }
}
