package com.example.demo.customer.e2e;

import com.example.demo.e2e.WebDriverFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("scenario-base")
public class ViewOpenApiE2ET {

    @Test
    void testHappyCase() throws Exception {
        var factory = new WebDriverFactory();
        var webDriver = factory.createChromeUser("admin-selenium");
        var admin = new VirtualUser("admin", "admin", webDriver);

        admin.goToDemo("/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#");
        admin.waitSeconds(30);
    }
}
