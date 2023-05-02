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

    private static double euclidean(Coords initial, Coords position) {
        return Math.sqrt(Math.pow((position.x - initial.x), 2) + Math.pow((position.y - initial.y), 2));
    }

    private static Coords closest(int rows, int columns, Coords agent, Coords goal, int visRadius) {
        double radius = visRadius;
        Coords bestPosition = null;
        double closestDistance = Double.MAX_VALUE;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Coords position = new Coords(i, j);

                if (grid.getLocation(i, j).getType() == LocationType.OBSTACLE) {
                    continue;
                }

                double distance = euclidean(agent, position);
                if (distance <= radius && distance < closestDistance) {
                    closestDistance = distance;
                    bestPosition = position;
                }
            }
        }

        return bestPosition;
    }   

    private static double[] simulateUntilConfident(Class<?> algo, double congestion, int rows, int columns) {
        Instrumentor instrumentor = new Instrumentor();
        List<Integer> results = new ArrayList<>();
        do {
            InstrumentedGrid grid = instrumentor.newInstrumentedGrid(mt.nextLong(), congestion, rows, columns);
            Coords agentPos = instrumentor.instrumentedCoordsToCoords(instrumentor.getInstrumentedAgentPos(grid));
            Coords goalPos = instrumentor.instrumentedCoordsToCoords(instrumentor.getInstrumentedGoalPos(grid));
            int visRadius = 5;

            if (visRadius < euclidean(agentPos, goalPos)) {
                goalPos = closest(rows, columns, agentPos, goalPos, visRadius);
            }

            Agent agent = new Agent(visRadius, agentPos, goalPos);
            int i = 0;
            int totalWork = 0;
            boolean broken = false;
            while (!agentPos.equals(goalPos)) {
                // System.out.println("Iteration #" + i);
                // System.out.println("AgentPos: " + agentPos);
                // System.out.println("GoalPos: " + goalPos);
                // System.out.println(instrumentor.instrumentedGridToString(grid));
                int work = instrumentor.moveAgent(agent, grid, algo);
                // System.out.println("Move work " + work);

                if (i > 10000) {
                    System.out.println("Infinite loop detected");
                    broken = true;
                    break;
                }
                totalWork += work;
                i++;
            }

            if (!broken) {
                results.add(totalWork);
            }
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

        Class<?>[] algos = {Bug.class, Dijkstras.class, BFS.class, AStar.class};
        for (Class<?> algo : algos) {
            // TODO Adjust this and change the rows/cols and congestion to make pretty graphs
            double[] interval = simulateUntilConfident(algo, 0.05, 50, 50);
            System.out.println("Confidence interval for " + algo.getSimpleName() + " " + interval[0] + " " + interval[1]);
            System.out.println("Mean for " + algo.getSimpleName() + " " + confidenceToMean(interval));
        }
    }
}
