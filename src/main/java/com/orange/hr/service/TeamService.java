package com.orange.hr.service;

import com.orange.hr.dto.EmployeeResponseDTO;

import java.util.List;

public interface TeamService {
    List<EmployeeResponseDTO> getMembers(Integer teamId);
}
