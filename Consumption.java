
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
          Thread.sleep(300);
          if(_status)
            addConsumption();
        } catch (InterruptedException ie) {
          System.err.println("Interrupted while sleeping");
        }
        
      }
      try {
        wait();
      } catch (InterruptedException ie) {
        System.err.println("Interrupted while waiting");
      }
      
    }
  }
  
  synchronized private void addConsumption () {
    ++ _consumption;
  }
  
  synchronized double getConsumption () {
    return _consumption;
  }
  
  synchronized void resetConsumption () {
    _consumption = 0;
  }
  
  synchronized public void setStatusOn () {
    _status = true;
    notify();
  }
  
  synchronized public void setStatusOff () {
    _status = false;
  }
  
  synchronized public boolean getStatus () {
    return _status;
  }
  
}
