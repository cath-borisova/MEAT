package simulator;

import simulator.algorithms.BFS;
import simulator.instrumentor.InstrumentedGrid;
import simulator.instrumentor.Instrumentor;

public class Sim {
    public static void main(String[] args) {
        // Dijkstras algo = new Dijkstras(); - TESTED AND WORKS
        // Coords newPos = algo.dijkstras(grid, agent, goal, 10000);
        // AStar algo = new AStar(); - TESTED AND WORKS
        // Coords newPos = algo.astar(grid, agent, goal, 10000);
        Class<?> currentAlgo = BFS.class;

        Instrumentor instrumentor = new Instrumentor();

        // Seed: 5734531 - tested
        // Seed: 573214531 -tested
        InstrumentedGrid grid = instrumentor.newInstrumentedGrid(573294531, 0.05);

        Coords agentPos = instrumentor.instrumentedCoordsToCoords(instrumentor.getInstrumentedAgentPos(grid));
        Coords goalPos = instrumentor.instrumentedCoordsToCoords(instrumentor.getInstrumentedGoalPos(grid));
        Agent agent = new Agent(10000, agentPos, goalPos);
        int i = 0;
        int totalWork = 0;
        while (!agentPos.equals(goalPos)) {
            System.out.println("Iteration #" + i++);
            System.out.println(instrumentor.instrumentedGridToString(grid));
            int work = instrumentor.moveAgent(agent, grid, currentAlgo);
            System.out.println("Move work " + work);

            totalWork += work;
        }

        System.out.println("Total work for " + currentAlgo.getSimpleName() + " " + totalWork);
    }
}
