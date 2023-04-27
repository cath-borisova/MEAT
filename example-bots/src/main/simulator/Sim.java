package simulator;

import simulator.algorithms.*;
import simulator.instrumentor.InstrumentedGrid;
import simulator.instrumentor.Instrumentor;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.ArrayList;
import java.util.List;

public class Sim {

    private static final int SEED = 573294531;
    private static final MersenneTwister mt = new MersenneTwister(SEED);

    private static double confidenceIntervalPointEstimate(List<Integer> numbers) {
        double mean = 0;
        double stdDev = 0;
        double sum = 0;
        for (Integer number : numbers) {
            sum += number;
        }
        mean = sum / numbers.size();
        for (Integer number : numbers) {
            stdDev += Math.pow(number - mean, 2);
        }
        stdDev = Math.sqrt(stdDev / numbers.size());
        double halfWidth = 1.96 * stdDev / Math.sqrt(numbers.size());
        return halfWidth / mean;
    }

    private static double[] confidenceInterval(List<Integer> numbers) {
        double mean = 0;
        double stdDev = 0;
        double sum = 0;
        double[] confidenceInterval = new double[2];
        for (Integer number : numbers) {
            sum += number;
        }
        mean = sum / numbers.size();
        for (Integer number : numbers) {
            stdDev += Math.pow(number - mean, 2);
        }
        stdDev = Math.sqrt(stdDev / numbers.size());
        confidenceInterval[0] = mean - 1.96 * stdDev / Math.sqrt(numbers.size());
        confidenceInterval[1] = mean + 1.96 * stdDev / Math.sqrt(numbers.size());
        return confidenceInterval;
    }

    private static double[] simulateUntilConfident(Class<?> algo, double congestion, int rows, int columns) {
        Instrumentor instrumentor = new Instrumentor();
        List<Integer> results = new ArrayList<>();
        do {
            InstrumentedGrid grid = instrumentor.newInstrumentedGrid(mt.nextLong(), congestion, rows, columns);

            Coords agentPos = instrumentor.instrumentedCoordsToCoords(instrumentor.getInstrumentedAgentPos(grid));
            Coords goalPos = instrumentor.instrumentedCoordsToCoords(instrumentor.getInstrumentedGoalPos(grid));
            Agent agent = new Agent(10000, agentPos, goalPos);
            int i = 0;
            int totalWork = 0;
            while (!agentPos.equals(goalPos)) {
                // System.out.println("Iteration #" + i);
                // System.out.println(instrumentor.instrumentedGridToString(grid));
                int work = instrumentor.moveAgent(agent, grid, algo);
                // System.out.println("Move work " + work);

                totalWork += work;
                if (i > 10000) {
                    System.out.println("Infinite loop detected");
                    break;
                }
                i++;
            }

            results.add(totalWork);
            // System.out.println("Iteration " + results.size());
        } while (confidenceIntervalPointEstimate(results) > 0.1 || results.size() < 50);

        return confidenceInterval(results);
    }

    private static double confidenceToMean(double[] interval) {
        return (interval[0] + interval[1]) / 2.0;
    }

    public static void main(String[] args) {
        // Dijkstras algo = new Dijkstras(); - TESTED AND WORKS
        // Coords newPos = algo.dijkstras(grid, agent, goal, 10000);
        // AStar algo = new AStar(); - TESTED AND WORKS
        // Coords newPos = algo.astar(grid, agent, goal, 10000);
        // Instrumentor instrumentor = new Instrumentor();
        // Class<?> currentAlgo = BFS.class;

        Class<?>[] algos = {BFS.class, AStar.class, /*DFS.class,*/ Dijkstras.class, /*IDDFS.class*/};
        for (Class<?> algo : algos) {
            // TODO Adjust this and change the rows/cols and congestion to make pretty graphs
            double[] interval = simulateUntilConfident(algo, 0.05, 50, 50);
            System.out.println("Confidence interval for " + algo.getSimpleName() + " " + interval[0] + " " + interval[1]);
            System.out.println("Mean for " + algo.getSimpleName() + " " + confidenceToMean(interval));
        }
    }
}
