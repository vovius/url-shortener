package com.assessment.samsung.urlshortener.control;

public interface URLService {
    String get(String id);
    String generate(String url);
    Integer getCount(String id);
}
