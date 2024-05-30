import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;

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
    protected int targetRadius;
    protected int centerX, centerY;
    
    public Enemy(int health, int speed) {
        this.health = health;
        this.speed = speed;
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
    
    public void act()
    {
        // Add your action code here.
    }
    
    // public boolean detectHero() {
        // return getObjectsInRange(targetRadius, Hero.class);
    // }
    
    private Pair getRandomPositionWithinRadius(double radius) {
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
    
}
