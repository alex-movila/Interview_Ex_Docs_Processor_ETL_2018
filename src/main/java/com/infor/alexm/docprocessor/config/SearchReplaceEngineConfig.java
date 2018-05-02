package com.infor.alexm.docprocessor.config;

import com.infor.alexm.docprocessor.searchreplace.SearchReplaceEngine;
import com.infor.alexm.docprocessor.searchreplace.SearchReplacePhraseEngine;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SearchReplaceEngineConfig {
    @Bean("searchReplaceEngine")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public SearchReplaceEngine getSearchReplaceEngine() {
        return new SearchReplacePhraseEngine();
    }
}
