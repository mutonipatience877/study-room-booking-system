package com.studyroom.booking.dao;

import com.studyroom.booking.model.StudyRoom;
import com.studyroom.booking.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

/** Full CRUD DAO for StudyRoom, one of the two entities chosen for this phase. */
public class StudyRoomDAO {

    public List<StudyRoom> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from StudyRoom order by building, roomNumber", StudyRoom.class).list();
        }
    }

    public StudyRoom findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(StudyRoom.class, id);
        }
    }

    public void save(StudyRoom room) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(room);
            session.getTransaction().commit();
        }
    }

    public void update(StudyRoom room) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(room);
            session.getTransaction().commit();
        }
    }

    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            StudyRoom room = session.get(StudyRoom.class, id);
            if (room != null) {
                session.delete(room);
            }
            session.getTransaction().commit();
        }
    }

    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select count(r) from StudyRoom r", Long.class).uniqueResult();
        }
    }
}
