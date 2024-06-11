import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * One of the weapons: bow. This is coupled with an arrow.
 * 
 * @author Andy Feng
 * @version June 10th, 2024
 */
public class Projectile extends SuperSmoothMover
{
    private GreenfootImage arrow = new GreenfootImage("images/weapon_arrow.png");
    private int direction_X;
    private int direction_Y;
    private int speed;
    private ArrayList<Actor> actors;
    private int deltaX;
    private int deltaY;
    private int angle;
    private boolean firstTime;
    private int baseDamage; // Base damage without charge
    private int maxChargeDamage; // Maximum damage at full charge
    private boolean traveling;
    private int disappearTimer;
    private boolean chargeable;
    private Actor hit;
    
    private int chargeTime; // Store the charge time
    private int damage;

    public Projectile(int direction_X, int direction_Y, int baseDamage, int maxChargeDamage, boolean chargeable) {
        setImage(arrow);
        speed = 10;
        firstTime = true;
        this.direction_X = direction_X;
        this.direction_Y = direction_Y;
        this.baseDamage = baseDamage;
        this.maxChargeDamage = maxChargeDamage;
        traveling = false;
        disappearTimer = 60;
        this.chargeable = chargeable;
        chargeTime = 0; // Initialize charge time
    }

    public Projectile(int direction_X, int direction_Y) {
        setImage(arrow);
        speed = 10;
        firstTime = true;
        this.direction_X = direction_X;
        this.direction_Y = direction_Y;
        this.baseDamage = 0;
        this.maxChargeDamage = 0;
        traveling = false;
        disappearTimer = 60;
        this.chargeable = false;
        
        chargeTime = 0; // Initialize charge time
    }
    
    public void act() {
        if(chargeable) {
            if (GameWorld.isMouseHolding() && firstTime) {
                chargeTime++;
                updateTarget();
                angle = calculateRotation();
            } else {
                setRotation(angle - 90);
                firstTime = false;
                traveling = true;
                damage = calculateFinalDamage();
            }
        } else if(!chargeable && firstTime) {
            damage = calculateFinalDamage();
            setRotation(calculateRotation() - 90);
            firstTime = false;
            traveling = true;
        }
        
        if (traveling) {
            move(speed);
            checkCollision(); // Check for collisions with other objects
        }
        
        if(hit != null) {
            if(hit instanceof Hero) {
                Hero hero = (Hero) hit;
                setLocation(hero.getX() + (hero.right ? -5 : 5), hero.getY());
            } else if(hit instanceof Enemy) {
                Enemy e = (Enemy) hit;
                setLocation(e.getX() + (e.right ? -5 : 5), e.getY());
            } else if(hit instanceof Wall) {
                setLocation(getX(), getY());
            }
        }
        
        disappear();
    }

    private int calculateRotation() {
        deltaX = getX() - direction_X;
        deltaY = getY() - direction_Y;
        int rotationAngle = -1 * (int)(Math.atan2(deltaX, deltaY) * 180 / Math.PI);
        return rotationAngle;
    }

    private void checkCollision() {
        // Check if the projectile has hit any actors
        actors = (ArrayList<Actor>) getIntersectingObjects(Actor.class);
        if (actors.size() != 0) {
            Actor actor = actors.get(0);
            if (actor instanceof Enemy) {
                Enemy e = (Enemy) actor;
                e.health -= damage;
                speed = 0;
                hit = e; // Store the hit object
            } else if (actor instanceof Wall) {
                // If it hits a wall, stop the projectile
                Wall wall = (Wall) actor;
                speed = 0;
                hit = wall; // Store the hit object (in this case, the arrow itself)
            }
        }
    }

    private int calculateFinalDamage() {
        if(!chargeable) return 1;
        double ratio = (double) (maxChargeDamage - baseDamage) / 100.0;
        int finalDamage = baseDamage + (int)(ratio * chargeTime);
        return Math.min(finalDamage, maxChargeDamage);
    }

    private void disappear() {
        if (speed == 0 && !traveling) {
            disappearTimer--;
        }
        if (disappearTimer == 0) {
            getWorld().removeObject(this);
            return;
        }
    }
    
    private void updateTarget() {
        direction_X = GameWorld.getMouseX();
        direction_Y = GameWorld.getMouseY();
    }
}
