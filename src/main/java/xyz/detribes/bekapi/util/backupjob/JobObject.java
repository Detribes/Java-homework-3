package xyz.detribes.bekapi.util.backupjob;

import org.slf4j.*;
import xyz.detribes.bekapi.storage.*;

/**
 * ОБъект "джобы" который по сути является самой примитивной обёрткой для стораджа. Я даже
 * не знаю, по большей части он тут излишний, но в задании он нужен для бекап-джобы, поэтому
 * он тут есть.
 */
public class JobObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobObject.class);

    private final Storage storage;

    public JobObject(Storage storage) {
        this.storage = storage;
    }

    public Storage getStorage() {
        return storage;
    }
}