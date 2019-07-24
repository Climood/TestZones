package com.example.springguidesactionswithrestwebservice;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*
Как видите, это простой Java класс с горсткой полей и соответствующими методами их получения.
Он содержит аннотацию @JsonIgnoreProperties библиотеки обработки JSON Jackson,
 которая показывает, что любые поля, не связанные с полями класса, должны быть проигнорированы.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {
    private String name;
    private String about;
    private String phone;
    private String website;

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }
}
