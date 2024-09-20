package url.ingsoftware.rrhh.RRHH_Backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import url.ingsoftware.rrhh.RRHH_Backend.exception.ResourceNotFoundException;
import url.ingsoftware.rrhh.RRHH_Backend.model.Employee;
import url.ingsoftware.rrhh.RRHH_Backend.model.Department;
import url.ingsoftware.rrhh.RRHH_Backend.model.Roles;
import url.ingsoftware.rrhh.RRHH_Backend.model.Schedules;
import url.ingsoftware.rrhh.RRHH_Backend.repository.EmployeeRepository;
import url.ingsoftware.rrhh.RRHH_Backend.repository.DepartmentRepository;
import url.ingsoftware.rrhh.RRHH_Backend.repository.RolesRepository;
import url.ingsoftware.rrhh.RRHH_Backend.repository.ScheduleRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    // get all employees
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // create employee rest api
    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // get employee by id rest api
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));
        return ResponseEntity.ok(employee);
    }

    // update employee rest api
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

        employee.setEmpName(employeeDetails.getEmpName());
        employee.setEmpLastName(employeeDetails.getEmpLastName());
        employee.setPhoneNumber(employeeDetails.getPhoneNumber());
        employee.setEmailAddress(employeeDetails.getEmailAddress());

        // Update Department
        if (employeeDetails.getDepartment() != null) {
            Department department = departmentRepository.findById(employeeDetails.getDepartment().getDeptId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            employee.setDepartment(department);
        }

        // Update Role
        if (employeeDetails.getRoles() != null) {
            Roles roles = rolesRepository.findById(employeeDetails.getRoles().getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            employee.setRoles(roles);
        }

        // Update Schedule
        if (employeeDetails.getSchedule() != null) {
            Schedules schedule = scheduleRepository.findById(employeeDetails.getSchedule().getScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
            employee.setSchedule(schedule);
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    // delete employee rest api
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}