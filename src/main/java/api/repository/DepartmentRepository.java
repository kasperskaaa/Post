package api.repository;

import api.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findDepartmentByAddress(String address);
    Optional<Department> findDepartmentByTitle(String title);
}
