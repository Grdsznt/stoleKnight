import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Deque;



/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends SuperSmoothMover
{
    /**
     * Act - do whatever the Enemy wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    protected boolean pursuing, isMoving;
    protected int health, speed, damage;
    protected double targetRadius;
    protected int centerX, centerY;
    protected Hero h = null;
    protected int[][] directions = new int[][] {
            {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };
    protected Deque<int[]> currentPath;
    public static int GRID_CHECK = 16;
    
    public Enemy(int health, int speed, double targetRadius) {
        this.health = health;
        this.speed = speed;
        this.targetRadius = targetRadius;
    }
    
    class Cell {
        private int parent_i, parent_j;
        private double f, g, h;
       
        /**
         * Creates a new cell for pathfinding
         *
         */
        public Cell() {
            this.parent_i = -1;
            this.parent_j = -1;
            this.f = -1;
            this.g = -1;
            this.h = -1;
        }
        /**
         * Returns parent
         *
         * @return Returns the coords of its parent
         */
        public int[] getParent() {
            return new int[]{parent_i, parent_j};
        }
       
        /**
         * Returns f
         *
         * @return Returns the value of f
         */
        public double getF() {
            return f;
        }
       
        /**
         * Returns g
         *
         * @return Returns the value of g
         */
        public double getG() {
            return g;
        }
       
        /**
         * Returns h
         *
         * @return Returns the value h
         */
        public double getH() {
            return h;
        }
       
        /**
         * Changes value of f
         *
         * @param val Sets f to val
         */
        public void setF(double val) {
            f = val;
        }
       
        /**
         * Changes value of g
         *
         * @param val Sets g to val
         */
        public void setG(double val) {
            g = val;
        }
       
        /**
         * Changes value of h
         *
         * @param val Sets h to val
         */
        public void setH(double val) {
            h = val;
        }
       
        /**
         * Sets the parent
         *
         * @param i The i-position (row) of the parent
         * @param j The j-position (col) of the parent
         */
        public void setParent(int i, int j) {
            parent_i = i;
            parent_j = j;
        }
    }

    
    public void act()
    {
        // Add your action code here.
    }
    
    protected Hero getHeroInRadius() {
        return getObjectsInRange((int)targetRadius, Hero.class).get(0);
    }
    
    /**
     * Get a Random Pair of a position in an enemy's home radius
     *
     * @param radius - the enemy's home radius
     */
    protected Pair getRandomPositionWithinRadius(double radius) {
        Random random = new Random();
        boolean isValid = false;
        int curX = getX(), curY = getY();
        int newX = 0, newY = 0;
        
        while (!isValid) {
            double angle = 2 * Math.PI * random.nextDouble(); // Random angle
            double distance = radius * Math.sqrt(random.nextDouble()); // Random distance within the radius
        
            newX = (int) (centerX + distance * Math.cos(angle));
            newY = (int) (centerY + distance * Math.sin(angle));
        
            setLocation(newX, newY);
            
            // Check if the new location intersects with any Obstacles
            if (!isTouching(Wall.class)) {
                isValid = true; // If no intersection, mark as valid and break loop
            }
        }
        setLocation(curX, curY);
        
        return new Pair(newX, newY);
    }
    
    protected void pathfind() {
        
    }
    
    /**
    * <div>
    * All people in the simulation are a subclass of this class
    * This class gives the basic methods to all people
    * </div>
    * Uses A* for path finding. <br>
    * Some information on the algorithm: <a href="https://en.wikipedia.org/wiki/A*_search_algorithm"> A* Star</a><br>
    *
    * Slightly Edited by Andy Feng (path find and avoid algorithm)
    * Edit for this game (Edwin)
    *
    * @author Felix Zhao
    * @version April 8th 2024
    */
    protected void aStar(int targetX, int targetY, double radius, boolean overWrite) {
        boolean pathFound = false;
        int exactStartX = getX();
        int exactStartY = getY();
       
        int totalRows = getWorld().getHeight()/GRID_CHECK;
        int totalCols = getWorld().getWidth()/3*2/GRID_CHECK;
        int targetRow = targetY/GRID_CHECK;
        int targetCol = targetX/GRID_CHECK;
        // r, c for current Row and current col respectivly
        int r = getY()/GRID_CHECK;
        int c = getX()/GRID_CHECK;
        
        // In order of (f, r, c)
        // has to double[] as the first value may have to be a decimal due to diagonal movements - The points (r, c) are casted to int when used
        PriorityQueue<double[]> openList = new PriorityQueue<double[]>((a, b) -> Double.compare(a[0], b[0]));
        boolean[][] closedList = new boolean[totalRows][totalCols];
        Cell[][] cellData = new Cell[totalRows][totalCols];
       
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalCols; j++) {
                cellData[i][j] = new Cell();
            }
        }
       
        cellData[r][c].setF(0);
        cellData[r][c].setG(0);
        cellData[r][c].setH(0);
        cellData[r][c].setParent(r, c);
        openList.add(new double[]{0, r, c});
       
        while (openList.size() != 0) {
            double[] item = openList.poll();
            r = (int)item[1];
            c = (int)item[2];
            if (closedList[r][c]) {
                continue;
            }
            closedList[r][c] = true;
            if (r == targetRow && c == targetCol || (calculateDistance(c,targetCol,r,targetRow)*GRID_CHECK <= radius)) {
                //System.out.println("found path");
                pathFound = true;
                currentPath = new LinkedList<int[]>(tracePath(cellData, new int[]{r, c}));
                break;
            }

            for (int[] position : directions) {
                int newRow = r + position[0];
                int newCol = c + position[1];
                // checks if it's out of bounds
                if (newRow < 0 || newRow >= totalRows || newCol < 0 || newCol >= totalCols) {
                    continue;
                }
                // check if the person can walk through the spot
                int currentX = getX();
                int currentY = getY();
                int currentRotation = getRotation();
                setLocation(newCol*GRID_CHECK, newRow*GRID_CHECK);
                setRotation(0);
                boolean valid = true;
                if (isTouching(Wall.class)) valid = false;
                setLocation(currentX, currentY);
                setRotation(currentRotation);
                if (!valid) {
                    continue;
                }

                if (closedList[newRow][newCol]) {
                    continue;
                }

                double newG = cellData[r][c].getG()+1;
                if (position[0] != 0 && position[1] != 0) {
                    // adds another 0.4 because diagonal movements are longer
                    // 1.4 is near sqrt2
                    newG += 0.4;
                }
                int newH = Math.abs(targetRow-newRow) + Math.abs(targetCol-newCol);
                double newF = newG + newH;
                if (cellData[newRow][newCol].getF() == -1 || cellData[newRow][newCol].getF() > newF) {
                    openList.add(new double[]{newF, newRow, newCol});
                    cellData[newRow][newCol].setF(newF);
                    cellData[newRow][newCol].setG(newG);
                    cellData[newRow][newCol].setH(newH);
                    cellData[newRow][newCol].setParent(r, c);
                }
            }
        }
    }
    
    protected ArrayList<int[]> tracePath(Cell[][] cellData, int[] target) {
        ArrayList<int[]> path = new ArrayList<int[]>();
        int row = target[0];
        int col = target[1];
       
        while (!(cellData[row][col].getParent()[0] == row && cellData[row][col].getParent()[1] == col)) {
            // reveresed as cols is the x-axis and row is the y-axis
            path.add(new int[]{col*GRID_CHECK, row*GRID_CHECK});
            int tempRow = cellData[row][col].getParent()[0];
            int tempCol = cellData[row][col].getParent()[1];
            row = tempRow;
            col = tempCol;
           
        }
       
        Collections.reverse(path);
        return path;
    }
    
    protected double calculateDistance(int x1, int y1, int x2, int y2)
    {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx*dx + dy*dy);
    }
   
    // Method to check if two line segments intersect
    protected boolean doLinesIntersect(Line l1, Line l2) {
        int o1 = orientation(l1.start, l1.end, l2.start);
        int o2 = orientation(l1.start, l1.end, l2.end);
        int o3 = orientation(l2.start, l2.end, l1.start);
        int o4 = orientation(l2.start, l2.end, l1.end);

        if (o1 != o2 && o3 != o4) return true;
        return false;
    }

    // Helper method to find the orientation of the ordered triplet (p, q, r)
    protected int orientation(Pair p, Pair q, Pair r) {
        int val = (q.s - p.s) * (r.f - q.f) - (q.f - p.f) * (r.s - q.s);
        if (val == 0) return 0; // collinear
        return (val > 0) ? 1 : 2; // clockwise : counterclockwise
    }

    // Method to check line of sight
    protected boolean hasLineOfSight(Pair enemy, Pair player, List<Line> obstacles) {
        Line sightLine = new Line(enemy, player);

        for (Line obstacle : obstacles) {
            if (doLinesIntersect(sightLine, obstacle)) {
                return false;
            }
        }
        return true;
    }
    
    protected List<Line> processWalls(ArrayList<Wall> a) {
        List<Line> obstacles = new ArrayList<Line>();
        for (Wall w: a) {
            obstacles.add(w.getTopBoundary());
            obstacles.add(w.getBottomBoundary());
            obstacles.add(w.getLeftBoundary());
            obstacles.add(w.getRightBoundary());
        }
        return obstacles;
    }
    
}
