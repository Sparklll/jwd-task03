package by.training.jwd.task03.printer.impl;

import by.training.jwd.task03.entity.XmlAttribute;
import by.training.jwd.task03.entity.XmlDocument;
import by.training.jwd.task03.entity.XmlElement;
import by.training.jwd.task03.printer.XmlHierarchyPrinter;

import java.util.List;

public final class XmlHierarchyPrinterImpl implements XmlHierarchyPrinter {
    public static final String ATTRIBUTE_FORMAT = "%s=%s";
    public static final String ATTRIBUTES_SEPARATOR = ", ";
    public static final String CONTENT_FORMAT = "\"%s\"";
    public static final String BLANK_SPACE = " ";
    public static final String COLON = ":";
    public static final String TAB = "\t";


    public XmlHierarchyPrinterImpl() {
    }

    @Override
    public void printHierarchy(XmlDocument document) {
        XmlElement root = document.getRoot();
        printElement(root, 0);
    }

    public void printElement(XmlElement element, int tabNumber) {
        String preparedElement = prepareElement(element);
        String tabulation = getTabulation(tabNumber);
        System.out.println(tabulation + preparedElement);

        if(element.hasChildElements()) {
            for(XmlElement childElement : element.getChildElements()) {
                printElement(childElement, tabNumber + 1);
            }
        }
    }

    private String prepareElement(XmlElement element) {
        StringBuilder preparedElement = new StringBuilder(element.getName());

        if (element.hasAttributes()) {
            String wrappedAttributes = wrapAttributes(element.getAttributes());
            preparedElement.append(wrappedAttributes);
        }
        if (element.hasContent()) {
            String wrappedContent = wrapContent(element.getContent());
            preparedElement.append(COLON).append(BLANK_SPACE).append(wrappedContent);
        }

        return preparedElement.toString();
    }

    private String wrapAttributes(List<XmlAttribute> attributes) {
        StringBuilder wrappedAttributes = new StringBuilder("(");

        for (int i = 0; i < attributes.size(); i++) {
            XmlAttribute attribute = attributes.get(i);
            String attributeName = attribute.getName();
            String attributeValue = attribute.getValue();

            wrappedAttributes.append(String.format(ATTRIBUTE_FORMAT, attributeName, attributeValue));
            if (i < attributes.size() - 1) {
                wrappedAttributes.append(ATTRIBUTES_SEPARATOR);
            }
        }
        wrappedAttributes.append(")");

        return wrappedAttributes.toString();
    }

    private String wrapContent(String content) {
        return String.format(CONTENT_FORMAT, content);
    }

    private String getTabulation(int tabNumber) {
        return String.valueOf(TAB).repeat(tabNumber);
    }
}
