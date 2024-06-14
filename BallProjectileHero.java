import greenfoot.*;
import java.util.ArrayList;
/**
 * Red glowing projectile (for heroes to use)
 * 
 * @author Edwin Dong, Andy Feng
 * @version (a version number or a date)
 */
public class BallProjectileHero extends SuperSmoothMover
{
    // instance variables - replace the example below with your own
    private GreenfootImage img = new GreenfootImage("glow_ball.png");
    private int size;
    private int damage;
    private int rotation;
    private SimpleHitbox hitbox;
    private Overlay overlay;
    private boolean attackOnce;
    
    /**
     * Constructor for objects of class BallProjectile
     */
    
    public BallProjectileHero(int size, int damage, int rotation)
    {
        this.size = size; 
        this.damage = damage; 
        this.rotation = rotation;
        img.scale(size, size);
        setImage(img);
        setRotation(rotation);
        hitbox = new SimpleHitbox(this, getImage().getWidth()/2-2, getImage().getHeight()/2-2, 0, 0);
        //overlay = new Overlay(this, hitbox);
    }
    
    public void addedToWorld(World world) {
        //world.addObject(overlay, getX(), getY());
        SimpleHitbox.allHitboxesInWorld.add(hitbox);
    }
    
    public void act() {
        ArrayList<Enemy> enemies = (ArrayList<Enemy>) getWorld().getObjects(Enemy.class);
        for (Enemy e : enemies) {
            if (hitbox.intersectsOval(e)) {
                e.takeDamage(damage);
                getWorld().removeObject(overlay);
                getWorld().removeObject(this);
                return;
            }
        }
        move(3);
        if (isTouching(Wall.class) || getX() > 1180 || getY() > 700 || getX() < 10 || getY() < 10) {
            getWorld().removeObject(overlay);
            getWorld().removeObject(this);
        }
    }
}
