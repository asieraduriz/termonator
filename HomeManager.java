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
	   * @brief Visualizar el menu
	   * 
	   */
	  private static void printMenu () {
	    
	  	System.out.println("1.- Turn on the heater");
	  	System.out.println("2.- Turn off the heater");
	  	System.out.println("3.- Change target temperature");
	  	System.out.println("4.- Update room temperature");
	  	System.out.println("5.- Check heater status");
	  	System.out.println("6.- Update secret password");
	  	
	  }
	  
	  /**
	   * @brief comprueba el @param choice si entra dentro del intervalo
	   * @param choice valor del menu
	   * @return true si está dentro, false si no está dentro
	   */
	  
	  private static boolean isChoiceOk (int choice) {
	  	return (choice > 0 && choice < 6)? true: false;
	  }

	  private static void dispatchMenuChoice (Home home, int menuChoice) 
			  throws InvalidSecretException { 
		  	switch (menuChoice) {
		    	case 1:
		    		home.heaterOn(home.getSecret());
		    		break;
		    	case 2:
		    		home.heaterOff(home.getSecret());
		    		break;
		    	case 3:
		    		home.setTemperature(home.getSecret(), askTemperature()); 
		    		break;
		    	case 4:
		    		home.setRoomTemperature(askTemperature());
		    		break;
		    	case 5:
		    		System.out.println(home.getHeatingStatus());
		    		break;
		    	case 6:
		    		home.updateSecret();
		    		break;
		  	}
		  }
	  private static double askTemperature () {
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("askTemperature");
		  	double temperature = 0;
			
		  	System.out.print("Enter new temperature --> ");
			
		  	try {
		  	    temperature = Double.parseDouble(br.readLine());
		  	} catch(IOException ioException) {
		  	    System.err.println("Error reading number");
		  	    temperature = 100;
		  	    
		  	} catch (NumberFormatException nfException) {
		  	    System.err.println("Wrong number format\n");
		  	    return askTemperature();
		  	}
		  	return temperature;
		  }
}
