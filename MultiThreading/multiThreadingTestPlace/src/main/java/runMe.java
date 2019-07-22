import java.io.IOException;
import java.util.concurrent.Executor;


public class runMe {
    public static void main(String... args) throws IOException, InterruptedException {
        //Примеры создания потоков через реализацию интерфейса Runnable и реализацию метода run() (в это смлучае класс потом надо поместить в поток(можно в самом классе через this или в проге))
        //или через наследование класса от класса Thread , тогда тоже надо определить метод run, но его уже можео самого запускать через start, не помещая в поток, (нежелательно, так как наследуем класс)
        Thread currThread = Thread.currentThread();
        System.out.println("name "+currThread);
        currThread.setName("blabla");
        System.out.println("name "+currThread.getName());
        System.out.println("Главный Поток " + currThread + " Запущен? "+currThread.isAlive());
        try{
            System.out.println("До паузы");
            currThread.sleep(1000l);
            System.out.println("После паузы в 1 секунду");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ThreadA A = new ThreadA();
        ThreadB B = new ThreadB();
        //A.run();
        //B.run();

        //Thread a=new Thread(A,"A");
        //Thread b=new Thread(B,"B");
        //a.start();
        //b.start();

        A.runMe(); //запуск можно сделать и в конструкторе
        B.runMe();


        classThread C = new classThread();
        Executor exe = new Executor() {
            public void execute(Runnable runnable) {
                runnable.run();
            }
        };
        //exe.execute(A);
        //exe.execute(B);

        //Runnable[] threads = {A,B};
        //for(int i=0;i<2;i++){
        //    threads[i].run();
        //}

          //Далее примеры контролля завершения потоков для завершения главного через isAlive() и join()
        /*
        final boolean isAlive() - return true, если его поток(для кот вызван) еще исполняется, иначе false
        final void join( тут можно указать max время ожидания ) - исп чаще, исп чтобы дождаться завершения потока исполнения, кидает ошибку InterruptedException
        ожидает завершение того потока исполнения для кот вызван
        (имя отражает след принцип: вызывающий поток ОЖИДАЕТ когда указанный поток ПРИСОЕДИНИТСЯ к нему)
         */

        System.out.println("Главный Поток " + currThread + " Запущен? "+currThread.isAlive());
        System.out.println("Поток" + A.currThread + "Запущен?"+A.currThread.isAlive()); //сам поток делается в методе runMe класса A, а ссылка на него запоминается в переменной
        System.out.println("Поток "+B.currThread+"запущен?"+B.currThread.isAlive());
        System.out.println("Поток" + C + "запущен?"+C.isAlive());

        try {
            System.out.println("Ждем завершения потоков");
            A.currThread.join();//тут мы вызываем эти методы в главном потоке (main) , а в качестве их указанных идут A,B,C, и вызывающий main ждет, пока все три потока присоединяться к нему
            B.currThread.join();
            C.join();
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
            e.printStackTrace();
        }
        System.out.println("Поток" + A.currThread + "Запущен?"+A.currThread.isAlive());
        System.out.println("Поток "+B.currThread+"запущен?"+B.currThread.isAlive());
        System.out.println("Поток" + C + "запущен?"+C.isAlive());

        System.out.println("Главный Поток " + currThread + " Запущен? "+currThread.isAlive()); //он будет вырублен только с окончанием программы


        //Рассмотрим приоритеты потоков подробнее, про вытеснение понятно, более высокий может вытеснить более низкий, но зависит от ОС
        //для задания приоритета потоку служит метод
        // final void setPriority(int уровень) из класса Thread (уровень в границах от MIN_PRIORITY до MAX_PRIORITY [1,10]
        // для возврата уровня по умолчанию - NORM_PRIORITY (5)
        // можно например сделать метод setPrior в классе реализующем Runnable, а в этом методе уже воздействовать на currThread.setPriority()
        //чтобы получить приоритет используем final int getPriority()
        //старайся использовать не вытесняющую многозадачность, а уступление ЦП для потоков

        System.out.println("приоритет Потока" + A.currThread + "="+A.currThread.getPriority()); //они кстати уже мертвы
        System.out.println("приоритет Потока" + B.currThread + "="+B.currThread.getPriority());
        System.out.println("приоритет Потока(он мертв)" + C + "="+C.getPriority());
        C.setPriority(6); //сделаем его самым высокоприоритетным, но все равно он мертв
        System.out.println("приоритет Потока(попытка изменения приоритета завершенного потока на 6) " + C + "="+C.getPriority()); //и видим , что , такак как он мертв, его приоритет не меняется
        //оживим
        C = new classThread();
        C.setPriority(6); //сделаем его самым высокоприоритетным, но все равно он мертв
        System.out.println("приоритет Потока(изменили приоритет активного (живого) потока на 6) " + C + "="+C.getPriority()); //и видим , что приоритет изменился
        C.join(); //Ждем пока посчитает, чтобы приступить к синхронизациям потом


        //ДАЛЕЕ рассмотрим СИНХРОНИЗАЦИЮ (на основе Монитора(взаимоисключающая блокировка) )
        //для синхрониации есть 2 способа, исп ключ слово synchronized
        //1. применение синхронизационных методов
        //у всех объектов есть свои неявно связанные мониторы, для схода в него надо просто вызвать метод объявленный с
        //доступом synchronized
        //Когда поток будет в теле такого синх. метода, все иные потоки или синх методы, пыт вызвать этот метод для такого же
        //экземпляра , будут ждать освобождения
        Callme target = new Callme(); //создаем 1 объект и 3 потока обращаются к нему используя один и тот же его метод
        System.out.println("Без синхронизации");
        Caller cal1 = new Caller(target,"Синхронизация"); //как выводит без синхронизации
        Caller cal2 = new Caller(target,"Прошла");
        Caller cal3 = new Caller(target,"Успешно?");
        try{
            cal1.currThread.join(); //если не ставить ожидание потоков, не успеет вывести их синхронизацию
            cal2.currThread.join();
            cal3.currThread.join();
        }catch (InterruptedException e){
            System.out.println("прервано");
        }

        System.out.println("С синхронизацией");//для того, чтобы гарантировать завершение работы первым syncCaller в методе Callme ,
        //а только потом передавать управление следующему syncCaller, делаем метод syncCall() synchronized, доступ к нему будет только по очереди
        syncCaller sCal1 = new syncCaller(target,"Синхронизация"); //как выводит без синхронизации
        syncCaller sCal2 = new syncCaller(target,"Прошла");
        syncCaller sCal3 = new syncCaller(target,"Успешно?"); //почему то этот поток выводится ранее второго, тупит комп?
        //но даже если идут не по порядку, видно, что доступ одновременно лишь у одного потока(т.к. даже при наличии паузы, каждый поток сначала заканчивает свое дело(ставит ]), лишь потом вступает следующий
        try{
            sCal1.currThread.join();
            sCal2.currThread.join();
            sCal3.currThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        //УЧИТЫВАЕМ что в этом способе (1) , синхронизирован лишь один метод, остальные методы могут быть и не синхронизированы
        //ДАЛЕЕ 2ой способ синхронизации, если не подходит снхронизация отдельных методов
        //Оператор Synchronized
        //1ый способ пригоден не всегда, т.к. требует изменение кода исходного класса
        //ЧТО ЕСЛИ требуется синхронизировать доступ к объектам класса, который изначально писался не под многопоточный доступ?
        //а исходгный код класса нам не доступен и его мы изменить не можем => не можем сделать методы synchronized по 1ому способу
        //ТОГДА используем этот (2) способ
        //Можно заключить вызовы методов такого класса в блок оператора synchronized
        /*
        synchronized(ссылка на объект){
           //синхронизируемые операторы
        }
         */
        //гарантирует, что вызов метода, явл членом того же класса, что и синхронизируеиый объект под ссылкой, произойдет только тогда
        //когда текущий поток исполнения успешно войдет в монитор данного объекта (и не даст войти другим потокам)
        //ПРИМЕР, использщуем несинхронизированный метод call() и несихнронизированный nonSCaller но в синхронизированном блоке
        Callme target1 = new Callme(); //создаем 1 объект и 3 потока обращаются к нему используя один и тот же его метод
        System.out.println("С синхронизацией в блоке synchronized");
        nonSCaller cal4 = new nonSCaller(target,"Синхронизация"); //как выводит без синхронизации
        nonSCaller cal5 = new nonSCaller(target,"Прошла");
        nonSCaller cal6 = new nonSCaller(target,"Успешно?");
        try{
            cal4.currThread.join(); //если не ставить ожидание потоков, не успеет вывести их синхронизацию, если глав поток завершается сразу после них
            cal5.currThread.join();
            cal6.currThread.join();
        }catch (InterruptedException e){
            System.out.println("прервано");
        }

        //Взаимодействие потоков исполнения
        //более точное управление потоками исполнения
        //чтобы избежать системы опроса (поставщик данных ждет, пока потребитель закончит свои действия, потом потребитель ждет, пока поставщик сформирует новый пул данных
        //используем методы wait() notify() notifyAll() ( они final и принадлежат Object => доступны всем классам)
        //все три МОГУТ быть вызваны ТОЛЬКО ИЗ СИНХРОНИЗИРОВАННОГО контекста
        /*
        final void wait( можно указать время ожидания ) вынуждает выз поток исполнения уступить монитор и перейти в состояние ожидания до тех пор, пока какой нибудь другой
        поток исполнения не войдет в тот же монитор и не вызовет метод notify() т.е. не скажет о готовности выйти
        final void notify() возобновляет исполнение потока, из кот был вызван метод wait() для того же самого объекта
        final void notifyAll() возобновляет исполнение всех потоков, из кот был вызван метод wait() для того же самого объекта
        одному из этих потоков предоставляется доступ
        */
        //ВНИМАНИЕ бывает редкий случай при котором ожидающий поток (wait) может быть возобновлен вследствие ЛОЖНОЙ активации
        //т.е. без вызова notify или notifyAll
        //Из за этой маловероятной пакости Oracle рекомендует вызывать wait() в цикле, проверяющем условие, по которому поток ожидает
        //возобновления
        //так мы и напишем в примере
        //сначала неправильный пример реализации поставщика и потребителя данных
        System.out.println("неправильный пример реализации поставщика и потребителя данных");
        Queue queue = new Queue();
        new Producer(queue);
        new Consumer(queue);
        //даже несмотря на то, что put() и get() synchronized в Queue, ничто не остановит переполнение потребителя данными от поставщика или
        //ничто не помешает потребителю дважды извлечь один и тот же элемент из очереди
        //чтобы наладить работу, мы используем методы wait() и notify() для передачи уведомлений в обе стороны
        System.out.println("ПРАВИЛЬНЫЙ пример реализации поставщика и потребителя данных");
        GoodQueue goodQueue = new GoodQueue();
        new GoodProducer(goodQueue);
        new GoodConsumer(goodQueue);
        //в get вызывается wait, и в итоге ждем, пока Producer не notify() (уведомит) , что данные прочитаны, как только уведомит,
        //возобновляем get() . далее как только данные получены Producer'ом , get делает notify() , чтобы put мог считать порцию данных
        //далее, wait останавливает поток в Consumer, пока он не уведомит, что Consumer извлек данные из очереди, когда извлечет и поток вощобновится,
        //делаем notify() этим уведомляя, что можем извлечь новый элемент из очереди
        /*
        Producer -> put -> [wait -> put n -> notify] -> Consumer -> get -> [wait -> get n -> notify] -> ...
         */

        //так же есть частая ОШИБКА ВЗАИМНАЯ БЛОКИРОВКА
        /*
        может произойти если потоки имеют циключескую зависимость от пары синзронизированных объектов
        Допустим 1ин поток входит в монитор X, а другой в монитор Y.
        если поток 1ый поток в Х пытается вызвать любой синхронизированный метод для У
        то он будет блокирован (т.к. монитор У занят)
        но если поток в У попробует, в свою очередь, вызывать любой синхронизированный метод для Х,
        то он будет ожидать вечно, поскольку
        для получения доступа к Х он должен снять свою блокировку с У, чтобы поток в Х мог завершиться и освободть Х

        ЭТА ошибка возникает по 2 причинам и трудно дебажиться
        1) очень редко, если исполнение потоков точно совпадает по времени
        2) если в ней учавствуютт >2 потоков исполнения и 2х синронизированных объектов

        СТАРАЙСЯ избегать, пример такой ошибки стр 309 шилд
        */



        //ПРиостановка, возобновление и остановка потоков исполнения
        //resume suspend and stop объявлены устаревшими методами и нежелательны к использованию (могут повлечь ошибки)
        //вместо них пишем свои аналоги, вызывающиеся в run()
        /*
        метод run должен переодически проверять должно ли исполнение потока быть приостановлено, возобновлено или прервано
        обычно для этого используем флаговые переменные, созданные собственноручно
        они обозначают состояние потока исполнения
        сами остановки делаем через wait и notify
        как будет реализован принцип - выбор за мной

         */
        ControlThread controlThread1 = new ControlThread("Контролируемый поток 1");
        ControlThread controlThread2 = new ControlThread("Контролируемый поток 2");
        try{
            Thread.sleep(1000); //гланвый поток спит на секунду
            controlThread1.mySuspend();
            System.out.println("Приостановка " + controlThread1.currThread.getName());
            Thread.sleep(1000); //гланвый поток спит на секунду
            controlThread1.myResume();
            System.out.println("Возобновление  " + controlThread1.currThread.getName());
            controlThread2.mySuspend();
            System.out.println("Приостановка " + controlThread2.currThread.getName());
            Thread.sleep(1000); //гланвый поток спит на секунду
            controlThread2.myResume();
            System.out.println("Возобновление  " + controlThread2.currThread.getName());
            controlThread1.currThread.join();
            controlThread2.currThread.join(); //ждем их выполнение, чтобы завершить главный поток
        }catch (InterruptedException e){
            e.printStackTrace();
            System.out.println("главный поток прерван");
        }
        //мы так же можем ПОЛУЧАТЬ СОСТОЯНИЕ ПОТОКА ИСПОЛНЕНИЯ
        /*
        Thread.State getState()
        Thread.State - > перечисление , определенное в классе потока, имеет следующие значения:
        BLOCKED //приостановка
        NEW
        RUNNABLE
        TERMINATED //завершение
        TIMED_WAITING
        WAITING
         */
        //проверим состояние главного потока
        Thread.State state = Thread.currentThread().getState();
        System.out.println("Состояние потока "+Thread.currentThread()+" = "+state);
        if(state == Thread.State.RUNNABLE){
            System.out.println("Поток выполняется");
        }

        //ДЛЯ СОЗДАНИЯ ПРОГ ИНТЕНСИВНЫХ ВЫЧИСЛЕНИЙ ДЛЯ МНОГОЯДЕРНЫХ СИСТЕМ ПРЕДПОЧТИТЕЛЬНО ИСПОЛЬЗОВАТЬ
        // Fork / Join Framework
    }
}

//Класс потока, для отработки паузы, возобновления и прекращения работы потока
class ControlThread implements Runnable{
    Thread currThread;
    boolean suspendFlag; ///suspend - приостановка



    ControlThread(String name){
        currThread = new Thread(this,name);
        suspendFlag = false;
        currThread.start();

    }

    //synchronized
    void mySuspend(){
        synchronized (this) {
            suspendFlag = true;
        }
    }
    //synchronized Сделал методы НЕсинхронизованными, но внутри них помести синхронизированный блок, гаранитирующий их вызов в мониторе объекта
    void myResume(){
        synchronized (this) {
            suspendFlag = false;
            notify();
        }
    }

    public void run(){
        try{
            for(int i=0;i<20;i++){
                System.out.println(currThread.getName() + " :"+i);
                Thread.sleep(200);
                synchronized (this){
                    while(suspendFlag){
                        wait();
                    }
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}


//Классы очередь, поставщик и потребитель данных для тестирования НЕПРАВИЛЬНОГО взаимодействия потоков
class Queue{
    int n;

    synchronized int get(){
        System.out.println("Получено" + n);
        return n;
    }

    synchronized void put(int n){
        this.n = n;
        System.out.println("отправлено" + n);
    }
}

class Producer implements Runnable{
    Queue queue;

    Producer(Queue queue){
        this.queue = queue;
        new Thread(this,"поставщик").start();
    }


    public void run(){
        int i = 0;
        while (i<10){
            queue.put(i++);
        }
    }
}

class Consumer implements Runnable{
    Queue queue;
    Consumer(Queue queue){
        this.queue = queue;
        new Thread(this,"Потребитель").start();
    }

    public void run(){

        int i = 0;
        while (i<10){
            queue.get();
            i++;
        }
    }
}

//ПРАВИЛЬНЫЕ Классы очередь, поставщик и потребитель для тестирования ПРАВИЛЬНОГО взаимодействия потоков
class GoodQueue{
    int n;
    boolean isValueSet= false;

    synchronized int get(){
        while(!isValueSet) //та самая проверка, которую рекомендует Oracle для предотвращние непредвиденного прекращения wait()
            try { //isValueSet будет изменен в put
             wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        System.out.println("получено "+ n);
        isValueSet = false;
        notify();//уведомляем об освобождении монитора
        return n;
    }

    synchronized void put(int n){
        while (isValueSet)//isValueSet будет изменен в get
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        this.n = n;
        isValueSet = true;
        System.out.println("отправлено "+ n);
        notify();//уведомляем об освобождении монитора

    }
}
class GoodProducer implements Runnable{
    GoodQueue goodQueue;//для тестирования ПРАВИЛЬНОГО взаимодействия потоков

    GoodProducer(GoodQueue goodQueue){
        this.goodQueue = goodQueue;
        new Thread(this,"поставщик").start();
    }

    public void run(){
        int i = 0;
        while (i<10){
            goodQueue.put(i++);
        }
    }
}

class GoodConsumer implements Runnable{
    GoodQueue goodQueue;//для тестирования ПРАВИЛЬНОГО взаимодействия потоков
    GoodConsumer(GoodQueue goodQueue){
        this.goodQueue = goodQueue;
        new Thread(this,"Потребитель").start();
    }

    public void run(){

        int i = 0;
        while (i<10){
            goodQueue.get();
            i++;
        }
    }
}


//классы для тестирования синхронизации
class Callme{ //будет выводить сообщение в скобках (перед последней задержка для наглядности проблемы асинхронизации
    void call(String msg){
        System.out.print("["+msg);
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("]");
    }

    synchronized void syncCall(String msg){ //такое тупо копирование для наглядности одновременно с синх и без синх
        System.out.print("["+msg);
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("]");
    }

}
class Caller implements Runnable{
    String msg;
    Callme target;
    Thread currThread;

    public Caller(Callme targ, String msg){
        target = targ;
        this.msg = msg;
        currThread = new Thread(this); //выделяем свой поток
        currThread.start();
    }
    public void run(){
        target.call(msg);
    }
}

class nonSCaller implements Runnable{
    String msg;
    Callme target;
    Thread currThread;

    public nonSCaller(Callme targ, String msg){
        target = targ;
        this.msg = msg;
        currThread = new Thread(this); //выделяем свой поток
        currThread.start();
    }
    public void run(){
        synchronized (target) { //зайдем в монитор объекта Callme, а только потом вызовем его метод, чтобы не дать другим nonSCaller зайти в него
            target.call(msg);
        }
    }
}

class syncCaller implements Runnable{ //caller использующий синхронизированный метод syncCall
    String msg;
    Callme target;
    Thread currThread;

    public syncCaller(Callme targ, String msg){
        target = targ;
        this.msg = msg;
        currThread = new Thread(this); //выделяем свой поток
        currThread.start();
    }
    public void run(){
        target.syncCall(msg);
    }
}

//классы для тестирования создания и запуска потоков
class ThreadA implements  Runnable{
    Thread currThread; //переменная для хранения указателя на поток, в котором исполняемся, дял проверок
    public void run(){
        //ThreadB B = new ThreadB();
        //B.run();
        for(int i = 0;i<3;i++){ //для наглядности ставим 10
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);

        }
    }
    void runMe(){
        currThread = new Thread(this,"threadA");
        currThread.start();
    }
}

class ThreadB implements  Runnable{
    Thread currThread; //переменная для хранения указателя на поток, в котором исполняемся, дял проверок
    public void run(){
        for(int i = 10;i<13;i++){//для наглядности ставим 20
            try {
                Thread.sleep(1001);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);

        }
    }
    void runMe(){
        currThread = new Thread(this,"threadB");
        currThread.start();
    }
}
class classThread extends Thread{
    classThread(){
        super("новый поток");
        System.out.println(this);
        start();
    }
    @Override
    public void run(){
        for(int i = 20;i<23;i++){//для наглядности ставим 30
            try {
                Thread.sleep(1005);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);

        }
    }
}
