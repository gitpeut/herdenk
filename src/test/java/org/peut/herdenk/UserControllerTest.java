package org.peut.herdenk;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.peut.herdenk.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

    @SpringBootTest
    public class UserControllerTest{

        @Autowired
        private UserController userController;

        @Test
        public void contextLoads() throws Exception {
            assertThat(userController).isNotNull();
        }
    }

