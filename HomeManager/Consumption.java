
public class Consumption extends Thread {

  private double _consumption;
  private boolean _status;
  
  
  public Consumption () {
    _consumption = 0;
    _status = false;
  }
  
  /**
   * @brief Función principal donde se deja al hilo en un bucle infinito
   * para calcular el consumo cuando el estado sea activo, o pararse cuando
   * el estado sea negativo.
   */
  public void run () {
    while (true) {
      while(_status) 
        try {
          Thread.sleep(800);
          if(_status)
            addConsumption();
        } catch (InterruptedException ie) {
          System.err.println("Interrupted while sleeping");
        }
      
      goToSleep();
    }
}

/**
 * @brief Suma un valor al consumo
 */
  synchronized private void addConsumption () {
    ++ _consumption;
  }
/**
 * @brief Devuelve el valor del consumo
 */
  synchronized double getConsumption () {
    interrupt();
    return _consumption;
  }

/**
 * @brief Reinicia el valor del consumo
 */
  synchronized void resetConsumption () {
    _consumption = 0;
  }

/**
 * @brief Función que hará que el consumo se ponga en marcha
 */
  synchronized public void setStatusOn () {
    System.out.println("Consumption on");
    _status = true;
    notify();
  }

/**
 * @brief Función que parará el consumo
 */
  synchronized public void setStatusOff () {
    System.out.println("Consumption off");
    _status = false;
  }

/**
 * @brief Función para dar a conocer el estado del consumo
 * @return true si está consumiendo
 * @return false si no está consumiendo
 */
  synchronized public boolean getStatus () {
    return _status;
  }

  /**
   * @brief Función que el único objetivo es dormir al proceso en ejecución
   */
  synchronized private void goToSleep () {
    try {
      wait();
    } catch (InterruptedException ie) {
      System.err.println("Interrupted while waiting");
    }
  }
  
}
