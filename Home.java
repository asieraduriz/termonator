
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.NumberFormatException;

@SuppressWarnings("unused")
public class Home extends Thread {
  
  private final int _SECRET_LENGTH = 4;
  
  protected BufferedReader br;
    
  private int _floor;
  private String _door;
  private int _incomingWaterTemperature;
  private int _outgoingWaterTemperature;
  
  private double _roomTemperature;
  private double _temperature;
  private double _consumption = 0;

  private String _secret;
  private boolean _status; //0 for OFF, 1 for ON
    
    

  public Home (int floor, String door, int incomingWaterTemperature,
               int outgoingWaterTemperature) {
    _floor = floor;
    _door = door;
    _incomingWaterTemperature = incomingWaterTemperature;
    _outgoingWaterTemperature = outgoingWaterTemperature;
    
    br = new BufferedReader (new InputStreamReader(System.in));
    _roomTemperature = 20;
    _temperature = 0;
    _secret = "0000";
    _status = false;
  }

  public void run () {
    
  	int menuChoice = 0;
  	while(true) {
  		menuChoice = getMenuChoice();
  		if(menuChoice != 0) {
  			dispatchMenuChoice (menuChoice);
  		}
  	}
  }
  

  //User interface
  private int getMenuChoice () {
	int userChoice = 0;
	
	printMenu ();
	
	try {
	    userChoice = Integer.valueOf(br.readLine()).intValue();
	} catch(IOException ioException) {
	    System.err.println("Error reading number");
	    return 0;
	    
	} catch (NumberFormatException nfException) {
	    System.err.println("Wrong number format\n");
	    return getMenuChoice();
	}
	
	return (isChoiceOk(userChoice))?userChoice:getMenuChoice();
  }
  
  private void printMenu () {
	System.out.println("1.- Turn on the heater");
	System.out.println("2.- Turn off the heater");
	System.out.println("3.- Change target temperature");
	System.out.println("4.- Update room temperature");
	System.out.println("5.- Update secret password");
	System.out.print("Your choice --> ");
  }
  
  private boolean isChoiceOk (int choice) {
  	return (choice > 0 && choice < 6)? true: false;
  }

  private boolean dispatchMenuChoice (int menuChoice) { 
  	//return true if the choice has been successfully completed
  	switch (menuChoice) {
  	case 1:
  		setHeaterOn();
  		break;
  	case 2:
  		setHeaterOff();
  		break;
  	case 3:
  		updateTemperature (askTemperature()); //handle true or false
  		break;
  	case 4:
  		updateRoomTemperature (askTemperature()); //handle true or false
  		break;
  	case 5:
  		updateSecret(); //handle true or false
  		break;
  	}
  	
  	
  	return true;
  }
  
  //Both
  public boolean setHeaterOn () {
	try {
	    if(!_status) {
	    	_status = true;
	    	//start consumption
	    	return true;
	    }
	} catch(Exception exception) {
	    System.err.println("Error while setting off the heater");
	}
	return false;
  }
  
  //Both
  public boolean setHeaterOff () {
	try {
	    if(_status) {
			_status = false;
			//stop consumption
			//save consumption
			return true;
	    }
	} catch(Exception exception) {
	    System.err.println("Error while setting off the heater");
	}
	return false;
  }

  //Ice
  public boolean getStatus() {
  	return _status;
  }
  
  //User Interface
  public double askTemperature () { 
  	//Valid for desired temperature (option 3) or room temperature (option 5)

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

  //Both
  public boolean updateTemperature (double newTemperature) {
  	try {
  		_temperature = newTemperature;
  		//check with room temperature to start automatic consumption only if status is UP
  	} catch(Exception ex) {
  		System.err.println("Exception while updating desired temperature");
  		return false;
  	}
  	return true;
  }
  
  //Ice
  public double getTemperature () {
    return _temperature;
  }
  
  //User interface
  private boolean updateRoomTemperature (double newRoomTemperature) {
  	try {
  		_roomTemperature = newRoomTemperature;
  		//check with temperature to start automatic consumption only if status is UP
  	} catch(Exception ex) {
  		System.err.println("Exception while updating room temperature");
  		return false;
  	}
  	return true;
  }
  
  //Ice
  public String getSecret () {
    return _secret;
  }
  
  //User Interface
  private boolean updateSecret () {
	String secret = new String();
	String reSecret = new String();
	
	System.out.print("Enter new secret password: ");

	try {
	    secret = br.readLine();
	} catch(IOException ioException) {
	    System.err.println("Error reading first secret");
	}
	
	if(_secret.equals(secret)) 
	    return false;
	System.out.print("Re enter new secret password: ");
	
	try {
	    reSecret = br.readLine();
	} catch(IOException ioException) {
	    System.err.println("Error reading second secret");
		}
		
	if(!secret.equals(reSecret) && secret.length() == _SECRET_LENGTH)
	    return false;
	else
	    _secret = secret;

	return true;
  }
}
