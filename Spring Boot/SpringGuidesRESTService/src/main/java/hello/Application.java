package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*
Создание приложения исполняемым

Несмотря на то, что возможно упаковать этот сервис в традиционный WAR файл для развертывания
на стороннем сервере приложений, более простой подход, продемонстрированный ниже создает отдельное
самостоятельное приложение. Вы упаковываете все в единый, исполняемый JAR-файл, который запускается
через хорошо знакомый старый main() Java-метод. Попутно, вы используете поддержку Spring для встроенного
Tomcat контейнера сервлетов как HTTP среду выполнения вместо развертывания на сторонний экземпляр.
 */
//@ComponentScan
//@EnableAutoConfiguration   ВНИМАНИЕ @EnableAutoConfiguration и @ComponentScan УСТАРЕЛИ, их заменяет
// @SpringBootApplication   , он выполняет все их функции
  @SpringBootApplication    
public class Application {

    public static void main(String... args){
        SpringApplication.run(Application.class,args);
    }
}
/*
 main() метод передает управление вспомогательному классу SpringApplication,
 где Application.class - аргумент его run() метода.
 Это сообщает Spring о чтении метаданных аннотации из Application и управлении
  ею как компонента в Spring application context.

Аннотация @ComponentScan говорит Spring'у рекурсивно искать в пакете hello
и его потомках классы, помеченные прямо или косвенно Spring аннотацией @Component.
Эта директива гарантирует, что Spring найдет и зарегистрирует GreetingController,
потому что он отмечен @RestController, который, в свою очередь, является своего рода @Component аннотацией.

Аннотация @EnableAutoConfiguration переключает на приемлемое по умолчанию поведение,
основанное на содержании вашего classpath. К примеру, т.к. приложение зависит от встраиваемой
версии Tomcat (tomcat-embed-core.jar), Tomcat сервер установлен и сконфигурирован на приемлемое
по умолчанию поведение от вашего имени. И т.к. приложение также зависит от Spring MVC (spring-webmvc.jar),
Spring MVC DispatcherServlet сконфигурирован и зарегистрирован для вас - web.xml не нужен! Автоконфигурация
полезный и гибкий механизм. Подробную информацию смотрите в API документации.

ВНИМАНИЕ @EnableAutoConfiguration и @ComponentScan УСТАРЕЛИ, их заменяет   @SpringBootApplication   , он выполняет все их функции
 */
