import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
    private boolean pursuing;
    private int health, speed;
    private int targetRadius;
    
    public Enemy(int health, int speed) {
        this.health = health;
        this.speed = speed;
    }
    
    
    public void act()
    {
        // Add your action code here.
    }
    
    // public boolean detectHero() {
        // return getObjectsInRange(targetRadius, Hero.class);
    // }
    
}
