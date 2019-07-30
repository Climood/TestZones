package com.example.bostongenespringrestservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

//public interface UserRepository extends JpaRepository<User,Long> {
@RepositoryRestResource(collectionResourceRel = "user" , path = "user")
public interface UserRepository extends PagingAndSortingRepository<User,Long> {
    List<User> findByEmail(@Param("email") String email);
    List<User> findByLastName(@Param("lastName") String lastName);
}
/*
http://spring-projects.ru/guides/accessing-data-rest/
 Этот репозиторий является интерфейсом и позволяет вам выполнять различные операции с участием Person объектов.
 Он получает эти операции, наследуя интерфейс PagingAndSortingRepository, определенным в Spring Data Commons.

В процессе выполнения, Spring Data REST будет создавать реализацию этого интерфейса автоматически.
Затем он будет использовать аннотацию @RepositoryRestResource, обращаясь к Spring MVC для создания RESTful точки выхода /people.

@RepositoryRestResource не обязательна для указания. Она используется только когда необходимо изменить детали экспорта, такие как использование /people вместо значения по умолчанию /persons.
 */