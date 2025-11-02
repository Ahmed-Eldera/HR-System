package com.orange.hr.unit;


import com.orange.hr.entity.Team;
import com.orange.hr.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamUnitTest {
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void CreateTeam_WithName_ShouldReturnSavedTeam() {
        String teamName = "Team 1";
        Team team = new Team(null, teamName);
        assertNull(team.getTeamId());
        teamRepository.save(team);
        assertNotNull(team.getTeamId());
        assertEquals(team.getName(), teamName);
    }

    @Test
    public void CreateTeam_WithExistingName_ShouldThrowException() {
        String teamName = "Team 1";
        Team team1 = new Team(null, teamName);
        Team team11 = new Team(null, teamName);
        teamRepository.save(team1);
        assertThrows(Exception.class, () -> teamRepository.save(team11));
    }
}
