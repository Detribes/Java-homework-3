package xyz.nasrally.domashka.repository;

import xyz.nasrally.domashka.restorepoint.*;
import xyz.nasrally.domashka.util.factory.enums.*;

import java.nio.file.*;
import java.util.*;

public interface Repository {
    /**
     * Геттер для имени репозитория
     * @return имя репозитория
     */
    String getName();

    /**
     * Метод для добавления в репозиторий нового рестор-поинта. Должен использоваться только фабрикой
     * @param name имя рестор-поинта
     * @param restorePoint сам рестор-поинт
     */
    void addRestorePoint(String name, RestorePoint restorePoint);

    /**
     * Геттер для конкретного рестор-поинта
     * @param name имя запрашиваемого рестор-поинта
     * @return найденный рестор-поинт
     */
    RestorePoint getRestorePoint(String name);

    /**
     * Геттер для всего ассоциативного массива (имя -> рестор-поинт) с рестор-поинтами
     * @return map with restore points
     */
    Map<String, RestorePoint> getRestorePoints();

    /**
     * Этот метод возвращает строку с расположением репозитория в каком-либо формате. Формат,
     * по идее, специфичен для каждого репозитория, но т.к у нас только один тип репы, то это
     * "типа для будущего типа для расширения возможностей"
     * Каждый рестор-поинт должен создаваться относительно этого пути.
     * @return строка с расположением репозитория
     */
    String getLocation();

    /**
     * В нашей реализации всего этого говна только репозиторий знает с какими типами стораджей
     * он работает.
     * @return тип рестор-поинта
     */
    RestorePointType getRestorePointType();

    /**
     * Метод, вызов которого заставляет репозиторий подгрузить все свои рестор-поинты
     * exist).
     */
    void load();

    /**
     * Метод, при вызове которого репозиторий запишет все стораджи из конкретного рестор-поинта
     * в заданную директорию.
     * @param name имя рестор-поинта
     * @param path путь в ФС куда он "восстановится"
     */
    void restore(String name, Path path);
}