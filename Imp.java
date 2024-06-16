import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * One of the main enemies; Imps will move towards the player (faster)
 * 
 * @author Edwin
 * @version 1
 */
public class Imp extends Enemy
{
    // Animation frames (no need for red version since already red and dies in one shot)
    private static GreenfootImage[] idleFrames = {
        new GreenfootImage("Imp/imp_idle_anim_f0.png"),new GreenfootImage("Imp/imp_idle_anim_f1.png"),new GreenfootImage("Imp/imp_idle_anim_f2.png"),
        new GreenfootImage("Imp/imp_idle_anim_f3.png")
    };
    private static GreenfootImage[] runFrames = {
        new GreenfootImage("Imp/imp_run_anim_f0.png"),new GreenfootImage("Imp/imp_run_anim_f1.png"),new GreenfootImage("Imp/imp_run_anim_f2.png"),
        new GreenfootImage("Imp/imp_run_anim_f3.png")
    };
    
    private static GreenfootImage[] redIdleFrames = {
        new GreenfootImage("Imp/imp_run_anim_f0_red.png"),new GreenfootImage("Imp/imp_run_anim_f1_red.png"),new GreenfootImage("Imp/imp_run_anim_f2_red.png"),
        new GreenfootImage("Imp/imp_run_anim_f3_red.png")
    };
    private static GreenfootImage[] redRunFrames = {
        new GreenfootImage("Imp/imp_run_anim_f0_red.png"),new GreenfootImage("Imp/imp_run_anim_f1_red.png"),new GreenfootImage("Imp/imp_run_anim_f2_red.png"),
        new GreenfootImage("Imp/imp_run_anim_f3_red.png")
    };
    
    private Pair target; // target coords
    private GameWorld gw;
    private int damageTimer; // damage timer for red animation
    /**
     * Act - do whatever the Imp wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (!pursuing) {
            if (actNum % 200 == 0) {
                target = getRandomPositionWithinRadius(homeRadius); // get random position in radius
            }
            if (target != null) moveTowardsTarget(target.f, target.s); // pathfind to this random position in radius
            if (actNum % 20 == 0) h = getHeroInRadius();
        }
        if (h != null && h.getWorld() != null) {// If the hero is in the world, and there is a hero
            gw = (GameWorld) getWorld(); 
            if (hasLineOfSight(new Pair(getX(), getY()), new Pair(h.getX(), h.getY()), processWalls(gw.getObstacles()))) { // if has line of sight with hero
                pursuing = true;  // chase the hero
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
                if (actNum % 30 == 0) aStar(h.getX(), h.getY(), 25, true); // no line of sight, pathfind (avoid walls)
                if (currentPath.size() > 0) { // if found a path
                    int[] nextPosition = currentPath.peekFirst(); // get next tile to move to
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
            if (hitbox.intersectsOval(getWorld().getObjects(Hero.class).get(0))) { // check intersection of hitboxes
                h = getIntersectingObjects(Hero.class).get(0);
                if (h != null && h.getWorld() != null) {
                    h.takeDamage(damage); // damage the hero
                }
            }
        }
        if (tookDamage) { // switch to a red version of Imp if damaged
            if (damageTimer == 0) damageTimer = actNum;
            if (damageTimer != 0 && Math.abs(actNum - damageTimer) > 20) {
                tookDamage = false;
                damageTimer = 0;
            }
        }
        animate();
        actNum++;
        super.act();
    }
    
    public Imp(int centerX, int centerY) {
        super(20, 3, 1, 300, centerX, centerY);        
        homeRadius = 60; 
        // Scale image to appropriate size
        for (GreenfootImage img: idleFrames) {
            img.scale(40, 40);
        }
        for (GreenfootImage img: runFrames) {
            img.scale(40, 40);
        }
        for (GreenfootImage img: redIdleFrames) {
            img.scale(40, 40);
        }
        for (GreenfootImage img: redRunFrames) {
            img.scale(40, 40);
        }
        setImage(idleFrames[0]);
        // Initialize variables
        actNum = 0;
        frameNum = 0;
        damageTimer = 0;
        tookDamage = false;
        hitbox = new SimpleHitbox(this, getImage().getWidth()/2-6, getImage().getHeight()/2-8, 5, 2); // init hitbox
        //overlay = new Overlay(this, hitbox);
    }
    
    public void addedToWorld(World w) {
        //w.addObject(overlay, getX(), getY());
        super.addedToWorld(w); // see class Enemy for more info
    }
    
    private void animate() {
        // animate, modify with y = mx + b
        if (actNum % (speed !=0 ? (int) (-6 * speed + 15) : 10) == 0) {
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
    
}
