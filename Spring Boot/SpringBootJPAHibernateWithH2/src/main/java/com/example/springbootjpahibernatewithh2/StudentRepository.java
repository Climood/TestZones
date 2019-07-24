package com.example.springbootjpahibernatewithh2;
import com.example.springbootjpahibernatewithh2.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/*
public interface StudentRepository extends JpaRepository<Student, Long> -
We are extending JpaRepository using two generics - Student & Long.
Student is the entity that is being managed and the primary key of Student is Long.
 */
public interface StudentRepository extends JpaRepository<Student,Long> {

}
