import utils._BoilerDisp;
import utils.InvalidSecretException;
import utils.ItemNotFoundException;
import utils.ControllerPrx;
import Ice.Current;
import java.util.ArrayList;

public class Boiler extends _BoilerDisp {

  private boolean status;
  private ArrayList<Controller> controllerList;

  public Boiler() {
    status = false;
    controllerList = new ArrayList<Controller>();
  }

  protected ControllerPrx findProxy(int floor, String door)
                                    throws ItemNotFoundException {
    for(Controller item: controllerList) {
      if(item.getFloor() == floor && item.getDoor().equals(door)) {
        return item.getProxy();
      }
    }
    throw new ItemNotFoundException();
  }

  public boolean addController(int floor, String door, ControllerPrx proxy,
                            Current __current) {
    Controller tmpController = new Controller(floor, door, proxy);
    try {
      findProxy(floor, door);
      return false;
    } catch(ItemNotFoundException e) {
      controllerList.add(tmpController);
      return true;
    }
  }

  public boolean turnOff(Current __current) {
    if(status == false) {
      return false;
    } else {
      status = false;
      return true;
    }
  }

  public boolean turnOn(Current __current) {
    if(status == true) {
      return false;
    } else {
      status = true;
      return true;
    }
  }

  public boolean getStatus(Current __current) {
    return status;
  }

  public boolean turnOffHeating(String secret, int floor, String door,
                                Current __current)
                                throws InvalidSecretException,
                                       ItemNotFoundException {
    if(findProxy(floor, door).heaterOff(secret) == false) {
      return false;
    } else {
      return true;
    }
  }
  
  public boolean turnOnHeating(String secret, int floor, String door,
                               Current __current)
                               throws InvalidSecretException,
                                      ItemNotFoundException {
    if(findProxy(floor, door).heaterOn(secret) == false) {
      return false;
    } else {
      return true;
    }
  }

  public void changeTemperature(String secret, int floor, String door,
                                double temperature, Current __current)
                                throws InvalidSecretException,
                                       ItemNotFoundException {
    findProxy(floor, door).setTemperature(secret, temperature);
  }

  public boolean getHeatingStatus(String secret, int floor, String door,
                                  Current __current)
                                  throws InvalidSecretException,
                                         ItemNotFoundException {
    return findProxy(floor, door).getStatus(secret);
  }

  public double getHeatingTemperature(String secret, int floor, String door,
                                       Current __current)
                                       throws InvalidSecretException,
                                              ItemNotFoundException {
    return findProxy(floor, door).getTemperature(secret);
  }

  public double getHeatingConsumption(int floor, String door,
                                      Current __current)
                                      throws ItemNotFoundException {
    return findProxy(floor, door).getConsumption();
  }
}
