package com.orange.hr.unit;

import com.orange.hr.entity.Department;
import com.orange.hr.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamUnitTest {
    @Autowired
    TeamRepository teamRepository;
    @Test
    public void CreateTeam_WithName_ShouldReturnSavedTeam(){
        Team team = new Team(null,"Team 1");
        assertNull(team.GetTeamId());
        teamRepository.save(team);
        assertNotNull(team.getTeamId());
        assertEquals(team.getName(),"Team 1");
    }
}
