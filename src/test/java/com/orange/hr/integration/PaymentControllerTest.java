package com.orange.hr.integration;

import com.orange.hr.payment.PayrollScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockitoSpyBean(types = PayrollScheduler.class)
public class PaymentControllerTest extends AbstractTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    PayrollScheduler payrollScheduler;

    @BeforeEach
    public void setup() {
        Mockito.reset(payrollScheduler);
    }

    @Test
    public void payUsingApi_ExpectAccepted() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/payment"));
        verify(payrollScheduler, times(1)).generatePayroll();
        result.andExpect(status().isAccepted());
    }

    @Test
    public void payUsingApi_GivenJobExists_ExpectForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/payment"));
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/payment"));
        verify(payrollScheduler, times(2)).generatePayroll();
        result.andExpect(status().isForbidden());
    }

    @Test
    public void payUsingApi_Given2DifferentMonths_ExpectAccepted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/payment"));
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/payment"));
        verify(payrollScheduler, times(2)).generatePayroll();
        result.andExpect(status().isForbidden());
    }
}
