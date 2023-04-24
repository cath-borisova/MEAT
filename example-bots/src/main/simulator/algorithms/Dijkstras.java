package simulator.algorithms;

import simulator.*;
import java.util.*;

public class Dijkstras implements Algo {

    public Coords nextMove(Grid grid, Coords initial, Coords goal, int visRadius) {
        return dijkstras(grid, initial, goal, visRadius);
    }

    public Coords dijkstras(Grid grid, Coords initial, Coords goal, int visRadius) {
        boolean[][] visited = new boolean[grid.world.length][grid.world[0].length];
        PriorityQueue<Coords> frontier = new PriorityQueue<Coords>(11, new Comparator<Coords>() {
            public int compare(Coords a, Coords b) {
                return Double.compare(a.pathCost, b.pathCost);
            }
        });
        Map<Coords, Coords> parents = new HashMap<>();

        // Initialize
        visited[initial.x][initial.y] = true;
        frontier.add(initial);
        parents.put(initial, null);

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
                    if (inBounds(grid, visited, initial, visRadius, xf, yf)) {
                        double cost = curr.pathCost + grid.getLocation(xf, yf).getCost();
                        Coords next = new Coords(xf, yf, cost);
                        frontier.add(next);
                        parents.put(next, curr);
                        visited[next.x][next.y] = true;
                    }
                }
            }
        }

        // Means goal wasn't found
        return null;
    }

    // Dijkstras Ignores rubble since there is no heuristic involved
    public boolean inBounds(Grid grid, boolean[][] visited, Coords initial, int visRadius, int x, int y) {
        // Check if in range of the grid
        if (x < 0 || y < 0 || x >= grid.world.length || y >= grid.world[0].length) {
            return false;
        }

        // Check if has been visited or is traversalable
        if (visited[x][y] || grid.getLocation(x, y).getType() == LocationType.OBSTACLE) {
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
