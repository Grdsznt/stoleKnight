import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;
import java.util.List;

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
    protected boolean pursuing;
    protected int health, speed, damage;
    protected double targetRadius;
    protected int centerX, centerY;
    protected Hero h = null;
    
    public Enemy(int health, int speed, double targetRadius) {
        this.health = health;
        this.speed = speed;
        this.targetRadius = targetRadius;
    }
    
    /**
     * Pair is a utility class that stores 2 numbers. 
     * <p>
     * Used for coordinates in the world's tile system.
     */
    protected class Pair {
        int f, s;
        public Pair(int first, int second) {
           f = first; s = second; // stores 2 numbers
        } 
    }
    
     /**
     * Line is a utility class that stores 2 pairs. 
     * <p>
     * Used for Line of sight.
     */
    protected class Line {
        Pair start, end;
        Line(Pair start, Pair end) {
            this.start = start;
            this.end = end;
        }
    }

    
    public void act()
    {
        // Add your action code here.
    }
    
    // public boolean detectHero() {
        // return getObjectsInRange(targetRadius, Hero.class);
    // }
    
    protected Hero getHeroInRadius() {
        return getObjectsInRange((int)targetRadius, Hero.class).get(0);
    }
    
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
            // if (!detectedObstacles()) {
                // isValid = true; // If no intersection, mark as valid and break loop
            // }
        }
        setLocation(curX, curY);
        
        return new Pair(newX, newY);
    }
    
    protected void pathfind() {
        
    }
    
    protected double calculateDistance(int x1, int y1, int x2, int y2)
    {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx*dx + dy*dy);
    }
   
    // Method to check if two line segments intersect
    public boolean doLinesIntersect(Line l1, Line l2) {
        int o1 = orientation(l1.start, l1.end, l2.start);
        int o2 = orientation(l1.start, l1.end, l2.end);
        int o3 = orientation(l2.start, l2.end, l1.start);
        int o4 = orientation(l2.start, l2.end, l1.end);

        if (o1 != o2 && o3 != o4) return true;
        return false;
    }

    // Helper method to find the orientation of the ordered triplet (p, q, r)
    public int orientation(Pair p, Pair q, Pair r) {
        int val = (q.s - p.s) * (r.f - q.f) - (q.f - p.f) * (r.s - q.s);
        if (val == 0) return 0; // collinear
        return (val > 0) ? 1 : 2; // clockwise : counterclockwise
    }

    // Method to check line of sight
    public boolean hasLineOfSight(Pair enemy, Pair player, List<Line> obstacles) {
        Line sightLine = new Line(enemy, player);

        for (Line obstacle : obstacles) {
            if (doLinesIntersect(sightLine, obstacle)) {
                return false;
            }
        }
        return true;
    }
    
}
