package com.orange.hr.service.impl;

import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Team;
import com.orange.hr.exceptions.NoSuchTeamException;
import com.orange.hr.mapper.EmployeeMapper;
import com.orange.hr.repository.TeamRepository;
import com.orange.hr.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeResponseDTO> getMembers(Integer teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new NoSuchTeamException(HttpStatus.NOT_FOUND, "Can't find selected Team."));
        List<Employee> members = team.getMembers();
        List<EmployeeResponseDTO> response = members.stream().map(employeeMapper::toDTO).toList();
        return response;
    }
}
