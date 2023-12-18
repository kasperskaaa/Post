package api.controller;

import api.dto.IdDto;
import api.dto.OrderDto;
import api.entity.Order;
import api.service.OrderService;
import api.utils.exceptions.EntityNotFoundException;
import api.utils.validations.BindingResultParser;
import api.valueobject.Status;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:9091")
public class OrderController {

    private final OrderService service;

    @GetMapping
    public ResponseEntity<?> index() {
        return new ResponseEntity<>(service.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showById(@PathVariable int id) {
        Order order = checkOrder(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/by_sender/{sender_id}")
    public ResponseEntity<?> showOrdersBySender(@PathVariable int sender_id) {
        return new ResponseEntity<>(service.getOrdersBySender(sender_id), HttpStatus.OK);
    }

    @GetMapping("/by_recipient/{recipient_id}")
    public ResponseEntity<?> showOrdersByRecipient(@PathVariable int recipient_id) {
        return new ResponseEntity<>(service.getOrdersByRecipient(recipient_id), HttpStatus.OK);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> checkStatus(@PathVariable int id) {
        Order order = checkOrder(id);
        return new ResponseEntity<>(order.getStatus(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody OrderDto dto, BindingResult bindingResult) {
        checkValidation(bindingResult);

        service.create(dto);
        return new ResponseEntity<>("Order "  + dto.getTitle() + " was created", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/sent")
    public ResponseEntity<?> changeOnSent(@PathVariable int id, @RequestBody IdDto employee_id) {
        Order order = checkOrder(id);
        order.setStatus(Status.SENT);

        service.update(order, employee_id.getId());
        return new ResponseEntity<>("Parcel was " + order.getStatus(), HttpStatus.OK);
    }

    @PatchMapping("/{id}/came")
    public ResponseEntity<?> changeOnCame(@PathVariable int id, @RequestBody IdDto employee_id) {
        Order order = checkOrder(id);
        order.setStatus(Status.CAME);

        service.update(order, employee_id.getId());
        return new ResponseEntity<>("Parcel was " + order.getStatus(), HttpStatus.OK);
    }

    @PatchMapping("/{id}/received")
    public ResponseEntity<?> changeOnReceived(@PathVariable int id, @RequestBody IdDto employee_id) {
        Order order = checkOrder(id);
        order.setStatus(Status.RECEIVED);

        service.update(order, employee_id.getId());
        return new ResponseEntity<>("Parcel was " + order.getStatus(), HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancelled")
    public ResponseEntity<?> changeOnCancelled(@PathVariable int id) {
        Order order = checkOrder(id);
        order.setStatus(Status.CANCELLED);

        service.update(order);
        return new ResponseEntity<>("Order was " + order.getStatus(), HttpStatus.OK);
    }

    private Order checkOrder(int id) {
        Optional<Order> order = service.getOrderById(id);
        if (order.isEmpty()) {
            throw new EntityNotFoundException("Employee not found");
        }

        return order.get();
    }

    private void checkValidation (BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(BindingResultParser.parse(bindingResult));
        }
    }

}
