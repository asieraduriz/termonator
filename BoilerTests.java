import utils.InvalidSecretException;
import utils.ItemNotFoundException;
import utils.ControllerPrx;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import static org.easymock.EasyMock.*;

public class BoilerTests {

  private Boiler boiler;
  private ControllerPrx proxy;

  @Before
  public void setUp() {
    boiler = new Boiler();
    proxy = createMock(ControllerPrx.class);
  }

  @Test
  public void test_turnOff_isOff() {
    assertEquals(false, boiler.turnOff());
  }

  @Test
  public void test_turnOn_isOff() {
    assertEquals(true, boiler.turnOn());
  }

  @Test
  public void test_turnOff_isOn() {
    assumeTrue(boiler.getStatus());
    assertEquals(true, boiler.turnOff());
  }

  @Test
  public void test_turnOn_isOn() {
    assumeTrue(boiler.getStatus());;
    assertEquals(false, boiler.turnOn());
  }

  @Test
  public void test_addController_OK() {
    replay(proxy);
    boolean ok = boiler.addController(5, "Left", proxy);
    assertEquals(true, ok);
  }

  @Test
  public void test_addController_NotOK() {
    replay(proxy);
    assumeTrue(boiler.addController(5, "Left", proxy));
    boolean ok = boiler.addController(5, "Left", proxy);
    assertEquals(false, ok);
  }

  @Test(expected= ItemNotFoundException.class)
  public void test_turnOffHeating_WrongController()
              throws InvalidSecretException, ItemNotFoundException {
    boiler.turnOnHeating("mysecret", 5, "Left");
  }

  @Test(expected= InvalidSecretException.class)
  @Ignore
  public void test_turnOffHeating_WrongSecret()
              throws InvalidSecretException, ItemNotFoundException {
    replay(proxy);
    assumeTrue(boiler.addController(5, "Left", proxy));
    //boiler.turnOnHeating(
  }

  @Test
  @Ignore
  public void test_turnOffHeating_AllOk_isOff()
              throws InvalidSecretException, ItemNotFoundException {
    replay(proxy);
    assumeTrue(boiler.addController(5, "Left", proxy));

  }
}
