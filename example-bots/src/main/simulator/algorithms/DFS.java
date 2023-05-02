package simulator.algorithms;
import simulator.*;
import java.util.*;
public class DFS implements Algo {
    Coords coords;

    public Coords nextMove(Grid grid, Coords initial, Coords goal, int visRadius){
        Coords destination;
        destination = depthFirstSearch(grid, initial, goal, visRadius);
        return destination;
    }
    public Coords depthFirstSearch(Grid grid, Coords initial, Coords goal, int visRadius){
        Stack<Coords> s = new Stack<>();
        boolean [] [] visited = new boolean [grid.rows][grid.columns];
        Map<Coords, Coords> parents = new HashMap<>();
        parents.put(initial, null);
        s.push(initial);
        visited[initial.x][initial.y] = true;

        while(!s.empty()){
            Coords curr = s.pop();

            // Goal was found
            if (curr.equals(goal)) {
                List<Coords> path = new ArrayList<>();
                while (curr != null) {
                    path.add(curr);
                    curr = parents.get(curr);
                }
                //System.out.println(path);
                // Return back the position after the initial location
                return path.get(path.size() - 2);
            }
            int x = curr.x;
            int y = curr.y;


            for (int row = -1; row <= 1; row++) {
                for (int col = -1; col <= 1; col++) {
                    if (row != 0 || col != 0) {
                        Coords next = new Coords(x + row, y + col);
                        if(inBounds(visited, next.x, next.y, visRadius, initial, grid)) {
                            visited[next.x][next.y] = true;
                            parents.put(next, curr);
                            s.push(next);

                        }
                    }
                }
            }

        }

            System.out.println(s);
            for(boolean[] array : visited){
                System.out.println(Arrays.toString(array));
            }
            //System.out.println(Arrays.toString(visited));
            System.exit(1);

        return new Coords(-1, -1);
    }

    public boolean inBounds(boolean [][] visited, int x, int y, int visRadius, Coords initial, Grid grid){ //ignores rubble
        if(x < 0 || y < 0 || x >= grid.rows || y >= grid.columns){
            return false;
        }

        if(visited[x][y]){
            return false;
        }
        double euclidean = Math.sqrt(Math.pow((x - initial.x), 2) + Math.pow((y - initial.y), 2));
        if(euclidean > visRadius){
            return false;
        }
        if(grid.getLocation(x, y).getType() == LocationType.OBSTACLE){
            return false;
        }
        return true;
    }
}
