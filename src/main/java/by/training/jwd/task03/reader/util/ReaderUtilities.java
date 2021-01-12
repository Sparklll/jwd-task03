package by.training.jwd.task03.reader.util;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public final class ReaderUtilities {

    private ReaderUtilities() {
    }

    public static Path getFilePathFromResources(String fileName) {
        ClassLoader classLoader = ReaderUtilities.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        return file.toPath();
    }
}
