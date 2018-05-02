package com.infor.alexm.docprocessor.fileprocessors;

import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
@Service
public final class XmlProcessor extends FileProcessor {

    private static final String XML_EXT = "xml";

    private final XMLEventFactory xmlEventFactory = XMLEventFactory.newFactory();

    @Override
    protected String getSupportedFileExtension() {
        return XML_EXT;
    }

    @Override
    protected void processFile(final String inputfilePath) throws IOException {
        XMLEventReader reader = createXMLEventReader(inputfilePath);
        XMLEventWriter writer = createXMLEventWriter();
        try {
            while (reader.hasNext()) {
                XMLEvent event = (XMLEvent) reader.next();
                modifyAndWriteEvent(event, writer);
            }
            writer.flush();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        } finally {
            try {
                writer.close();
            } catch (XMLStreamException ignored) {
            }
        }
    }

    private void modifyAndWriteEvent(final XMLEvent event,
                                     final XMLEventWriter writer) throws XMLStreamException {
        //write this event to Consumer (XMLOutputStream)
        if (event.isCharacters()) {
            writer.add(getNewCharactersEvent(event.asCharacters()));
        } else if (event.isStartElement()) {
            writer.add(getNewStartElementEvent(event.asStartElement()));
        } else {
            writer.add(event);
        }
    }

    private XMLEventReader createXMLEventReader(final String inputfilePath) throws IOException {
        try {
            return XMLInputFactory.newInstance()
                    .createXMLEventReader(
                            new java.io.FileInputStream(inputfilePath));
        } catch (XMLStreamException | FileNotFoundException e) {
            throw new IOException(e);
        }
    }

    private XMLEventWriter createXMLEventWriter() throws IOException {
        try {
            return XMLOutputFactory.newInstance()
                    .createXMLEventWriter(
                            Files.newBufferedWriter(outputFilePath));
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    private Characters getNewCharactersEvent(final Characters event) {

        String text = event.getData();
        String newText = searchAndReplace(text);

        //nothing changed by replace
        if (text.equalsIgnoreCase(newText)) {
            return event;
        } else {
            if (event.isCData()) {
                return xmlEventFactory.createCData(newText);
            } else {
                return xmlEventFactory.createCharacters(
                        newText);
            }
        }
    }

    private StartElement getNewStartElementEvent(final StartElement event) {

        QName crtqName = event.getName();

        String text = crtqName.getLocalPart();
        String newText = searchAndReplace(text);
        if (text.equalsIgnoreCase(newText) && !event.getAttributes().hasNext()) {
            return event;
        }

        return xmlEventFactory.createStartElement(crtqName.getPrefix(),
                crtqName.getNamespaceURI(),
                newText,
                getNewAttributeEvents(event.getAttributes()),
                event.getNamespaces(),
                event.getNamespaceContext());

    }

    private Iterator<Attribute> getNewAttributeEvents(Iterator<Attribute> attributes) {

        final List<Attribute> result = new ArrayList<>();
        attributes.forEachRemaining(attr -> result.add(processAttribute(attr)));
        return result.iterator();
    }

    private Attribute processAttribute(final Attribute attr) {

        String text = attr.getValue();
        String newText = searchAndReplace(text);

        //nothing changed by replace
        if (text.equalsIgnoreCase(newText)) {
            return attr;
        } else {
            return xmlEventFactory.createAttribute(attr.getName(), newText);
        }
    }

}
