package com.infor.alexm.docprocessor;

import com.infor.alexm.docprocessor.config.ProcessorsConfig;
import com.infor.alexm.docprocessor.config.SearchReplaceEngineConfig;
import com.infor.alexm.docprocessor.fileprocessors.Processors;
import com.infor.alexm.docprocessor.searchreplace.SearchReplaceData;
import com.infor.alexm.docprocessor.searchreplace.SearchReplaceEngine;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DocProcessor {

    private static final int NO_ARGS = 4;
    private static final int ARG_INDEX_INPUT_FILE_NAME = 0;
    private static final int ARG_INDEX_OUTPUT_FILE_NAME = 1;
    private static final int ARG_INDEX_SEARCH_TEXT = 2;
    private static final int ARG_INDEX_REPLACEMENT_TEXT = 3;
    private Path outputFilePath;
    private File inputFile;
    private String searchText;
    private String replacementText;
    private List<SearchReplaceData> searchReplaceDataList;

    public static void main(final String[] args) {
        //load dinamically processors for each file type
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ProcessorsConfig.class, SearchReplaceEngineConfig.class);

        DocProcessor docProcessor = new DocProcessor();
        try {
            docProcessor.parseMainArguments(args);
            docProcessor.process(context);

        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);

    }

    private void parseMainArguments(final String[] args) throws IOException {

        if (args.length != NO_ARGS) {
            throw new IllegalArgumentException("Invalid number of program arguments!");
        }

        String inputFileName = args[ARG_INDEX_INPUT_FILE_NAME];
        String outputFileName = args[ARG_INDEX_OUTPUT_FILE_NAME];
        searchText = args[ARG_INDEX_SEARCH_TEXT];
        replacementText = args[ARG_INDEX_REPLACEMENT_TEXT];

        if (inputFileName.length() == 0
                || outputFileName.length() == 0
                || searchText.length() == 0) {
            throw new IllegalArgumentException("Invalid program argument values!");
        }

        inputFile = initInputFile(inputFileName);
        outputFilePath = Paths.get(outputFileName);

    }

    private File initInputFile(final String inputFileName) throws IOException {
        File input = Paths.get(inputFileName).toFile();
        if (!input.exists()
                || !input.isFile()
                || !input.canRead()) {
            throw new IOException("Cannot access the input file!");
        }
        return input;
    }

    private void process(final AnnotationConfigApplicationContext context) {

        Processors processors = context.getBean(Processors.class);
        SearchReplaceEngine searchReplaceEngine = context.getBean("searchReplaceEngine", SearchReplaceEngine.class);
        SearchReplaceData searchReplaceData = new SearchReplaceData(searchText, replacementText, searchReplaceEngine);
        searchReplaceDataList = new ArrayList<>();
        searchReplaceDataList.add(searchReplaceData);

        processors.process(inputFile, outputFilePath, searchReplaceDataList);
    }
}
