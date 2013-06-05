#ifndef TERMONATOR_ICE
#define TERMONATOR_ICE
module utils {
  exception InvalidSecretException {
  };
  exception ItemNotFoundException {
  };
  interface Controller {
    bool heaterOn (string secret) throws InvalidSecretException;
    bool heaterOff (string secret) throws InvalidSecretException;
    bool setTemperature (string secret, double temperature) throws InvalidSecretException;
    bool getStatus (string secret) throws InvalidSecretException;
    double getConsumption ();
  };	
  interface Boiler {
    bool turnOn();
    bool turnOff();
    bool getStatus();
    bool addController(int floor, string door, utils::Controller* proxy) throws InvalidSecretException, ItemNotFoundException;
    bool turnOnHeating(string secret, int floor, string door) throws InvalidSecretException, ItemNotFoundException;
    bool turnOffHeating(string secret, int floor, string door) throws InvalidSecretException, ItemNotFoundException;
    void changeTemperature(string secret, int floor, string door, double temperature) throws InvalidSecretException, ItemNotFoundException;
    bool getHeatingStatus(string secret, int floor, string door) throws InvalidSecretException, ItemNotFoundException;
    double getHeatingTemperature(string secret, int floor, string door) throws InvalidSecretException, ItemNotFoundException;
    double getHeatingConsumption(int floor, string door) throws ItemNotFoundException;
  };
 interface DataBase
	{
		void SaveIncident(string incident);
		void addBoilerController(string street, int portal, utils::Boiler* proxy);
	};
};
#endif
