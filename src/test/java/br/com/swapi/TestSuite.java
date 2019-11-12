package br.com.swapi;

import br.com.swapi.suites.ServicesTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ServicesTestSuite.class
})
@ActiveProfiles("test")
public class TestSuite {

}
