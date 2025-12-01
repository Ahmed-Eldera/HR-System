package com.orange.hr.controller;


import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeeInTeam(@PathVariable("teamId") Integer teamId) {
        List<EmployeeResponseDTO> members = teamService.getMembers(teamId);
        return new ResponseEntity<>(members, HttpStatus.OK);

    }
}
