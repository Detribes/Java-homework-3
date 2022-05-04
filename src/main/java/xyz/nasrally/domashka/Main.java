package xyz.nasrally.domashka;

import xyz.nasrally.domashka.repository.*;
import xyz.nasrally.domashka.storage.*;
import xyz.nasrally.domashka.util.backupjob.*;
import xyz.nasrally.domashka.util.factory.*;
import xyz.nasrally.domashka.util.factory.enums.*;

import java.nio.file.*;
import java.util.*;

public class Main {
//    public static void main(String[] args) {
//        Repository repo = RepositoryFactory.create(FileSystemRepository.class,
//                "myrepo",
//                Path.of("/home/nikita/domatest"));
//
//        RestorePoint rp = RestorePointFactory.create(
//                repo,
//                ZipSingleFileRestorePoint.class,
//                "pizda",
//                Path.of("/home/nikita/domatest/zaloopa"));
//
//        rp.addNewStorage("nasral", StorageFactory.create(MemoryStorage.class, "nasral", "Eba vot eno nasral"));
//        rp.addNewStorage("govno", StorageFactory.create(MemoryStorage.class, "govno", "Eba vot eno govno"));
////        rp.copyStorage("mocha", StorageFactory.create(MemoryStorage.class, "mocha", "Eba vot eno net"));
////        rp.copyStorage("pupok", StorageFactory.create(MemoryStorage.class, "pupok", "Eba vot eno danet"));
//
//        rp.save();
//
//        Storage stor = rp.retrieveStorage("nasral");
//
////        rp.copyStorage("nasral", StorageFactory.create(MemoryStorage.class, "nasral", "Eba vot eno nasral"));
////        rp.copyStorage("govno", StorageFactory.create(MemoryStorage.class, "govno", "Eba vot eno govno"));
//        rp.addNewStorage("mocha", StorageFactory.create(MemoryStorage.class, "mocha", "Eba vot eno net"));
//        rp.addNewStorage("pupok", StorageFactory.create(MemoryStorage.class, "pupok", "Eba vot eno danet"));
//
//        rp.save();
//    }

    public static void main(String[] args) {
//        var repo = (FileSystemRepository) Helper.createFileSystemRepository(Path.of("/home/nikita/domatest"));
        Repository repo = RepositoryFactory.create(RepositoryType.FS,
                RestorePointType.ZipSingleFS,
                "myrepo",
                Path.of("/home/nikita/domatest"));

//        BackupJob job = new BackupJob("restore1", (FSRepository) repo, ZipSingleFileRestorePoint.class);
        BackupJob job = new BackupJob("restore2", repo);

        List<Storage> storages = new ArrayList<>();
//        storages.add(StorageFactory.create(StorageType.MEMORY, "govno", "Eba vot eno da"));
//        storages.add(StorageFactory.create(StorageType.MEMORY, "mocha", "Eba vot eno net"));
//        storages.add(StorageFactory.create(StorageType.MEMORY, "pupok", "Eba vot eno danet"));
        storages.add(StorageFactory.create(StorageType.MEMORY, "asho1", "Eba vot eno da"));
        storages.add(StorageFactory.create(StorageType.MEMORY, "LEHA!", "Eba vot eno net"));
        storages.add(StorageFactory.create(StorageType.MEMORY, "да_нее", "Eba vot eno danet"));

        storages.forEach((stor) -> job.addJob(new JobObject(stor)));

        job.run();

//        repo.restore("3", Path.of("/home/nikita/newdir"));
    }
}