package com.example.bostongenespringrestservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;

/*
https://github.com/spring-guides/gs-authenticating-ldap/
Эти зависимости добавляют Spring Security и ApacheDS, LDAP сервер с открытым исходным кодом.
 С этого момента вы можете использовать Java для настройки вашей политики безопасности
 */
@Configuration
//@EnableWebSecurity подключает необходимые бины для использования Spring Security. УЖЕ НЕ ИСПОЛЬЗУЕТСЯ
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .ldapAuthentication()
                        .userDnPatterns("uid={0},ou=people")
                        .groupSearchBase("ou=people")
                        .contextSource()
                                .url("ldap://localhost:8389/dc=springframework,dc=org")
                                .and()
                        .passwordCompare()
                                //.passwordEncoder(new LdapShaPasswordEncoder()) //SHA устарел и не рекомендуется
                                //используем Bcrypt
                                .passwordEncoder(new BCryptPasswordEncoder(9))
                                //но для него все пароли в конфиге должны быть зашифрованы в Bcrypt виде (я их переписал используя https://www.devglan.com/online-tools/bcrypt-hash-generator
                                .passwordAttribute("userPassword");
    }
}
/*
Вам также нужен LDAP сервер. Spring Security LDAP модуль подключает встроенный сервер,
написанный на чистой Java, которая используется в этом уроке.
 Метод ldapAuthentication() настроен так, что имя пользователя из формы входа помещается в {0},
 таким образом осуществляется поиск uid={0},ou=people,dc=springframework,dc=org на LDAP сервере.
 Also, the passwordCompare() method configures the encoder and the name of the password’s attribute
 */

/*
Установка пользовательских данных

LDAP servers can use LDIF (LDAP Data Interchange Format) files to exchange user data.
The spring.ldap.embedded.ldif property inside application.properties allow to Spring Boot pulls in an LDIF data file.
This makes it easy to pre-load demonstration data.
 Создадим такой файл в src/main/resources/test-server.ldif

 ВНИМАНИЕ Using an LDIF file isn’t standard configuration for a production system.
 However, it’s very useful for testing purposes or guides.



If you visit the site at http://localhost:8080, you should be redirected to a login page provided by Spring Security.

Enter username bob and password bobspassword. You should see this message in your browser:

Welcome to the home page!


 */