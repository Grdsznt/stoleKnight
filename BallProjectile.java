import greenfoot.*;
import java.util.ArrayList;
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
    private int size;
    private int damage;
    private int rotation;

    /**
     * Constructor for objects of class BallProjectile
     */
    
    public BallProjectile(int size, int damage, int rotation)
    {
        this.size = size; 
        this.damage = damage; 
        this.rotation = rotation;
        img.scale(size, size);
        setImage(img);
        setRotation(rotation);
    }
    
    public void act() {
        if (isTouching(Hero.class)) {
            causeDamage();
        }
        // double angle = Math.toRadians(rotation);
        // int newX = getX() + (int) (2 * Math.cos(angle));
        // int newY = getY() + (int) (2 * Math.sin(angle));
        // setLocation(newX, newY);
        move(2);
        if (isTouching(Wall.class) || getX() > 1180 || getY() > 700 || getX() < 10 || getY() < 10) getWorld().removeObject(this);
    }
    
    public void causeDamage() {
        ArrayList<Hero> heroes = (ArrayList<Hero>) getIntersectingObjects(Hero.class);
        if (heroes.size() != 0) {
            if (heroes.get(0).getWorld() != null) heroes.get(0).takeDamage(damage);
        }
    }
}
