package simulator.algorithms;

import simulator.*;
import java.util.*;

public class BellmanFord implements Algo {
    final int X_RANGE = 8;
    final int Y_RANGE = 8;

    public Coords nextMove(Grid grid, Coords initial, Coords goal, int visRadius) {
        return bellmanFord(grid, initial, goal, visRadius);
    }

    public Coords bellmanFord(Grid grid, Coords initial, Coords goal, int visRadius) {
        int height = grid.world.length, width = grid.world[0].length;
        Coords[][] map = new Coords[height][width];

        int leftX = Math.max(initial.x - visRadius, 0);
        int rightX = Math.min(initial.x + visRadius, height - 1);
        int leftY = Math.max(initial.y - visRadius, 0);
        int rightY = Math.min(initial.y + visRadius, width - 1);
        System.out.println(leftX);
        System.out.println(rightX);
        System.out.println(leftY);
        System.out.println(rightY);

        for (int r = 1; r <= visRadius; r++) {
            for (int x = -X_RANGE; x <= X_RANGE; x++) {
                for (int y = -Y_RANGE; y <= Y_RANGE; y++) {
                    if (distanceSquared(x, y) == r) {
                        int xs = x - sign(x) + initial.x, ys = y - sign(y) + initial.y;
                        int xa = x + sign(x) + initial.x, ya = y + sign(y) + initial.y;
                        map[xs][ys] = new Coords(xs , ys, 1);
                        map[xs][ys].next = new Coords(xa, ya);
                    }
                }
            }
        }

        debug(map);
        Set<String> visited = new HashSet<>(Arrays.asList(encode(0, 0)));

        for (int r = 1; r <= visRadius; r++) {
            for (int x = -X_RANGE; x <= X_RANGE; x++) {
                for (int y = -Y_RANGE; y <= Y_RANGE; y++) {
                    if (distanceSquared(x, y) == r) {
                        if (x < 0 || x > grid.rows || y < 0 || y > grid.columns) {
                            continue;
                        }

                        if (distanceSquared(x, y) > visRadius || grid.getLocation(x, y).getType() == LocationType.OBSTACLE) {
                            continue;
                        }

                        List<int[]> surroundings = new ArrayList<>();
                        for (int dx = -1; dx <= 1; dx++) {
                            for (int dy = -1; dy <= 1; dy++) {
                                if (dx != 0 && dy != 0 && distanceSquared(x + dx, y + dy) <= visRadius) {
                                    surroundings.add(new int[]{dx, dy});
                                }
                            }
                        }

                        final int finalX = x, finalY = y;
                        surroundings.sort(Comparator.comparing(arr -> distanceSquared(finalX + arr[0], finalY + arr[1])));
                        
                        for (int[] arr : surroundings) {
                            int dx = arr[0];
                            int dy = arr[1];
                            if (visited.contains(encode(x + dx, y + dy))) {
                                if (map[x][y].pathCost > map[x + dx][y + dy].pathCost) {
                                    map[x][y].pathCost = map[x + dx][y + dy].pathCost;
                                    if (x + dx == 0 && y + dy == 0) {
                                        map[x][y].next = map[initial.x - dx][initial.y - dy];
                                    } else {
                                        map[x][y].next = map[x + dx][y + dy];
                                    }
                                }
                            }
                        }
                        if (grid.getLocation(x, y) != null) {
                            map[x][y].pathCost += grid.getLocation(x, y).getCost();
                        } else {
                            map[x][y].pathCost += 0;
                        }

                        visited.add(encode(x, y));
                    }
                }
            }
        }
        debug(map);

        // for (int x = leftX; x <= rightX; x++) {
        //     for (int y = leftY; y <= rightY; y++) {
        //         double distance = Math.sqrt(Math.pow(x - initial.x, 2) + Math.pow(y - initial.y, 2));
        //         if (distance <= visRadius) {
        //             map[x][y] = new Coords(x, y, 0);
        //         }
        //     }
        // }

        // int num = 1;
        // for (int radius = 0; radius <= visRadius; radius++) {
        //     // iterate over each point on the current spiral radius
        //     for (int x = initial.x - radius; x <= initial.x + radius; x++) {
        //         for (int y = initial.y - radius; y <= initial.y + radius; y++) {
        //             // check if the current point is within the grid bounds and at the current radius
        //             double distance = Math.sqrt(Math.pow(x - initial.x, 2) + Math.pow(y - initial.y, 2));
        //             // System.out.println("Cost: " + distance + " at (" + x + "," + y + ")");
        //             if (x >= 0 && x < height && y >= 0 && y < width && distance <= radius) {
        //                 if (map[x][y].pathCost == 0) {
        //                     map[x][y].pathCost = num++;
        //                 }
        //             }
        //         }
        //     }
        // }
        
        return null;

    }

    private int distanceSquared(int x, int y) {
        return x * x + y * y;
    }

    private int sign(int x) {
        if (x > 0) {
            return 1;
        } else if (x < 0) {
            return -1;
        }
        return 0;
    }

    private String encode(int x, int y) {
        String encoding = (x >= 0) ? "0" + Integer.toString(x) : "1" + Integer.toString(Math.abs(x));
        encoding += (y >= 0) ? "0" + Integer.toString(y) : "1" + Integer.toString(Math.abs(y));
        return encoding;
    }

    public void debug(Coords[][] map) {
        String res = "";
        for (Coords[] row : map) {
            StringBuilder rowString = new StringBuilder();
            for (Coords e : row) {
                if (e == null) {
                    rowString.append(String.format("%-4s", "*"));
                } else {
                    rowString.append(String.format("%-4.0f", e.pathCost));
                }
            }
            res += rowString.toString() + "\n";
        }
        System.out.println(res);
    }

}
