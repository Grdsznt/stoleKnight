import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Projectile here.
 * 
 * @author Andy Feng
 * @version 1.0
 */
public class Projectile extends Actor
{
    private GreenfootImage arrow = new GreenfootImage("images/weapon_arrow.png");
    private int direction_X;
    private int direction_Y;
    private int speed = 5;
    private Actor actor;
    private int deltaX;
    private int deltaY;
    private int angle;
    private boolean firstTime;
    private int damage;
    
    public Projectile(int direction_X, int direction_Y, int damage){
        setImage(arrow);
        this.direction_X = direction_X;
        this.direction_Y = direction_Y;
        firstTime = true;
        this.damage = damage;
    }
    
    public void act()
    {   
        if(firstTime) {
            angle = calculateRotation();
            setRotation(angle - 90);
            firstTime = false;
        }
        
        move(speed);
    }

    private int calculateRotation(){
        deltaX = getX() - direction_X;
        deltaY = getY() - direction_Y;
        int rotationAngle = -1 * (int)(Math.atan2(deltaX, deltaY) * 180 / Math.PI);
        
        return rotationAngle;
    }
    
    private void checkCollision() {
        // Check if the projectile has hit any actors
        actor = getOneIntersectingObject(Actor.class);
        
        if (actor != null) {
            if (actor instanceof Enemy) {
                // If it hits an enemy, you might want to deal damage or remove the projectile
                Enemy e = (Enemy) actor;
                e.health -= damage;
                speed = 0;
                setLocation(e.getX(), e.getY());
            } else if (actor instanceof Wall) {
                // If it hits a wall, stop the projectile
                speed = 0;
            }
        }
    }
}
