import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Necromancer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wizard extends Enemy
{
    /**
     * Act - do whatever the Necromancer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private static GreenfootImage[] idleFrames = {
        new GreenfootImage("Wizard/wizzard_m_idle_anim_f0.png"),new GreenfootImage("Wizard/wizzard_m_idle_anim_f1.png"),new GreenfootImage("Wizard/wizzard_m_idle_anim_f2.png"),
        new GreenfootImage("Wizard/wizzard_m_idle_anim_f3.png")
    };
    private static GreenfootImage[] runFrames = {
        new GreenfootImage("Wizard/wizzard_m_run_anim_f0.png"),new GreenfootImage("Wizard/wizzard_m_run_anim_f1.png"),new GreenfootImage("Wizard/wizzard_m_run_anim_f2.png"),
        new GreenfootImage("Wizard/wizzard_m_run_anim_f3.png")
    };
    
    private GreenfootImage hitImage = new GreenfootImage("Wizard/wizzard_m_hit_anim_f0.png");
    private int actNum, frameNum;
    private int homeRadius;
    private Wand w;
    private Pair target;
    public void act()
    {
        if (h == null) { // in this case, pursuing is attacking
            // pathfind to this random position in radius
            if (actNum % 600 == 0) {
                target = getRandomPositionWithinRadius(homeRadius);
            }
            if (target != null) moveTowardsTarget(target.f, target.s);
            if (actNum % 20 == 0) h = getHeroInRadius();
        } else {
            //shoot ball at
            w.shoot(h.getX()-getX(), h.getY()-getY());
            if (calculateDistance(getX(), getY(), h.getX(), h.getY()) > targetRadius) {
                h = null;
            }
        }
    }
    
    public Wizard() {
        super(300, 4, 5, 60, 450, 450);
        setImage(idleFrames[0]);
        for (GreenfootImage img: idleFrames) {
            img.scale(36, 63);
        }
        for (GreenfootImage img: runFrames) {
            img.scale(36, 63);
        }
        actNum = 0; frameNum = 0;
        homeRadius = 60;
        w = new Wand(3);
    }
    
    public void addedToWorld(World world) {
        world.addObject(w, getX(), getY());
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
