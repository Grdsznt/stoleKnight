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
    
    private int chargeTime; // Store the charge time
    private int damage;
    private Overlay overlay;
    protected SimpleHitbox hitbox;
    private boolean dealDamageOnce = true;
    
    private static GreenfootSound[] impactSounds;
    private static int soundFrames;

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
        
        hitbox = new SimpleHitbox(this, getImage().getWidth() / 2, getImage().getHeight() / 2, 0 ,0);
        //overlay = new Overlay(this, hitbox);
        impactSounds = new GreenfootSound[5];
        soundFrames = 0;
        for(int i = 0; i < impactSounds.length; i++) {
            impactSounds[i] = new GreenfootSound("sounds/arrowImpact.mp3");
        }
    }

    
    /**
     * Constrcutor of the Projectile.class
     * 
     * 
     */
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

        hitbox = new SimpleHitbox(this, getImage().getWidth() / 2, getImage().getHeight() / 2, 0 ,0);
        overlay = new Overlay(this, hitbox);
    }
    
    public void addedToWorld(World w) {
        //w.addObject(overlay, getX(), getY());
        SimpleHitbox.allHitboxesInWorld.add(hitbox);
    }
    
    public void act() {
        if(chargeable) {
            if (GameWorld.isMouseHolding() && firstTime) {
                chargeTime++;
                updateTarget();
                angle = calculateRotation();
            } else {
                setRotation(angle - 90);
                hitbox.rotateHitbox(angle - 90);
                firstTime = false;
                traveling = true;
                damage = calculateFinalDamage();
            }
        } else if(!chargeable && firstTime) {
            damage = calculateFinalDamage();
            hitbox.rotateHitbox(calculateRotation() - 90);
            setRotation(calculateRotation() - 90);
            firstTime = false;
            traveling = true;
        }
        
        if (traveling) {
            move(speed);
            checkCollision(); // Check for collisions with other objects
        }
        
        disappear();
        if (getX() > 1180 || getY() > 700 || getX() < 150 || getY() < 10) {
            getWorld().removeObject(overlay);
            getWorld().removeObject(this);
        }
    }

    private int calculateRotation() {
        deltaX = getX() - direction_X;
        deltaY = getY() - direction_Y;
        int rotationAngle = -1 * (int)(Math.atan2(deltaX, deltaY) * 180 / Math.PI);
        return rotationAngle;
    }

    private void checkCollision() {
        ArrayList<SimpleHitbox> hitBoxes = SimpleHitbox.allHitboxesInWorld;
        if(chargeable) {
            for(SimpleHitbox target : hitBoxes) {
                if(target.getActor() instanceof Enemy && hitbox.isHitBoxesIntersecting(target) && speed != 0 && dealDamageOnce) {
                    Enemy e = (Enemy) target.getActor();
                    e.takeDamage(damage);
                    dealDamageOnce = false;
                    playImpactSound();
                }
            }
        } else {
            for(SimpleHitbox target : hitBoxes) {
                if(target.getActor() instanceof Hero && hitbox.isHitBoxesIntersecting(target)) {
                    Hero h = (Hero) target.getActor();
                    h.takeDamage(damage);
                }
            }
        }
        if(isTouching(Wall.class) || isTouching(Void.class) || isTouching(RoomExit.class)) {
            speed = 0;
            damage = 0;
        }
    }

    private int calculateFinalDamage() {
        if(!chargeable) return 1;
        double ratio = (double) (maxChargeDamage - baseDamage) / 100.0;
        int finalDamage = baseDamage + (int)(ratio * chargeTime);
        return Math.min(finalDamage, maxChargeDamage);
    }

    private void disappear() {
        if (speed == 0 && !traveling && isTouching(Wall.class)) {
            disappearTimer--;
        }
    }
    
    private void updateTarget() {
        direction_X = GameWorld.getMouseX();
        direction_Y = GameWorld.getMouseY();
    }
    
    private static void playImpactSound() {
        impactSounds[soundFrames].setVolume(50);
        impactSounds[soundFrames].play();
        soundFrames++;
        if(soundFrames > impactSounds.length) {
            soundFrames = 0;
        }
    }
}