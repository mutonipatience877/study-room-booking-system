package com.studyroom.booking.listener;

import com.studyroom.booking.dao.StudentDAO;
import com.studyroom.booking.dao.StudyRoomDAO;
import com.studyroom.booking.model.RoomStatus;
import com.studyroom.booking.model.Student;
import com.studyroom.booking.model.StudyRoom;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Seeds a handful of students and study rooms on first startup so the
 * Booking CRUD screens have something to select from immediately.
 */
@WebListener
public class DataSeedListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        StudentDAO studentDAO = new StudentDAO();
        if (studentDAO.count() == 0) {
            studentDAO.save(new Student("Alice Uwase", "alice.uwase@example.com", "23001", "0788000001"));
            studentDAO.save(new Student("Brian Niyonzima", "brian.niyonzima@example.com", "23002", "0788000002"));
            studentDAO.save(new Student("Clarisse Mutesi", "clarisse.mutesi@example.com", "23003", "0788000003"));
        }

        StudyRoomDAO studyRoomDAO = new StudyRoomDAO();
        if (studyRoomDAO.count() == 0) {
            studyRoomDAO.save(new StudyRoom("A101", "Main Library", 1, 4, true, true, RoomStatus.AVAILABLE));
            studyRoomDAO.save(new StudyRoom("A102", "Main Library", 1, 6, false, true, RoomStatus.AVAILABLE));
            studyRoomDAO.save(new StudyRoom("B201", "Science Block", 2, 10, true, true, RoomStatus.UNDER_MAINTENANCE));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        com.studyroom.booking.util.HibernateUtil.shutdown();
    }
}
