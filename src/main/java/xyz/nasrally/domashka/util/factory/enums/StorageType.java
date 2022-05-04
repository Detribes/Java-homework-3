package xyz.nasrally.domashka.util.factory.enums;

/**
 * Перечисление с доступными типами стораджей. Используется в соответствующей фабрике.
 * Сейчас доступны два типа стораджа: FILE в форме файла в ФС и MEMORY в виде файла
 * хранящегося в оперативной памяти (т.е. пока существует процесс с программой).
 */
public enum StorageType {
    FILE, MEMORY
}