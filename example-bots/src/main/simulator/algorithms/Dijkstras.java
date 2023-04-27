package simulator.algorithms;

import simulator.*;
import java.util.*;

public class Dijkstras implements Algo {
    Coords coords;

    public Coords nextMove(Grid grid, Coords initial, Coords goal, int visRadius) {
        return dijkstras(grid, initial, goal, visRadius);
    }

    public Coords dijkstras(Grid grid, Coords initial, Coords goal, int visRadius) {
        double[][] costSoFar = new double[grid.world.length][grid.world[0].length];
        for (double[] row : costSoFar) Arrays.fill(row, -1.0);

        PriorityQueue<Coords> frontier = new PriorityQueue<Coords>(11, new Comparator<Coords>() {
            public int compare(Coords a, Coords b) {
                return Double.compare(a.pathCost, b.pathCost);
            }
        });
        Map<Coords, Coords> parents = new HashMap<>();

        // Initialize
        frontier.add(initial);
        parents.put(initial, null);
        costSoFar[initial.x][initial.y] = 0;

        while (!frontier.isEmpty()) {

            Coords curr = frontier.poll();

            // Goal was found
            if (curr.equals(goal)) {
                List<Coords> path = new ArrayList<>();
                while (curr != null) {
                    path.add(curr);
                    curr = parents.get(curr);
                }

                // Return back the position after the initial location
                return path.get(path.size() - 2);
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;

                    int xf = curr.x + dx, yf = curr.y + dy;
                    if (!inBounds(grid, initial, visRadius, xf, yf)) {
                        continue;
                    }

                    double gScore = costSoFar[curr.x][curr.y] + grid.getLocation(xf, yf).getCost();
                    if (costSoFar[xf][yf] == -1.0 || gScore < costSoFar[xf][yf]) {
                        costSoFar[xf][yf] = gScore;
                        Coords next = new Coords(xf, yf);
                        next.pathCost = gScore + heuristic(goal, next);
                        frontier.add(next);
                        parents.put(next, curr);
                    }
                }
            }
        }

        // Means goal wasn't found
        return null;
    }

    // Euclidean distance
    public double heuristic(Coords goal, Coords next) {
        return 0;
    }

    public boolean inBounds(Grid grid, Coords initial, int visRadius, int x, int y) {
        // Check if in range of the grid
        if (x < 0 || y < 0 || x >= grid.world.length || y >= grid.world[0].length) {
            return false;
        }

        // Check if has been visited or is traversalable
        if (grid.getLocation(x, y).getType() == LocationType.OBSTACLE) {
            return false;
        }

        // Check if in vision range
        double euclidean = Math.sqrt(Math.pow((x - initial.x), 2) + Math.pow((y - initial.y), 2));
        if (euclidean > visRadius) {
            return false;
        }

        return true;
    }
}
