package com.example.demo.customer.cucumber;

import com.example.demo.customer.CustomerAgent;
import com.example.demo.customer.dtos.CustomerDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb"})
@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UpdateCustomerSteps {

    private final CustomerAgent customerAgent;

    private CustomerDto initialCustomer;

    @Given("un cliente existente con id {long} y nombre {word}")
    public void un_cliente_existente_con_id_y_nombre(long id, String name) throws Exception {
        initialCustomer = new CustomerDto(id, name);
        customerAgent.createCustomer(initialCustomer);
    }

    @When("cambiamos el nombre de este cliente por {word}")
    public void cambiamos_el_nombre_de_este_cliente_por(String newName) throws Exception {
        CustomerDto customerDto = new CustomerDto(initialCustomer.id(), newName);
        customerAgent.updateCustomer(customerDto).andExpect(status().isOk());
    }

    @Then("tras consultarlo otra vez, su nombre debe ser {word}")
    public void tras_consultarlo_nuevamente_su_nombre_debe_ser_John(String newName) throws Exception {
        initialCustomer = customerAgent.getCustomer(initialCustomer.id());
        assertEquals(newName, initialCustomer.name());
    }
}
