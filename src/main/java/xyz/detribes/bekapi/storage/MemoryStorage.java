package xyz.detribes.bekapi.storage;

import org.slf4j.*;

import java.io.*;

public class MemoryStorage implements Storage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryStorage.class);

    private byte[] data;
    private final String name;


    public MemoryStorage(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public OutputStream copyInto() {
        return new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                super.close();
                data = this.buf;
            }
        };
    }

    @Override
    public InputStream retrieve() {
        return new ByteArrayInputStream(data);
    }
}