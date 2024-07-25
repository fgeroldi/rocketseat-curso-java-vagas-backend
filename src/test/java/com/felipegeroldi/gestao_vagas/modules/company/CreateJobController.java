package com.felipegeroldi.gestao_vagas.modules.company;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.felipegeroldi.gestao_vagas.modules.company.dto.CreateJobDTO;
import com.felipegeroldi.gestao_vagas.modules.company.entities.CompanyEntity;
import com.felipegeroldi.gestao_vagas.modules.company.repositories.CompanyRepository;
import com.felipegeroldi.gestao_vagas.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateJobController {
    private MockMvc mvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    public void ShouldBeAbleToCreateANewJob() throws Exception {
        var company = CompanyEntity.builder()
            .description("COMPANY_DESCRIPTION")
            .email("company@company.com")
            .password("1234567890123456")
            .username("COMPANY_USERNAME")
            .name("COMPANY_NAME")
            .build();

        companyRepository.saveAndFlush(company);

        var jobDTO = CreateJobDTO.builder()
            .benefits("BENEFITS_TEST")
            .description("DESCRIPTION_TEST")
            .level("LEVEL_TEST")
            .build();

        String token = TestUtils.generateToken(company.getId(), "test_secret_vagascomp123@$#!");

        mvc.perform(
            MockMvcRequestBuilders.post("/company/job")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJSON(jobDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void ShouldNotBeAbleToCreateANewJobWithoutCompany() throws Exception {
        var jobDTO = CreateJobDTO.builder()
            .benefits("BENEFITS_TEST")
            .description("DESCRIPTION_TEST")
            .level("LEVEL_TEST")
            .build();

        String token = TestUtils.generateToken(UUID.randomUUID(), "test_secret_vagascomp123@$#!");

        mvc.perform(
            MockMvcRequestBuilders.post("/company/job")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJSON(jobDTO)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
