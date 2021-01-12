package by.training.jwd.task03.main;

import by.training.jwd.task03.entity.XmlDocument;
import by.training.jwd.task03.entity.XmlElement;
import by.training.jwd.task03.parser.XmlDocumentBuilder;
import by.training.jwd.task03.parser.impl.XmlDocumentBuilderImpl;
import by.training.jwd.task03.printer.XmlHierarchyPrinter;
import by.training.jwd.task03.printer.impl.XmlHierarchyPrinterImpl;
import by.training.jwd.task03.reader.util.ReaderUtilities;

import java.nio.file.Path;

public class main {
    public static void main(String[] args) {
        Path xmlPath = ReaderUtilities.getFilePathFromResources("breakfast-menu.xml");

        XmlDocumentBuilder documentBuilder = new XmlDocumentBuilderImpl(xmlPath);
        XmlDocument document = documentBuilder.parse();
        XmlElement root = document.getRoot();

        XmlHierarchyPrinter xmlHierarchyPrinter = new XmlHierarchyPrinterImpl();
        xmlHierarchyPrinter.printHierarchy(document);
    }
}
