package com.orange.hr.unit;


import com.orange.hr.entity.Expertise;
import com.orange.hr.repository.ExpertiseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExpertiseUnitTest {
    @Autowired
    ExpertiseRepository expertiseRepository;

    @Test
    public void CreateExpertise_ShouldReturnSavedExpertise() {
        Expertise expertise = new Expertise(null, "java", null);
        expertiseRepository.save(expertise);
        assertNotNull(expertise.getExpertiseId());
    }

    @Test
    public void CreateExpertise_WithNoName_ShouldThrowException() {
        Expertise expertise = new Expertise(null, null, null);

        assertThrows(Exception.class,()-> expertiseRepository.save(expertise));
    }
}
