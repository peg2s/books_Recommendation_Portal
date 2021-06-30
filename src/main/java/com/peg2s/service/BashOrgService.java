package com.peg2s.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BashOrgService {
    private Elements getRandomQuotes() {
        try {
            Document document = Jsoup.connect("https://bash.im/random")
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://www.google.com")
                    .get();
            return document.select("div.quote__body");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Elements();
    }

    public String getRandomQuote() {
        Elements quotes = getRandomQuotes();
        return quotes.get((int) (Math.random() * quotes.size())).toString();
    }

    public List<String> getNQuotes(int n) {
        List<String> quotes = new ArrayList<>();
        getRandomQuotes().forEach(quote -> quotes.add(quote.toString()));
        return quotes.subList(0, n);

    }
}
