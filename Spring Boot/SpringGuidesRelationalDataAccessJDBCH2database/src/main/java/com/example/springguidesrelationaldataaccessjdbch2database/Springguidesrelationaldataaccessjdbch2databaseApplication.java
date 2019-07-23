package com.example.springguidesrelationaldataaccessjdbch2database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class Springguidesrelationaldataaccessjdbch2databaseApplication {

    public static void main(String[] args) {
        //SpringApplication.run(Springguidesrelationaldataaccessjdbch2databaseApplication.class, args);
        // простая in-memory БД h2 настроенная для тестирования (не для реального использования!)
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class); //зависимость от h2 созданная генератором не подошла, выгрузил напрямую (у выгруженной отобразило версию)
        dataSource.setUsername("sa");
        //dataSource.setUrl("jdbc:h2:mem"); //Такой вариант дает ошибку,ЧТОБЫ РАБОТАЛ, НАДО добавить Инструменты разработчика для pom.xml
        /*
        <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <optional>true</optional>
    </dependency>
     Доступ к db из http://localhost:8080/h2-console/
     Укажите jdbc:h2:mem:testdb как URL-адрес JDBC
     !Это не помогло! но оставлю заметки
     Помог следующий вариант
         */
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=- 1;DB_CLOSE_ON_EXIT=FALSE"); //Такой вариант работает, видимо бд закрывалась до того, как я что то сделаю?
        /*
        [ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:2.1.6.RELEASE:run
        (default-cli) on project springguidesrelationaldataaccessjdbch2database: An exception occurred while running.
        null: InvocationTargetException: Failed to obtain JDBC Connection;
        nested exception is org.h2.jdbc.JdbcSQLNonTransientConnectionException:
        Путь неявно является относительным для текущего рабочего каталога и не
        допустим в URL базы данных "jdbc:h2:mem". Используйте абсолютный путь, ~/name, ./name, или настройку baseDir.

         */
        //dataSource.setUrl("jdbc:h2:file:/home/climood/IdeaProjects/SpringGuidesRelationalDataAccessJDBCH2database/demo");//указываем режим сохранения бд в файле и рабочую директорию прямо, (где хранить бд)
        /*
         jdbc url, говорящий JDBC API с какой именно базой я хочу установить соединение.
          В общем виде формат url такой: «jdbc:driver:options», где driver — имя драйвер
           базы, options — параметры, специфичные для выбранного драйвера, указывающие ему
            на конкретную базу данных. В моём случае «jdbc:h2:mem» означает, что я хочу
             использовать драйвер базы данных H2, которому сообщается что мне нужна встроенная
              анонимная in-memory база.
         */
        dataSource.setPassword("");

        //Собственно создаем сам шаблок спринга, передавая ему созданную бд
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        //создаем таблички
        System.out.println(LocalTime.now() +" " + "["+Thread.currentThread().getName()+"] создаем таблички");
        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");//любимый SQL, удаляем если уже такая есть
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");
        String[] names="John Wick;Norman Ridus;Ashen one;Victorion Targarien".split(";");
        for(String fullName:names){
            String[] fName=fullName.split(" ");
            System.out.printf(LocalTime.now() +" " + "["+Thread.currentThread().getName()+"] Делаем запись клиента с данными: %s %s\n",fName[0],fName[1]); //в качестве форматтера printf
            jdbcTemplate.update("INSERT INTO customers(first_name, last_name) VALUES (?,?)",
                    fName[0],fName[1]);
        }

        System.out.println(LocalTime.now() +" " + "["+Thread.currentThread().getName()+"] Выберем клиентов чье имя(first_name) Ashen");
        List<Customer> results = jdbcTemplate.query("select * from customers where first_name = ?", new Object[]{ "Ashen" },
                new RowMapper<Customer>(){
                    @Override
                    public Customer mapRow(ResultSet resultSet, int rowNum) throws SQLException{
                        return new Customer(resultSet.getLong("id"),resultSet.getString("first_name"),resultSet.getString("last_name"));
                    }
                }); //анонимный класс для возврата объектов Customer полученных из запроса

        //Выведем результат
        for(Customer customer:results){
            System.out.println(customer);   //исп переопределенный toString
        }


    }

}
/*
Сохранение и получение данных

Spring предоставляет шаблонный класс JdbcTemplate, который упрощает работу с SQL и JDBC реляционных СУБД.
Большая часть JDBC кода состоит из получения ресурсов, управления соединением, обработки исключений
и вообще проверки ошибок, ни как не связанных с тем, для чего предназначен код. JdbcTemplate берет на
себя все это за вас. Все, что остается вам сделать, это сосредоточиться на решении поставленной задачи.

 Сначала настроили JDBC [DataSource] с использованием удобного Spring SimpleDriverDataSource.
 Затем, вы использовали DataSource для создания экземпляра JdbcTemplate.
SimpleDriverDataSource - удобный класс, но не предназначен для реальной работы.

После того, как вы сконфигурировали JdbcTemplate,
достаточно просто начать делать запросы к БД.
Для начала, вы устанавливаете несколько DDL,
используя execute метод класса JdbcTemplate.
Затем вы создаете несколько записей в вашей новосозданной таблице,
используя update метод класса JdbcTemplate.
Первым аргументом метода является строка запроса, следующий аргумент(массив Object объектов)
состоит из значений, которые будут подставлены в строку запроса вместо "?".
Используйте ? для аргументов, чтобы избежать атак на основе SQL инъекций.

В конце вы используете метод query для поиска по вашей таблице записей,
соответствующих критерию. Вы вновь используете "?" аргументы для
создания параметров запроса, подставляя необходимые значения при вызове.
Последний аргумент в методе query создает экземпляр RowMapper<T>. Spring делает 90% всей работы,
но ему неизвесто, что вы планируете делать дальше с полученным результатом.
Таким образом, вы предоставляете образец RowMapper<T>, который Spring будет вызывать для каждой записи,
объединять результаты и возвращать как коллекцию.
 */

/*
Тоже самое, но в стиле Java 8 с лямбдами и streams

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        // Split up the array of whole names into an array of first/last names
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
        splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

        log.info("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> log.info(customer.toString()));
    }
}
 */