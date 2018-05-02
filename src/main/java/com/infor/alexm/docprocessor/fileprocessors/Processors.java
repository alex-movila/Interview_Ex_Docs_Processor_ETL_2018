package com.infor.alexm.docprocessor.fileprocessors;

import com.infor.alexm.docprocessor.searchreplace.SearchReplaceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
public final class Processors {
    private List<Processor> fileProcessors;

    @Autowired
    public Processors(final List<Processor> processors) {
        this.fileProcessors = processors;
    }

    public void process(final File inputFile,
                        final Path outputFilePath,
                        final List<SearchReplaceData> searchReplaceDataList) {
        fileProcessors.forEach(p -> {
            try {
                p.tryProcess(inputFile, outputFilePath, searchReplaceDataList);
            } catch (IOException e) {
                throw new RuntimeException("Unable to process file!", e);
            }
        });
    }
}
