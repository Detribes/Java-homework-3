package xyz.detribes.bekapi.restorepoint;

import java.nio.file.*;

public abstract class AbstractFSRestorePoint extends AbstractRestorePoint {
    private final Path path;
    
    public AbstractFSRestorePoint(String name, Path path) {
        super(name);
        this.path = path.resolve(name);
    }

    public Path getPath() {
       return path;
    }
}