package org.city_discover.utill;

import lombok.experimental.UtilityClass;
import org.city_discover.dto.wrapper.ErrorsMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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
