package com.infor.alexm.docprocessor.searchreplace;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public abstract class SearchReplaceRegexBaseEngine implements SearchReplaceEngine {

    //holds precompilerd list of patterns
    private static final ConcurrentHashMap<String, Pattern> searchPatterns = new ConcurrentHashMap<>();

    @Override
    public String searchAndReplace(String textToSearchIn, String searchMe, String replacement) {
        searchPatterns.computeIfAbsent(searchMe, p -> createPattern(searchMe));
        return doSearchAndReplace(textToSearchIn, searchMe, replacement);
    }

    protected Pattern getPattern(String searchMe) {
        return searchPatterns.get(searchMe);
    }

    protected abstract Pattern createPattern(String searchMe);

    protected abstract String doSearchAndReplace(String textToSearchIn, String searchMe, String replacement);
}
