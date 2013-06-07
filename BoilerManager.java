import utils.BoilerPrx;
import utils.BoilerPrxHelper;
import utils.DataBasePrx;
import utils.DataBasePrxHelper;

public class BoilerManager {

  public static void main(String[] args){

    Ice.Object boiler = new Boiler();
    Ice.Communicator iceCommunicator;
    Ice.ObjectAdapter boilerAdapter;

    iceCommunicator = Ice.Util.initialize(args);
    boilerAdapter = iceCommunicator.createObjectAdapter("BoilerAdapter");

    boilerAdapter.add(boiler, iceCommunicator.stringToIdentity("BoilerID"));
    Ice.ObjectPrx boilerObjPrx = boilerAdapter.addWithUUID(boiler);
    BoilerPrx boilerPrx = BoilerPrxHelper.checkedCast(boilerObjPrx);

    boilerAdapter.activate();

    /*Ice.ObjectPrx dataBaseObjPrx;
    dataBaseObjPrx = iceCommunicator.stringToProxy("DataBase@")
    DataBasePrx dataBasePrx;*/
  }
}
