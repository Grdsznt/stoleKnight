import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * One of the main enemies, has a wand and can shoot projectiles
 * 
 * @author Edwin
 * @version 1
 */
public class Wizard extends Enemy
{
    // Animation arrays
    private static GreenfootImage[] idleFrames = {
        new GreenfootImage("Wizard/wizzard_m_idle_anim_f0.png"),new GreenfootImage("Wizard/wizzard_m_idle_anim_f1.png"),new GreenfootImage("Wizard/wizzard_m_idle_anim_f2.png"),
        new GreenfootImage("Wizard/wizzard_m_idle_anim_f3.png")
    };
    private static GreenfootImage[] runFrames = {
        new GreenfootImage("Wizard/wizzard_m_run_anim_f0.png"),new GreenfootImage("Wizard/wizzard_m_run_anim_f1.png"),new GreenfootImage("Wizard/wizzard_m_run_anim_f2.png"),
        new GreenfootImage("Wizard/wizzard_m_run_anim_f3.png")
    };
    
    private static GreenfootImage[] redRunFrames = {
        new GreenfootImage("Wizard/wizzard_m_run_anim_f0_red.png"),new GreenfootImage("Wizard/wizzard_m_run_anim_f1_red.png"),new GreenfootImage("Wizard/wizzard_m_run_anim_f2_red.png"),
        new GreenfootImage("Wizard/wizzard_m_run_anim_f3_red.png")
    };
    private static GreenfootImage[] redIdleFrames = {
        new GreenfootImage("Wizard/wizzard_m_idle_anim_f0_red.png"),new GreenfootImage("Wizard/wizzard_m_idle_anim_f1_red.png"),new GreenfootImage("Wizard/wizzard_m_idle_anim_f2_red.png"),
        new GreenfootImage("Wizard/wizzard_m_idle_anim_f3_red.png")
    };
    
    private Wand w; // Wizard's wand
    private Pair target; // current target to move to
    private GameWorld gw;
    private int damageTimer; // for damage animation
    
    /**
     * Act - do whatever the Necromancer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (h == null) { // in this case, pursuing is attacking
            if (actNum % 200 == 0) {
                target = getRandomPositionWithinRadius(homeRadius); // get a random position in home radius
            }
            if (target != null) moveTowardsTarget(target.f, target.s); // pathfind to this random position in radius
            if (actNum % 20 == 0) h = getHeroInRadius();
        } else {
            gw = (GameWorld) getWorld();
            if (actNum % 60 == 0 && hasLineOfSight(new Pair(getX(), getY()), new Pair(h.getX(), h.getY()), processWalls(gw.getObstacles()))) 
                w.shoot(h.getX()-getX(), h.getY()-getY()); // add a projectile and aim it towards the hero
            if (calculateDistance(getX(), getY(), h.getX(), h.getY()) > targetRadius) {
                h = null; // if hero left radius, set null
            }
        }
        w.followWizard(this); // make the wizard follow the wand
        if (getWorld().getObjects(Hero.class).size() != 0) {
            if (hitbox.intersectsOval(getWorld().getObjects(Hero.class).get(0))) {
                h = getIntersectingObjects(Hero.class).get(0); // if hitboxes intersect, get hero and damage hero
                if (h != null && h.getWorld() != null) {
                    h.takeDamage(damage);
                }
                // maybe red damage animation
            }
        }
        super.act(); // handle dying
        
        if (tookDamage) { // turn to red version of wizard if damaged, use damage timer
            if (damageTimer == 0) damageTimer = actNum;
            if (damageTimer != 0 && Math.abs(actNum - damageTimer) > 20) {
                tookDamage = false;
                damageTimer = 0;
            }
        }
        animate();
        actNum++;
    }
    
    public Wizard(int centerX, int centerY) {
        super(30, 4, 3, 400, centerX, centerY);
        setImage(idleFrames[0]);
        // Scale images to appropriate size
        for (GreenfootImage img: idleFrames) {
            img.scale(36, 63);
        }
        for (GreenfootImage img: runFrames) {
            img.scale(36, 63);
        }
        for (GreenfootImage img: redIdleFrames) {
            img.scale(36, 63);
        }
        for (GreenfootImage img: redRunFrames) {
            img.scale(36, 63);
        }
        
        // Initialize variables
        actNum = 0; frameNum = 0;
        damageTimer = 0;
        tookDamage = false;
        homeRadius = 60;
        // Create a new Wand
        w = new Wand(3);
        hitbox = new SimpleHitbox(this, getImage().getWidth()/2-2, getImage().getHeight()/2-9, 10, 2); // Make a hitbox that matches the size of the wizard
        // overlay = new Overlay(this, hitbox); // hitbox overlay for debugging
    }
    
    public Wand getWand() { // getter for wand
        return w;
    }
    
    public void addedToWorld(World world) {
        world.addObject(w, getX()-4, getY()+17); // add wand to world
        // world.addObject(overlay, getX(), getY()); // hitbox overlay for debugging
        super.addedToWorld(world);
    }
    
    private void animate() {
        // animate, modify with y = mx + b
        if (actNum % (speed !=0 ? (int) (-6 * speed + 15) : 10) == 0) {
            if (frameNum >= 3) {
                frameNum = 0;
            } else {
                frameNum++;
            }
            if (isMoving) { // Set run and idle frames depending on the state
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
    
        double distance = Math.sqrt(diffX * diffX + diffY * diffY); // euclidean dist
        double moveX = (diffX / distance) * speed;
        double moveY = (diffY / distance) * speed;
    
        currentX += moveX;
        currentY += moveY; // add movement to x and y coords
    
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
