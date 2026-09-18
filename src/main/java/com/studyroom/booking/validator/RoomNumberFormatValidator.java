package com.studyroom.booking.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * VALIDATION TYPE 3 (custom validator): business-rule validation that
 * neither a plain "required" attribute nor a Bean Validation annotation
 * expresses cleanly - a room number must look like "A101" (1-3 uppercase
 * letters identifying the wing, followed by 2-4 digits).
 *
 * Registered on the field with validatorId="roomNumberFormatValidator"
 * (see rooms/form.xhtml).
 */
@FacesValidator("roomNumberFormatValidator")
public class RoomNumberFormatValidator implements Validator<String> {

    private static final String PATTERN = "^[A-Z]{1,3}[0-9]{2,4}$";

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        if (value == null || value.trim().isEmpty()) {
            return; // let the "required" validation report empty values
        }
        if (!value.trim().toUpperCase().matches(PATTERN)) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Invalid room number format",
                    "Room number must look like a wing code followed by digits, e.g. A101 or LIB204.");
            throw new ValidatorException(message);
        }
    }
}
