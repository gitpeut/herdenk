package org.peut.herdenk;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.peut.herdenk.controller.GraveController;
import org.peut.herdenk.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GraveControllerTest{

    @Autowired
    private GraveController graveController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(graveController).isNotNull();
    }
}
