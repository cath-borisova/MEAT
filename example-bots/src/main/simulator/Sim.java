package simulator;

import simulator.*;
import simulator.algorithms.AStar;
import simulator.algorithms.BFS;
import simulator.algorithms.DFS;
import simulator.instrumentor.InstrumentedGrid;
import simulator.instrumentor.Instrumentor;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.ArrayList;
import java.util.List;

public class Sim {
    public static void main(String[] args) {
        Grid grid = new Grid(573214531, 0.05);

        // Manual testing since code in agent is broken
        Coords agent = grid.agentPos;
        Coords goal = grid.goalPos;
        int i = 0;
        while (!agent.equals(goal)) {
            System.out.println("Iteration #" + i++);
            System.out.println(grid);

            DFS algo = new DFS(); // - TESTED AND WORKS
            Coords newPos = algo.depthFirstSearch(grid, agent, goal, 10000);
            //System.out.println(newPos);
            // Dijkstras algo = new Dijkstras(); - TESTED AND WORKS
            // Coords newPos = algo.dijkstras(grid, agent, goal, 10000);
            // AStar algo = new AStar(); - TESTED AND WORKS
            // Coords newPos = algo.astar(grid, agent, goal, 10000);
            //System.exit(1);
            grid.setLocation(newPos.x, newPos.y, grid.getLocation(agent.x, agent.y));
            grid.setLocation(agent.x, agent.y, new GridLocation(LocationType.EMPTY));
            agent.x = newPos.x;
            agent.y = newPos.y;
        }
    }
}
