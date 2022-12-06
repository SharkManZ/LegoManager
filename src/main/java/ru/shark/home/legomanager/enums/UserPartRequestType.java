package ru.shark.home.legomanager.enums;

public enum UserPartRequestType {
    ALL,            // все детали
    ONLY_ADDED,     // только добавленные
    ONLY_NOT_ADDED, // только не добавленные
    NOT_EQUALS,     // добавленные и не совпадающие по колличеству
    LOWER_COUNT     // только те которых меньше чем должно быть
}
