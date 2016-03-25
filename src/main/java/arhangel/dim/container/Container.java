package arhangel.dim.container;

import arhangel.dim.container.BeanXmlReader;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
import java.lang.reflect.Constructor;

import arhangel.dim.container.beans.Car;
import arhangel.dim.container.beans.Engine;
import arhangel.dim.container.beans.Gear;

class Vertex {
    Bean bean;
}

class Graph {
    Map<Vertex, List<Vertex>> vertices = new HashMap<>();

    private enum VisitType { not_visited, visited, fin }

    private class CircleFound {
        boolean flag;
    }

    public Vertex addVertex(Bean value) {
        Vertex vertex = new Vertex();
        vertex.bean = value;
        vertices.put(vertex,new LinkedList<>());
        return vertex;
    }

    public void addEdge(Vertex from, Vertex to) {
        List lst = vertices.get(from);
        lst.add(to);
    }

    public boolean isConnected(Vertex v1, Vertex v2) {
        return vertices.containsKey(v1) && vertices.get(v1).contains(v2);
    }

    public List<Vertex> getLinked(Vertex vertex) {
        if (!vertices.containsKey(vertex)) {
            return null;
        }
        return vertices.get(vertex);
    }

    private void topologicalSort(Vertex vertex, List<Vertex> list,
                                 Map<Vertex, VisitType> toVisit,
                                 CircleFound cycle, Stack<Vertex> finalList) {
        //если вершина является черной, то не производим из нее вызов процедуры
        if (toVisit.get(vertex) == VisitType.fin) {
            return;
        }
        //выходим из процедуры, если уже нашли один из циклов
        if (cycle.flag) {
            return;
        }
        //если вершина является серой, то орграф содержит цикл
        if (toVisit.get(vertex) == VisitType.visited) {
            cycle.flag = true;
            return;
        }
        toVisit.put(vertex, VisitType.visited); //помечаем вершину как серую
        //запускаем обход из всех вершин, смежных с вершиной v
        Iterator<Vertex> it = list.iterator();
        while (it.hasNext()) {
            Vertex nextVertex = it.next();
            topologicalSort(nextVertex, vertices.get(nextVertex),toVisit, cycle, finalList);
            if (cycle.flag) {
                return;
            }
        }
        toVisit.put(vertex, VisitType.fin); //помечаем вершину как черную
        //добавляем посещенную вершину в топологический порядок
        finalList.push(vertex);
    }

    public List<Vertex> sort() {
        Set<Map.Entry<Vertex,List<Vertex>>> vertSet = vertices.entrySet();
        Map toVisit = new HashMap<Vertex, VisitType>(vertices.size());
        CircleFound cycle = new CircleFound();
        Stack<Vertex> reverseResult = new Stack<Vertex>();
        for (Map.Entry<Vertex,List<Vertex>> mapElem : vertSet) {
            topologicalSort(mapElem.getKey(), mapElem.getValue(),toVisit, cycle,reverseResult);
        }
        if (cycle.flag == true) {
            return null;
        }
        List<Vertex> result = new ArrayList<Vertex>(reverseResult.size());
        while (!reverseResult.isEmpty()) {
            result.add(reverseResult.pop());
        }
        return result;
    }
}

/**
 * Используйте ваш xml reader чтобы прочитать конфиг и получить список бинов
 */
public class Container {

    private List<Bean> beans;

    private String convertBeanToClassName(String name) {
        StringBuilder className = new StringBuilder(
                Character.toUpperCase(
                        name.charAt(0)
                )
        );
        className.append(name.substring(1, name.length() - 5));
        return className.toString();
    }

    private String convertValToClassName(String name) {
        StringBuilder className = new StringBuilder(
                Character.toUpperCase(
                        name.charAt(0)
                )
        );
        className.append(name.substring(1));
        return className.toString();
    }

    private String convertToSetMethod(String name) {
        StringBuilder methodFullName = new StringBuilder("set");
        methodFullName.append(
                Character.toUpperCase(
                        name.charAt(0)
                )
        );
        methodFullName.append(name.substring(1));
        return methodFullName.toString();
    }

    private List<Bean> sortBeans() {
        Iterator<Bean> itBean = beans.iterator();
        List<Vertex> vertList = new LinkedList<>();
        Graph graph = new Graph();
        while (itBean.hasNext()) {
            vertList.add(graph.addVertex(itBean.next()));
        }
        Iterator<Vertex> itVert = vertList.iterator();
        Vertex testVert = null;
        while (itVert.hasNext()) {
            Vertex next = itVert.next();
            Set<Map.Entry<String,Property>> propSet = next.bean.getProperties().entrySet();
            for (Map.Entry<String,Property> mapElem : propSet) {
                if (mapElem.getValue().getType() == ValueType.REF) {
                    itBean = beans.iterator();
                    Bean bean = null;
                    while (itBean.hasNext()) {
                        bean = itBean.next();
                        if (mapElem.getValue().getValue().equals(bean.getName())) {
                            break;
                        }
                    }
                    Iterator<Vertex> itVert1 = vertList.iterator();

                    while (itVert1.hasNext()) {
                        testVert = itVert1.next();
                        if (testVert.bean.equals(bean)) {
                            break;
                        }
                    }
                    graph.addEdge(testVert, next);
                }
            }
        }
        vertList = graph.sort();
        List<Bean> beanList = new LinkedList<>();
        itVert = vertList.iterator();
        while (itVert.hasNext()) {
            beanList.add(itVert.next().bean);
        }

        return beanList;
    }

    private Object getByBean(Bean bean) {
        Class clazz = null;
        Object result = null;
        try {
            clazz = Class.forName(bean.getClassName());
            result = clazz.newInstance();
        } catch (Exception exception) {
            System.out.println(exception.toString());
            return null;
        }

        Set<Map.Entry<String,Property>> propSet = bean.getProperties().entrySet();
        Integer fakeInt = new Integer(0);
        Class[] paramIntType = new Class[] { int.class };
        Class[] paramClassType = new Class[] { int.class };
        try {
            for (Map.Entry<String, Property> mapElem : propSet) {
                if (mapElem.getValue().getType() == ValueType.REF) {
                    clazz.getMethod(
                            convertToSetMethod(mapElem.getValue().getName()),
                            paramClassType
                    ).invoke(result, getByName(
                            convertBeanToClassName(mapElem.getValue().getValue()))
                    );
                } else {
                    fakeInt.parseInt(mapElem.getValue().getValue());
                    clazz.getMethod(
                            convertToSetMethod(mapElem.getValue().getName()),
                            paramIntType
                    ).invoke(result, fakeInt);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return result;
    }

    /**
     * Если не получается считать конфиг, то бросьте исключение
     * @throws InvalidConfigurationException неверный конфиг
     */
    public Container(String pathToConfig) throws InvalidConfigurationException {
        try {
            beans = BeanXmlReader.parseBeans(pathToConfig);
            if (beans == null) {
                throw new InvalidConfigurationException("broken XML file");
            }
            // вызываем BeanXmlReader
            beans = sortBeans();
            if (beans == null) {
                throw new InvalidConfigurationException("cycle found");
            }
        } catch (InvalidConfigurationException ex) {
            throw ex;
        }
        int aa = 2;
        aa++;
    }

    /**
     *  Вернуть объект по имени бина из конфига
     *  Например, Car car = (Car) container.getByName("carBean")
     */
    public Object getByName(String name) {
        Iterator<Bean> it = beans.iterator();
        Object result = null;
        while (it.hasNext()) {
            Bean test = it.next();
            if (test.getName().equals(name)) {
                result = test;
                break;
            }
        }
        return result;
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("arhangel.dim.container.Car")
     */
    public Object getByClass(String className) {
        return null;
    }

    private void instantiateBean(Bean bean) {

        /*
        // Примерный ход работы

        String className = bean.getClassName();
        Class clazz = Class.forName(className);
        // ищем дефолтный конструктор
        Object ob = clazz.newInstance();


        for (String name : bean.getProperties().keySet()) {
            // ищем поле с таким именен внутри класса
            // учитывая приватные
            Field field = clazz.getDeclaredField(name);
            // проверяем, если такого поля нет, то кидаем InvalidConfigurationException с описание ошибки

            // Делаем приватные поля доступными
            field.setAccessible(true);

            // Далее определяем тип поля и заполняем его
            // Если поле - примитив, то все просто
            // Если поле ссылка, то эта ссылка должа была быть инициализирована ранее

            */

    }

}
