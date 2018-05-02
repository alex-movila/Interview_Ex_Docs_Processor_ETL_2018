package com.infor.alexm.docprocessor.fileprocessors;

import com.infor.alexm.docprocessor.searchreplace.SearchReplaceData;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FileProcessor implements Processor {

    protected File inputFile;
    protected Path outputFilePath;
    private List<SearchReplaceData> searchReplaceDataList;

    protected abstract String getSupportedFileExtension();

    protected abstract void processFile(String inputfilePath) throws IOException;

    private void initialize(File inputFile,
                            Path outputFilePath,
                            List<SearchReplaceData> searchReplaceData) {

        this.inputFile = inputFile;
        this.outputFilePath = outputFilePath;
        this.searchReplaceDataList = searchReplaceData;
    }


    protected boolean isSupported() {
        return getSupportedFileExtension()
                .equalsIgnoreCase(FilenameUtils.getExtension(
                        inputFile.getName()));
    }

    @Override
    public void tryProcess(File input,
                           Path outputFilePath,
                           List<SearchReplaceData> searchReplaceData) throws IOException {
        initialize(input, outputFilePath, searchReplaceData);
        if (isSupported()) {
            processFile(inputFile.getAbsolutePath());
        }
    }

    protected List<String> getSearchMeTextList() {
        return searchReplaceDataList.stream().map(x -> x.getSearchMeText()).collect(Collectors.toList());
    }

    protected String searchAndReplace(final String textToSearchIn) {
        String result = textToSearchIn;
        for (SearchReplaceData x : searchReplaceDataList) {
            result = x.searchAndReplace(result);
        }
        return result;
    }
}
