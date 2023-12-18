package api.dto;

import api.utils.validations.CreateValidation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DepartmentDto {

    @NotNull(message = "Title cannot be empty", groups = CreateValidation.class)
    private String title;

    @NotNull(message = "Address cannot be empty", groups = CreateValidation.class)
    private String address;

}
