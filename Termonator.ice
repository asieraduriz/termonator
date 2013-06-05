#ifndef TERMONATOR_ICE
#define TERMONATOR_ICE
module utils {

  exception InvalidSecretException {
    
  };
  interface Controller {
    bool heaterOn (string secret) throws InvalidSecretException;
    bool heaterOff (string secret) throws InvalidSecretException;
    bool setTemperature (string secret, double temperature) throws InvalidSecretException;
    double getTemperature (string secret) throws InvalidSecretException;
    bool getStatus (string secret) throws InvalidSecretException;
    double getConsumption (string secret) throws InvalidSecretException;
  };	
  interface Boiler {
    bool turnOn();
    bool turnOff();
    bool getStatus();
    bool addController(int floor, string door, utils::Controller* proxy);
    bool turnOnHeating(string secret, int floor, string door);
    bool turnOffHeating(string secret, int floor, string door);
    void changeTemperature(string secret, int floor, string door, double temperature);
    bool getHeatingStatus(string secret, int floor, string door);
    double getHeatingConsumption(string secret, int floor, string door);
  };
  interface DataBase {

  };
};
#endif
