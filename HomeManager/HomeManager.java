import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import utils.BoilerPrx;
import utils.BoilerPrxHelper;
import utils.ControllerPrx;
import utils.ControllerPrxHelper;
import utils.InvalidSecretException;


public class HomeManager {

  private final static int _MIN_CHOICE = 1;
  private final static int _MAX_CHOICE = 7;
  
  /**
   * @brief Función inicial que arranca las demás funcionalidades
   * En esta función, primero se lee de un fichero que especifica cuatro
   * variables de la vivienda.
   * Después se hace la conexión entre la vivienda y el controlador central
   * Para acabar, se entra en un bucle infinito para que siempre esté disponible
   * para el usuario de la vivienda.
   * @param args Parámetro que especifica un fichero de configuración necesario
   */
  public static void main (String[] args) {
    Consumption consumption = new Consumption ();
    		
    int _floor = 1;
    String _door = "L";
    int _incomingWaterTemperature = 0;
    int _outgoingWaterTemperature = 0;
    
    final String PATH = "../info.txt";
    try (BufferedReader fileBr = new BufferedReader(new FileReader(PATH))) {
      _floor = Integer.parseInt(fileBr.readLine());
      _door = fileBr.readLine();
      _incomingWaterTemperature = Integer.parseInt(fileBr.readLine());
      _outgoingWaterTemperature = Integer.parseInt(fileBr.readLine());
       
    } catch (IOException ioe) {
      System.err.println("Error while reading from file");
    }
    
    Ice.Communicator homeComm = Ice.Util.initialize(args);
    Ice.ObjectAdapter homeObjAdapter = homeComm.createObjectAdapter("homeAdapter");
    Home home = new Home(consumption, _incomingWaterTemperature, _outgoingWaterTemperature);
    Ice.ObjectPrx homeObjPrx = homeObjAdapter.addWithUUID(home);
    ControllerPrx homePrx = ControllerPrxHelper.uncheckedCast(homeObjPrx);
    homeObjAdapter.activate();
    
    Ice.ObjectPrx boilerObjPrx = homeComm.propertyToProxy("Boiler.Proxy");
    BoilerPrx boilerPrx = BoilerPrxHelper.checkedCast(boilerObjPrx);
    System.out.println("Send my prx: "+homePrx);
    boilerPrx.addController(_floor, _door, homePrx);
    
    consumption.start();
    
    int menuChoice = 0;
    do {
      menuChoice = getMenuChoice();
      try {
          dispatchMenuChoice (home, menuChoice);
      } catch (InvalidSecretException e) {
        System.out.println("Invalid Secret");
      }
    }while(true);
        
  }
  
  /**
   * @brief Función sencilla que comprueba la entrada que corresponderá a la
   * opción del menu @see printMenu.
   * Una vez leído, si no ha habido errores, se comprueba el formato y el
   * rango del valor.
   * @return 0 solo si hay error
   * @return getMenuChoice() si el formato es incorrecto y no está dentro del
   * rango de valores del menu
   * @return userChoice el valor dentro del rango del menu por el usuario
   */
  private static int getMenuChoice () {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int userChoice = 0;
      	
    printMenu ();
      	
    try {
      userChoice = Integer.valueOf(br.readLine()).intValue();
    } catch(IOException ioException) {
      System.err.println("Error reading number");
      return 0;
    } catch (NumberFormatException nfException) {
      System.err.println("Wrong number format");
      return getMenuChoice();
    }
    return (isChoiceOk(userChoice))?userChoice:getMenuChoice();
  }
  
  /**
   * @brief Función que únicamente muestra en la pantalla las opciones del menu
   * 
   */
  private static void printMenu () {
    
    System.out.println("1.- Turn on the heater");
    System.out.println("2.- Turn off the heater");
    System.out.println("3.- Change target temperature");
    System.out.println("4.- Update room temperature");
    System.out.println("5.- Check heater status");
    System.out.println("6.- Check boiler status");
    System.out.println("7.- Update secret password");
  	
  }
  
  /**
   * @brief comprueba el @param choice si entra dentro del intervalo
   * @param choice valor del menu
   * @return true si está dentro del rango
   * @return false si está fuera del rango
   */
  
  private static boolean isChoiceOk (int choice) {
    return (choice < _MIN_CHOICE || choice > _MAX_CHOICE)? false: true;
  }
  
  /**
   * @brief Dependiendo de @param menuChoice llamará a función correspondiente
   * @param home Valor de la clase Home para compartir la misma referencia
   * @param menuChoice Opción escogida por el usuario
   * @return true o falsedependiendo del valor del retorno de 
   * las funciones ejecutadas
   * @throws InvalidSecretException en caso de que las claves secretas 
   * no coincidan
   */
  public static boolean dispatchMenuChoice (Home home, int menuChoice) 
  throws InvalidSecretException { 
    switch (menuChoice) {
      case 1:
        return home.heaterOn(home.getSecret());
      case 2:
        return home.heaterOff(home.getSecret());
      case 3:
        return home.setTemperature(home.getSecret(), askTemperature()); 
      case 4:
        return home.setRoomTemperature(askTemperature());
      case 5:
        return home.getHeatingStatus();
      case 6:
        return home.getStatus(home.getSecret());
      case 7:
        return home.updateSecret();
      default: 
        return false;
    }
  }
  
  /**
   * @brief Pregunta por la temperatura a introducir, y la pasa por dos filtros:
   * Uno; comprueba si ha habido errores al leer, y dos; si el valor de la
   * temperatura no es de un formato numérico.
   * @return 0 si ha habido error al leer, o si el usuario a escogido ese valor
   * @return askTemperature si el formato no es numérico
   * @return temperature el valor correcto leido del usuario.
   */
  private static double askTemperature () {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("askTemperature");
    double temperature = 0;
    
    System.out.print("Enter new temperature --> ");
    
    try {
      temperature = Double.parseDouble(br.readLine());
    } catch(IOException ioException) {
      System.err.println("Error reading number");
      temperature = 0;
        
    } catch (NumberFormatException nfException) {
      System.err.println("Wrong number format\n");
      return askTemperature();
    }
    return temperature;
  }
}
