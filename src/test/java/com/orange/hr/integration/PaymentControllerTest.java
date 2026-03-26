package com.orange.hr.integration;

import com.orange.hr.payment.PayrollScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @Test
    public void payUsingApi_ExpectAccepted() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/payment").contentType(MediaType.APPLICATION_JSON));
        verify(payrollScheduler, times(2)).generatePayroll();
        result.andExpect(status().isAccepted());
    }

    @Test
    public void payUsingApi_GivenJobExists_ExpectForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/payment").contentType(MediaType.APPLICATION_JSON));
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/payment").contentType(MediaType.APPLICATION_JSON));
        verify(payrollScheduler, times(2)).generatePayroll();
        result.andExpect(status().isForbidden());
    }
}
