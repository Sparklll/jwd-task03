package by.training.jwd.task03.reader.impl;

import by.training.jwd.task03.reader.XmlContentReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class XmlContentReaderImpl implements XmlContentReader {
    private Path xmlFilePath;

    public XmlContentReaderImpl(Path xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    public Path getXmlFilePath() {
        return xmlFilePath;
    }

    public void setXmlFilePath(Path xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    @Override
    public String getXmlFileContents() {
        String content = "";
        try {
             content = Files.readString(xmlFilePath);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return content;
    }
}
