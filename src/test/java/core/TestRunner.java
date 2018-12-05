package core;

import core.controllers.Tiles.*;
import core.controllers.GamelogicTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({GamelogicTests.class, BeetleTests.class, GrasshopperTests.class, QueenTest.class, SoldierAntTest.class, SpiderTests.class})
public class TestRunner {

}
