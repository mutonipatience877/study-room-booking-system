package com.studyroom.booking.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.time.LocalTime;

/**
 * VALIDATION TYPE 3 (custom validator), applied to the "end time" field
 * of the booking form. It reaches across to the "start time" component
 * (bound through f:attribute "startTimeComponent" in bookings/form.xhtml)
 * to enforce the cross-field rule: end time must be after start time.
 * This kind of rule cannot be expressed with a plain Bean Validation
 * annotation on a single field, which is exactly why a custom validator
 * is used here.
 */
@FacesValidator("bookingTimeRangeValidator")
public class BookingTimeRangeValidator implements Validator<LocalTime> {

    @Override
    public void validate(FacesContext context, UIComponent component, LocalTime endTime) throws ValidatorException {
        if (endTime == null) {
            return;
        }
        UIComponent startComponent = (UIComponent) component.getAttributes().get("startTimeComponent");
        if (startComponent == null) {
            return;
        }
        UIInput startInput = (UIInput) startComponent;
        Object raw = startInput.getValue();
        if (!(raw instanceof LocalTime)) {
            raw = startInput.getSubmittedValue();
        }
        LocalTime startTime = null;
        if (raw instanceof LocalTime) {
            startTime = (LocalTime) raw;
        } else if (raw instanceof String && !((String) raw).isEmpty()) {
            startTime = LocalTime.parse((String) raw);
        }

        if (startTime != null && !endTime.isAfter(startTime)) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Invalid time range",
                    "End time must be after start time.");
            throw new ValidatorException(message);
        }
    }
}
