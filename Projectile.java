import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Projectile here.
 * 
 * @author Andy Feng
 * @version 1.0
 */
public class Projectile extends Actor
{
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage arrow = new GreenfootImage("images/weapon_arrow.png");
    private int direction_X;
    private int direction_Y;
    
    public Projectile(){
        setImage(arrow);
    }
    
    public void act()
    {
        // Add your action code here.
    }
}
