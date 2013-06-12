import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Ice.Current;
import utils.BoilerPrx;
import utils._DataBaseDisp;
import java.util.ArrayList;

public class DataBaseI extends _DataBaseDisp {
	
	private ArrayList<Boiler> _BoilerList;

	
	public void SaveIncident(String Incident, Current __current) {
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
				  PreparedStatement prepStmt = conn.prepareStatement(
				  	    "INSERT INTO Incidents (I_description) VALUES (?)");
				  prepStmt.setString(1, Incident);
					ResultSet resultset = prepStmt.executeQuery();
				} catch (SQLException e1) {
					System.out.println("ERROR executing query");
				}
			  
		  }catch(Exception e){
			  System.out.println("ERROR on the DataBase Connection");
		  }
		  try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("ERROR closing DataBase connection");
		}
	}

	public void addBoilerController(String street, int portal, BoilerPrx proxy, 
	                                Current __current) {
		Boiler testBoiler = new Boiler(street,portal,proxy);
		if(!_BoilerList.contains(testBoiler)){
			_BoilerList.add(testBoiler);
		}
	}
	
	public ArrayList<Boiler> getBoilerList(){
    return _BoilerList;
	}

}
