package com.studyroom.booking.dao;

import com.studyroom.booking.model.Booking;
import com.studyroom.booking.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

/** Full CRUD DAO for Booking, the second entity chosen for this phase. */
public class BookingDAO {

    public List<Booking> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select b from Booking b join fetch b.student join fetch b.studyRoom order by b.bookingDate desc, b.startTime",
                    Booking.class).list();
        }
    }

    public Booking findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Booking.class, id);
        }
    }

    public void save(Booking booking) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(booking);
            session.getTransaction().commit();
        }
    }

    public void update(Booking booking) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(booking);
            session.getTransaction().commit();
        }
    }

    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Booking booking = session.get(Booking.class, id);
            if (booking != null) {
                session.delete(booking);
            }
            session.getTransaction().commit();
        }
    }
}
