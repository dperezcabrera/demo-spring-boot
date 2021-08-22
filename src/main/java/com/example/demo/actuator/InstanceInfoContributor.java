package com.example.demo.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstanceInfoContributor implements InfoContributor {
 
    private final Environment env;
    
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("instance", env.getProperty("INSTANCE"));
    }
}
