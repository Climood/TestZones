import org.joda.time.LocalTime; //Как я заметил, его нет в списке автовыбора репозиториев мавен, зато если вписать его зависимоть вручную, класс найдет

public class runMe {
    public static void main(String... args){
        LocalTime currentTime = new LocalTime();
        System.out.println("The current local time is: " + currentTime);

        Greeter greeter = new Greeter();
        System.out.println(greeter.hello());
    }
}
