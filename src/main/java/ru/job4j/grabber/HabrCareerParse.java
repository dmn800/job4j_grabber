package ru.job4j.grabber;

import ru.job4j.grabber.utils.DateTimeParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.Parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final int PAGES = 5;

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) {
        String description = null;
        try {
            description = Jsoup.connect(link).get().select(".vacancy-description__text").first().text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return description;
    }

    private Post parsePost(Element row) {
        Post post = new Post();
        Element element = row.select(".vacancy-card__title").first();
        String link = String.format("%s%s", SOURCE_LINK, element.child(0).attr("href"));
        Element date = row.select(".vacancy-card__date").first().child(0);
        post.setTitle(element.text());
        post.setLink(link);
        post.setDescription(retrieveDescription(link));
        post.setCreated(dateTimeParser.parse(date.attr("datetime")));
        return post;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        for (int page = 1; page <= PAGES; page++) {
            try {
                Connection connection = Jsoup.connect(link.replace("?q", "?page=%s&q".formatted(page)));
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> posts.add(parsePost(row)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }
}
