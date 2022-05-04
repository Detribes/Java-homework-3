package xyz.nasrally.domashka.util.factory;

import org.slf4j.*;
import xyz.nasrally.domashka.storage.*;
import xyz.nasrally.domashka.util.factory.enums.*;

import java.nio.file.*;

/**
 * Фабрика используемая для создания репозиториев.
 * Доступны два типа репозиториев. Для создания FILE нужно дополинтельно передать объект
 * типа Path который содержит полный путь к этому файлу. Для MEMORY нужно передать строку
 * которая содержит данные виртуального "файла". В принципе второй полезен только для
 * тестирования.
 * См. RepositoryFactory
 */
public final class StorageFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageFactory.class);

    public static Storage create(StorageType type,
                                 String name,
                                 Object... args) {
        Storage result = null;
        switch (type) {
            case FILE:
                result = new FileStorage(name, (Path) args[0]);
                break;
            case MEMORY:
                result = new MemoryStorage(name, ((String) args[0]).getBytes());
                break;
        }
        return result;
    }
}