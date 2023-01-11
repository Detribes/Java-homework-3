package xyz.detribes.bekapi.repository;

import org.slf4j.*;
import xyz.detribes.bekapi.restorepoint.*;
import xyz.detribes.bekapi.util.factory.*;
import xyz.detribes.bekapi.util.factory.enums.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FSRepository implements Repository {
    private static final Logger LOGGER = LoggerFactory.getLogger(FSRepository.class);

    private final String name;
    private final Path path;
    private final Map<String, RestorePoint> restorePoints;
    private final RestorePointType type;

    public FSRepository(String name, Path path, RestorePointType type) {
        if (!Files.isDirectory(path)) throw new IllegalArgumentException("Path '" + path + "' is not a directory.");

        this.name = name;
        this.path = path.resolve(name);
        this.type = type;
        restorePoints = new HashMap<>();
        load();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addRestorePoint(String name, RestorePoint restorePoint) {
        restorePoints.put(name, restorePoint);
    }

    @Override
    public RestorePoint getRestorePoint(String name) {
        if (!restorePoints.containsKey(name)) throw new IllegalArgumentException("Restore point '" + name + "' doesn't exist'");

        return restorePoints.get(name);
    }

    @Override
    public Map<String, RestorePoint> getRestorePoints() {
        return restorePoints;
    }

    @Override
    public String getLocation() {
        return path.toString();
    }

    @Override
    public RestorePointType getRestorePointType() {
        return type;
    }

    @Override
    public void load() {
        try {
            Files.walk(getPath(), 1)
                    .skip(1)
                    .forEach(p -> {
                        String restorePointName = p.getFileName().toString();
                        RestorePoint restorePoint = RestorePointFactory.create(this,
                                getRestorePointType(),
                                restorePointName);
                        restorePoint.load();
                        addRestorePoint(restorePointName, restorePoint);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Path getPath() {
        return path;
    }

    /**
     * Тут нет реализации, потому что ты наверняка сам сможешь её написать
     * См. описание метода в интерфейсе
     */
    @Override
    public void restore(String name, Path path) {

    }
}