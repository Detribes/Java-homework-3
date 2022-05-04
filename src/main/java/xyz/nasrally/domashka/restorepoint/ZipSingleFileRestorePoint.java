package xyz.nasrally.domashka.restorepoint;

import org.slf4j.*;
import xyz.nasrally.domashka.storage.*;
import xyz.nasrally.domashka.util.factory.*;
import xyz.nasrally.domashka.util.factory.enums.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class ZipSingleFileRestorePoint extends AbstractFSRestorePoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipSingleFileRestorePoint.class);

    private Path dirUnzipStorages;

    private boolean isLoaded = false;

    public ZipSingleFileRestorePoint(String name, Path path) {
        super(name, path);

        createNewTmpDir();

        Runtime.getRuntime().addShutdownHook(new Thread(this::removeTmpDir));

        load();
    }

    @Override
    public void save() {
        load();
        Path copy = Path.of(getPath().toString() + "_copy");

        try (ZipOutputStream zos = openZipWrite(copy)) {
            Map<String, Storage> newStorages = new HashMap<>(getStorages());

            getStoragesToCopy().entrySet().stream()
                    .filter(e -> !newStorages.containsKey(e.getKey()))
                    .forEach(e -> newStorages.put(e.getKey(), e.getValue()));

            for (Map.Entry<String, Storage> e : newStorages.entrySet()) {
                ZipEntry entry = new ZipEntry(e.getKey());
                zos.putNextEntry(entry);
                e.getValue().retrieve().transferTo(zos);
                zos.closeEntry();
            }
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }

        if (Files.exists(getPath())) {
            try {
                Files.delete(getPath());
                Files.move(copy, getPath());
            } catch (IOException e) {
                LOGGER.error(e.toString());
            }
        } else {
            try {
                Files.move(copy, getPath());
            } catch (IOException e) {
                LOGGER.error(e.toString());
            }
        }

        resetStoragesToCopy();
        removeTmpDir();
        isLoaded = false;
        load();
    }

    @Override
    public void load() {
        if (isLoaded) return;
        if (!Files.exists(getPath())) {
            isLoaded = true;
            return;
        }

        removeTmpDir();
        createNewTmpDir();

        try (ZipInputStream zis = openZipRead(getPath())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
//                ZipEntry entry = zis.getNextEntry();

                Storage newStor = StorageFactory.create(
                        StorageType.FILE,
                        Objects.requireNonNull(entry).getName(),
                        dirUnzipStorages.resolve(entry.getName()));

                getStorages().put(entry.getName(), newStor);
                zis.transferTo(newStor.copyInto());
                zis.closeEntry();
            }
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }

        isLoaded = true;
    }

    private ZipOutputStream openZipWrite(Path path) throws IOException {
        OpenOption[] openOptions;
        if (!Files.exists(path)) {
            createParentDirs(path);
            openOptions = new OpenOption[] {StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE};
        } else {
            openOptions = new OpenOption[] {StandardOpenOption.WRITE};
        }

        return new ZipOutputStream(Files.newOutputStream(path, openOptions));
    }

    private ZipInputStream openZipRead(Path path) throws IOException {
        OpenOption[] openOptions;
        if (!Files.exists(path)) {
            createParentDirs(path);
            openOptions = new OpenOption[] {StandardOpenOption.CREATE_NEW, StandardOpenOption.READ};
        } else {
            openOptions = new OpenOption[] {StandardOpenOption.READ};
        }

        return new ZipInputStream(Files.newInputStream(path, openOptions));
    }

    private void createParentDirs(Path path) {
        Path parent = path.getParent();
        if (!Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                LOGGER.error(e.toString());
            }
        }
    }

    private void removeTmpDir() {
        if (Files.exists(dirUnzipStorages)) {
            try {
                Files.newDirectoryStream(dirUnzipStorages).forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        LOGGER.error(e.toString());
                    }
                });
                Files.delete(dirUnzipStorages);
            } catch (IOException e) {
                LOGGER.error(e.toString());
            }
        }
    }

    private void createNewTmpDir() {
        try {
            dirUnzipStorages = Files.createTempDirectory("domashkaZipSingle");
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

}