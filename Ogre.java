import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * One of the main enemies; ogres will move towards the player 
 * 
 * @author Edwin
 * @version 1
 */
public class Ogre extends Enemy
{
    
    // Animation arrays
    private static GreenfootImage[] idleFrames = {
        new GreenfootImage("Ogre/ogre_idle_anim_f0.png"),new GreenfootImage("Ogre/ogre_idle_anim_f1.png"),new GreenfootImage("Ogre/ogre_idle_anim_f2.png"),
        new GreenfootImage("Ogre/ogre_idle_anim_f3.png")
    };
    private static GreenfootImage[] runFrames = {
        new GreenfootImage("Ogre/ogre_run_anim_f0.png"),new GreenfootImage("Ogre/ogre_run_anim_f1.png"),new GreenfootImage("Ogre/ogre_run_anim_f2.png"),
        new GreenfootImage("Ogre/ogre_run_anim_f3.png")
    };
    
    private static GreenfootImage[] redIdleFrames = {
        new GreenfootImage("Ogre/ogre_run_anim_f0_red.png"),new GreenfootImage("Ogre/ogre_run_anim_f1_red.png"),new GreenfootImage("Ogre/ogre_run_anim_f2_red.png"),
        new GreenfootImage("Ogre/ogre_run_anim_f3_red.png")
    };
    private static GreenfootImage[] redRunFrames = {
        new GreenfootImage("Ogre/ogre_run_anim_f0_red.png"),new GreenfootImage("Ogre/ogre_run_anim_f1_red.png"),new GreenfootImage("Ogre/ogre_run_anim_f2_red.png"),
        new GreenfootImage("Ogre/ogre_run_anim_f3_red.png")
    };
    private GameWorld gw;
    private Pair target;
    private int damageTimer; // damage timer for red animation
    public Ogre(int centerX, int centerY) {
        super(40, 2, 2, 300, centerX, centerY);        
        homeRadius = 60; 
        for (GreenfootImage img: idleFrames) {
            img.scale(64, 64); // scale the images to 64, 64
        }
        for (GreenfootImage img: runFrames) {
            img.scale(64, 64);
        }
        for (GreenfootImage img: redIdleFrames) {
            img.scale(64, 64);
        }
        for (GreenfootImage img: redRunFrames) {
            img.scale(64, 64);
        }
        setImage(idleFrames[0]); 
        // initialize variables to 0
        actNum = 0;
        frameNum = 0;
        damageTimer = 0;
        tookDamage = false;
        hitbox = new SimpleHitbox(this, getImage().getWidth()/2-11, getImage().getHeight()/2-9, 7, 0); // new hitbox
        // overlay = new Overlay(this, hitbox);
    }
    
    public void addedToWorld(World w) {
        // w.addObject(overlay, getX(), getY());
        super.addedToWorld(w);        
    }
    
    public void act()
    { 
        if (!pursuing) { // if attacking
            if (actNum % 200 == 0) {
                target = getRandomPositionWithinRadius(homeRadius); // get random position in the radius
            }
            if (target != null) {
                moveTowardsTarget(target.f, target.s); // move to this random position in radius
                
            }
            if (actNum % 20 == 0) h = getHeroInRadius(); // detect hero every 20 acts
        }
        if (h != null && h.getWorld() != null) { // If the hero is in the world, and there is a hero detected
            gw = (GameWorld) getWorld(); 
            if (hasLineOfSight(new Pair(getX(), getY()), new Pair(h.getX(), h.getY()), processWalls(gw.getObstacles()))) { // if has line of sight with hero
                pursuing = true; // chase the hero
                // Calculate the direction vector from enemy to player
                float dx = h.getX() - getX();
                float dy = h.getY() - getY();
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                
                // Normalize the direction vector
                if (distance != 0) {
                    dx /= distance;
                    dy /= distance;
                }
                
                // Move the enemy towards the player
                setLocation(getX() + dx * speed, getY() + dy * speed);
                isMoving = true;
                // if the hero is no longer in the radius do not pursue
                if (calculateDistance(getX(), getY(), h.getX(), h.getY()) > targetRadius) {
                    pursuing = false;
                    h = null;
                    isMoving = false; 
                    setRotation(0); // set rotation to 0
                }
            } else {
                if (actNum % 60 == 0) aStar(h.getX(), h.getY(), 55, true); // no line of sight, pathfind to enemy (prevent walking in walls)
                if (currentPath.size() > 0) {
                    int[] nextPosition = currentPath.peekFirst(); // get the next tile to move to in current path
                    float dx = nextPosition[0] - getX();
                    float dy = nextPosition[1] - getY();
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
                    // Normalize the direction vector
                    if (distance != 0) {
                        dx /= distance;
                        dy /= distance;
                    }
        
                    // Calculate next step
                    float nextX = getX() + dx * (float) speed;
                    float nextY = getY() + dy * (float) speed;
        
                    // Check if the next step oversteps the target
                    if (Math.sqrt(Math.pow(nextPosition[0] - nextX, 2) + Math.pow(nextPosition[1] - nextY, 2)) < speed) {
                        // Move directly to the point if the next step is beyond it
                        setLocation(nextPosition[0], nextPosition[1]);
                        currentPath.pollFirst(); // Remove the reached point
                    } else {
                        // Move towards the point
                        setLocation((int) nextX, (int) nextY);
                        isMoving = true;
                    }
                }      
            }
        }
        if (getWorld().getObjects(Hero.class).size() != 0) {
            if (hitbox.intersectsOval(getWorld().getObjects(Hero.class).get(0))) {
                h = getIntersectingObjects(Hero.class).get(0); // if the enemy hitbox intersectsthe hero's
                if (h != null && h.getWorld() != null) {
                    h.takeDamage(damage); // deal damage to the hero
                }
            }
        }
        if (tookDamage) { // switch to a red version of ogre if damaged
            if (damageTimer == 0) damageTimer = actNum;
            if (damageTimer != 0 && Math.abs(actNum - damageTimer) > 20) {
                tookDamage = false;
                damageTimer = 0;
            }
        }
        animate(); // handle animation
        actNum++;
        super.act(); // handle death
    }
        
    private void animate() {
        // animate, modify with y = mx + b
        if (actNum % (speed !=0 ? (int) (-6 * speed + 25) : 10) == 0) {
            if (frameNum >= 3) {
                frameNum = 0;
            } else {
                frameNum++;
            }
            if (isMoving) { // if took damage set to red version, otherwise, use either idle image or running image based on isMoving
                if (tookDamage) setImage(redRunFrames[frameNum]);
                else setImage(runFrames[frameNum]);
            } else {
                if (tookDamage) setImage(redIdleFrames[frameNum]);
                else setImage(idleFrames[frameNum]);
            }
        }
    }
    
    /**
     * Moves the object towards a specified target location.
     * 
     * <p>This method calculates the direction and distance to the target location
     * and moves the object by a fixed speed in that direction. If the object is 
     * very close to the target, it will snap to the target and stop moving.</p>
     * 
     * @param targetX The X coordinate of the target location
     * @param targetY The Y coordinate of the target location
     */
    private void moveTowardsTarget(int targetX, int targetY) {
        int currentX = getX();
        int currentY = getY();
        int diffX = targetX - currentX;
        int diffY = targetY - currentY;
    
        double distance = Math.sqrt(diffX * diffX + diffY * diffY); // euclidean distance
        double moveX = (diffX / distance) * speed;
        double moveY = (diffY / distance) * speed;
    
        currentX += moveX;
        currentY += moveY; // add movement to x and y coords
    
        setLocation(currentX, currentY);
        if (isTouching(Wall.class) || isTouching(Void.class)) {
            setLocation(currentX-moveX, currentY-moveY);
            isMoving = false;
            target = null;
            return;
        }
        
        // Check if target is reached
        if (Math.abs(diffX) <= speed && Math.abs(diffY) <= speed) {
            setLocation(targetX, targetY); // Snap to the target if very close
            isMoving = false;
            target = null;
        } else {
            isMoving = true;
        }
    }
    
    public SimpleHitbox getHitBox() { // getter for the hitbox
        return hitbox;
    }
}
