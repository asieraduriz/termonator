
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
	
	/**
	 * @brief Función para encender la calefacción encenderla automáticamente si
	 * la temperatura deseada supera a la temperatura ambiente
	 * @param secret Clave secreta
	 * @return true si se ha pasado de apagada a encendida
	 * @return false si no ha ocurrido nada
	 * @throws InvalidSecretException si las claves secretas no coinciden
	 */
	public boolean heaterOn(String secret, Current __current)
			           throws InvalidSecretException {
		checkSecret(secret);
		if(_heaterStatus) 
			return false;
		else {
			_heaterStatus = true;
			goAutomatic();
			return true;
		}
	}

	/**
	 * @brief Función para apagar la calefacción independientemente de las
	 * temperaturas
	 * @param secret Clave secreta
	 * @return true si se ha pasado de encendida a apagada @see heaterDown
	 * @return false si no ha ocurrido nada
	 * @throws InvalidSecretException si las claves secretas no coinciden
	 */
	public boolean heaterOff(String secret, Current __current)
			           throws InvalidSecretException {
		checkSecret(secret);
		return heaterDown();
	}

	/**
	 * @brief Función que apaga la calefacción
	 * @return true si se ha pasado de encendida a apagada
	 * @return false si no ha ocurrido nada
	 */
	public boolean heaterDown(Current __current) {
		if(!_heaterStatus) 
			return false;
		else {
			_heaterStatus = false;
			_consumption.setStatusOff();
			return true;
		}
	}

	/**
	 * @brief Función para especificar la temperatura deseada
	 * @param secret Clave secreta
	 * @param temperature Nueva temperatura deseada
	 * @return true si se ha cambiado correctamente
	 * @return false si ha habido algún problema a la hora de actualizar
	 * @throws InvalidSecretException si las claves secretas no coinciden
	 */
	public boolean setTemperature(String secret, double temperature,
			                        Current __current) throws InvalidSecretException {
		checkSecret(secret);
		try {
		_temperature = temperature;
		goAutomatic();
		return true;
		} catch(Exception ex) {
		  return false;
		}
	}

	/**
	 * @brief Función simple que devuelvel el valor de la temperatura deseada
	 * @param secret Clave secreta
	 * @return valor de la temperatura deseada actual
	 */
	public double getTemperature(String secret, Current __current)
			throws InvalidSecretException {
		return _temperature;
	}

	/**
	 * @brief 
	 * @param roomTemperature Nueva temperatura ambiente
	 * @return true si se ha cambiado la temperatura ambiente satisfactoriamente
	 * @return false si ha habido algún problema
	 */
	public boolean setRoomTemperature(double roomTemperature) {
  	  try {
  		_roomTemperature = roomTemperature;
  		goAutomatic();
  		return true;
  	  } catch(Exception ex) {
  	    return false;
  	  }
	}
	
	/**
	 * @brief Función sencilla para dar a conocer el estado de la calefacción
	 * @return true si el estado está encendido
	 * @return false si está apagado
	 */
	public boolean getHeatingStatus () {
		return _heaterStatus;
	}
	 
	/**
	 * @brief Función para dar a conocer el estado del consumo
	 * @param secret Clave secreta
	 * @return true si está consumiendo
	 * @return false si no está consumiendo
	 */
	public boolean getStatus(String secret, Current __current)
	               throws InvalidSecretException {
		return _consumption.getStatus();
	}

	/**
	 * @brief Función que devuelve el consumo ya calculado
	 * Primero recoge el consumo, y después el consumo lo reinicia a cero.
	 * @return valor del cálculo del consumo
	 */
	public double getConsumption(Current __current) {
	    double coef = 0;
	    coef = (_incomingTemperature - _outgoingTemperature) * _PONDER;
	    
	    double finalConsumption = 0;
	    finalConsumption = _consumption.getConsumption() * coef;
	    
	    _consumption.resetConsumption();
	    
	    return finalConsumption;
	}

	/**
	 * @brief Función para cambiar la clave secreta
	 * Primero se lee la nueva clave, y se compara con la previa.
	 * Si no lo son, se le pide al usuario que la repita.
	 * @return true si la clave secreta se ha actualizado a la última
	 * @return false si es la misma que la anterior
	 * @return false si la clave y su comprobación no coinciden.
	 */
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
	
  /**
   * @brief Función sencilla que devuelve el valor de la clave secreta
   * @return Clave secreta
   */
  public String getSecret() {
	  return _secret;
  }
  
  /**
   * @brief Función automática que compara la temperatura deseada con la 
   * temperatura ambiente siempre que el estado de la calefacción sea encendida
   * Si la temperatura deseada es menor que la ambiente y la calefacción está 
   * encencida, para el consumo
   * Si la temperatura supera a la ambiente, empieza a consumir.
   */
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
	
  /**
   * @brief Función que compara el valor de la clave secreta almacenada con
   * una supuesta clave secreta
   * @param possibleSecret Valor que contiene la supuesta clave secreta
   * @throws InvalidSecretException si las claves secretas no coinciden
   */
	private void checkSecret (String possibleSecret) 
	             throws InvalidSecretException {
		System.out.println("Request for checkSecret");
		if(!_secret.equals(possibleSecret)) 
		  throw new InvalidSecretException();
	}
}
