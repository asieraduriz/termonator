import utils._BoilerDisp;
import utils.FailureIceException;
import utils.InvalidSecretException;
import utils.ItemNotFoundException;
import utils.ControllerPrx;
import utils.DataBasePrx;
import Ice.Current;
import java.util.ArrayList;

/**
  @brief Clase que implementa el dispositivo controlador de la caldera
*/
public class Boiler extends _BoilerDisp {

  private boolean status; ///< Status of the boiler (true = ON, false = OFF)
  private ArrayList<Controller> controllerList; ///< List of home controllers
  private DataBasePrx incidentServer;

  /**
    @brief Constructor for the Boiler class
  */
  public Boiler() {
    status = false;
    controllerList = new ArrayList<Controller>();
    incidentServer = null;
  }

  /**
    @brief Sets the server to store the incidents
    @param iServer The servers proxy
  */
  public void setIncidentServer(DataBasePrx iServer) {
    incidentServer = iServer;
  }

  /**
    @brief Find the proxy of a home controller given it's floor and door
    @param floor The floor number of the controller
    @param door The door (left, right, A, B, C, ...) of the controller
    @return The proxy of the home controller
  */
  protected ControllerPrx findProxy(int floor, String door)
                                    throws ItemNotFoundException {
    for(Controller item: controllerList) {
      if(item.getFloor() == floor && item.getDoor().equals(door)) {
        return item.getProxy();
      }
    }
    throw new ItemNotFoundException();
  }

  /**
    @brief Add a controller to the list
    @param floor The floor number of the controller
    @param door The door (left, right, A, B, C, ...) of the controller
    @return True if controller was added successfully, false otherwise
  */
  public boolean addController(int floor, String door, ControllerPrx proxy,
                            Current __current) {
    Controller tmpController = new Controller(floor, door, proxy);
    try {
      findProxy(floor, door);
      return false;
    } catch(ItemNotFoundException e) {
      controllerList.add(tmpController);
      System.out.println("Nuevo jincho en el barrio: " + floor + " " + door +
                         " " + proxy);
      return true;
    }
  }

  /**
    @brief Turn off the boiler
    @return True if status changed, false otherwise
  */
  public boolean turnOff(Current __current) {
    if(status == false) {
      return false;
    } else {
      status = false;
      return true;
    }
  }

  /**
    @brief Turn on the boiler
    @return True if status changed, false otherwise
  */
  public boolean turnOn(Current __current) {
    if(status == true) {
      return false;
    } else {
      status = true;
      return true;
    }
  }

  /**
    @brief Get status of the boiler
    @return Status of the boiler
  */
  public boolean getStatus(Current __current) {
    return status;
  }

  /**
    @brief Turn off the heating on a given home
    @param secret The secret key which only the user knows
    @param floor The floor number of the controller
    @param door The door (left, right, A, B, C, ...) of the controller
    @return True if status changed, false otherwise
  */
  public boolean turnOffHeating(String secret, int floor, String door,
                                Current __current)
                                throws InvalidSecretException,
                                       ItemNotFoundException,
                                       FailureIceException {
    try {
      if(findProxy(floor, door).heaterOff(secret) == false) return false;
      else return true;
    }catch(InvalidSecretException | ItemNotFoundException ex1) {
      throw ex1;
    }catch(Exception ex2) {
      if(incidentServer != null) {
        String incident = "floor: " + floor + " door: " + door  + " message: "
                        + ex2.getMessage() + " cause: " + ex2.getCause();
        incidentServer.SaveIncident(incident);
      }
      throw new FailureIceException();
    }
  }
  
  /**
    @brief Turn on the heating on a given home
    @param secret The secret key which only the user knows
    @param floor The floor number of the controller
    @param door The door (left, right, A, B, C, ...) of the controller
    @return True if status changed, false otherwise
  */
  public boolean turnOnHeating(String secret, int floor, String door,
                               Current __current)
                               throws InvalidSecretException,
                                      ItemNotFoundException ,
                                      FailureIceException {
    try {
      return !findProxy(floor, door).heaterOn(secret) ? false : true;
    }catch(InvalidSecretException | ItemNotFoundException ex1) {
      throw ex1;
    }catch(Exception ex2) {
      if(incidentServer != null) {
        String incident = "floor: " + floor + " door: " + door  + " message: "
                        + ex2.getMessage() + " cause: " + ex2.getCause();
        incidentServer.SaveIncident(incident);
      }
      throw new FailureIceException();
    }
  }

  /**
    @brief Change the heating's temperature of a given home
    @param secret The secret key which only the user knows
    @param floor The floor number of the controller
    @param door The door (left, right, A, B, C, ...) of the controller
    @param temperature The temperature desired
  */
  public void changeTemperature(String secret, int floor, String door,
                                double temperature, Current __current)
                                throws InvalidSecretException,
                                       ItemNotFoundException ,
                                       FailureIceException {
    try {
      findProxy(floor, door).setTemperature(secret, temperature);
    }catch(InvalidSecretException | ItemNotFoundException ex1) {
      throw ex1;
    }catch(Exception ex2) {
      if(incidentServer != null) {
        String incident = "floor: " + floor + " door: " + door  + " message: "
                        + ex2.getMessage() + " cause: " + ex2.getCause();
        incidentServer.SaveIncident(incident);
      }
      throw new FailureIceException();
    }
  }

  /**
    @brief Get the status of the heating system on a given home
    @param secret The secret key which only the user knows
    @param floor The floor number of the controller
    @param door The door (left, right, A, B, C, ...) of the controller
    @return True if ON, false if OFF
  */
  public boolean getHeatingStatus(String secret, int floor, String door,
                                  Current __current)
                                  throws InvalidSecretException,
                                         ItemNotFoundException ,
                                         FailureIceException {
    try {
      return findProxy(floor, door).getStatus(secret);
    }catch(InvalidSecretException | ItemNotFoundException ex1) {
      throw ex1;
    }catch(Exception ex2) {
      if(incidentServer != null) {
        String incident = "floor: " + floor + " door: " + door  + " message: "
                        + ex2.getMessage() + " cause: " + ex2.getCause();
        incidentServer.SaveIncident(incident);
      }
      throw new FailureIceException();
    }
  }

  /**
    @brief Get the temperature of the heating system on a given home
    @param secret The secret key which only the user knows
    @param floor The floor number of the controller
    @param door The door (left, right, A, B, C, ...) of the controller
    @return The temperature of the given home's heating system
  */
  public double getHeatingTemperature(String secret, int floor, String door,
                                       Current __current)
                                       throws InvalidSecretException,
                                              ItemNotFoundException ,
                                              FailureIceException {
    try {
      return findProxy(floor, door).getTemperature(secret);
    }catch(InvalidSecretException | ItemNotFoundException ex1) {
      throw ex1;
    }catch(Exception ex2) {
      if(incidentServer != null) {
        String incident = "floor: " + floor + " door: " + door  + " message: "
                        + ex2.getMessage() + " cause: " + ex2.getCause();
        incidentServer.SaveIncident(incident);
      }
      throw new FailureIceException();
    }
  }

  /**
    @brief Get the consumption of the heating system on a given home
    @param floor The floor number of the controller
    @param door The door (left, right, A, B, C, ...) of the controller
    @return The consumption of the given home's heating system
  */
  public double getHeatingConsumption(int floor, String door,
                                      Current __current)
                                      throws ItemNotFoundException ,
                                             FailureIceException {
    try {
      return findProxy(floor, door).getConsumption();
    }catch(ItemNotFoundException ex1) {
      throw ex1;
    }catch(Exception ex2) {
      if(incidentServer != null) {
        String incident = "floor: " + floor + " door: " + door  + " message: "
                        + ex2.getMessage() + " cause: " + ex2.getCause();
        incidentServer.SaveIncident(incident);
      }
      throw new FailureIceException();
    }
  }
}
