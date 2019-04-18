package br.com.ebexs.routerfinder.startup;

import br.com.ebexs.routerfindermodel.model.Graph;
import br.com.ebexs.routerfindermodel.model.RouteFind;
import br.com.ebexs.routerfindermodel.model.RouteModel;
import br.com.ebexs.routerfindermodel.model.Vertex;
import br.com.ebexs.routerfinderservice.exception.ResourceRouterUnavailableException;
import br.com.ebexs.routerfinderservice.service.CalculateCheapestWay;
import br.com.ebexs.routerfinderservice.service.GenerateGraph;
import br.com.ebexs.routerfinderservice.service.ReadRouteRegistry;
import br.com.ebexs.routerfinderservice.service.impl.CalculateCheapestWayImpl;
import br.com.ebexs.routerfinderservice.service.impl.GenerateGraphImpl;
import br.com.ebexs.routerfinderservice.service.impl.ReadCSV;

import java.util.List;
import java.util.Scanner;

public class Startup {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        if (args.length == 0) {
            throw new Exception ("File Path is missing !!!!");
        }

        System.out.println("Let´s find the cheapest way " );
        System.out.printf("From " );
        String from = scanner.next();
        System.out.printf("To " );
        String to = scanner.next();

        List<RouteModel> routeModels = getRouteModels(args[0]);

        Graph graph = getGraph(routeModels);

        CalculateCheapestWay calculateCheapestWay = new CalculateCheapestWayImpl();

        RouteFind routeFind = getRouteFind(from, to, graph, calculateCheapestWay);
        System.out.println(routeFind.getTotal());
    }

    private static RouteFind getRouteFind(String from, String to, Graph graph, CalculateCheapestWay calculateCheapestWay) throws Exception {
        RouteFind routeFind = calculateCheapestWay.doTheMath(graph, new Vertex(from), new Vertex(to));

        routeFind.getRoute().stream().forEach( rota -> System.out.print(rota + " - "));
        return routeFind;
    }

    private static List<RouteModel> getRouteModels(String arg) throws ResourceRouterUnavailableException {
        ReadRouteRegistry readCSV = new ReadCSV();
        return (List<RouteModel>) readCSV.read(arg);
    }

    private static Graph getGraph(List<RouteModel> routeModels) {
        GenerateGraph generateGraph = new GenerateGraphImpl(routeModels);
        Graph graph = generateGraph.generateGraph();
        return graph;
    }
}
