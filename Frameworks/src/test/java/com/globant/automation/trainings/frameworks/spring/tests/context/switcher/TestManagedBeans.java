package com.globant.automation.trainings.frameworks.spring.tests.context.switcher;

import com.globant.automation.trainings.frameworks.spring.context.switcher.ComponentModeSwitcherMBean;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.IOException;

/**
 * Created by jkrzemien on 27/06/2016.
 */

@ContextConfiguration(classes = {Context.class})
@TestPropertySource("/application.properties")
public class TestManagedBeans extends AbstractJUnit4SpringContextTests {

    @Autowired
    private ComponentModeSwitcherMBean modeSwitcherMBean;

    @Test
    public void test1() throws IOException {
        modeSwitcherMBean.switchToSimulatorMode();
        modeSwitcherMBean.switchToNormalMode();
    }

}

@Configuration
class Context {

    @Bean
    public ComponentModeSwitcherMBean createComponentModeSwitcher() {
        return new ComponentModeSwitcherMBean();
    }

}