
public class Consumption extends Thread {

  private double _consumption;
  private boolean _status;
  
  
  public Consumption () {
    _consumption = 0;
    _status = false;
  }
  
  public void run () {
    while (true) {
      while(_status) {
        try {
          Thread.sleep(800);
          if(_status)
            addConsumption();
        } catch (InterruptedException ie) {
          System.err.println("Interrupted while sleeping");
        }
        
      }
      goToSleep();
    }
  }
  
  /**
   * @brief sumar un valor al consumo
   */
  synchronized private void addConsumption () {
    ++ _consumption;
  }
  /**
   * @brief devolver el valor del consumo
   */
  synchronized double getConsumption () {
	interrupt();
    return _consumption;
  }
  
  /**
   * @brief reiniciar el valor del consumo
   */
  synchronized void resetConsumption () {
    _consumption = 0;
  }
  
  /**
   * @brief declarar la calefacción a encendida
   */
  synchronized public void setStatusOn () {
    System.out.println("Consumption on");
    _status = true;
    notify();
  }
  
  /**
   * @brief declarar la calefacción a apagada
   */
  synchronized public void setStatusOff () {
    System.out.println("Consumption off");
    _status = false;
  }
  
  /**
   * @brief devolver el valor de la calefacción
   * @return true si está encencida, false si está apagada
   */
  synchronized public boolean getStatus () {
    return _status;
  }
  
  synchronized private void goToSleep () {
    try {
      System.out.println("Lotara");
      wait();
      System.out.println("Lo ez");
    } catch (InterruptedException ie) {
      System.err.println("Interrupted while waiting");
    }
  }
  
}
