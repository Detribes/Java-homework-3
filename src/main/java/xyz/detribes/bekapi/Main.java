package xyz.detribes.bekapi;

import xyz.detribes.bekapi.repository.*;
import xyz.detribes.bekapi.storage.*;
import xyz.detribes.bekapi.util.backupjob.*;
import xyz.detribes.bekapi.util.factory.*;
import xyz.detribes.bekapi.util.factory.enums.*;

import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
//        var repo = (FileSystemRepository) Helper.createFileSystemRepository(Path.of("/home/Roman/domatest"));
        Repository repo = RepositoryFactory.create(RepositoryType.FS,
                RestorePointType.ZipSingleFS,
                "myrepo",
                Path.of("/home/Roman/domatest"));

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

//        repo.restore("3", Path.of("/home/Roman/newdir"));
    }
}