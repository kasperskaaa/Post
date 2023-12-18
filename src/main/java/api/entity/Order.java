package api.entity;

import api.valueobject.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "parcel_price")
    private BigDecimal parcel_price;

    @Column(name = "delivery_price")
    private BigDecimal delivery_price;

    @Column(name = "total_price")
    private BigDecimal total_price;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "department_sender_id")
    private Department department_sender;

    @ManyToOne
    @JoinColumn(name = "department_recipient_id")
    private Department department_recipient;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "status")
    private Status status;
}
