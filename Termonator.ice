#ifndef TERMONATOR_ICE
#define TERMONATOR_ICE
module utils {
  exception FailureIceException {
  };
  exception InvalidSecretException {
  };
  exception ItemNotFoundException {
  };
  interface Controller {
    bool heaterOn (string secret) throws InvalidSecretException;
    bool heaterOff (string secret) throws InvalidSecretException;
    bool heaterDown();
    bool setTemperature (string secret, double temperature) throws InvalidSecretException;
    double getTemperature (string secret) throws InvalidSecretException;
    bool getStatus (string secret) throws InvalidSecretException;
    double getConsumption ();
  };	
  interface Boiler {
    bool turnOn();
    bool turnOff();
    bool getStatus();
    bool addController(int floor, string door, utils::Controller* proxy);
    bool turnOnHeating(string secret, int floor, string door) throws InvalidSecretException, ItemNotFoundException, FailureIceException;
    bool turnOffHeating(string secret, int floor, string door) throws InvalidSecretException, ItemNotFoundException, FailureIceException;
    void changeTemperature(string secret, int floor, string door, double temperature) throws InvalidSecretException, ItemNotFoundException, FailureIceException;
    bool getHeatingStatus(string secret, int floor, string door) throws InvalidSecretException, ItemNotFoundException, FailureIceException;
    double getHeatingTemperature(string secret, int floor, string door) throws InvalidSecretException, ItemNotFoundException, FailureIceException;
    double getHeatingConsumption(int floor, string door) throws ItemNotFoundException, FailureIceException;
  };
 interface DataBase
	{
		void SaveIncident(string incident);
		void addBoilerController(string street, int portal, utils::Boiler* proxy);
	};
};
#endif
