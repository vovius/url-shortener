package com.assessment.samsung.urlshortener.control;

import org.junit.Test;

import static org.junit.Assert.*;

public class URLServiceImplTest {

    private final static String TEST_URL = "http://www.google.com";

    @Test
    public void simpleGenerate() {
        URLService urlService = new URLServiceImpl(5);
        String shortUrl = urlService.generate(TEST_URL);
        assertNotNull(shortUrl);
        assertEquals(5, shortUrl.length());
    }

    @Test
    public void generateMultiple() {
        URLService urlService = new URLServiceImpl(5);
        String shortUrl = urlService.generate(TEST_URL);
        assertNotNull(shortUrl);
        assertEquals(5, shortUrl.length());

        String shortUrl1 = urlService.generate(TEST_URL);
        assertEquals(shortUrl, shortUrl1);
    }

    @Test
    public void getOk() {
        URLService urlService = new URLServiceImpl(5);
        String shortUrl = urlService.generate(TEST_URL);
        assertNotNull(shortUrl);
        assertEquals(5, shortUrl.length());

        assertEquals(TEST_URL, urlService.get(shortUrl));
    }

    @Test
    public void getNegative() {
        URLService urlService = new URLServiceImpl(5);
        assertNull(urlService.get("111"));
    }

    @Test
    public void getCountOk() {
        URLService urlService = new URLServiceImpl(5);
        String shortUrl = urlService.generate(TEST_URL);
        assertNotNull(shortUrl);
        assertEquals(5, shortUrl.length());

        assertEquals(TEST_URL, urlService.get(shortUrl));
        assertNotNull(urlService.getCount(shortUrl));
        assertEquals(1, urlService.getCount(shortUrl).intValue());
    }

    @Test
    public void getCountNegative() {
        URLService urlService = new URLServiceImpl(5);
        assertNotNull(urlService.getCount("111"));
        assertEquals(0, urlService.getCount("111").intValue());
    }

}