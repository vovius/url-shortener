package com.assessment.samsung.urlshortener.boundary;

import com.assessment.samsung.urlshortener.control.URLService;
import com.assessment.samsung.urlshortener.entity.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

@Controller
public class LinkResolverController {

    private final static Logger LOG = LoggerFactory.getLogger(LinkResolverController.class);

    @Autowired
    private URLService urlService;

    @GetMapping(value = {"{id}", "/stats/{id}"})
    public ModelAndView resolveLink(@PathVariable("id") String id, HttpServletRequest request) {
        if (request.getRequestURL().toString().contains("stats")) {
            return getStatsById(id);
        }

        String url = urlService.get(id);
        ModelAndView model;
        if (url == null) {
            model = new ModelAndView("index");
            model.addObject("id", url);
            //bindingResult.addError(new ObjectError("url", MessageFormat.format("URL not found: {0}", id)));
        } else {
            model = new ModelAndView("redirect:" + url);
        }
        return model;
    }

    @PostMapping
    public ModelAndView createLink(@ModelAttribute Link link, BindingResult bindingResult, HttpServletRequest request) {
        if (link.getValue() == null || link.getValue().isEmpty()) {
            bindingResult.addError(new ObjectError("url", "URL is empty"));
        } else if (!urlValid(link.getValue())) {
            bindingResult.addError(new ObjectError("url", MessageFormat.format("URL is not valid: {0}", link.getValue())));
        }

        ModelAndView model = new ModelAndView("index");
        if (!bindingResult.hasErrors()) {
            String url = urlService.generate(link.getValue());
            String requestURL = request.getRequestURL().toString();
            String prefix = requestURL.substring(0, requestURL.indexOf(request.getRequestURI(), "http://".length()));
            model.addObject("url", prefix + "/" + url);
        }
        return model;
    }

    private ModelAndView getStatsById(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("stats");
        modelAndView.addObject("id", id);
        modelAndView.addObject("count", urlService.getCount(id));
        return modelAndView;
    }



    private boolean urlValid(String url) {
        boolean result = true;
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            LOG.error("Malformed url {}", url);
            result = false;
        }

        return result;
    }

    private String getUrlWithPrefix(String url, HttpServletRequest request) {
        return getPrefix(request) + "/" + url;
    }

    private String getPrefix(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        return requestURL.substring(0, requestURL.indexOf(request.getRequestURI(), "http://".length()));
    }
}
