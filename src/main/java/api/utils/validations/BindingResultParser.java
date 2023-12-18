package api.utils.validations;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BindingResultParser {

    public static String parse(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()){
            errorMsg.append(error.getDefaultMessage())
                    .append(". ");
        }

        return errorMsg.toString();
    }
}
