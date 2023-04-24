package simulator;

import simulator.algorithms.AStar;
import simulator.algorithms.BFS;
import simulator.algorithms.Dijkstras;

public class Sim {
    public static void main(String[] args) {
        // Seed: 5734531 - tested
        // Seed: 573214531 -tested
        Grid grid = new Grid(573214531, 0.05);

        // Manual testing since code in agent is broken
        Coords agent = grid.agentPos;
        Coords goal = grid.goalPos;
        int i = 0;
        while (!agent.equals(goal)) {
            System.out.println("Iteration #" + i++);
            System.out.println(grid);

            BFS algo = new BFS(); // - TESTED AND WORKS
            Coords newPos = algo.breadthFirstSearch(grid, agent, goal, 10000);
            // Dijkstras algo = new Dijkstras(); - TESTED AND WORKS
            // Coords newPos = algo.dijkstras(grid, agent, goal, 10000);
            // AStar algo = new AStar(); - TESTED AND WORKS
            // Coords newPos = algo.astar(grid, agent, goal, 10000);
            grid.setLocation(newPos.x, newPos.y, grid.getLocation(agent.x, agent.y));
            grid.setLocation(agent.x, agent.y, new GridLocation(LocationType.EMPTY));
            agent.x = newPos.x;
            agent.y = newPos.y;
        }
    }
}
