package com.orange.hr.integration;

import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.entity.Team;
import com.orange.hr.mapper.EmployeeMapper;
import com.orange.hr.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamControllerIntegrationTest extends AbstractTest {
    private static final int TEAM_ID = 1;
    private static final int INVALID_TEAM_ID = 99;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    public void getMemebersFromTeam_GivenValidTeamId_ShouldReturnOk() throws Exception {
        prepareDB("/datasets/TeamController/getMembersInTeam.xml");
        //arrange
        Team team = teamRepository.findById(TEAM_ID).get();
        List<Employee> members = team.getMembers();
        List<EmployeeResponseDTO> dtos = members.stream().map(employeeMapper::toDTO).toList();
        String expectedOutput = objectMapper.writeValueAsString(dtos);
        //act
        ResultActions result = mockMvc.perform(get("/team/" + TEAM_ID + "/members"));
        String actualOutput = result.andReturn().getResponse().getContentAsString();

        //assert
        JSONAssert.assertEquals(expectedOutput, actualOutput, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    public void getMemebersFromTeam_GivenInValidTeamId_ShouldReturnBadRequest() throws Exception {
        prepareDB("/datasets/TeamController/getMembersInTeam.xml");

        //act
        ResultActions result = mockMvc.perform(get("/team/" + INVALID_TEAM_ID + "/members"));
        //assert
        result
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find selected Team."));

    }
}
