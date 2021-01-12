package by.training.jwd.task03.entity;

import java.util.Objects;

public final class XmlDocument {
    private XmlElement root;

    public XmlDocument() {
    }

    public XmlDocument(XmlElement root) {
        this.root = root;
    }

    public XmlElement getRoot() {
        return root;
    }

    public void setRoot(XmlElement root) {
        this.root = root;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmlDocument that = (XmlDocument) o;
        return Objects.equals(root, that.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }
}
