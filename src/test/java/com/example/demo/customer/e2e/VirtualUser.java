package com.example.demo.customer.e2e;

import com.example.demo.e2e.TestConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
@RequiredArgsConstructor
public class VirtualUser {

    private static final long DEFAULT_TIMEOUT = 60L;

    @Getter
    private final String username;
    private final String alias;
    private final WebDriver driver;

    public VirtualUser goToDemo(String path) {
        log.debug("{}: Abriendo la aplicacion...", alias);
        var url = TestConfiguration.E2E.getServiceUrl("demo-spring-boot").concat(path);
        driver.get(url);
        log.debug("{}: La aplicación esta abierta", alias);
        return this;
    }

    public synchronized VirtualUser waitSeconds(long time) throws InterruptedException {
        wait(time * 1000);
        return this;
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }
}
