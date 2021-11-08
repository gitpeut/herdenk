package org.peut.herdenk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "herdenk")
@Configuration("herdenkConfig")
public class HerdenkConfig {
    private String uploads;
    private Long jwtexpiration;

    public Long getJwtexpiration() {
        return jwtexpiration;
    }

    public void setJwtexpiration(Long jwtexpiration) {
        this.jwtexpiration = jwtexpiration;
    }

    public String getUploads() {
        return uploads;
    }

    public void setUploads(String uploads) {
        this.uploads = uploads.trim() + ( uploads.endsWith("/")?"":"/");
    }


}
