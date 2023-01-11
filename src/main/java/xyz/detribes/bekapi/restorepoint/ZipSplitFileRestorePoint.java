package xyz.detribes.bekapi.restorepoint;

import org.slf4j.*;
import xyz.detribes.bekapi.storage.*;
import xyz.detribes.bekapi.util.factory.*;
import xyz.detribes.bekapi.util.factory.enums.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class ZipSplitFileRestorePoint extends AbstractFSRestorePoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipSplitFileRestorePoint.class);

    private Path dirUnzipStorages;

    private boolean isLoaded = false;

    public ZipSplitFileRestorePoint(String name, Path path) {
        super(name, path);

        createNewTmpDir();

        Runtime.getRuntime().addShutdownHook(new Thread(this::removeTmpDir));

        load();
    }

    @Override
    public void save() {
        load();

        Map<String, Storage> newStorages = new HashMap<>();

        getStoragesToCopy().entrySet().stream()
                .filter(e -> !getStorages().containsKey(e.getKey()))
                .forEach(e -> newStorages.put(e.getKey(), e.getValue()));

        for (Map.Entry<String, Storage> e : newStorages.entrySet()) {
            try (ZipOutputStream zos = openZipWrite(getPath().resolve(e.getKey()))) {
                Storage stor = e.getValue();

                ZipEntry entry = new ZipEntry(stor.getName());
                zos.putNextEntry(entry);

                stor.retrieve().transferTo(zos);
                zos.closeEntry();
            } catch (IOException ex) {
                ex.printStackTrace();
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

        try {
            Files.find(getPath(), 1, (p, a) -> a.isRegularFile())
                    .forEach(path -> {
                        Path newFile = null;
                        String newFileName = null;
                        try {
                            newFile = Files.createFile(dirUnzipStorages.resolve(path.getFileName().toString()));
                            newFileName = newFile.getFileName().toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Objects.requireNonNull(newFile);
                        Objects.requireNonNull(newFileName);

                        try (ZipInputStream zis = openZipRead(path);
                             OutputStream fos = new BufferedOutputStream(
                                     Files.newOutputStream(
                                         newFile,
                                         StandardOpenOption.WRITE))) {
                            zis.transferTo(fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Storage newStor = StorageFactory.create(
                                StorageType.FILE,
                                newFileName,
                                dirUnzipStorages.resolve(newFileName));

                        getStorages().put(newFileName, newStor);
                    });
        } catch (IOException e) {
            e.printStackTrace();
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
            dirUnzipStorages = Files.createTempDirectory("domashkaZipSplit");
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }
}