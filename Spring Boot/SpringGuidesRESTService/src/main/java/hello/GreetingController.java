package hello;
//http://spring-projects.ru/guides/rest-service/

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;
/*
Атомарные классы пакета util.concurrent

Пакет java.util.concurrent.atomic содержит девять классов для выполнения атомарных операций.
Операция называется атомарной, если её можно безопасно выполнять при параллельных вычислениях в нескольких потоках,
 не используя при этом ни блокировок, ни синхронизацию synchronized.
 */

/*
Далее вы создаете контроллер ресурса, который будет обрабатывать эти приветствия.
Создание контроллера ресурса

Подход Spring при построении RESTful web-сервиса заключается в обработке HTTP-запросов контроллером.
Эти компоненты легко опознаются по @RestController аннотации, а GreetingController ниже обрабатывает
GET запросы для /greeting, возвращая новый экземпляр Greeting класса:

 */
@RestController
public class GreetingController {
     private static final String template = "Hello, %s!";
     private final AtomicLong counter = new AtomicLong(); //счетчик, работающий норм с многопотоками без синхронизайций,
    //используем его метод incrementAndGet() чтобы генерировать id
    //для форматированния приветствия используем String.format()
    /*
    Использование String.format
В случае, если нет необходимости выводить отформатированную строку, а нужно просто ее сохранить для дальнейшего использования
(например, для записи в лог или базу данных) следует использовать метод format из класса String.
Принципы форматирования в этом случае абсолютно такие же, как у описанного выше printf,
но этот метод вместо вывода строки сохраняет ее как отформатированную строку.
Пример использования:
String s = String.format("Курс валют: %-4s%-8.4f  %-4s%-8.4f","USD", 58.4643, "EUR", 63.3695);
     */
     @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name",required = false,defaultValue = "Anonimous") String name){
         return new Greeting(counter.incrementAndGet(), //id
                 String.format(template,name)); //content
     }
     /*
     В примере выше не указывается GET, PUT, POST и т.д.,
      потому то @RequestMapping сопоставляет все HTTP-операции по умолчанию.
      Использование @RequestMapping(method=GET) определяет это сопоставление.

       @RequestParam связывает значение строкового параметра name запроса с параметром name метода greeting().
       Этот строковый параметр запроса не required(не обязателен); если он отсутствует в запросе,
       то будет использовано defaultValue "World".

      Реализация метода создает и возвращает новый Greeting объект с атрибутами id и content,
       основанными на следующем значении counter и форматированном значении name по шаблону template.
        Ключевое отличие между традиционным MVC контроллером и контроллером RESTful web-сервиса выше в создании
         тела HTTP-ответа. Вместо того, чтобы опираться на view-технологию для рендеринга на серверной стороне
          сообщения приветствия в HTML, RESTful web-сервис контроллер просто заполняет и возвращает Greeting объект.
           Данные объекта будут записаны напрямую в HTTP-ответ как JSON.

Этот код использует новую в Spring 4 @RestController аннотацию, помечая класс как контроллер,
 где каждый метод возвращает объект вместо представления(view). Это сокращение для @Controller и @ResponseBody, вместе взятых.

Greeting объект должен быть конвертирован в JSON. Благодаря поддержке Spring HTTP конвертера,
вам не требуется выполнять эту конвертацию вручную. Когда Jackson 2 в classpath, MappingJackson2HttpMessageConverter
из Spring выбирается автоматически для конвертации экземпляра Greeting в JSON.
      */

}
