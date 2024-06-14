 import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

// for A* search
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Deque; 

/**
 * Main game enemy superclass
 * 
 * @author Edwin Dong
 * @author Felix Zhao
 * @version 1
 */
public abstract class Enemy extends SuperSmoothMover
{
    protected boolean pursuing, isMoving, tookDamage; // utility booleans (attacking, animation then damage)
    protected int health, speed, damage; // standard variables
    protected double targetRadius; // target hero radius
    protected int centerX, centerY; // home x, home y
    protected Hero h = null; 
    protected int[][] directions = new int[][] {
            {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
    }; // used for pathfinding
    protected Deque<int[]> currentPath; // path the enemy is tracing
    public static int GRID_CHECK = 24; // individual square size (used for a*)
    protected int spawnX; 
    protected int spawnY; // spawn coordniates
    
    protected boolean right;
    protected SimpleHitbox hitbox; // hitbox
    protected Overlay overlay; // overlay that follows hitbox (debugging)
    protected int actNum, frameNum; // used for animation and other misc things
    protected int homeRadius; // radius in which it can move from its home
    
    public Enemy(int health, int speed, int damage, double targetRadius, int centerX, int centerY) {
        this.health = health;
        this.speed = speed;
        this.damage = damage;
        this.targetRadius = targetRadius;
        this.centerX = centerX; this.centerY = centerY;
        currentPath = new LinkedList<int[]>(); // initialize to prevent error
        spawnX = centerX; // set the center coords to spawn coorss
        spawnY = centerY;
    }
     
    /**
     * Act - do whatever the Enemy wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // if the enemy isdead, remove the hitbox
        if (health <= 0) {
            getWorldOfType(GameWorld.class).enemyDied(this);
            SimpleHitbox.allHitboxesInWorld.remove(hitbox);
            getWorld().getObjects(Hero.class).get(0).getGold(1); // add gold
            //getWorld().removeObject(overlay);
            getWorld().removeObject(this); // remove enemy
        }
    }
    
    public Overlay getOverlay() {
        return overlay;
    }
    
     /**
     * Get the hero in the target radius
     */
    protected Hero getHeroInRadius() {
        ArrayList<Hero> hero = (ArrayList<Hero>) getObjectsInRange((int)targetRadius, Hero.class);
        if (hero.size() != 0) {
            return hero.get(0);
        }
        return null;
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
            } else {
                newX = centerX;
                newY = centerY;
                break;
            }
            
        }
        setLocation(curX, curY); // set back to current locatoin
        
        return new Pair(newX, newY);
    }
    
     /**
     * Process the path to be used in pathfinding
     *
     * @param cellData - 2d array of cells, which contain data about a singular tile in the world
     * @param target - one array with 2 elements, x and y coordinate
     */
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
       
        Collections.reverse(path); //reverse the path to use for moving
        return path;
    }
    /**
     * Euclidean distance calculator
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    protected double calculateDistance(int x1, int y1, int x2, int y2)
    {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    /**
     * Method to check if two line segments intersect
     *
     * @param l1 - first line
     * @param l2 - second line
     */
    protected boolean doLinesIntersect(Line l1, Line l2) {
        int o1 = orientation(l1.start, l1.end, l2.start);
        int o2 = orientation(l1.start, l1.end, l2.end);
        int o3 = orientation(l2.start, l2.end, l1.start);
        int o4 = orientation(l2.start, l2.end, l1.end);
        
        // General case
        if (o1 != o2 && o3 != o4) return true;
    
        // Special cases
        // l1 and l2 are collinear and l2.start lies on segment l1
        if (o1 == 0 && onSegment(l1.start, l2.start, l1.end)) return true;
    
        // l1 and l2 are collinear and l2.end lies on segment l1
        if (o2 == 0 && onSegment(l1.start, l2.end, l1.end)) return true;
    
        // l2 and l1 are collinear and l1.start lies on segment l2
        if (o3 == 0 && onSegment(l2.start, l1.start, l2.end)) return true;
    
        // l2 and l1 are collinear and l1.end lies on segment l2
        if (o4 == 0 && onSegment(l2.start, l1.end, l2.end)) return true;

        return false;
    }

    /**
     * Helper method to find the orientation of the ordered triplet (p, q, r)
     *
     * @param p - first point
     * @param q - second point
     * @param r - third point
     */
    protected int orientation(Pair p, Pair q, Pair r) {
        int val = (q.s - p.s) * (r.f - q.f) - (q.f - p.f) * (r.s - q.s);
        if (val == 0) return 0; // collinear
        return (val > 0) ? 1 : 2; // clockwise : counterclockwise
    }

    /**
     * Method to check line of sight
     * @param enemy - coords of enemy
     * @param player - coords of player
     * @param obstacles - List of wall lines
     */
    // Method to check line of sight
    protected boolean hasLineOfSight(Pair enemy, Pair player, List<Line> obstacles) {
        Line sightLine = new Line(enemy, player);
        // sl.updateLine(enemy, player);
        
        for (Line obstacle : obstacles) {
            if (doLinesIntersect(sightLine, obstacle)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Method to check pt q lies on segment pr
     * @param p 
     * @param q
     * @param r
     */
    protected boolean onSegment(Pair p, Pair q, Pair r) {
        if (q.f <= Math.max(p.f, r.f) && q.f >= Math.min(p.f, r.f) &&
            q.s <= Math.max(p.s, r.s) && q.s >= Math.min(p.s, r.s)) {
            return true;
        }
        return false;
    }
    /**
     * Method to unload walls into a list of lines
     * @param a - arraylist of walls
     */
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
    
    // getter for health
    public int getHealth(){
        return health;
    }
    
    // setter for health
    public void setHealth(int health) {
        this.health = health;
    }
    
    // Damage the enemy
    public void takeDamage(int damage) {
        if (!tookDamage && (this instanceof Ogre || this instanceof Wizard)) {
            health -= damage;
            tookDamage = true;
        } else if (this instanceof Imp){
            health -= damage;
            tookDamage = true;
        }
    }
    
    public SimpleHitbox getHitbox() { // getter for hitbox
        return hitbox;
    }

    /**
     * Sets the spawn position when you enter the room
     *
     */
    public void setSpawnPosition(int x, int y) {
        spawnX = x;
        spawnY = y;
    }
    
    
    /**
     * Sets the locations to the spawnX and spawnY
     *
     */
    public void addedToWorld(World w) {
        setLocation(spawnX, spawnY);
        SimpleHitbox.allHitboxesInWorld.add(hitbox); // add hitbox to list
    }

    
    
    /**
     * Path finding algorithim
     *
     * @param targetX The x destination
     * @param targetY The y destination
     * @param radius The min radius/distance the actor has to get in before it counts as found
     * @param overWrite if true, the current path will be overwritten
     */
    protected void aStar(int targetX, int targetY, double radius, boolean overWrite) {
        boolean pathFound = false;
        int exactStartX = getX();
        int exactStartY = getY();
       
        int totalRows = getWorld().getHeight()/GRID_CHECK;
        int totalCols = getWorld().getWidth()/GRID_CHECK;
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
                if (isTouching(Wall.class) ||isTouching(Void.class)) valid = false;
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
    /**
     * Helper Class for a* search
     */ 
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
}
