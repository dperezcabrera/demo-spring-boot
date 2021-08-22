package com.example.demo.customer.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;

@Tag("acceptance")
@CucumberOptions(
        features = "test/features/customers/update-customer/update-customer.feature",
        plugin = {"pretty", "html:target/cucumber/customers/update-customer.html"})
@RunWith(Cucumber.class)
public class UpdateCustomerIT {

}
