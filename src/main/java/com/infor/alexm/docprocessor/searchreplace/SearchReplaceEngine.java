package com.infor.alexm.docprocessor.searchreplace;

public interface SearchReplaceEngine {
    String searchAndReplace(final String textToSearchIn,
                            final String searchMe,
                            final String replacement);
}
