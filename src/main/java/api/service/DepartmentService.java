package api.service;

import api.dto.DepartmentDto;
import api.entity.Department;
import api.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository repository;

    public List<Department> getAll() {
        return repository.findAll();
    }

    public Optional<Department> showById(int id) {
        return repository.findById(id);
    }

    public Optional<Department> showByAddress(String address) {
        return repository.findDepartmentByAddress(address);
    }

    public Optional<Department> showByTitle(String title) {
        return repository.findDepartmentByTitle(title);
    }

    @Transactional
    public void create(DepartmentDto dto) {
        Department department = convertDepartmentDto(dto);
        repository.save(department);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Transactional
    public void update(Department department) {
        repository.save(department);
    }


    private static Department convertDepartmentDto(DepartmentDto departmentDto) {
        Department department = new Department();

        department.setTitle(departmentDto.getTitle());
        department.setAddress(departmentDto.getAddress());

        return department;
    }

}
