package api.controller;

import api.dto.DepartmentDto;
import api.entity.Department;
import api.service.DepartmentService;
import api.utils.exceptions.AddressAlreadyUsedException;
import api.utils.exceptions.TitleAlreadyUsedException;
import api.utils.validations.BindingResultParser;
import api.utils.validations.CreateValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:9091")
public class DepartmentController {

    private final DepartmentService service;

    @GetMapping
    public ResponseEntity<?> index() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInfo(@PathVariable int id) {

        Department department = checkDepartment(id);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> add(@Validated(CreateValidation.class) @RequestBody DepartmentDto dto, BindingResult bindingResult) {
        checkValidation(bindingResult);
        checkDepartmentUniq(dto);

        service.create(dto);
        return new ResponseEntity<>("User " + dto.getTitle() + " was opened",HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> close(@PathVariable int id) {
        Department department = checkDepartment(id);
        service.delete(id);
        return new ResponseEntity<>("Department " + department.getTitle() + " was closed", HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid DepartmentDto dto, BindingResult bindingResult) {
        checkValidation(bindingResult);
        Department department = checkDepartment(id);
        checkDepartmentUniq(dto);

        department.setAddress(dto.getAddress() == null ? department.getAddress() : dto.getAddress());
        department.setTitle(dto.getTitle() == null ? department.getTitle() : dto.getTitle());
        department.setId(id);

        service.update(department);
        return new ResponseEntity<>("Department " + dto.getTitle() + " was updated", HttpStatus.OK);
    }

    private Department checkDepartment(int id) {
        Optional<Department> department = service.showById(id);
        if (department.isEmpty()) {
            throw new EntityNotFoundException("Department not found");
        }

        return department.get();
    }

    private void checkDepartmentUniq(DepartmentDto dto) {
        Optional<Department> departmentByTitle = service.showByTitle(dto.getTitle());
        if (departmentByTitle.isPresent()){
            throw new TitleAlreadyUsedException();
        }

        Optional<Department> departmentByAddress = service.showByAddress(dto.getAddress());
        if (departmentByAddress.isPresent()){
            throw new AddressAlreadyUsedException();
        }
    }

    private void checkValidation (BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(BindingResultParser.parse(bindingResult));
        }
    }


}
