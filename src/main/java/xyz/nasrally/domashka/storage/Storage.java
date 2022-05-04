package xyz.nasrally.domashka.storage;

import java.io.*;

public interface Storage {
    String getName();
//    void copy(InputStream data);
    OutputStream copyInto() throws IOException;
    InputStream retrieve() throws IOException;
}