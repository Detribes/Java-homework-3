package xyz.nasrally.domashka.restorepoint;

import xyz.nasrally.domashka.storage.*;

import java.util.*;

public interface RestorePoint {
    String getName();
    void addNewStorage(String name, Storage storage);
    Storage getStorage(String name);
    Map<String, Storage> getStorages();

    void save();
    void load();
}