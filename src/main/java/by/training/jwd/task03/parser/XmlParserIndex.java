package by.training.jwd.task03.parser;

public class XmlParserIndex {
    private int index;

    public XmlParserIndex() {
    }

    public XmlParserIndex(int index) {
        this.index = index;
    }

    public int value() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void increment() {
        index++;
    }

    public void decrement() {
        index--;
    }

    public void add(int number) {
        index += number;
    }

    public void sub(int number) {
        index -= number;
    }
}
