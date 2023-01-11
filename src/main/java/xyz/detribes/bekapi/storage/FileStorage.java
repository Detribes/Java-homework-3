package xyz.detribes.bekapi.storage;

import org.slf4j.*;

import java.io.*;
import java.nio.file.*;

public class FileStorage implements Storage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorage.class);

    private final Path path;
    private final String name;

    public FileStorage(String name, Path path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public OutputStream copyInto() throws IOException {
        Path parent = path.getParent();
        if (!Files.exists(parent, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OpenOption[] openOptions;
        if (!Files.exists(path)) {
            openOptions = new OpenOption[]{
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE
            };
        } else {
            openOptions = new OpenOption[]{
                    StandardOpenOption.WRITE
            };
        }

        return Files.newOutputStream(path, openOptions);
//        try (OutputStream dest = Files.newOutputStream(path, openOptions)) {
//            data.transferTo(dest);
//        } catch (IOException e) {
//            LOGGER.error(e.toString());
//        }
    }

    public Path getPath() {
        return path;
    }

    @Override
    public InputStream retrieve() {
        InputStream result = null;
        OpenOption[] openOptions;
        if (!Files.exists(path)) {
            openOptions = new OpenOption[]{
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.READ
            };
        } else {
            openOptions = new OpenOption[]{
                    StandardOpenOption.READ
            };
        }

        try {
            result = Files.newInputStream(path, openOptions);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        return result;
    }
}