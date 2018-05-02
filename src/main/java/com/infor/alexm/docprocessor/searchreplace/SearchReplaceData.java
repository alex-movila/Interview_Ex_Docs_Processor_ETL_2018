package com.infor.alexm.docprocessor.searchreplace;

public final class SearchReplaceData {

    private final SearchReplaceEngine searchReplaceEngine;
    private final String searchMe;
    private final String replacement;


    public SearchReplaceData(final String searchMe,
                             final String replacement,
                             final SearchReplaceEngine searchReplaceEngine) {
        //for now we support only one type
        this.searchReplaceEngine = searchReplaceEngine;
        this.searchMe = searchMe;
        this.replacement = replacement;
    }

    public String getSearchMeText() {
        return searchMe;
    }

    private String getReplacementText() {
        return replacement;
    }

    public String searchAndReplace(final String textToSearchIn) {
        return searchReplaceEngine.searchAndReplace(textToSearchIn,
                getSearchMeText(),
                getReplacementText());
    }
}
