package by.training.jwd.task03.parser.impl;

import by.training.jwd.task03.entity.XmlDocument;
import by.training.jwd.task03.entity.XmlElement;
import by.training.jwd.task03.entity.XmlAttribute;
import by.training.jwd.task03.parser.XmlDocumentBuilder;
import by.training.jwd.task03.parser.XmlParserIndex;
import by.training.jwd.task03.parser.exception.XmlParserException;
import by.training.jwd.task03.reader.XmlContentReader;
import by.training.jwd.task03.reader.impl.XmlContentReaderImpl;

import java.nio.file.Path;

public class XmlDocumentBuilderImpl implements XmlDocumentBuilder {

    // Not so cute at first side but effective and simple

    private enum ParserState {
        NEXT,
        TAG_OPEN,
        TAG_CLOSE,
        PROLOG,
        COMMENT,
        ATTRIBUTE_NAME,
        ATTRIBUTE_QUOTE,
        ATTRIBUTE_VALUE,
        NEXT_ATTRIBUTE,
        CONTENT,
    }

    Path xmlFilePath;

    public XmlDocumentBuilderImpl(Path xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    public XmlDocument parse() {
        XmlContentReader contentReader = new XmlContentReaderImpl(xmlFilePath);
        String xmlContents = contentReader.getXmlFileContents();
        XmlParserIndex parserIndex = new XmlParserIndex();
        XmlElement root = readHierarchy(xmlContents, parserIndex);

        return new XmlDocument(root);
    }


    private XmlElement readHierarchy(String xmlContents, XmlParserIndex parserIndex) {
        XmlElement result = null;

        ParserState prevState = ParserState.NEXT;
        ParserState state = ParserState.NEXT;

        StringBuilder contentName = new StringBuilder();
        StringBuilder contentValue = new StringBuilder();

        char c;

        do {
            boolean isWhitespace;
            boolean skipBlankChar = false;
            boolean isInside = state == ParserState.CONTENT
                    || state == ParserState.TAG_OPEN
                    || state == ParserState.TAG_CLOSE
                    || state == ParserState.ATTRIBUTE_NAME
                    || state == ParserState.ATTRIBUTE_VALUE;


            do {
                if (parserIndex.value() >= xmlContents.length()) {
                    if (state == ParserState.NEXT) {
                        return null; // then we have no useful data
                    }
                    throw new XmlParserException("Unexpected end of data");
                }

                c = xmlContents.charAt(parserIndex.value());
                isWhitespace = Character.isWhitespace(c);
                skipBlankChar = isWhitespace && !isInside;

                parserIndex.increment();
            } while (skipBlankChar);


            switch (state) {
                case NEXT -> {
                    switch (c) {
                        case '<' -> {
                            state = ParserState.TAG_OPEN;
                            contentName.setLength(0);
                        }
                        default -> {
                            throw new XmlParserException("Unexpected character");
                        }
                    }
                }

                case TAG_OPEN -> {
                    switch (c) {
                        case '?' -> {
                            if (xmlContents.charAt(parserIndex.value() - 2) == '<') {
                                state = ParserState.PROLOG;
                            } else {
                                contentName.append(c);
                            }
                        }
                        case '!' -> {
                            if (parserIndex.value() < xmlContents.length() - 3
                                    && xmlContents.charAt(parserIndex.value() - 2) == '<'
                                    && xmlContents.charAt(parserIndex.value()) == '-'
                                    && xmlContents.charAt(parserIndex.value() + 1) == '-') {
                                state = ParserState.COMMENT;
                                prevState = ParserState.NEXT;
                            } else {
                                contentName.append(c);
                            }
                        }
                        case '/' -> {
                            result = new XmlElement(contentName.toString());
                            state = ParserState.TAG_CLOSE;
                        }
                        case '>' -> {
                            result = new XmlElement(contentName.toString());
                            state = ParserState.CONTENT;
                        }
                        case ' ' -> {
                            result = new XmlElement(contentName.toString());
                            contentName.setLength(0);
                            state = ParserState.ATTRIBUTE_NAME;
                        }
                        default -> {
                            contentName.append(c);
                        }
                    }
                }

                case TAG_CLOSE -> {
                    switch (c) {
                        case '>' -> {
                            // previously created:
                            return result;
                        }
                        default -> {
                            // verify that the close tag matches the open tag
                        }
                    }
                }

                case ATTRIBUTE_NAME -> {
                    switch (c) {
                        case '/' -> {
                            state = ParserState.TAG_CLOSE;
                        }
                        case '=' -> {
                            state = ParserState.ATTRIBUTE_QUOTE;
                        }
                        default -> {
                            if (contentName.length() > 0 || !Character.isWhitespace(c)) {
                                contentName.append(c);
                            }
                        }
                    }
                }

                case ATTRIBUTE_QUOTE -> {
                    if (c == '"') {
                        state = ParserState.ATTRIBUTE_VALUE;
                        contentValue.setLength(0);
                    } else {
                        throw new XmlParserException("Unexpected character");
                    }
                }

                case ATTRIBUTE_VALUE -> {
                    switch (c) {
                        case '"' -> {
                            XmlAttribute attribute = new XmlAttribute(contentName.toString(), contentValue.toString());
                            result.addAttribute(attribute);
                            contentValue.setLength(0);
                            state = ParserState.NEXT_ATTRIBUTE;
                        }
                        default -> {
                            contentValue.append(c);
                        }
                    }
                }

                case NEXT_ATTRIBUTE -> {
                    switch (c) {
                        case '/' -> {
                        }
                        case '>' -> {
                            if (xmlContents.charAt(parserIndex.value() - 2) == '/') {
                                return result;
                            }
                            state = ParserState.CONTENT;
                        }
                        default -> {
                            if (Character.isLetter(c)) {
                                contentName.setLength(0);
                                contentName.append(c);
                                state = ParserState.ATTRIBUTE_NAME;
                            } else {
                                throw new XmlParserException("Unexpected character");
                            }
                        }
                    }
                }

                case PROLOG -> {
                    if (c == '>') {
                        state = ParserState.NEXT;
                    }
                }

                case COMMENT -> {
                    switch (c) {
                        case '>' -> {
                            if (xmlContents.charAt(parserIndex.value() - 2) == '-'
                                    && xmlContents.charAt(parserIndex.value() - 3) == '-') {
                                state = prevState;
                            }
                        }
                    }
                }

                case CONTENT -> {
                    switch (c) {
                        case '<' -> {
                            if (parserIndex.value() < xmlContents.length()
                                    && xmlContents.charAt(parserIndex.value()) == '/') {
                                state = ParserState.TAG_CLOSE;

                                String preparedContentValue = contentValue
                                        .toString()
                                        .replaceAll("\\s+", " ")
                                        .trim();
                                result.setContent(preparedContentValue);
                            } else if (parserIndex.value() < xmlContents.length() - 3
                                    && xmlContents.charAt(parserIndex.value()) == '!'
                                    && xmlContents.charAt(parserIndex.value() + 1) == '-'
                                    && xmlContents.charAt(parserIndex.value() + 2) == '-') {
                                state = ParserState.COMMENT;
                                prevState = ParserState.CONTENT;
                                parserIndex.add(2);
                            } else {
                                parserIndex.decrement();
                                XmlElement childElement = readHierarchy(xmlContents, parserIndex);
                                if (childElement == null)
                                    throw new XmlParserException("Unexpected end of data");
                                result.addChildElement(childElement);
                            }
                        }
                        default -> {
                            contentValue.append(c);
                        }
                    }
                }
            }
        } while (true);
    }
}