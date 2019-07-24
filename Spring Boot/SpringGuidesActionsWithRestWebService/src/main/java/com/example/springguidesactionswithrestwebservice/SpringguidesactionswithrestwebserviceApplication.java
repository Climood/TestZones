package com.example.springguidesactionswithrestwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//http://spring-projects.ru/guides/consuming-rest/
/*
 После того, как вы настроили проект, вы можете создать простое приложение,
 которое будет взаимодействовать с RESTful web-сервисом.

Предположим, что вы хотите найти что-нибудь, что известно Facebook о Pivotal.
 Зная, что Pivotal имеет страницу на Facebook c ID "pivotalsoftware", вы должны будете выполнить запрос к Facebook’s Graph API по URL:

http://graph.facebook.com/pivotalsoftware

Если выполните запрос из вашего браузера или curl, то получите JSON с кучей данных, включая всякий лишний мусор
Это достаточно просто сделать, но не особо полезно, когда данные получаются через браузер или curl.

Более полезный способ взаимодействия с REST web-сервисом является программный.
Для решения этой задачи, Spring предоставляет удобный шаблонный класс RestTemplate.
RestTemplate осуществляет взаимодействие с большинством RESTful сервисами однострочным заклинанием.
И он может даже привести данные к пользовательским типам.


 Для начала, вы создаете класс, который содержит необходимую вам информацию.
 Если вам нужно знать название Pivotal, номер телефона, URL web-сайта и информацию о pivotalsoftware,
 то представленный класс Page как раз то, что вам нужно:

Создание приложения исполняемым

Несмотря на то, что пакет этого сервиса может быть в составе web-приложения и WAR файлов,
более простой подход, продемонстрированный ниже создает отдельное самостоятельное приложение.
Вы упаковываете все в единый, исполняемый JAR-файл,
который запускается через хорошо знакомый старый main() Java-метод.
Попутно, вы используете поддержку Spring для встроенного Tomcat контейнера сервлетов как
 HTTP среду выполнения вместо развертывания на сторонний экземпляр.

Теперь вы можете создать Application класс, который использует
 RestTemplate для получения данных со страницы Pivotal на Facebook и преобразует в Page объект.
 */
@SpringBootApplication
public class SpringguidesactionswithrestwebserviceApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpringguidesactionswithrestwebserviceApplication.class, args);
        RestTemplate restTemplate = new RestTemplate();
        String url = "";
        System.out.println("Вставьте ссылку на интересующую компанию из graph.facebook (пример http://graph.facebook.com/pivotalsoftware)");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            url = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        Page page = restTemplate.getForObject(url,Page.class);
        //Graph facebook это специальное api предоставоябщее json ы c инфой
        //текущий запрос даст ошибку, так как там приделали идентификацию (400 bad request)
        //https://developers.facebook.com/docs/graph-api/using-graph-api/
        System.out.println("Name:    " + page.getName());
        System.out.println("About:   " + page.getAbout());
        System.out.println("Phone:   " + page.getPhone());
        System.out.println("Website: " + page.getWebsite());
    }

}

/*
 Т.к. библиотека обработки JSON Jackson доступна из classpath, RestTemplate будет использовать её
 (через message converter) для конвертации входящих JSON данных в Page объект.
 Затем, содержимое Page объекта будет напечатано на консоли.

В этом примере вы использовали RestTemplate для создания только HTTP GET запроса.
Но RestTemplate также поддерживает и другие виды HTTP запросов, такие как POST, PUT и DELETE.
 */
