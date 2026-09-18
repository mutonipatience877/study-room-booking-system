package com.studyroom.booking.bean;

import com.studyroom.booking.dao.StudyRoomDAO;
import com.studyroom.booking.model.StudyRoom;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/** Converts the h:selectOneMenu submitted id back into a managed StudyRoom entity. */
@FacesConverter("studyRoomConverter")
public class StudyRoomConverter implements Converter<StudyRoom> {

    private final StudyRoomDAO studyRoomDAO = new StudyRoomDAO();

    @Override
    public StudyRoom getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return studyRoomDAO.findById(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, StudyRoom studyRoom) {
        if (studyRoom == null || studyRoom.getId() == null) {
            return "";
        }
        return String.valueOf(studyRoom.getId());
    }
}
