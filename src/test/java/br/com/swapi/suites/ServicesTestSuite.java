package br.com.swapi.suites;

import br.com.swapi.services.PlanetServiceImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PlanetServiceImplTest.class
})
public class ServicesTestSuite {
}
