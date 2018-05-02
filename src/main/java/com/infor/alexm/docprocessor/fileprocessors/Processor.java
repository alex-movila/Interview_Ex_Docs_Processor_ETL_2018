package com.infor.alexm.docprocessor.fileprocessors;

import com.infor.alexm.docprocessor.searchreplace.SearchReplaceData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface Processor {

    void tryProcess(File inputFile,
                    Path outputFilePath,
                    List<SearchReplaceData> searchReplaceData) throws IOException;
}
