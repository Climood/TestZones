package com.example.bostongenespringrestservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11); //сила (кол во хеширований) 11
    //Используем для хранения пароля в безопасном виде

    @RequestMapping(value = "/findByEmail", method = RequestMethod.GET)
    public User findByEmail(@RequestParam(value="email",required = true) String email){
        if(userRepository.findByEmail(email).isEmpty()){
            System.out.println("пользователя с таким имеил нету");
            return null;
        }
        return userRepository.findByEmail(email).get(0);
    }
    //http://localhost:8080/user/findByEmail?email=one@mail.net


    //Для многих методов и маппингов надо сделать два типа запросов GET и PUT(POST)/DELETE
    // Т.к. когда мы только переходим например на /add , нам выводит страницу полученную с GET запроса, а затем, мы можем
    //что то в ней заполнить и отправить результат уже через PUT(POST)/DELETE метод
    /*
    контроллер содержит два метода. Первый (addMan) отрабатывает при GET запросе и выводит созданную ранее страницу с формой.
    Второй же (addManPost) получает данные поступившие на сервер от формы и выполняет их обработку.
     */

    /*
    Для хранения паролей в безопасной форме мы их кодируем, а потом при сравнении раскодируем
     */
    @RequestMapping(value = "/add" , method = RequestMethod.GET)//PUT почему то не работал, требовало GET ^There was an unexpected error (type=Method Not Allowed, status=405).
    //Request method 'GET' not supported
    public void addUserGet(@RequestParam(value = "firstName", required = true) String firstName,
                        @RequestParam(value = "lastName", required = true) String lastName,
                        @RequestParam(value = "birthday", required = false, defaultValue = "01-01-2000") String birthday,
                        @RequestParam(value = "email" , required = true) String email,
                        @RequestParam(value = "password", required = true) String password){
        if(userRepository.findByEmail(email).size() != 0 ){
            System.out.println("Польхователь с таким email уже есть");
            return;
        }
        System.out.println("Создаем пользователя:"+firstName+" "+lastName+" "+birthday+" "+email+" "+password);
        System.out.println("Пароль в закодированном виде "+ passwordEncoder.encode(password));
        userRepository.save(new User(firstName,lastName,birthday,email,passwordEncoder.encode(password)));
    }
    //http://localhost:8080/user/add?firstName=adam&lastName=first&birthday=0/0/0&email=one@mail.net&password=111111
    @RequestMapping(value = "/add" , method = RequestMethod.PUT)//PUT почему то не работал, требовало GET ^There was an unexpected error (type=Method Not Allowed, status=405).
    //Request method 'GET' not supported
    public void addUserPost(@RequestParam(value = "firstName", required = true) String firstName,
                        @RequestParam(value = "lastName", required = true) String lastName,
                        @RequestParam(value = "birthday", required = false, defaultValue = "01-01-2000") String birthday,
                        @RequestParam(value = "email" , required = true) String email,
                        @RequestParam(value = "password", required = true) String password){
        if(userRepository.findByEmail(email).size() != 0 ){
            System.out.println("Польхователь с таким email уже есть");
            return;
        }
        System.out.println("Создаем пользователя:"+firstName+" "+lastName+" "+birthday+" "+email+" "+password);
        System.out.println("Пароль в закодированном виде "+ passwordEncoder.encode(password));
        userRepository.save(new User(firstName,lastName,birthday,email,passwordEncoder.encode(password)));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)//DELETE почему то не работал, требовало GET ^There was an unexpected error (type=Method Not Allowed, status=405).
    //Request method 'GET' not supported
    public void deleteUser(@RequestParam(value = "email",required = true) String email,
                           @RequestParam(value = "password",required = true) String password){
        if(userRepository.findByEmail(email).isEmpty()){
            System.out.println("пользователя с таким имеил нету");
            return;
        }
        User tmp = userRepository.findByEmail(email).get(0);
        Long userId = tmp.getId();
        if( passwordEncoder.matches(password,tmp.getPassword()) ){
            System.out.println("Пароль верный, удаляем");
            userRepository.deleteById(userId);
        }else{
            System.out.println("Пароль неверный!");
            return;
        }
    }
    //http://localhost:8080/user/delete?email=one@mail.net&password=111111
        //должен спрашивать пароль от пользователя, которого надо удалить

}
