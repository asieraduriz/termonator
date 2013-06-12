
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Ice.Current;
import utils.InvalidSecretException;
import utils._ControllerDisp;

@SuppressWarnings("serial")
public class Home extends _ControllerDisp {

	private final double _PONDER = 1.3;
	
	private Consumption _consumption;
	
	
	private double _incomingTemperature;
	private double _outgoingTemperature;
	private double _temperature;
	private double _roomTemperature;
	private boolean _heaterStatus;
	private String _secret;
	
	public Home (Consumption consumption, double incomingTemperature, 
			double outgoingTemperature) {
		_consumption = consumption;
		_incomingTemperature = incomingTemperature;
		_outgoingTemperature = outgoingTemperature;
		_heaterStatus = false;
		_secret = "0000";
	}
	
	public boolean heaterOn(String secret, Current __current)
			throws InvalidSecretException {
		System.out.println("set heater on request");
		checkSecret(secret);
		if(_heaterStatus) 
			return false;
		else {
			_heaterStatus = true;
			goAutomatic();
			return true;
		}
	}

	public boolean heaterOff(String secret, Current __current)
			throws InvalidSecretException {
		System.out.println("set heater off request");
		
		checkSecret(secret);
		return heaterDown();
	}

	public boolean heaterDown(Current __current) {
		if(!_heaterStatus) 
			return false;
		else {
			_heaterStatus = false;
			_consumption.setStatusOff();
			return true;
		}
	}

	public boolean setTemperature(String secret, double temperature,
			Current __current) throws InvalidSecretException {
		System.out.println("set temperature request: "+temperature);
		checkSecret(secret);
		_temperature = temperature;
		goAutomatic();
		return true;
	}

	public double getTemperature(String secret, Current __current)
			throws InvalidSecretException {
		System.out.println("get temperature request");
		return _temperature;
	}

	public boolean setRoomTemperature(double roomTemperature) {
		System.out.println("set room temperature request");
		_roomTemperature = roomTemperature;
		goAutomatic();
		return true;
	}
	
	public boolean getHeatingStatus () {
		return _heaterStatus;
	}
	  
	public boolean getStatus(String secret, Current __current)
			throws InvalidSecretException {
		System.out.println("get status request");
		return _consumption.getStatus();
	}

	public double getConsumption(Current __current) {
		System.out.println("get consumption request");
	    double coef = 0;
	    coef = (_incomingTemperature - _outgoingTemperature) * _PONDER;
	    
	    double finalConsumption = 0;
	    finalConsumption = _consumption.getConsumption() * coef;
	    System.out.println("get consumption consumption");
	    _consumption.resetConsumption();
	    System.out.println("reset consumption");
	    System.out.println("Consumption: "+finalConsumption);
	    return finalConsumption;
	}

	  public boolean updateSecret () {
		  	String secret = new String();
		  	String reSecret = new String();
		  	
		  	System.out.print("Enter new secret password: ");
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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
		  		
		  	if(secret.equals(reSecret) && secret.length() == 4)
		  	  _secret = secret;
		  	else
		  	  return false;
		  
		  	return true;
		  }
	  
	  public String getSecret() {
		  return _secret;
	  }
	  
	  private void goAutomatic () {
		  System.out.println("go automatic");
		  if(_heaterStatus) {
		    if(_consumption.getStatus()) {
		      if(_temperature <= _roomTemperature) 
		    	  _consumption.setStatusOff();
		    } else
		      if(_temperature > _roomTemperature) 
		    	  _consumption.setStatusOn();
		  }
	  }
	  
		private void checkSecret (String possibleSecret) 
                throws InvalidSecretException {
			System.out.println("Request for checkSecret");
			if(!_secret.equals(possibleSecret)) 
			throw new InvalidSecretException();
			
		}
}
