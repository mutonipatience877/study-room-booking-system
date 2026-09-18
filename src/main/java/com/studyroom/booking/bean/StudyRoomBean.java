package com.studyroom.booking.bean;

import com.studyroom.booking.dao.StudyRoomDAO;
import com.studyroom.booking.model.RoomStatus;
import com.studyroom.booking.model.StudyRoom;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "studyRoomBean")
@SessionScoped
public class StudyRoomBean implements Serializable {

    private final StudyRoomDAO studyRoomDAO = new StudyRoomDAO();

    private List<StudyRoom> rooms;
    private StudyRoom selectedRoom = new StudyRoom();
    private boolean editMode;

    public List<StudyRoom> getRooms() {
        rooms = studyRoomDAO.findAll();
        return rooms;
    }

    public StudyRoom getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(StudyRoom selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public RoomStatus[] getStatuses() {
        return RoomStatus.values();
    }

    /** READ + prepare for CREATE. */
    public String prepareNew() {
        this.selectedRoom = new StudyRoom();
        this.selectedRoom.setStatus(RoomStatus.AVAILABLE);
        this.editMode = false;
        return "form?faces-redirect=true";
    }

    /** READ (single) + prepare for UPDATE. */
    public String prepareEdit(Long id) {
        this.selectedRoom = studyRoomDAO.findById(id);
        this.editMode = true;
        return "form?faces-redirect=true";
    }

    /** CREATE or UPDATE depending on editMode. */
    public String save() {
        if (editMode) {
            studyRoomDAO.update(selectedRoom);
            addMessage("Study room updated successfully.");
        } else {
            studyRoomDAO.save(selectedRoom);
            addMessage("Study room created successfully.");
        }
        return "list?faces-redirect=true";
    }

    /** DELETE. */
    public String delete(Long id) {
        studyRoomDAO.delete(id);
        addMessage("Study room deleted.");
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
