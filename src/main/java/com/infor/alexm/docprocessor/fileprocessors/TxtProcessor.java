package com.infor.alexm.docprocessor.fileprocessors;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@Service
public final class TxtProcessor extends FileProcessor {

    private static final String TXT_EXT = "txt";

    private static final int MAX_BUFFER_LINE_SIZE = 500;

    private String buffer = "";
    private BufferedWriter writer;

    @Override
    protected String getSupportedFileExtension() {
        return TXT_EXT;
    }

    @Override
    protected void processFile(final String inputfilePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            this.writer = writer;

            try (Stream<String> inputStream = Files.lines(Paths.get(inputfilePath))) {
                inputStream.forEachOrdered(line -> {
                    try {
                        processLine(line);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            //write leftover text
            writer.write(buffer);
            writer.flush();
        }
    }

    private void processLine(final String line) throws IOException {
        buffer += line + System.lineSeparator();

        if (buffer.length() >= MAX_BUFFER_LINE_SIZE) {
            String newText = searchAndReplace(buffer);
            int endRegionLength = newText.length() - getSearchMeTextList()
                    .stream()
                    .mapToInt(String::length).max().orElse(0);

            writeNewLine(endRegionLength, writer, newText);

            //TODO the way endRegion is computed is too simplistic, we may neec to consider also
            //TODO last replacement position, for example what happens if replacement is empty string
            //clear buffer except the end region
            buffer = newText.substring(endRegionLength);
        }
    }

    private void writeNewLine(int endRegionLength, BufferedWriter writer, String newText) throws IOException {
        if (endRegionLength >= 0) {
            writer.write(newText.substring(0, endRegionLength));
        }

    }

}
