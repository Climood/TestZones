package com.example.springguideshibernatejpah2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;


@SpringBootApplication
public class Springguideshibernatejpah2Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Springguideshibernatejpah2Application.class);
        ClientRepository repository = context.getBean(ClientRepository.class);

        //сохраняем пару клиентов в репозиторий
        repository.save(new Client("Jack","Balabol"));
        repository.save(new Client("Eva","Firstone"));
        repository.save(new Client("C3PO","mechanich"));
        repository.save(new Client("Dont","Find"));
        repository.save(new Client("Is","Find"));

        // fetch all customers Получим всех клиентов
        Iterable<Client> clients = repository.findAll();
        System.out.println("Клиенты найденные через findAll():");
        System.out.println("-------------------------------");
        for(Client client:clients){
            System.out.println(client);
        }
        System.out.println();

        //Найдем конкретного по ID
        Optional<Client> optionalClient = repository.findById(1L);
        System.out.println("Клиент найденный через findById(1L):");
        System.out.println("--------------------------------");
        System.out.println(optionalClient.get());
        System.out.println();

        //Найдем конкретного по фамилии
        List<Client> finded = repository.findByLastName("Find");
        System.out.println("Клиенты найденные через findByLastName():");
        System.out.println("-------------------------------");
        for(Client client:finded){
            System.out.println(client);
        }
        context.close(); //Не забудь закрыть
    }



}

/*
 В конфигурацию вам необходимо добавить аннотацию @EnableAutoConfiguration.
  Эта аннотация говорит Spring Boot конфигурировать JPA репозитории и сущности из текущего пакета.
   Поэтому для любых интерфейсов, которые расширяют org.springframework.data.repository.Repository,
    будут автоматически сгенерированы реализации.  ЭТО УЖЕ ЕСТЬ В @SpringBootApplication

    Расширяя JpaRepository, ваш интерфейс CustomerRepository транзитивно расширяет Repository.
    Следовательно, Spring Data JPA будет находить его и создавать для вас реализацию.

Application включает метод main(), который использует CustomerRepository в нескольких тестах.
Для начала, он получает CustomerRepository из контекста приложения Spring.
Затем он сохраняет несколько объектов Customer через метод save().
Далее, вызывается findAll() для получения всех объектов Customer из БД.
Затем вызывается findById() для получения единственного Optional Client по его ID. Далее option я использую чтобы получить клиента
В заключении, вызывается findByLastName() для получения всех объектов с фамилией "Bauer".
 */