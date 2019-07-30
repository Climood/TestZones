package com.example.bostongenespringrestservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
Создание простого web-контроллера

В Spring, конечной точкой web-обработки являются простые Spring MVC контроллеры.
 Ниже представлен Spring MVC контроллер, который обрабатывает GET / запрос и возвращает простое сообщение:
 */
@Controller //@RestConroller не дает парсить html страницы
@RequestMapping
public class HomeController {
    /*
    @RequestMapping(value = "/")
    public String greeting(){ //чтобы это возвращало не стороку greeting а html макет с таким названием
        //подключаем <artifactId>spring-boot-starter-thymeleaf</artifactId>
        //вывести главную страницу
        return "greeting";
    }
    */
    @RequestMapping(value = "/")
    public @ResponseBody
    String index() {
        return "Добро пожаловать, прочтите примеры команд!\n/user/findByEmail?email=...\n/user/add?firstName=adam&lastName=first&birthday=0/0/0&email=one@mail.net&password=111111\n/user/delete?email=one@mail.net&password=111111";
    }
}
/*
 Весь класс отмечен @Controller, поэтому Spring MVC может обнаружить контроллер,
 используя встроенные возможности сканирования и настройки web-маршрутов.

Метод помечен как @RequestMapping, обозначающий путь и REST действие.
В данном случае, GET является поведением по умолчанию; он возвращает сообщение о том, что вы на домашней странице.

@ResponseBody сообщает Spring MVC писать текст напрямую в тело HTTP ответа, поэтому здесь нет представлений.
Вместо этого, когда вы переходите на страницу, вы получите в браузере сообщение о том, что данный урок имеет LDAP защиту.
 */