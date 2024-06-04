import greenfoot.*;
/**
 * Write a description of class BallProjectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BallProjectile extends Actor
{
    // instance variables - replace the example below with your own
    private GreenfootImage img = new GreenfootImage("glow_ball.png");
    private int size, damage, rotation;

    /**
     * Constructor for objects of class BallProjectile
     */
    
    public BallProjectile(int size, int damage, int rotation)
    {
        this.size = size; this.damage = damage; this.rotation = rotation;
        img.scale(size, size);
        setImage(img);
        setRotation(rotation);
    }
    
    public void act() {
        // move this in the rotation direction
    }
    
    public void causeDamage() {
        ArrayList<Hero> heroes = (ArrayList<Hero>) getIntersectingObjects(Hero.class);
        heroes.get(0).health -= damage;
    }
    

}
