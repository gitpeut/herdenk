package org.peut.herdenk;

import org.junit.jupiter.api.Test;
import org.peut.herdenk.controller.GraveController;
import org.peut.herdenk.controller.MediaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MediaControllerTest {
    @Autowired
    private MediaController mediaController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(mediaController).isNotNull();
    }
}
