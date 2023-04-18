package simulator.src.Algorithms;
import simulator.src.*;
import java.util.*;
public class BFS implements Algo{

    public Coords nextMove(Grid grid, int curX, int curY, int desX, int desY){
        Coords destination;
        destination = breadthFirstSearch(grid, curX, curY, desX, desY);
        return destination;
    }

    public Coords breadthFirstSearch(Grid grid, int startX, int startY, int desX, int desY){
        Queue<LinkedList<Coords>> q = new LinkedList<>();
        boolean [] [] visited = new boolean [grid.world.length][grid.world[0].length];
        q.add(new Coords(startX, startY));
        visited[startX][startY] = true;

        while(!q.isEmpty()){
            
            LinkedList<Coords> path = q.remove();
            int x = path[path.size()-1];
            int y = curr.y;
            if(curr.x == desX && curr.y == desY){ //found goal
                break;
            }

            if(inBounds(visited, x + 1, y)){
                q.add(new Coords(x+1, y));
            }
            if(inBounds(visited, x - 1, y)){
                q.add(new Coords(x-1, y));
            }
            if(inBounds(visited, x, y+1)){
                q.add(new Coords(x, y+1));

            }
            if(inBounds(visited, x, y-1)){
                q.add(new Coords(x, y-1));
            }
        }
        return new Coords(-1, -1); //not quite sure yet
    }

    public boolean inBounds(boolean [][] visited, int x, int y){ //check for obstacles and rubbles and vision radius
        if(x < 0 || y < 0 || x >= visited.length || y >= visited[0].length){
            return false;
        }
        if(visited[x][y]){
            return false;
        }
        return true;
    }
}
