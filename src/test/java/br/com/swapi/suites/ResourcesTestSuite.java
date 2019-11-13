package br.com.swapi.suites;

import br.com.swapi.controllers.PlanetControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PlanetControllerTest.class
})
public class ResourcesTestSuite {
}
