package xyz.detribes.bekapi.util.factory;

import org.slf4j.*;
import xyz.detribes.bekapi.repository.Repository;
import xyz.detribes.bekapi.restorepoint.RestorePoint;
import xyz.detribes.bekapi.restorepoint.ZipSingleFileRestorePoint;
import xyz.detribes.bekapi.restorepoint.ZipSplitFileRestorePoint;
import xyz.detribes.bekapi.util.factory.enums.RestorePointType;
import xyz.nasrally.domashka.repository.*;
import xyz.nasrally.domashka.restorepoint.*;
import xyz.nasrally.domashka.util.factory.enums.*;

import java.nio.file.*;


/**
 * Фабрика используемая для создания репозиториев.
 * Доступны два типа репозиториев. Для создания обоих ничего не надо дополнительно передавать
 * все данные в расположении они получают у объекта repo.
 * См. RepositoryFactory
 */
public final class RestorePointFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestorePointFactory.class);

    public static RestorePoint create(Repository repo,
                                      RestorePointType type,
                                      String name) {
        RestorePoint result = null;
        switch (type) {
            case ZipSingleFS:
                result = new ZipSingleFileRestorePoint(name, Path.of(repo.getLocation()));
                break;
            case ZipSplitFS:
                result = new ZipSplitFileRestorePoint(name, Path.of(repo.getLocation()));
                break;
        }
        repo.addRestorePoint(name, result);

        return result;
    }
}