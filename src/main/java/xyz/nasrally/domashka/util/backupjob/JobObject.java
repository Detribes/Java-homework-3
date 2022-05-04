package xyz.nasrally.domashka.util.backupjob;

import org.slf4j.*;
import xyz.nasrally.domashka.storage.*;

/**
 * ОБъект "джобы" который по сути является самой примитивной обёрткой для стораджа. Я даже
 * не знаю, по большей части он тут излишний, но в задании он нужен для бекап-джобы, поэтому
 * он тут есть.
 *
 * ВАЖНО: все логгеры можешь игнорить, тебе ниче логгировать не сказано, я их везде расставил
 * просто потому что "а вдруг надо", но в другом коде они испльзуются чтобы чекалка ошибок
 * не ругалась на обычный вывод эксепшнов, она хочет чтобы их логгировали, отсюда и логгер.
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