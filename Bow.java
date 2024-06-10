import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * One of the weapons: bow. This is coupled with an arrow.
 * 
 * @author Jean Pan, Andy Feng
 * @version May 2024
 * 
 */
public class Bow extends Weapon
{
    private int actCount = 0;
    // Attacking frames
    protected GreenfootImage[] bowRightFrames = {new GreenfootImage("weapon_bow0.png"), new GreenfootImage("weapon_bow1.png")};
    protected GreenfootImage[] bowLeftFrames = {new GreenfootImage("weapon_bow0.png"), new GreenfootImage("weapon_bow1.png")};

    // Charge related fields
    private boolean isCharging;
    private boolean addOneArrow;
    private Projectile arrow;

    /**
     * Constructor of Bow to set its original image.
     */
    public Bow(){
        super(0);
        setImage(bowRightFrames[0]);
        isCharging = false;
        addOneArrow = true;

        for(int i = 0; i < bowLeftFrames.length; i++){
            bowLeftFrames[i].mirrorHorizontally();
        }
    }

    public void act()
    {
        super.act();
        animation();
        attack();
        if(GameWorld.isMouseHolding() && arrow != null && beingUsed) updateProjectilePosition(arrow);
        System.out.println(beingUsed);
    }

    /**
     * Animate it to attack.
     */
    public void attack(){
        // Attack by pulling the bow
        if (!beingUsed) return;
        if (hero != null) {
            if (GameWorld.isMouseHolding() && addOneArrow) {
                arrow = new Projectile(GameWorld.getMouseX(), GameWorld.getMouseY(), 1, 10, true);
                getWorld().addObject(arrow, getX(), getY());
                updateProjectilePosition(arrow);
                addOneArrow = false;
            }
            if(!GameWorld.isMouseHolding()){
                addOneArrow = true;
            }
        }
        if (enemy != null) {
            
        }
    }

    private void animation(){
        if (!beingUsed) return;
        if (hero != null) {
            if (hero.right) {
                if (isAttacking) setImage(bowRightFrames[1]);
                else setImage(bowRightFrames[0]);
            } else {
                if (isAttacking) setImage(bowLeftFrames[1]);
                else setImage(bowLeftFrames[0]);
            }
        }
        if (enemy != null) {
            if (enemy.right) {
                if (isAttacking) setImage(bowRightFrames[1]);
                else setImage(bowRightFrames[0]);
            } else {
                if (isAttacking) setImage(bowLeftFrames[1]);
                else setImage(bowLeftFrames[0]);
            }
        }
    }
    
    private void updateProjectilePosition(Projectile p) {
        p.setLocation(getX() + (hero.right? 5 : -5), getY());
    }
}
