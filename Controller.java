import utils.ControllerPrx;

/**
  @brief Class for local use of the Boiler class
*/
public class Controller {

  private ControllerPrx _proxy; ///< ICE proxy of the controller
  private String _door; ///< Door (left, right, A, B, C, ...) of the controller
  private int _floor; ///< Floor number of the controller

  /**
    @brief Constructor function
    @param floor The floor number of the controller
    @param door The door (left, right, A, B, C, ...) of the controller
    @param proxy An ICE proxy to the controller
  */
  public Controller(int floor, String door, ControllerPrx proxy) {
    _proxy = proxy;
    _door = door;
    _floor = floor;
  }

  /**
    @brief Get the controllers floor
    @return The floor of the controller
  */
  public int getFloor() {
    return _floor;
  }

  /**
    @brief Get the controllers door
    @return The door of the controller
  */
  public String getDoor() {
    return _door;
  }

  /**
    @brief Get the controllers proxy
    @return The proxy of the controller
  */
  public ControllerPrx getProxy() {
    return _proxy;
  }
}
