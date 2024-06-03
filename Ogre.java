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
        new GreenfootImage("Ogre/ogre_idle_anim_f0"),new GreenfootImage("Ogre/ogre_idle_anim_f1"),new GreenfootImage("Ogre/ogre_idle_anim_f2"),
        new GreenfootImage("Ogre/ogre_idle_anim_f3")
    };
    private static GreenfootImage[] runFrames = {
        new GreenfootImage("Ogre/ogre_run_anim_f0"),new GreenfootImage("Ogre/ogre_run_anim_f1"),new GreenfootImage("Ogre/ogre_run_anim_f2"),
        new GreenfootImage("Ogre/ogre_run_anim_f3")
    };
    
    private int frameNum, actNum;
    private GameWorld gw;
    private int homeRadius;
    Pair target;
    public Ogre() {
        super(1000, 3, 50);
        homeRadius = 30;
    }
    public void act()
    {
        if (!pursuing) {
            // pathfind to this random position in radius
            if (actNum % 600 == 0) {
                target = getRandomPositionWithinRadius(homeRadius);
            }
            if (target != null) moveTowardsTarget(target.f, target.s);
            if (actNum % 20 == 0) h = getHeroInRadius();
        }
        
        if (h != null && hasLineOfSight(new Pair(getX(), getY()), new Pair(h.getX(), h.getY()), processWalls(gw.getObstacles()))) {
            pursuing = true;
            // chase the hero
            turnTowards(h.getX(), h.getY());
            move(speed);
            isMoving = true;
            // if the hero is no longer in the radius do not pursue
            if (calculateDistance(getX(), getY(), h.getX(), h.getY()) > targetRadius) {
                pursuing = false;
                h = null;
                isMoving = false;
            }
        } else if (h != null) {
            if (currentPath.size() > 0) {
                double distanceRequired = speed;
                int[] position = currentPath.peekFirst();
                turnTowards(position[0], position[1]);
                double distance = calculateDistance(getX(), position[0], getY(), position[1]);
                if (distance <= speed) {
                    while (distanceRequired >= distance && currentPath.size() > 0) {
                        setLocation(position[0], position[1]);
                        currentPath.pollFirst();
                        distanceRequired -= distance;
                        if (currentPath.size() == 0) {
                            break;
                        }
                        position = currentPath.peekFirst();
                        distance = calculateDistance(getX(), position[0], getY(), position[1]);
                        turnTowards(position[0], position[1]);
                    }
                    if (currentPath.size() > 0) {
                        move(distanceRequired);
                    }
                } else {
                    move(speed);
                }
            }
        }
        animate();
        actNum++;
    }
    
    private void animate() {
        if (actNum % (speed !=0 ? (int) (-6 * speed + 20) : 10) == 0) {
            if (frameNum >= 4) {
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
