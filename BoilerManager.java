import utils.BoilerPrx;
import utils.BoilerPrxHelper;
import utils.DataBasePrx;
import utils.DataBasePrxHelper;

public class BoilerManager {

  public static void main(String[] args){

    Ice.Object boiler = new Boiler();

    Ice.Communicator iceCom = Ice.Util.initialize(args);

    Ice.ObjectPrx dataBaseObjPrx;
    dataBaseObjPrx = iceCom.propertyToProxy("DataBase.Proxy");
    DataBasePrx dataBasePrx = DataBasePrxHelper.checkedCast(dataBaseObjPrx);

    Ice.ObjectAdapter boilerAdap = iceCom.createObjectAdapter("BoilerAdap");
    Ice.ObjectPrx boilerObjPrx = boilerAdap.addWithUUID(boiler);
    boilerAdap.add(boiler, iceCom.stringToIdentity("BoilerID"));
    BoilerPrx boilerPrx = BoilerPrxHelper.checkedCast(boilerObjPrx);

    boilerAdap.activate();

    dataBasePrx.addBoilerController("Kale Nagusia", 12, boilerPrx);

    Scanner kb = new Scanner(System.in);

    kb.next();
    ic.destroy();
  }
}
