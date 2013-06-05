
import utils._ControllerDisp;
import utils.InvalidSecretException;
import Ice.Current;

public class HomeServant extends _ControllerDisp {

	private Home _home;
	
	public HomeServant (Home home) {
		_home = home;
		_home.start();
	}
	
	public boolean heaterOn(String secret, Current __current) 
	                        throws InvalidSecretException {
	  checkSecret(secret);
	  return _home.setHeaterOn();
	}

	public boolean heaterOff(String secret, Current __current) 
                           throws InvalidSecretException {
	  checkSecret(secret);
		return _home.setHeaterOff();
	}

	public boolean setTemperature(String secret, double temperature, 
	                              Current __current) 
	                              throws InvalidSecretException {
	  checkSecret(secret);
		return _home.updateTemperature(temperature);
	}

  public double getTemperature(String secret, Current __current)
                               throws InvalidSecretException {
    return _home.getTemperature();
  }
	
	public boolean getStatus(String secret, Current __current) 
	                         throws InvalidSecretException {
	  checkSecret(secret);
		return _home.getStatus();
	}

	public double getConsumption(String secret, Current __current) 
	                             throws InvalidSecretException {
	  checkSecret(secret);
		return _home.getConsumption();
	}

	private void checkSecret (String possibleSecret) 
	                          throws InvalidSecretException {
	  if(!_home.getSecret().equals(possibleSecret)) 
	    throw new InvalidSecretException();
	}

}
