package ir.jiring.assignment.dto;

import java.util.Objects;

public class News implements Comparable<News> {
    private short priority;
    private String headline;

    public News() {
    }

    public News(short priority, String headline) {
        this.priority = priority;
        this.headline = headline;
    }

    public static News of(short priority, String headline) {
        return new News(priority, headline);
    }

    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    @Override
    public int compareTo(News o) {
        return Short.compare(this.priority, o.priority);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return priority == news.priority && Objects.equals(headline, news.headline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority, headline);
    }
}
