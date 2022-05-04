package xyz.nasrally.domashka.restorepoint;

import xyz.nasrally.domashka.storage.*;

import java.util.*;

public abstract class AbstractRestorePoint implements RestorePoint {
    private final String name;

    private Map<String, Storage> storagesToCopy = new HashMap<>();
    private final Map<String, Storage> actualStorages = new HashMap<>();

    public AbstractRestorePoint(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addNewStorage(String name, Storage storage) {
        storagesToCopy.put(name, storage);
    }

    @Override
    public Storage getStorage(String name) {
        return actualStorages.get(name);
    }

    @Override
    public Map<String, Storage> getStorages() {
        return actualStorages;
    }

    protected Map<String, Storage> getStoragesToCopy() {
        return storagesToCopy;
    }

    protected void resetStoragesToCopy() {
        storagesToCopy = new HashMap<>();
    }
}