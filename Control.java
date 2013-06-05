
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import utils.ControllerPrx;
import utils.ControllerPrxHelper;
import utils.BoilerPrx;
import utils.BoilerPrxHelper;

public class Control {

  private final String PATH = "../info.txt";
  
	Ice.Communicator homeComm;
	Ice.ObjectAdapter homeObjAdapter;
	Ice.Object homeServant;
	Ice.ObjectPrx homeObjPrx;
	ControllerPrx homePrx;
	
	Home home;
  int floor = 0;
  String door = "";
	
	Ice.ObjectPrx boilerObjPrx;
	BoilerPrx boilerPrx;
	
	public Control () {
		homeComm = Ice.Util.initialize();
		homePrx = null;
		
	  floor = 0;
	  door = "";
		
		boilerPrx = null;
	}
	
	public static void main (String[] args) {
		Control control = new Control();
		
		if(control.readFromFile()) { 
  		
  		  control.iceInit(); 
  		
  		  control.addHome();
		}
		else
		  System.out.println("Exiting");
	}
	
	private boolean readFromFile () {
	  int incomingWaterTemperature = 0;
    int outgoingWaterTemperature = 0;
     
    try (BufferedReader fileBr = new BufferedReader(new FileReader(PATH))) {
        
      floor = Integer.parseInt(fileBr.readLine());
        
      door = fileBr.readLine();
        
      incomingWaterTemperature = Integer.parseInt(fileBr.readLine());
        
      outgoingWaterTemperature = Integer.parseInt(fileBr.readLine());
      
    } catch (IOException ioe) {
      System.err.println("Error while reading from file");
      return false;
    }  
    
    home = new Home(incomingWaterTemperature, outgoingWaterTemperature);

    return true;
 }
	
	private void iceInit() {
	  
		homeObjAdapter = homeComm.createObjectAdapter("homeAdapter");
		
		homeServant = new HomeServant(home);
		
		homeObjPrx = homeObjAdapter.addWithUUID(homeServant);
		homePrx = ControllerPrxHelper.uncheckedCast(homeObjPrx);
		
  	try {	
  		boilerObjPrx = homeComm.stringToProxy("BoilerID@.....................");
  		boilerPrx = BoilerPrxHelper.checkedCast(boilerObjPrx);
  	} catch(Exception ex) {
  	  System.err.println("Ice error");
  	}
  	
	}
	
	private void addHome () {
	  boilerPrx.addController(floor, door, homePrx);
	}
}
