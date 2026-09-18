package com.studyroom.booking.bean;

import com.studyroom.booking.dao.BookingDAO;
import com.studyroom.booking.dao.StudentDAO;
import com.studyroom.booking.dao.StudyRoomDAO;
import com.studyroom.booking.model.Booking;
import com.studyroom.booking.model.BookingStatus;
import com.studyroom.booking.model.Student;
import com.studyroom.booking.model.StudyRoom;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@ManagedBean(name = "bookingBean")
@SessionScoped
public class BookingBean implements Serializable {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final StudyRoomDAO studyRoomDAO = new StudyRoomDAO();

    private List<Booking> bookings;
    private Booking selectedBooking = new Booking();
    private boolean editMode;

    public List<Booking> getBookings() {
        bookings = bookingDAO.findAll();
        return bookings;
    }

    public List<Student> getStudents() {
        return studentDAO.findAll();
    }

    public List<StudyRoom> getStudyRooms() {
        return studyRoomDAO.findAll();
    }

    public Booking getSelectedBooking() {
        return selectedBooking;
    }

    public void setSelectedBooking(Booking selectedBooking) {
        this.selectedBooking = selectedBooking;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public BookingStatus[] getStatuses() {
        return BookingStatus.values();
    }

    /** READ + prepare for CREATE. */
    public String prepareNew() {
        this.selectedBooking = new Booking();
        this.selectedBooking.setBookingDate(LocalDate.now());
        this.selectedBooking.setStatus(BookingStatus.PENDING);
        this.editMode = false;
        return "form?faces-redirect=true";
    }

    /** READ (single) + prepare for UPDATE. */
    public String prepareEdit(Long id) {
        this.selectedBooking = bookingDAO.findById(id);
        this.editMode = true;
        return "form?faces-redirect=true";
    }

    /** CREATE or UPDATE depending on editMode. */
    public String save() {
        if (editMode) {
            bookingDAO.update(selectedBooking);
            addMessage("Booking updated successfully.");
        } else {
            bookingDAO.save(selectedBooking);
            addMessage("Booking created successfully.");
        }
        return "list?faces-redirect=true";
    }

    /** DELETE. */
    public String delete(Long id) {
        bookingDAO.delete(id);
        addMessage("Booking deleted.");
        return "list?faces-redirect=true";
    }

    public String cancel() {
        return "list?faces-redirect=true";
    }

    private void addMessage(String text) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, text, null));
    }
}
