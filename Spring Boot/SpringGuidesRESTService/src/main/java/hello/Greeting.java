package hello;
/*
 Сервис будет обрабатывать GET запросы для /greeting, дополнительно - параметр name в строке запроса.
 GET запрос должен возвращать ответ со статусом 200 OK и JSON в теле, которое соответствует сообщению приветствия.
  он должен выглядеть таким образом:

{
    "id": 1,
    "content": "Hello, World!"
}

Поле id - это уникальный идентификатор приветствия, а content - текстовое представление.

Для модели представления приветствия вам необходимо создать класс, представляющего ресурс.
Он представляет собой простой java-объект с полями, конструкторами и методами доступа к значениям id и content:
 */

public class Greeting {
    private final long id;
    private final String content;

    Greeting(long id,String content){
        this.id = id;
        this.content = content;
    }
    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }


}
