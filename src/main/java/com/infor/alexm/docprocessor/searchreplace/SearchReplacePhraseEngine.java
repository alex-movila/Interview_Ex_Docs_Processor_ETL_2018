package com.infor.alexm.docprocessor.searchreplace;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SearchReplacePhraseEngine extends SearchReplaceRegexBaseEngine {

    @Override
    public String doSearchAndReplace(final String textToSearchIn,
                                     final String searchMe,
                                     final String replacement) {
        Matcher matcher = getPattern(searchMe).matcher(textToSearchIn);
        return matcher.replaceAll(replacement);
    }

    @Override
    protected Pattern createPattern(final String searchMe) {
        //multiline phrase supported (space or newline are equivalent)
        //TODO check if JAVA uses Boyer Moore Algorithm
        return Pattern.compile(searchMe.replaceAll(" ", "\\\\s+"));
    }
}
