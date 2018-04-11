package com.assessment.samsung.urlshortener.control;

import com.assessment.samsung.urlshortener.entity.Counter;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class URLServiceImpl implements URLService {
    private final static Logger LOG = LoggerFactory.getLogger(URLServiceImpl.class);

    private final Map<String, String> linksByUrl = new ConcurrentHashMap<>();
    private final Map<String, Counter> linksByShortUrl = new ConcurrentHashMap<>();
    private final Integer urlLength;

    public URLServiceImpl(@Value("${url.length}") Integer urlLength) {
        this.urlLength = urlLength;
    }

    @Override
    public String get(String id) {
        Counter counter = linksByShortUrl.computeIfPresent(id, (k, v) -> new Counter(v.getUrl(), v.getCount() + 1));
        return counter != null ? counter.getUrl() : null;
    }

    @Override
    public String generate(String url) {
        String link = linksByUrl.get(url);
        if (link != null) {
            return link;
        }

        String encodedUrl;
        do {
            encodedUrl = RandomStringUtils.randomAlphanumeric(urlLength).toUpperCase();
        } while (linksByShortUrl.containsKey(encodedUrl));

        LOG.debug("For url {} link {} has been created", url, encodedUrl);
        linksByUrl.put(url, encodedUrl);
        linksByShortUrl.put(encodedUrl, new Counter(url));
        return encodedUrl;
    }

    public Integer getCount(String id) {
        Optional<Counter> counter = Optional.ofNullable(linksByShortUrl.get(id));
        return counter.map(Counter::getCount).orElse(0);
    }
}
