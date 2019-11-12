package br.com.swapi.suites;

import br.com.swapi.services.PlanetServiceImplIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PlanetServiceImplIntegrationTest.class
})
public class ServicesTestSuite {
}
