package com.studyroom.booking.bean;

import com.studyroom.booking.dao.StudentDAO;
import com.studyroom.booking.model.Student;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/** Converts the h:selectOneMenu submitted id back into a managed Student entity. */
@FacesConverter("studentConverter")
public class StudentConverter implements Converter<Student> {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    public Student getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return studentDAO.findById(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Student student) {
        if (student == null || student.getId() == null) {
            return "";
        }
        return String.valueOf(student.getId());
    }
}
