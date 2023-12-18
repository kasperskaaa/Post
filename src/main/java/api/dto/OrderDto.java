package api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDto {

    @NotNull(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "Type cannot be empty")
    private String type;

    @NotNull(message = "Price of parcel cannot be empty")
    private BigDecimal parcel_price;

    @NotNull(message = "Price of delivery cannot be empty")
    private BigDecimal delivery_price;

    @NotNull(message = "Sender cannot be empty")
    private int sender_id;

    @NotNull(message = "Recipient cannot be empty")
    private int recipient_id;

    @NotNull(message = "Sender department cannot be empty")
    private int department_sender_id;

    @NotNull(message = "Recipient department cannot be empty")
    private int department_recipient_id;

    @NotNull(message = "Employee cannot be empty")
    private int employee_id;
}
