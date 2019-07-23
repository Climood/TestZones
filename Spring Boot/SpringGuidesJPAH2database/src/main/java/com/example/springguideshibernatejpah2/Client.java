package com.example.springguideshibernatejpah2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //Аннотация сущности JPA
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;

    protected Client(){

    }

    public Client(String firstName,String lastName){ //id будет генириться сам
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString(){
        return String.format("Клиент[id=%d,имя='%s',фамилия='%s']",id, firstName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

/*
 Здесь у вас есть класс с тремя атрибутами id, firstName и lastName.
 У вас также есть два конструктора. Конструктор по умолчанию существует только из-за JPA.
 Вы не будете его использовать напрямую, потому что он определен как protected.
 Другой конструктор вы будете использовать для создания экземпляра Customer, чтобы сохранить его в БД.

Класс Customer аннотирован как @Entity, означая, что он является JPA сущностью.
Из-за отсутствия аннотации @Table, предполагается, что сущность будет соответствовать таблице с названием Customer.

Свойство id класса Customer аннотировано как @Id, так что JPA распознает его как ID объекта.
Свойство id также аннотировано как @GeneratedValue, означая, что ID должен генерироваться автоматически.

Другие два свойства, firstName и lastName остались не аннотированными.
Это значит, что они будут соответствовать колонкам с такими же названиями и параметрами.

Метод tostring будет печатать свойства объекта.
 */