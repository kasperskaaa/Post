package api.service;

import api.dto.OrderDto;
import api.entity.Order;
import api.entity.User;
import api.repository.OrderRepository;
import api.utils.exceptions.EntityNotFoundException;
import api.valueobject.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository repository;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Optional<Order> getOrderById(int id) {
        return repository.findById(id);
    }

    public List<Order> getOrdersBySender(int sender_id) {
        User user = userService.showById(sender_id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return repository.findOrdersBySenderOrderByIdDesc(user);
    }

    public List<Order> getOrdersByRecipient(int recipient_id) {
        User user = userService.showById(recipient_id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return repository.findOrdersByRecipientOrderByIdDesc(user);
    }

    @Transactional
    public void create(OrderDto dto) {
        Order order = Order.builder()
                .title(dto.getTitle())
                .type(dto.getType())
                .parcel_price(dto.getParcel_price())
                .delivery_price(dto.getDelivery_price())
                .total_price(dto.getParcel_price().add(dto.getDelivery_price()))
                .sender(userService.showById(dto.getSender_id())
                        .orElseThrow(() -> new EntityNotFoundException("User (sender) not found")))
                .recipient(userService.showById(dto.getRecipient_id())
                        .orElseThrow(() -> new EntityNotFoundException("User (recipient) not found")))
                .department_sender(departmentService.showById(dto.getDepartment_sender_id())
                        .orElseThrow(() -> new EntityNotFoundException("Department (sender) not found")))
                .department_recipient(departmentService.showById(dto.getDepartment_recipient_id())
                        .orElseThrow(() -> new EntityNotFoundException("Department (recipient) not found")))
                .employee(employeeService.showById(dto.getEmployee_id())
                        .orElseThrow(() -> new EntityNotFoundException("Employee not found")))
                .status(Status.CREATED)
                .build();

        repository.save(order);
    }

    @Transactional
    public void update(Order order, int employee_id) {
        order.setEmployee(employeeService.showById(employee_id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found")));
        repository.save(order);
    }

    @Transactional
    public void update(Order order) {
        repository.save(order);
    }


}
