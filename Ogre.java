import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * One of the main enemies; ogres will move towards the player 
 * 
 * @author Edwin
 * @version 31.5.24
 */
public class Ogre extends Enemy
{
    private static GreenfootImage[] idleFrames = {
        new GreenfootImage("Ogre/ogre_idle_anim_f0.png"),new GreenfootImage("Ogre/ogre_idle_anim_f1.png"),new GreenfootImage("Ogre/ogre_idle_anim_f2.png"),
        new GreenfootImage("Ogre/ogre_idle_anim_f3.png")
    };
    private static GreenfootImage[] runFrames = {
        new GreenfootImage("Ogre/ogre_run_anim_f0.png"),new GreenfootImage("Ogre/ogre_run_anim_f1.png"),new GreenfootImage("Ogre/ogre_run_anim_f2.png"),
        new GreenfootImage("Ogre/ogre_run_anim_f3.png")
    };
    
    private int frameNum, actNum;
    private GameWorld gw;
    private int homeRadius;
    private SimpleHitbox hitbox;
    private Overlay overlay;
    private Pair target;
    public Ogre(int centerX, int centerY) {
        super(1000, 2, 3, 300, centerX, centerY);        
        homeRadius = 60; 
        for (GreenfootImage img: idleFrames) {
            img.scale(64, 64);
        }
        for (GreenfootImage img: runFrames) {
            img.scale(64, 64);
        }
        setImage(idleFrames[0]);
        actNum = 0;
        frameNum = 0;
        hitbox = new SimpleHitbox(this, getImage().getWidth()/2-11, getImage().getHeight()/2-9, 7, 0);
        //overlay = new Overlay(this, hitbox);
    }
    
    public void addedToWorld(World w) {
        //w.addObject(overlay, getX(), getY());
        super.addedToWorld(w);
    }
    
    public void act()
    { 
        if (!pursuing) {
            // pathfind to this random position in radius
            if (actNum % 400 == 0) {
                target = getRandomPositionWithinRadius(homeRadius);
            }
            if (target != null) moveTowardsTarget(target.f, target.s);
            if (actNum % 20 == 0) h = getHeroInRadius();
        }
        if (h != null && h.getWorld() != null) {
            gw = (GameWorld) getWorld();
            if (hasLineOfSight(new Pair(getX(), getY()), new Pair(h.getX(), h.getY()), processWalls(gw.getObstacles()))) {
                pursuing = true;
                // chase the hero
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
                    setRotation(0);
                }
            } else {
                if (actNum % 60 == 0) aStar(h.getX(), h.getY(), 20, true);
                if (currentPath.size() > 0) {
                    int[] nextPosition = currentPath.peekFirst();
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
                        // Move incrementally towards the waypoint
                        setLocation((int) nextX, (int) nextY);
                        isMoving = true;
                    }
                }      
            }
        }
        if (getWorld().getObjects(Hero.class).size() != 0) {
            if (hitbox.intersectsOval(getWorld().getObjects(Hero.class).get(0))) {
                h = getIntersectingObjects(Hero.class).get(0);
                if (h != null && h.getWorld() != null) {
                    h.takeDamage(damage);
                }
                // maybe red damage animation
            }
        }
        
        animate();
        actNum++;
    }
        
    private void animate() {
        if (actNum % (speed !=0 ? (int) (-6 * speed + 25) : 10) == 0) {
            if (frameNum >= 3) {
                frameNum = 0;
            } else {
                frameNum++;
            }
            if (isMoving) {
                setImage(runFrames[frameNum]);
            } else {
                setImage(idleFrames[frameNum]);
            }
        }
    }
    
    private void moveTowardsTarget(int targetX, int targetY) {
        int currentX = getX();
        int currentY = getY();
        int diffX = targetX - currentX;
        int diffY = targetY - currentY;
    
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);
        double moveX = (diffX / distance) * speed;
        double moveY = (diffY / distance) * speed;
    
        currentX += moveX;
        currentY += moveY;
    
        setLocation(currentX, currentY);
        
        // Check if target is reached
        if (Math.abs(diffX) <= speed && Math.abs(diffY) <= speed) {
            setLocation(targetX, targetY); // Snap to the target if very close
            isMoving = false;
            target = null;
        } else {
            isMoving = true;
        }
    }
}
