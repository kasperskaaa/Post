package api.controller;

import api.dto.EmployeeDto;
import api.entity.Department;
import api.entity.Employee;
import api.service.DepartmentService;
import api.service.EmployeeService;
import api.utils.exceptions.PhoneAlreadyUseException;
import api.utils.validations.BindingResultParser;
import api.utils.validations.CreateValidation;
import api.utils.validations.UpdateValidation;
import api.utils.exceptions.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:9091")
public class EmployeeController {

    private final EmployeeService service;
    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<?> index() {
        return new ResponseEntity<>(service.showNotFiredEmployees(), HttpStatus.OK);
    }

    @GetMapping("/fired")
    public ResponseEntity<?> firedEmployees() {
        return new ResponseEntity<>(service.showFiredEmployees(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable int id) {
        Employee employee = checkEmployee(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> hire(@Validated(CreateValidation.class) @RequestBody EmployeeDto dto, BindingResult bindingResult) {
        checkValidation(bindingResult);
        checkPhone(dto);
        Optional<Department> department = departmentService.showById(dto.getWork_department_id());
        if (department.isEmpty()) {
            throw new EntityNotFoundException("Department not found");
        }

        service.save(dto, department.get());
        return new ResponseEntity<>("Employee " + dto.getSurname() + " " + dto.getName() + " was hired",HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/fire")
    public ResponseEntity<?> fire(@PathVariable int id) {
        Employee employee = checkEmployee(id);
        service.fire(employee);
        return new ResponseEntity<>("Employee " + employee.getSurname() + " " + employee.getName() + " was fired", HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Validated(UpdateValidation.class) @RequestBody EmployeeDto dto,
                                    BindingResult bindingResult) {
        Employee pachedEmployee = checkEmployee(id);
        checkValidation(bindingResult);
        checkPhone(dto.getPhone());

        pachedEmployee.setEmail(dto.getEmail() == null ? pachedEmployee.getEmail() : dto.getEmail());
        pachedEmployee.setName(dto.getName() == null ? pachedEmployee.getName() : dto.getName());
        pachedEmployee.setSurname(dto.getSurname() == null ? pachedEmployee.getSurname() : dto.getSurname());
        pachedEmployee.setPatronymic(dto.getPatronymic() == null ? pachedEmployee.getPatronymic() : dto.getPatronymic());
        pachedEmployee.setPhone(dto.getPhone() == null ? pachedEmployee.getPhone() : dto.getPhone());
        pachedEmployee.setExperience(dto.getExperience() == 0 ? pachedEmployee.getExperience() : dto.getExperience());

        service.update(pachedEmployee);
        return new ResponseEntity<>("Employee " + pachedEmployee.getSurname() + " " + pachedEmployee.getName() + " was updated", HttpStatus.OK);
    }

    private Employee checkEmployee(int id) {
        Optional<Employee> employee = service.showById(id);
        if (employee.isEmpty()) {
            throw new EntityNotFoundException("Employee not found");
        }

        return employee.get();
    }

    private void checkValidation (BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(BindingResultParser.parse(bindingResult));
        }
    }

    private void checkPhone(String phone) {
        if (service.showByPhone(phone).isPresent()) {
            throw new PhoneAlreadyUseException();
        }
    }

    private void checkPhone(EmployeeDto dto) {
        Optional<Employee> employee = service.showByPhone(dto.getPhone());
        if (employee.isPresent()) {

            if (employee.get().getFired() != null) {
              service.delete(employee.get().getId());
            } else {
                throw new PhoneAlreadyUseException();
            }

        }
    }


}
