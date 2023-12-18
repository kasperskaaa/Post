package api.service;

import api.dto.EmployeeDto;
import api.entity.Department;
import api.entity.Employee;
import api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository repository;

    public List<Employee> showFiredEmployees() {
        List<Employee> employees = repository.findAll();
        return employees.stream().filter(employee -> employee.getFired() != null).toList();
    }

    public List<Employee> showNotFiredEmployees() {
        List<Employee> employees = repository.findAll();
        return employees.stream().filter(employee -> employee.getFired() == null).collect(Collectors.toList());
    }

    public Optional<Employee> showById(int id) {
        return repository.findById(id);
    }

    public Optional<Employee> showByPhone(String phone) {
        return repository.findEmployeeByPhone(phone);
    }

    @Transactional
    public void save(EmployeeDto dto, Department work_department) {
        Employee employee = convertEmployeeDto(dto);
        employee.setHired(LocalDate.now());
        employee.setWork_department(work_department);
        repository.save(employee);
    }

    @Transactional
    public void fire(Employee employee) {
        employee.setFired(LocalDate.now());
        repository.save(employee);
    }

    @Transactional
    public void update(Employee employee) {
        repository.save(employee);
    }

    @Transactional
   public void delete(int id) {
        repository.deleteById(id);
    }

    private Employee convertEmployeeDto(EmployeeDto dto) {
        Employee employee = new Employee();

        employee.setEmail(dto.getEmail());
        employee.setName(dto.getName());
        employee.setSurname(dto.getSurname());
        employee.setPatronymic(dto.getPatronymic());
        employee.setPhone(dto.getPhone());
        employee.setExperience(dto.getExperience());

        return employee;
    }

}
