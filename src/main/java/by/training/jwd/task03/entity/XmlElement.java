package by.training.jwd.task03.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class XmlElement {
    private String name;
    private String content = "";
    private List<XmlElement> childElements = new ArrayList<>();
    private List<XmlAttribute> attributes = new ArrayList<>();

    public XmlElement() {
    }

    public XmlElement(String name) {
        this.name = name;
    }

    public XmlElement(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<XmlElement> getChildElements() {
        return childElements;
    }

    public void setChildElements(List<XmlElement> childElements) {
        this.childElements = childElements;
    }

    public List<XmlAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<XmlAttribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(XmlAttribute attribute) {
        attributes.add(attribute);
    }

    public void addChildElement(XmlElement element) {
        childElements.add(element);
    }

    public boolean hasContent() {
        return content.length() > 0;
    }

    public boolean hasChildElements() {
        return childElements.size() > 0;
    }

    public boolean hasAttributes() {
        return attributes.size() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmlElement that = (XmlElement) o;
        return Objects.equals(name, that.name)
                && Objects.equals(content, that.content)
                && Objects.equals(childElements, that.childElements)
                && Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content, childElements, attributes);
    }
}
