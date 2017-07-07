package com.example.android.guardiannews;

/**
 * Created by Maffey on 2017-07-06.
 */

public class News {
    /**
     * Title of the article.
     */
    private String title;
    /**
     * Section to which the article belongs.
     */
    private String section;
    /**
     * URL link to the website with the corresponding article.
     */
    private String webUrl;

    /**
     *
     * @param title refers to the title.
     * @param section refers to the section (category) of the news.
     * @param webUrl is a url to the given article.
     */
    public News(String title, String section, String webUrl) {
        this.title = title;
        this.section = section;
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return this.title;
    }
    public String getSection() {
        return this.section;
    }
    public String getWebUrl() {
        return this.webUrl;
    }
}