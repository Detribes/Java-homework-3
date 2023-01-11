package xyz.detribes.bekapi.util.factory.enums;

/**
 * Перечисление с доступными типами рестор-поинтов. Используется в соответствующей фабрике.
 * Сейчас доступны два типа рестор-поинтов. ZipSplitFS сохраняет стораджи в отдельные
 * зип-архивы, а ZipSingleFS все стораджи в один зип-архив.
 */
public enum RestorePointType {
    ZipSplitFS, ZipSingleFS
}