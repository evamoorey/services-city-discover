package org.user_service.utill;

import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.user_service.dto.wrapper.ErrorsMap;

@UtilityClass
public class Converter {

    public static ErrorsMap getErrorsMap(BindingResult result) {
        ErrorsMap errors = new ErrorsMap();
        result.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
