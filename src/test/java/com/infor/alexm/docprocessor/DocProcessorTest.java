package com.infor.alexm.docprocessor;

import com.infor.alexm.docprocessor.config.ProcessorsConfig;
import com.infor.alexm.docprocessor.config.SearchReplaceEngineConfig;
import com.infor.alexm.docprocessor.fileprocessors.Processors;
import com.infor.alexm.docprocessor.searchreplace.SearchReplaceData;
import com.infor.alexm.docprocessor.searchreplace.SearchReplaceEngine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProcessorsConfig.class, SearchReplaceEngineConfig.class})
public class DocProcessorTest {

    private static final String TXT_SEARCH_ME1 =
            "He created some of the world's best-known fictional characters";
    private static final String TXT_REPLACEMENT1 = "Ala bala portocala";

    private static final String TXT_SEARCH_ME2 =
            "readers";
    private static final String TXT_REPLACEMENT2 = "";

    private static final String TXT_SEARCH_ME3 =
            "The Pickwick Papers";
    private static final String TXT_REPLACEMENT3 = "The Pickwick Tokens";

    private static final String XML_SEARCH_ME1 = "lu";
    private static final String XML_REPLACEMENT1 = "la";

    private static final String XML_SEARCH_ME2 = "ISBN";
    private static final String XML_REPLACEMENT2 = "ISBN2";

    private static final String XML_SEARCH_ME3 = "First and Last";
    private static final String XML_REPLACEMENT3 = "Gogu";

    @Autowired
    Processors processors;
    @Autowired
    SearchReplaceEngine searchReplaceEngine;
    List<SearchReplaceData> searchReplaceDataList = new ArrayList<>();
    private Path outputFilePath;
    private File inputFile;
    private ClassLoader classLoader = getClass().getClassLoader();
    private File referenceFile;

    private void addSearchReplaceData(List<SearchReplaceData> searchReplaceDataList,
                                      String searchMe,
                                      String replacement) {
        SearchReplaceData searchReplaceData = new SearchReplaceData(searchMe,
                replacement, searchReplaceEngine);

        searchReplaceDataList.add(searchReplaceData);


    }

    @Before
    public void setup() {
    }

    @Test
    public void testReplaceInTxt() throws URISyntaxException, IOException {
        searchReplaceDataList.clear();
        addSearchReplaceData(searchReplaceDataList, TXT_SEARCH_ME1, TXT_REPLACEMENT1);
        addSearchReplaceData(searchReplaceDataList, TXT_SEARCH_ME2, TXT_REPLACEMENT2);
        addSearchReplaceData(searchReplaceDataList, TXT_SEARCH_ME3, TXT_REPLACEMENT3);

        setUpFiles("file", "txt");

        processors.process(inputFile, outputFilePath, searchReplaceDataList);
        File outputFile = outputFilePath.toFile();
        Assert.assertTrue(SampleCompare.execute(outputFile, referenceFile));
    }

    @Test
    public void testReplaceInXml() throws URISyntaxException, IOException {
        searchReplaceDataList.clear();
        addSearchReplaceData(searchReplaceDataList, XML_SEARCH_ME1, XML_REPLACEMENT1);
        addSearchReplaceData(searchReplaceDataList, XML_SEARCH_ME2, XML_REPLACEMENT2);
        addSearchReplaceData(searchReplaceDataList, XML_SEARCH_ME3, XML_REPLACEMENT3);


        setUpFiles("BookCatalog", "xml");

        processors.process(inputFile, outputFilePath, searchReplaceDataList);
        File outputFile = outputFilePath.toFile();

        //TODO could use XMLUnit
        Assert.assertTrue(SampleCompare.execute(outputFile, referenceFile));

    }

    private void setUpFiles(String filePrefix, String fileExt) throws URISyntaxException {
        inputFile = new File(classLoader.getResource(filePrefix + "." + fileExt).toURI());
        outputFilePath = Paths.get(inputFile.getAbsolutePath().replaceFirst(filePrefix + "." + fileExt + "$",
                filePrefix + "Out." + fileExt));
        referenceFile = new File(classLoader.getResource(filePrefix + "Reference." + fileExt).toURI());
    }
}
