import java.util.Scanner;
import utils.BoilerPrx;
import utils.BoilerPrxHelper;
import utils.DataBasePrx;
import utils.DataBasePrxHelper;
import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;
import Glacier2.Router;
import Glacier2.SessionNotExistException;

public class BoilerManager {

  public static void main(String[] args){

    Ice.Object boiler = new Boiler();

    Ice.Communicator iceCom = Ice.Util.initialize(args);
    Ice.RouterPrx routerPrx = iceCom.getDefaultRouter();
    Glacier2.RouterPrx router = Glacier2.RouterPrxHelper.checkedCast(routerPrx);
    Glacier2.SessionPrx session;

    Ice.ObjectAdapter boilerAdap = iceCom.createObjectAdapter("BoilerAdap");
    Ice.ObjectPrx boilerObjPrx = boilerAdap.addWithUUID(boiler);
    boilerAdap.add(boiler, iceCom.stringToIdentity("BoilerID"));
    BoilerPrx boilerPrx = BoilerPrxHelper.uncheckedCast(boilerObjPrx);
    boilerAdap.activate();

    try {
      router.createSessionFromSecureConnection();
    } catch(CannotCreateSessionException e) {}
    catch(PermissionDeniedException e) {}

    Ice.ObjectPrx dataBaseObjPrx;
    dataBaseObjPrx = iceCom.propertyToProxy("DataBase.Proxy");
    DataBasePrx dataBasePrx = DataBasePrxHelper.uncheckedCast(dataBaseObjPrx);

    System.out.println("PRX "+dataBasePrx);
    dataBasePrx.addBoilerController("Kale", 12, boilerPrx);

    Scanner kb = new Scanner(System.in);

    kb.next();
    try {
      router.destroySession();
    } catch(SessionNotExistException e) {}
    iceCom.destroy();
  }
}
