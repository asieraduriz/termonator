import java.sql.*;

import java.util.Scanner;


public class Sede {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ice.Communicator ic = Ice.Util.initialize(args);
		Ice.ObjectAdapter adapter = ic.createObjectAdapterWithEndpoints(
		    "ServerAdapter", "tcp -h 127.0.0.1 -p 10000");
		DataBaseI dbI = new DataBaseI();
		adapter.add(dbI,ic.stringToIdentity("DataBase"));
		adapter.activate();
		while (true){
			Login();
		}
	}
	
	public static void Login(){
		String u_name ="", u_pass="";
		int log_result = -1;
		Scanner teclado = new Scanner(System.in);
		while(log_result < 0){
		System.out.println("");
		System.out.println("Welcome to Termonator 1.0");
		System.out.println("Login");
		System.out.print("User: ");
		u_name = teclado.next();
		System.out.println("");
		System.out.print("Password: ");
		u_pass = teclado.next();
		System.out.println("");
		log_result = CheckLogin(u_name, u_pass, log_result);
		if (log_result == -1) System.out.println("Invalid Username or Password");
		}
		
		if (log_result ==1) PrintMenuWorker(u_name);
		else PrintMenuUser(u_name);
		
	}
	
	public static int CheckLogin(String u_name, String u_pass, int log_result){
		/*log_result
		 * Bad user/pass = -1
		 * Is user = 0
		 * Is worker = 1
		 */
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
				  	PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM Users WHERE u_name = ? AND u_pass = ?");
				  	prepStmt.setString(1, u_name);
				  	prepStmt.setString(2, u_pass);
				  	ResultSet resultset = prepStmt.executeQuery();
				  	while(resultset.next()){
					if(resultset.getString("u_name").equals(u_name) && resultset.getString("u_pass").equals(u_pass)){
							log_result = Integer.parseInt(resultset.getString("Worker_User"));
						}
					else
						log_result = -1;		
					}
				} catch (SQLException e1) { System.out.println("ERROR executing query");}
		  }catch(Exception e){System.out.println("ERROR on the DataBase Connection");}
		  try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("ERROR closing DataBase connection");
		}
		  return log_result;
	}
	
	public static void PrintMenuUser(String u_name){
		Scanner teclado = new Scanner(System.in);
		int option = -1;
		while(option != 0){
		System.out.println("");
		System.out.println("**********************************************" +
				"**********************");
		System.out.println("Welcome "+u_name+" to Termonator 1.0");
		System.out.println("1.- See heater Status");
		System.out.println("2.- Modify Heater Temperature");
		System.out.println("3.- Switch ON your heater");
		System.out.println("4.- Switch OFF your heater");
		System.out.println("0.- Exit");
		System.out.println("**********************************************" +
				"**********************");
		System.out.print("Your Option: ");
		option = teclado.nextInt();
		ExecuteMenuUser(option,u_name);
		}
	}
	public static void ExecuteMenuUser(int option, String u_name){
		ControlerUser heater = new ControlerUser();
		switch(option){
		case 1: heater.HeaterStatus(u_name);
			break;
		case 2: heater.ModifyTemperature(u_name);
			break;
		case 3: heater.SwitchON(u_name);
			break;
		case 4: heater.SwitchOFF(u_name);
			break;
		case 0:
			break;
		default: 
			System.out.println("Incorrect Option. Try again.");
			break;
		}
	}
	
	public static void PrintMenuWorker(String u_name){
		Scanner teclado = new Scanner(System.in);
		int option =-1;
		while (option != 0){
		System.out.println("");
		System.out.println("*************************************************" +
				"*******************");
		System.out.println("Welcome "+u_name+" to Termonator 1.0");
		System.out.println("1.- See Consumption of a concrete Home");
		System.out.println("2.- Switch ON a boiler of a specific neighbourhood");
		System.out.println("3.- Switch OFF a boiler of a specific neighbourhood");
		System.out.println("0.- Exit");
		System.out.println("*************************************************" +
				"*******************");
		System.out.print("Your Option: ");
		option = teclado.nextInt();
		ExecuteMenuWorker(option);
		}
	}
	
	public static void ExecuteMenuWorker(int option){
		BoilerWorker boiler = new BoilerWorker();
		/*1 --> See consumption
		 * 2 --> Switch On
		 * 3 --> ShutDown
		 */
		
		switch(option){
		case 1: boiler.insertNeighbourhood(1);
			break;
		case 2: boiler.insertNeighbourhood(2);
			break;
		case 3: boiler.insertNeighbourhood(3);
			break;
		case 0: System.out.println("Bye");
			break;
		default: 
			System.out.println("Incorrect Option. Try again.");
			break;
		}
	}
	

}
