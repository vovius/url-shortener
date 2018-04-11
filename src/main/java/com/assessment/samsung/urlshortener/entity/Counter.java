package com.assessment.samsung.urlshortener.entity;

public class Counter {
    private String url;
    private Integer count;

    public Counter(String url) {
        this(url, 0);
    }

    public Counter(String url, Integer count) {
        this.url = url;
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
