package ir.jiring.assignment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.jiring.assignment.dto.News;
import ir.jiring.assignment.exception.JsonConversionException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Util {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger("News");
    private static final String[] positiveWords = new String[]{"up", "rise", "good", "success", "high"};

    private Util() {
    }

    public static boolean isPositive(String inputLine) {
        News news = getNews(inputLine);
        String[] allWords = news.getHeadline().split(" ");
        float positiveWordCount = Arrays.stream(allWords).filter(s -> Arrays.asList(positiveWords).contains(s)).toList().size();
        float i = (positiveWordCount / allWords.length) * 100;
        return i > 50;

    }

    private static News getNews(String jsonNews) {
        News news = null;
        try {
            news = mapper.readValue(jsonNews, News.class);
        } catch (JsonProcessingException e) {
            log(Level.WARNING, "Error during mapping to json !");
            throw new JsonConversionException();
        }
        return news;
    }

    public static SortedMap<Short, News> addHighestPriorities(SortedMap<Short, News> newsMap, String inputLine) {
        News news = getNews(inputLine);
        newsMap.put(news.getPriority(), news);

        if (newsMap.size() > 3) {
            TreeMap<Short, News> result = new TreeMap<>();
            List<News> newsList = new ArrayList<>();
            newsMap.values().iterator().forEachRemaining(newsList::add);

            for (int i = newsMap.size() - 3; i < newsMap.size(); i++) {
                result.put(newsList.get(i).getPriority(), newsList.get(i));
            }
            return result;
        }

        return newsMap;
    }

    public static void log(Level level, String content, Object... params) {
        logger.log(level, content, params);
    }

    public static String toJson(Object of) {
        try {
            return mapper.writeValueAsString(of);
        } catch (JsonProcessingException e) {
            log(Level.WARNING, "Error occurred json conversion !");
            throw new JsonConversionException();
        }
    }

}
