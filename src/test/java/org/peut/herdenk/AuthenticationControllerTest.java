package org.peut.herdenk;

import org.junit.jupiter.api.Test;
import org.peut.herdenk.controller.AuthenticationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthenticationControllerTest {

    @Autowired
    private AuthenticationController authenticationController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(authenticationController).isNotNull();
    }
}
