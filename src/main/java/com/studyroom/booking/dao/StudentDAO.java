package com.studyroom.booking.dao;

import com.studyroom.booking.model.Student;
import com.studyroom.booking.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

/**
 * Read-only DAO: Student is not one of the two CRUD entities for this
 * phase, it only needs to be listed for the Booking form's dropdown.
 */
public class StudentDAO {

    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Student order by fullName", Student.class).list();
        }
    }

    public Student findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        }
    }

    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select count(s) from Student s", Long.class).uniqueResult();
        }
    }

    public void save(Student student) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(student);
            session.getTransaction().commit();
        }
    }
}
