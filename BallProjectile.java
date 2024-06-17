import greenfoot.*;
import java.util.ArrayList;
/**
 * Red glowing projectile
 * 
 * @author Edwin Dong
 * @author Andy Feng
 * @version 0.1
 */
public class BallProjectile extends SuperSmoothMover
{
    // instance variables - replace the example below with your own
    private GreenfootImage img = new GreenfootImage("glow_ball.png");
    private int size;
    private int damage;
    private int rotation;
    private SimpleHitbox hitbox;
    private boolean attackOnce;
    protected Class<?> target;
    
    
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
        hitbox = new SimpleHitbox(this, getImage().getWidth()/2-2, getImage().getHeight()/2-2, 0, 0);
        
        target = Hero.class;
    }
    
    public void addedToWorld(World world) {
        //world.addObject(overlay, getX(), getY());
        SimpleHitbox.allHitboxesInWorld.add(hitbox);
    }
    
    public void act() {
        if (getWorld().getObjects(Hero.class).size() != 0) {
            if (hitbox.intersectsOval(getWorld().getObjects(Hero.class).get(0)) && target == Hero.class) {
                causeDamage();
                getWorld().removeObject(this);
                return;
            }
        }
        
        if(getWorld().getObjects(Enemy.class).size() != 0) {
            if(hitbox.intersectsOval(getWorld().getObjects(Enemy.class).get(0)) && target == Enemy.class) {
                causeDamage();
                getWorld().removeObject(this);
                return;
            }
        }
        
        // double angle = Math.toRadians(rotation);
        // int newX = getX() + (int) (2 * Math.cos(angle));
        // int newY = getY() + (int) (2 * Math.sin(angle));
        // setLocation(newX, newY);
        move(3);
        if (isTouching(Wall.class) || getX() > 1180 || getY() > 700 || getX() < 150 || getY() < 10) {
            getWorld().removeObject(this);
        }
    }
    
    private void causeDamage() {
        ArrayList<Hero> heroes = (ArrayList<Hero>) getIntersectingObjects(Hero.class);
        ArrayList<Enemy> enemies = (ArrayList<Enemy>) getIntersectingObjects(Enemy.class);
        if (heroes.size() != 0 && target == Hero.class) {
            if (heroes.get(0).getWorld() != null) heroes.get(0).takeDamage(damage);
        }
        if (enemies.size() != 0 && target == Enemy.class) {
            if (enemies.get(0).getWorld() != null) enemies.get(0).takeDamage(damage);
        }
    }
}
