package com.orange.hr.service.impl;

import com.orange.hr.dto.*;
import com.orange.hr.entity.*;
import com.orange.hr.exceptions.*;
import com.orange.hr.mapper.EmployeeMapper;
import com.orange.hr.repository.*;
import com.orange.hr.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Service
public class EmployeeServiceImpl implements EmployeeService {
    static final Double INSURANCE = 500d;
    static final Double TAX_RATIO = 0.15d;
    static final Double DEDCTION_PER_LEAVE = -500d;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ExpertiseRepository expertiseRepository;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private SalaryAdjustmentRepository salaryAdjustmentRepository;
    @Autowired
    private SalaryRepository salaryRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${senior.yoe}")
    private Integer seniorYOE;

    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employee) {
        // validating the input data
        if (employee.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new InValidDateException(HttpStatus.BAD_REQUEST, "Birth date can't be in the future");
        }

        Department dept = departmentRepository.findById(employee.getDepartmentId()).orElseThrow(() -> new NoSuchDepartmentException(HttpStatus.NOT_FOUND, "Can't find the Selected Department"));
        Team team = teamRepository.findById(employee.getTeamId()).orElseThrow(() -> new NoSuchTeamException(HttpStatus.NOT_FOUND, "Can't find the Selected Team"));

        Employee manager = null;
        if (employee.getManagerId() != null) {
            if (employee.getManagerId().isPresent()) {
                manager = employeeRepository.findById(employee.getManagerId().get()).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find the Selected Manager"));
            }
        }
        List<Expertise> expertises = expertiseRepository.findAllById(employee.getExpertise());
        if (expertises.size() != employee.getExpertise().size()) {
            throw new NoSuchExpertiseException(HttpStatus.NOT_FOUND, "Can't find the Selected Expertise");
        }
        //saving the employee
        Double newGrossSalary = employee.getSalary();
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee entity = employeeMapper.toEntity(employee, dept, team, manager, expertises);
        Salary newSalary = Salary.builder()
                .employee(entity)
                .gross(newGrossSalary)
                .percentage(0d)
                .build();
        entity.setSalaries(List.of(newSalary));
        employeeRepository.save(entity);
        return employeeMapper.toDTO(entity);
    }

    public EmployeeResponseDTO modifyEmployee(Integer id, EmployeeRequestDTO dto) {

        Employee entity = employeeRepository.findById(id).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Employee Not Found"));
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getGender() != null) {
            entity.setGender(dto.getGender());
        }

        if (dto.getSalary() != null) {
            Double newGrossSalary = dto.getSalary();
            Salary newSalary = Salary.builder()
                    .employee(entity)
                    .gross(newGrossSalary)
                    .percentage(0d)
                    .build();
            List<Salary> salaryhistory = entity.getSalaries();
            salaryhistory.add(newSalary);
            entity.setSalaries(salaryhistory);
        }

        if (dto.getExpertise() != null) {
            List<Expertise> expertises = expertiseRepository.findAllById(dto.getExpertise());
            if (expertises.size() != dto.getExpertise().size()) {
                throw new NoSuchExpertiseException(HttpStatus.NOT_FOUND, "Can't find the Selected Expertise");
            }
            entity.setExpertises(expertises);
        }

        if (dto.getGraduationDate() != null) {
            entity.setGraduationDate(dto.getGraduationDate());
        }

        if (dto.getDateOfBirth() != null) {
            entity.setDateOfBirth(dto.getDateOfBirth());
        }
        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(() -> new NoSuchDepartmentException(HttpStatus.NOT_FOUND, "Can't find the Selected Department"));
            entity.setDepartment(dept);
        }

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new NoSuchTeamException(HttpStatus.NOT_FOUND, "Can't find the Selected Team"));
            entity.setTeam(team);
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        if (dto.getManagerId() != null) {
            if (dto.getManagerId().isPresent()) {
                entity.setManager(employeeRepository.findById(dto.getManagerId().get()).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find the Selected Manager")));
            } else {
                entity.setManager(null);
            }
        }
        if (dto.getYoe() != null) {
            entity.setYoe(dto.getYoe());
        }

        employeeRepository.save(entity);
        return employeeMapper.toDTO(entity);
    }

    @Override
    public void deleteEmployeeAndReassignSubordinates(Integer id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find Such Employee"));
        if (employee.getManager() == null) {
            throw new MyException(HttpStatus.CONFLICT, "Can't delete a super manager");
        }
        Integer newManagerId = employee.getManager().getId();
        //reassign his subordinates to his manager before deleting him
        employeeRepository.reassignSubordinates(id, newManagerId);
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeResponseDTO getEmployee(Integer id) {
        Employee entity = employeeRepository.findById(id).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Employee Not Found"));
        EmployeeResponseDTO dto = employeeMapper.toDTO(entity);
        return dto;
    }

    @Override
    public SalaryDTO getSalary(Integer id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find the selected employee"));
        Double gross = salaryRepository.findCurrentSalaryByEmployee(employee).getGross();
        Double net = calculateNetSalary(gross);
        SalaryDTO salaryDTO = new SalaryDTO(gross, net);
        return salaryDTO;

    }

    @Override
    public List<EmployeeResponseDTO> getSubordinates(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find such employee.");
        }
        List<EmployeeHierarchyProjection> employees = employeeRepository.findSubordinatesRec(id);
        List<EmployeeResponseDTO> response = employeeMapper.projectionToDTO(employees);
        return response;
    }

    @Override
    public List<EmployeeResponseDTO> getDirectSubordinates(Integer managerId) {
        Employee manager = employeeRepository.findById(managerId).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find such Manager."));
        List<Employee> subs = employeeRepository.findByManager(manager);
        List<EmployeeResponseDTO> response = subs.stream().map(employeeMapper::toDTO).toList();
        return response;
    }

    @Override
    public LeaveResponseDTO addLeave(Integer employeeId, LeaveRequestDTO requestDTO) {
        if (requestDTO.getDate().getYear() != LocalDate.now().getYear()) {
            throw new InValidDateException(HttpStatus.BAD_REQUEST, "You can only record leaves in the current year.");
        }
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find selected employee."));
        Leave leave = Leave.builder()
                .employee(employee)
                .leaveDate(requestDTO.getDate())
                .build();
        leaveRepository.save(leave);

        return LeaveResponseDTO.builder()
                .id(leave.getLeaveID())
                .employeeId(employeeId)
                .date(leave.getLeaveDate())
                .createdAt(leave.getCreatedAt())
                .build();
    }

    @Override
    public BonusResponseDTO addBonus(Integer employeeId, BonusRequestDTO requestDTO) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "No Such Employee."));

        SalaryAdjustment bonus = SalaryAdjustment.builder()
                .amount(requestDTO.getAmount())
                .employee(employee)
                .build();
        salaryAdjustmentRepository.save(bonus);

        BonusResponseDTO response = BonusResponseDTO.builder()
                .id(bonus.getAdjustmentId())
                .employeeId(employeeId)
                .amount(bonus.getAmount())
                .createdAt(bonus.getCreatedAt().toLocalDate())
                .build();

        return response;
    }

    Double calculateNetSalary(Double gross) {
        return gross - gross * TAX_RATIO - INSURANCE;
    }

    @Override
    public SalaryDTO addRaise(Integer employeeId, RaiseRequestDTO request) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "No Such Employee."));
        Double raisePercentage = request.getRatio() / 100d;
        Salary lastSalary = salaryRepository.findFirstByEmployeeOrderByCreatedAtDesc(employee).orElseThrow(() -> new MyException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't find this employee's salary"));
        Double grossSalaryBeforeRaise = lastSalary.getGross();
        Double newGrossSalary = grossSalaryBeforeRaise + grossSalaryBeforeRaise * raisePercentage;
        Salary raise = Salary.builder()
                .employee(employee)
                .percentage(raisePercentage)
                .gross(newGrossSalary)
                .build();
        salaryRepository.save(raise);
        return SalaryDTO.builder()
                .gross(newGrossSalary)
                .net(calculateNetSalary(newGrossSalary))
                .build();
    }

    public Payment calculatePayment(Employee employee) {
        Salary salary = employee.getCurrentSalary();
        Double grossSalary = salary.getGross();
        List<SalaryAdjustment> adjustments = salaryAdjustmentRepository.findByEmployeeAndCreatedAtGreaterThanAndCreatedAtLessThanEqual(employee, LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        Double sumOfAdjustments = adjustments.stream().mapToDouble(SalaryAdjustment::getAmount).sum();
        Double netSalary = grossSalary - (grossSalary * TAX_RATIO + INSURANCE) + sumOfAdjustments;
        return Payment.builder()
                .amount(netSalary)
                .salary(salary)
                .build();
    }

    public List<SalaryAdjustment> calculateDeductions(Employee employee) {
        LocalDateTime startOfYear = LocalDateTime.now().withDayOfYear(1).withMinute(0).withHour(0);
        List<SalaryAdjustment> deductions = new ArrayList<>();
        Integer allowedLeavesCount = employee.getTotalYOE() < seniorYOE ? 21 : 30;
        int previousDeductions = salaryAdjustmentRepository.countByEmployeeAndAmountLessThanAndCreatedAtGreaterThanEqual(employee, 0d, startOfYear);
        int claimedLeaves = leaveRepository.countByEmployeeAndLeaveDateLessThanAndCreatedAtGreaterThanEqual(employee, LocalDate.now(), startOfYear);
        // if leaves count equals the allowed leaves and old deductions
        // that means that the exceeded leaves are already deducted in a previous month
        // and I don't need to deduct again
        // else that means that he has some exceeded leaves that need a deduction
        if (claimedLeaves > allowedLeavesCount + previousDeductions) {
            int exceededLeaves = claimedLeaves - allowedLeavesCount - previousDeductions;
            for (long i = 0; i < exceededLeaves; i++) {
                deductions.add(SalaryAdjustment.builder().employee(employee).amount(DEDCTION_PER_LEAVE).build());
            }
        }
        return deductions;
    }
}