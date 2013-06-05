import utils._BoilerDisp;
import utils.ControllerPrx;
import utils.InvalidSecretException;
import utils.ItemNotFoundException;
import Ice.Current;
import java.util.ArrayList;

public class Boiler extends _BoilerDisp {

  private boolean status;
  private ArrayList<Controller> controllerList;

  public Boiler() {
    status = false;
    controllerList = new ArrayList<Controller>();
  }

  public boolean addController(int floor, String door, ControllerPrx proxy,
                            Current __current) {
    Controller tmpController = new Controller(floor, door, proxy);
    if(controllerList.contains(tmpController) == false) {
      controllerList.add(tmpController);
      return true;
    } else {
      return false;
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
    for(Controller item: controllerList) {
      if(item.getFloor() == floor && item.getDoor() == door) {
        if(item.getProxy().heaterOff(secret) == false) {
          return false;
        } else {
          return true;
        }
      }
    }
    throw new ItemNotFoundException();
  }
  
  public boolean turnOnHeating(String secret, int floor, String door,
                               Current __current)
                               throws InvalidSecretException,
                                      ItemNotFoundException {
    for(Controller item: controllerList) {
      if(item.getFloor() == floor && item.getDoor() == door) {
        if(item.getProxy().heaterOn(secret) == false) {
          return false;
        } else {
          return true;
        }
      }
    }
    throw new ItemNotFoundException();
  }

  public void changeTemperature(String secret, int floor, String door,
                                double temperature, Current __current)
                                throws InvalidSecretException,
                                       ItemNotFoundException {
    for(Controller item: controllerList) {
      if(item.getFloor() == floor && item.getDoor() == door) {
        item.getProxy().setTemperature(secret, temperature);
        break;
      }
    }
    throw new ItemNotFoundException();
  }

  public boolean getHeatingStatus(String secret, int floor, String door,
                                  Current __current)
                                  throws InvalidSecretException,
                                         ItemNotFoundException {
    boolean item_found = false;
    for(Controller item: controllerList) {
      if(item.getFloor() == floor && item.getDoor() == door) {
        item_found = true;
        return item.getProxy().getStatus(secret);
      }
    }
    throw new ItemNotFoundException();
  }

  public double getHeatingTemperature(String secret, int floor, String door,
                                       Current __current)
                                       throws InvalidSecretException,
                                              ItemNotFoundException {
    boolean item_found = false;
    for(Controller item: controllerList) {
      if(item.getFloor() == floor && item.getDoor() == door) {
        item_found = true;
        return ietm.getProxy().getTemperature(secret);
      }
    }
    throw new ItemNotFoundException();
  }

  public double getHeatingConsumption(int floor, String door,
                                      Current __current)
                                      throws ItemNotFoundException {
    boolean item_found = false;
    for(Controller item: controllerList) {
      if(item.getFloor() == floor && item.getDoor() == door) {
        item_found = true;
        return item.getProxy().getConsumption();
      }
    }
    throw new ItemNotFoundException();
  }
}
