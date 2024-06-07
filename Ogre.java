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
        super(1000, 2, 3, 200, centerX, centerY);
        setImage(idleFrames[0]);
        homeRadius = 60; 
        for (GreenfootImage img: idleFrames) {
            img.scale(64, 64);
        }
        for (GreenfootImage img: runFrames) {
            img.scale(64, 64);
        }
        actNum = 0;
        frameNum = 0;
        hitbox = new SimpleHitbox(this, getImage().getWidth()/2-10, getImage().getHeight()/2-5);
        overlay = new Overlay(this, hitbox);
    }
    
    public void addedToWorld(World w) {
        w.addObject(overlay, getX(), getY());
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
        if (h != null) {
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
        }
        if (isTouching(Hero.class)) {
            h = getIntersectingObjects(Hero.class).get(0);
            if (h != null) {
                h.takeDamage(damage);
            }
            // maybe red damage animation
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
    
    private boolean intersectsOval(Actor other) {
        // Get the center of the oval hitbox (enemy)
        int enemyCenterX = getX();
        int enemyCenterY = getY();

        // Get the bounding box of the other actor
        GreenfootImage otherImage = other.getImage();
        int otherWidth = otherImage.getWidth();
        int otherHeight = otherImage.getHeight();
        int otherLeft = other.getX() - otherWidth / 2;
        int otherRight = other.getX() + otherWidth / 2;
        int otherTop = other.getY() - otherHeight / 2;
        int otherBottom = other.getY() + otherHeight / 2;
        int radiusX = getImage().getWidth() / 2;
        int radiusY = getImage().getHeight() / 2;

        // Check each corner of the other actor's bounding box
        return ellipseContains(enemyCenterX, enemyCenterY, radiusX, radiusY, otherLeft, otherTop)
            || ellipseContains(enemyCenterX, enemyCenterY, radiusX, radiusY, otherRight, otherTop)
            || ellipseContains(enemyCenterX, enemyCenterY, radiusX, radiusY, otherLeft, otherBottom)
            || ellipseContains(enemyCenterX, enemyCenterY, radiusX, radiusY, otherRight, otherBottom);
    }

    private boolean ellipseContains(int centerX, int centerY, int radiusX, int radiusY, int x, int y) {
        // Translate point (x, y) to the coordinate space of the ellipse
        int dx = x - centerX;
        int dy = y - centerY;

        // Check if the point (dx, dy) is within the ellipse
        return (dx * dx) / (double) (radiusX * radiusX) + (dy * dy) / (double) (radiusY * radiusY) <= 1.0;
    }
}
