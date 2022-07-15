package com.chen2dev.recetascubanas.model;

public class Post {
    private String title;
    private String content;
    private String excerpt;
    private String tags;
    private Integer categories;
    private String feature_image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getCategories() {
        return categories;
    }

    public void setCategories(Integer categories) {
        this.categories = categories;
    }

    public String getFeature_image() {

        return feature_image;
    }

    public void setFeature_image(String feature_image) {
        this.feature_image = feature_image;
    }
}
