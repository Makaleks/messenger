package arhangel.dim.container;

import arhangel.dim.container.beans.Car;
import arhangel.dim.container.beans.Engine;
import arhangel.dim.container.beans.Gear;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Алексей on 22.03.2016.
 */
public class MainApp {


    public static void main(String[] args) {
        /*
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse("config.xml");
            Node root = document.getDocumentElement();
            root.toString();
            int aa = 0;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }//*/
        Container cont = null;
        try {
            cont = new Container("config.xml");
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
        Gear gear = (Gear) cont.getByName("gearBean");
        Engine engine = (Engine) cont.getByClass("arhangel.dim.container.beans.Engine");
        //cont.beans.get(1);
        Car car = (Car) cont.getByName("carBean");
        return;
    }
}
