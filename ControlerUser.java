import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utils.BoilerPrx;
import utils.InvalidSecretException;
import utils.ItemNotFoundException;


public class ControlerUser {
	private String _neighbourhood;
	private int _portal;
	private int _floor;
	private String _door;
	private DataBaseI _takeBoilers;
  private ArrayList<Boiler> _BoilerList;
  private BoilerPrx _boilerProxy;
  private String _secret;
  private double _temperature;
	
	public void HeaterStatus(String u_name){
		takeFromDataBase(u_name);
    takeBoilerPrx();
    takeSecret();
    try {
      _boilerProxy.getHeatingStatus(_secret, _floor, _door);
    } catch (InvalidSecretException e) {
      System.out.println("INCORRECT secret");
    } catch (ItemNotFoundException e) {
      System.out.println("Cannot Connect to the House. Put in contact with" +
          "us");
    }
		
		
	}
	
	public void ModifyTemperature(String u_name){
	  takeFromDataBase(u_name);
    takeBoilerPrx();
    takeSecret();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Insert the temperature (ºC)");
      try {
        _temperature = Double.valueOf(br.readLine());
      } catch (IOException e) {
        System.out.println("ERROR taking input data");
      } 
    try {
      _boilerProxy.changeTemperature(_secret, _floor, _door, _temperature);
    } catch (InvalidSecretException e) {
      System.out.println("INCORRECT secret");
    } catch (ItemNotFoundException e) {
      System.out.println("Cannot Connect to the House. Put in contact with" +
          "us");
    }
    System.out.println("Your heating temperature is now established on " 
        +_temperature+"ºC");
	}
	
	public void SwitchON(String u_name){
	  takeFromDataBase(u_name);
    takeBoilerPrx();
    takeSecret();
    
    try {
      if(! _boilerProxy.turnOnHeating(_secret, _floor, _door)){
        System.out.println("Your heating is already ON");
      }
      else
        System.out.println("Your heating change to ON");
    } catch (InvalidSecretException e) {
      System.out.println("INCORRECT secret");
    } catch (ItemNotFoundException e) {
      System.out.println("Cannot Connect to the House. Put in contact with" +
          "us");
    }

	}
	
	public void SwitchOFF(String u_name){
	  takeFromDataBase(u_name);
    takeBoilerPrx();
    takeSecret();
    
    try {
      if(! _boilerProxy.turnOffHeating(_secret, _floor, _door)){
        System.out.println("Your heating is already OFF");
      }
      else
        System.out.println("Your heating change to OFF");
    } catch (InvalidSecretException e) {
      System.out.println("INCORRECT secret");
    } catch (ItemNotFoundException e) {
      System.out.println("Cannot Connect to the House. Put in contact with" +
          "us");
    }
	}
	
	public void takeFromDataBase(String u_name){
	  
	  Connection conn = null;
    String url = "jdbc:mysql://localhost:3306/";
    String dbName = "Termonator";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "Termonator"; 
    String password = "termonator";
    
    try{
      Class.forName(driver).newInstance();
      conn = DriverManager.getConnection(url+dbName,userName,password);
      try {
          PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM Users WHERE u_name = ?");
          prepStmt.setString(1, u_name);
          ResultSet resultset = prepStmt.executeQuery();
          while(resultset.next()){
            if(resultset.getString("u_name").equals(u_name)){
            _neighbourhood = resultset.getString("Neighbourhood");
            _portal = Integer.valueOf(resultset.getString("Portal"));
            _floor = Integer.valueOf(resultset.getString("Floor"));
            _door = resultset.getString("Door");
          }
        }
      } catch (SQLException e1) { System.out.println("ERROR executing query");}
    }catch(Exception e){System.out.println("ERROR on the DataBase Connection");}
      try {
        conn.close();
    }catch (SQLException e) {
      System.out.println("ERROR closing DataBase connection");
    }
	}
	
	public void takeBoilerPrx (){
	  _BoilerList = _takeBoilers.getBoilerList();
	  for(Boiler item: _BoilerList){
	    if(item.getStreet().equals(_neighbourhood) && item.getPortal() == _portal){
	      _boilerProxy = item.getBoiler();
	    }
	  }
	}
	public void takeSecret(){
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	  System.out.println("Insert your Secret");
	  try {
      _secret = br.readLine();
    } catch (IOException e) {
      System.out.println("ERROR taking input data");
    }
	}
}
