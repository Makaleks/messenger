package arhangel.dim.container;

/**
 * Created by Алексей on 18.03.2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import arhangel.dim.container.Property;

class BeanXmlReader {

    private static final String TAG_BEAN = "bean";
    private static final String TAG_PROPERTY = "property";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_VALUE = "val";
    private static final String ATTR_REF = "ref";
    private static final String ATTR_BEAN_ID = "id";
    private static final String ATTR_BEAN_CLASS = "class";

    public static ArrayList<Bean> parseBeans(String pathToFile) {
        ArrayList<Bean> result = new ArrayList<>();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(pathToFile);

            // корневой элемент
            //Node root = document.getDocumentElement();
            // Просматриваем все подэлементы корневого
            NodeList beans = document.getElementsByTagName(TAG_BEAN);
            Node beanNode;
            Node propNode;
            NodeList beanProps;
            Map<String, Property> properties;
            Property property;

            Bean bean;
            String idName;
            String className;
            String propName;
            String propVal;
            ValueType type;

            for (int i = 0; i < beans.getLength(); i++) {
                beanNode = beans.item(i);

                NamedNodeMap attributes = beanNode.getAttributes();
                idName = attributes.getNamedItem(ATTR_BEAN_ID).getNodeValue();
                className = attributes.getNamedItem(ATTR_BEAN_CLASS).getNodeValue();

                beanProps = beanNode.getChildNodes();
                properties = new HashMap<>();
                for (int j = 0; j < beanProps.getLength(); j++) {
                    propNode = beanProps.item(j);
                    if (propNode.getNodeType() != Node.TEXT_NODE) {
                        NamedNodeMap attrs = propNode.getAttributes();
                        propName = attrs.getNamedItem(ATTR_NAME).getNodeValue();
                        try {
                            propVal = attrs.getNamedItem(ATTR_VALUE).getNodeValue();
                            type = ValueType.VAL;
                        } catch (NullPointerException exception) {
                            // when toString90 is called for "null"
                            propVal = attrs.getNamedItem(ATTR_REF).getNodeValue();
                            type = ValueType.REF;
                        }
                        properties.put(propName, new Property(propName, propVal, type));
                    }
                }
                result.add(new Bean(idName,className,properties));
            }

        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        return result;
    }
}