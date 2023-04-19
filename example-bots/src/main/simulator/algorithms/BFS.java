package simulator.algorithms;
import simulator.*;
import java.util.*;
public class BFS implements Algo{

    public Coords nextMove(Grid grid, Coords initial, Coords goal, int visRadius){
        Coords destination;
        destination = breadthFirstSearch(grid, initial, goal, visRadius);
        return destination;
    }

    public Coords breadthFirstSearch(Grid grid, Coords initial, Coords goal, int visRadius){
        Queue<LinkedList<Coords>> q = new LinkedList<>();
        boolean [] [] visited = new boolean [grid.world.length][grid.world[0].length];
        LinkedList<Coords> path = new LinkedList<>();
        path.add(initial);
        q.add(path);
        visited[initial.x][initial.y] = true;

        while(!q.isEmpty()){
            
            LinkedList<Coords> curr_path = q.remove();
            int x = (curr_path.get(curr_path.size()-1)).x;
            int y = curr_path.get(curr_path.size()-1).y;

            if(x == goal.x && y == goal.y){ //found goal
                return curr_path.get(1); //first move
            }

            for(int row = -1; row <= 1; row ++){
                for(int col = -1; col <= 1; col ++){
                    if(row == 0 && col == 0){
                        continue;
                    }
                    if(inBounds(visited, row, col, visRadius, initial, grid)){
                        LinkedList<Coords> temp = curr_path;
                        temp.add(new Coords(row, col));
                        q.add(temp);
                        visited[x][y] = true;
                    }
                }
            }
        }

        return new Coords(-1, -1); //not quite sure yet
    }

    public boolean inBounds(boolean [][] visited, int x, int y, int visRadius, Coords initial, Grid grid){ //ignores rubble
        if(x < 0 || y < 0 || x >= visited.length || y >= visited[0].length){
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
