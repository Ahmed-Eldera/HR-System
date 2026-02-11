package com.orange.hr.integration;

import com.orange.hr.dto.LoginRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = true)
public class LoginTest extends AbstractTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void login_WithValidCredentials_ShouldReturnNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/DefaultDBState.xml");
        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .email("emp1@orange.com")
                .password("1234")
                .build();
        //act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

    }
}
