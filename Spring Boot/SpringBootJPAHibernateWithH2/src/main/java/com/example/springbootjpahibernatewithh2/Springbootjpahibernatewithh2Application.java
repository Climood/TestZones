package com.example.springbootjpahibernatewithh2;

import com.example.springbootjpahibernatewithh2.Entities.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*

Hibernate автоматически входит в  Spring boot Jpa, для него можно сделать настройки, создав файл, к онсоли должно писать
следующее(если jpa стоит)
NFO 15109 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate Core {5.3.10.Final}
2019-07-24 15:57:59.250  INFO 15109 --- [           main] org.hibernate.cfg.Environment            : HHH000206: hibernate.properties not found
2019-07-24 15:58:00.206  INFO 15109 --- [           main] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.0.4.Final}
2019-07-24 15:58:00.941  INFO 15109 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
https://www.springboottutorial.com/hibernate-jpa-tutorial-with-spring-boot-starter-jpa
В приложении нам потребуется настройка h2 базы данных через ее инструмены, для этого включим ее консоль в
/src/main/resources/application.properties
When you reload the application, you can launch up H2 Console at http://localhost:8080/h2-console.
 внимание,  База данных "mem:~/test" не найдена и её автоматическое создание запрещено флагом IFEXISTS=true
 т.е. дял таких бд ставим флаг false
 ЛИБО используем дефолтный
 jdbc:h2:mem:testdb (для jdbc)
 */

//Прогу запускал через консоль (mvn spring-boor:run)
/*
Exploring JpaRepository methods

Code belows shows the SpringBoot2JPAWithHibernateAndH2Application
class extended to execute some of the StudentRepository methods.


 */
@SpringBootApplication
public class Springbootjpahibernatewithh2Application implements CommandLineRunner { //реализуем интерфейс, он требует метода run

    /*

    -SpringBoot2JPAWithHibernateAndH2Application implements CommandLineRunner - Implementing CommandLineRunner
    helps to execute the repository methods at start of the application.
    -public void run(String... args) throws Exception { - Implement the run method defined in the CommandLineRunner interface.
    This method is executed as soon as the application is launched up.

     */

    //В другом примере (SpringGuidesJDBC H2, мне приходилось делать репозиторий вручную, но сейчас можно исп след аннотацию)
    //https://otus.ru/nest/post/171/
    //              @Autowired
            /*
            Принцип её работы очень прост.
Допустим у нас есть bean-зависимости:
@Configuration
public class Config {
    // тип бина - ServiceDependency, имя - dependency
    public ServiceDependency dependency() {
        return new ServiceDependencyImpl()
    }
}
И есть класс сервиса:
@Service
public class MyService {
    private ServiceDependency dependency;
    @Autowired
    public MyService(ServiceDependency dependency) {
        this.dependency = dependency;
    }
}
И при создании контекста Spring автоматически определит,
что для создания MyService требуется bean типа ServiceDependency (или наследник),
найдёт его у себя, в рамках подставит зависимость ServiceDependencyImpl в bean MyService.
На самом деле, начиная со Spring 4.0 аннотацию @Autowired можно не ставить на конструктор, если он единственный в классе.
             @Autowired можно ставить непосредственно на поле. Да-да, это будет работать и с private-полями:
             Также аннотацию можно ставить на сеттеры:
             Но можно так же ставить и на отдельные методы, например:
             */
    @Autowired
    StudentRepository repository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
    Spring Boot Auto Configuration sees H2 in the classpath.
    It understands that we would want to talk to an in memory database.
    It auto configures a datasource and also a JdbcTemplate connecting to that datasource.
     */
    public static void main(String[] args) {
        SpringApplication.run(Springbootjpahibernatewithh2Application.class, args);
    }

    @Override
    public void run(String... args){
        //Let’s extend the run method to execute a few methods on the student repository.
        logger.info("Inserting -> {}", repository.save(new Student("Sergey", "A1")));
        logger.info("Student id 1 -> {}", repository.findById(1L));
        logger.info("Inserting -> {}", repository.save(new Student("Tyran", "A6666666")));
        logger.info("All users 1 -> {}", repository.findAll());


        //Insert
        logger.info("Inserting -> {}", repository.save(new Student("Kler", "A1234657")));
        logger.info("Inserting -> {}", repository.save(new Student("leon", "A7665212")));
        //Update
        logger.info("обновим данные студента с ид 1 -> {}", repository.save(new Student(1L, "Name-Updated", "New-Passport")));

        logger.info("Удалим студента с ид4");
        repository.deleteById(4L);

        logger.info("All users 2 -> {}", repository.findAll());

        //все данные можно глянуть в h2 console http://localhost:8080/h2-console
    }
}
