package org.peut.herdenk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "herdenk")
@Configuration("herdenkConfig")
public class HerdenkConfig {
    private String uploads;

    public String getUploads() {
        return uploads;
    }

    public void setUploads(String uploads) {
        this.uploads = uploads.trim() + ( uploads.endsWith("/")?"":"/");
    }
}
