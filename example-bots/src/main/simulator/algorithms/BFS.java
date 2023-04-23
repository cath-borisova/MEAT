package simulator.algorithms;
import simulator.*;
import java.util.*;
public class BFS implements Algo{

    public Coords nextMove(Grid grid, Coords initial, Coords goal, int visRadius){
        return breadthFirstSearch(grid, initial, goal, visRadius);
    }

    public Coords breadthFirstSearch(Grid grid, Coords initial, Coords goal, int visRadius){
        boolean[][] visited = new boolean [grid.world.length][grid.world[0].length];
        Queue<Coords> queue = new LinkedList<>();
        Map<Coords, Coords> parent = new HashMap<>();
        visited[initial.x][initial.y] = true;
        queue.add(initial);

        while(!queue.isEmpty()){
            
            Coords curr = queue.remove();
            // Goal was found
            if (curr.equals(goal)) {
                List<Coords> path = new ArrayList<>();
                while (curr != null) {
                    path.add(curr);
                    curr = parent.get(curr);
                }

                // Return back the position after the initial location
                return path.get(path.size() - 2);
            }

            for(int dx = -1; dx <= 1; dx ++){
                for(int dy = -1; dy <= 1; dy ++){
                    if (dx == 0 && dy == 0) continue;

                    int xf = curr.x + dx, yf = curr.y + dy;
                    if(inBounds(grid, visited, initial, visRadius, xf, yf)){
                        Coords neighbor = new Coords(xf, yf);
                        queue.add(neighbor);
                        parent.put(neighbor, curr);
                        visited[neighbor.x][neighbor.y] = true;
                    }
                }
            }
        }

        // Means goal wasn't found
        return null;
    }

    // BFS Ignores rubble since there is no heuristic involved
    public boolean inBounds(Grid grid, boolean[][] visited, Coords initial, int visRadius, int x, int y){
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
