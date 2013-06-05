import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import utils.BoilerPrx;
/*1 --> See consumption
 * 2 --> Switch On
 * 3 --> ShutDown
 */
import utils.ItemNotFoundException;

public class BoilerWorker {
  
  private DataBaseI _takeBoilers;
  private ArrayList<Boiler> _BoilerList;
	
	public void insertNeighbourhood(int function){
	  String street = "";
	  int portal = 0;
	  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Insert the street name of the neighbourhood");
		try {
      street = br.readLine();
      System.out.println("Insert the portal of neighbourhood");
      portal = Integer.valueOf(br.readLine());
    } catch (IOException e) {
      System.out.println("ERROR Inserting DATA");
    }
		if(!testNeighbourhood(street, portal,function)){
		  System.out.println("Invalid Street or Portal");
		}
		
	}
	
	public boolean testNeighbourhood(String street, int portal, int function){
	  _BoilerList = _takeBoilers.getBoilerList();
	  for(Boiler item: _BoilerList){
	    if(item.getStreet().equals(street) && item.getPortal() == portal){
	      connectToController(item.getBoiler(), function);
	      break;
	    }
	  }
    return false;
	}
	
	public void connectToController(BoilerPrx boilerPrx, int function){
	  
	  switch(function){
	    case 1: connectToHome(boilerPrx);
	      break;
	    case 2: if(!boilerPrx.turnOn()) System.out.println("ERROR Switching ON");
	            else System.out.println("Now Boiler is ON");
	      break;
	    case 3: if(!boilerPrx.turnOff()) System.out.println("ERROR Switching OFF");
              else System.out.println("Now Boiler is OFF");
	      break;
	  }
	}
	public void connectToHome(BoilerPrx boilerPrx){
	  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	  int floor = 0;
	  String door = "";
	    
	  System.out.println("Insert the floor of the house");
    try {
      floor = Integer.valueOf(br.readLine());
      System.out.println("Insert the door of the floor (Ex. 'B'");
      door = br.readLine();
    } catch (IOException e) {
      System.out.println("ERROR Inserting DATA");
    }
    try {
      boilerPrx.getHeatingConsumption(floor, door);
    } catch (ItemNotFoundException e) {
      System.out.println("The house that you're consulting doesn't exist or" +
      		"the house is now unvailable");
      		
    }
   }
}
