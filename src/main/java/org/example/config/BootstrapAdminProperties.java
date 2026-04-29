package org.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.bootstrap")
public class BootstrapAdminProperties {
    private String adminFullName;
    private String adminEmail;
    private String adminPassword;
}
