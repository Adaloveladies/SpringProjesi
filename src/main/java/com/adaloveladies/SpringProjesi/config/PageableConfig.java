package com.adaloveladies.SpringProjesi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class PageableConfig implements WebMvcConfigurer {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_SORT_FIELD = "olusturmaTarihi";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // Sayfalama için resolver
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setOneIndexedParameters(true);
        pageableResolver.setMaxPageSize(MAX_PAGE_SIZE);
        pageableResolver.setPageParameterName("sayfa");
        pageableResolver.setSizeParameterName("boyut");
        pageableResolver.setFallbackPageable(PageRequest.of(0, DEFAULT_PAGE_SIZE, 
            Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_FIELD)));
        argumentResolvers.add(pageableResolver);

        // Sıralama için resolver
        SortHandlerMethodArgumentResolver sortResolver = new SortHandlerMethodArgumentResolver();
        sortResolver.setSortParameter("siralama");
        sortResolver.setPropertyDelimiter(",");
        sortResolver.setFallbackSort(Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_FIELD));
        argumentResolvers.add(sortResolver);
    }
} 