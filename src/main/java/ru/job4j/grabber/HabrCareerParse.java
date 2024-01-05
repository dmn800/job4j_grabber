package ru.job4j.grabber;

import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;

public class HabrCareerParse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";

    private String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Element description = document.select(".vacancy-description__text").first();
        return description.text();
    }

    public static void main(String[] args) throws IOException {
        for (int pageNumber = 1; pageNumber <= 5; pageNumber++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                Element dateElement = row.select(".vacancy-card__date").first().child(0);
                var timeParser = new HabrCareerDateTimeParser();
                String date = dateElement.attr("datetime");
                LocalDateTime dateTime = timeParser.parse(date);
                try {
                    String description = new HabrCareerParse().retrieveDescription(link);
                    System.out.printf("%s %s %s%n %s%n", vacancyName, link, dateTime, description);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
