
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.NumberFormatException;

public class Home extends Thread {
  
  private final int _SECRET_LENGTH = 4;
  private final double _PONDER = 1.3;
  
  protected BufferedReader br;
    
  private int _incomingWaterTemperature;
  private int _outgoingWaterTemperature;
  private double _roomTemperature;
  private double _temperature;
  private String _secret;
    
  Consumption consumption;

  public Home (int incomingWaterTemperature,int outgoingWaterTemperature) {
    
    _incomingWaterTemperature = incomingWaterTemperature;
    _outgoingWaterTemperature = outgoingWaterTemperature;
    
    br = new BufferedReader (new InputStreamReader(System.in));
    
    _roomTemperature = 20;
    _temperature = 0;
    _secret = "0000";
    
    consumption = new Consumption();
  }

  public void run () {
    
    consumption.start();
    
  	int menuChoice = 0;
  	while(true) {
  		menuChoice = getMenuChoice();
  		dispatchMenuChoice (menuChoice);
  	}
  }
  

  synchronized private int getMenuChoice () {
    
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

  synchronized private void dispatchMenuChoice (int menuChoice) { 
  	switch (menuChoice) {
    	case 1:
    		setHeaterOn();
    		break;
    	case 2:
    		setHeaterOff();
    		break;
    	case 3:
    		updateTemperature (askTemperature()); 
    		break;
    	case 4:
    		updateRoomTemperature (askTemperature()); 
    		break;
    	case 5:
    		updateSecret();
    		break;
  	}
  }
  
  synchronized public boolean setHeaterOn () {
	try {
	    if(!consumption.getStatus()) {
	    	
	    	goAutomatic();
	    	
	    	return true;
	    }
	    
	} catch(Exception exception) {
	    System.err.println("Error while setting the heater on");
	}
	return false;
  }
  
  synchronized public boolean setHeaterOff () {
	try {
	    if(consumption.getStatus()) {
			
  			consumption.setStatusOff();
  			
  			return true;
	    }
	    
	} catch(Exception exception) {
	    System.err.println("Error while setting the heater off");
	}
	return false;
  }

  synchronized public double askTemperature () { 

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

  synchronized public boolean updateTemperature (double newTemperature) {
  	try {
  		_temperature = newTemperature;
  		goAutomatic ();
  		
  	} catch(Exception ex) {
  		System.err.println("Exception while updating desired temperature");
  		return false;
  	}
  	return true;
  }
  
  synchronized public double getTemperature () {
    return _temperature;
  }
  
  synchronized private boolean updateRoomTemperature (double newRoomTemperature) {
  	try {
  		_roomTemperature = newRoomTemperature;
  		goAutomatic();
  		
  	} catch(Exception ex) {
  		System.err.println("Exception while updating room temperature");
  		return false;
  	}
  	return true;
  }
  
  synchronized private void goAutomatic () {
    if(consumption.getStatus()) {
      if(_temperature <= _roomTemperature) 
        consumption.setStatusOff();
    } else
      if(_temperature > _roomTemperature) 
        consumption.setStatusOn();
  }
  
  synchronized public boolean getStatus() {
    return consumption.getStatus();
  }
  
  synchronized public double getConsumption () {
    double coef = 0;
    coef = (_incomingWaterTemperature - _outgoingWaterTemperature) * _PONDER;
    
    double finalConsumption = 0;
    finalConsumption = consumption.getConsumption() * coef;
    
    consumption.resetConsumption();
    
    return finalConsumption;
  }
  
  private boolean updateSecret () {
  	String secret = new String();
  	String reSecret = new String();
  	
  	System.out.print("Enter new secret password: ");
  
  	try {
  	    secret = br.readLine();
  	} catch(IOException ioException) {
  	    System.err.println("Error reading first secret");
  	}
  	
  	if(_secret.equals(secret) || secret.isEmpty()) 
  	    return false;
  	
  	System.out.print("Re enter new secret password: ");
  	
  	try {
  	    reSecret = br.readLine();
  	} catch(IOException ioException) {
  	    System.err.println("Error reading second secret");
  		}
  		
  	if(secret.equals(reSecret) && secret.length() == _SECRET_LENGTH)
  	  _secret = secret;
  	else
  	  return false;
  
  	return true;
  }
  
  public String getSecret () {
    return _secret;
  }
}
