import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * One of the weapons: bow. This is coupled with an arrow.
 * 
 * @author Andy Feng
 * @version May 2024
 * 
 */public class Projectile extends SuperSmoothMover
{
    private GreenfootImage arrow = new GreenfootImage("images/weapon_arrow.png");
    private int direction_X;
    private int direction_Y;
    private int chargeSpeed;
    private int speed;
    private Actor actor;
    private int deltaX;
    private int deltaY;
    private int angle;
    private boolean firstTime;
    private int baseDamage; // Base damage without charge
    private int maxChargeDamage; // Maximum damage at full charge
    private boolean traveling;
    private int disappearTimer;
    private boolean chargeable;
    private SimpleTimer chargeTimer;

    public Projectile(int direction_X, int direction_Y, int baseDamage, int maxChargeDamage, boolean chargeable){
        setImage(arrow);
        speed = 5;
        firstTime = true;
        this.direction_X = direction_X;
        this.direction_Y = direction_Y;
        this.baseDamage = baseDamage;
        this.maxChargeDamage = maxChargeDamage;
        this.chargeSpeed = chargeSpeed;
        traveling = false;

        disappearTimer = 60;
        this.chargeable = chargeable;
        chargeTimer = new SimpleTimer();
    }

    public void act()
    {   
        if (GameWorld.isMouseHolding() && firstTime) {
            updateTarget();
            angle = calculateRotation();
        } else {
            setRotation(angle - 90);
            firstTime = false;
            traveling = true;
        }
        
        if (traveling) move(speed);
        if (isTouching(Wall.class) || isTouching(Enemy.class)) {
            speed = 0;
            traveling = false;
        }

        disappear();
        
        System.out.println(calculateFinalDamage());
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
                Enemy e = (Enemy) actor;
                int finalDamage = calculateFinalDamage(); // Calculate final damage considering charge
                e.health -= finalDamage;
                speed = 0;
                setLocation(e.getX(), e.getY());
            } else if (actor instanceof Wall) {
                // If it hits a wall, stop the projectile
                speed = 0;
            }
        }
    }

    private int calculateFinalDamage() {
        // Calculate damage based on charge level
        int finalDamage = baseDamage;
        
        return finalDamage;
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
