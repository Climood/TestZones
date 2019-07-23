package com.example.springguideshibernatejpah2;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/*
Создание простых запросов

Spring Data JPA ориентирована на использование JPA для сохранения данных в реляционную БД.
Наиболее полезной её возможностью является автоматическое создание реализаций репозитория во время выполнения из интерфейса.

Чтобы увидеть, как это работает, создайте интерфейс репозитория, который работает с сущностями Customer:
 */

public interface ClientRepository extends CrudRepository<Client,Long> {
    List<Client> findByLastName(String lastName);
}
/*
 CustomerRepository расширяет CrudRepository интерфейс.
 Тип сущности и ID, Customer и Long, указаны в общих параметрах CrudRepository.
 Расширяя CrudRepository, CustomerRepository наследует несколько методов для работы с Customer,
 включая методы сохранения, удаления и поиска.

Spring Data JPA позволяет также определить и другие методы запросов, просто описав сигнатуры метода.
В данном случае, таковым является метод findByLastName().

В типичном Java приложении, вы бы написали класс, который реализовывал CustomerRepository.
Но это то, что делает Spring Data JPA мощным инструментом: вам не нужно писать реализацию интерфейса репозитория.
Spring Data JPA создает реализацию на лету, когда вы запускаете приложение.

 */
