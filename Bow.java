import greenfoot.*;  // Greenfoot classes
import java.util.ArrayList;

/**
 * One of the weapons: bow. This is coupled with an arrow.
 * This class handles the behavior of the bow, including charging and shooting arrows.
 * It extends from Weapon class. <br>
 * 
 * Edited By Felix Zhao
 * 
 * @author Jean Pan
 * @author Andy Feng
 * @version May 2024
 */
public class Bow extends Weapon
{
    private int actCount = 0;
    // Attacking frames
    protected GreenfootImage[] bowRightFrames = {new GreenfootImage("weapon_bow0.png"), new GreenfootImage("weapon_bow1.png")};
    protected GreenfootImage[] bowLeftFrames = {new GreenfootImage("weapon_bow0.png"), new GreenfootImage("weapon_bow1.png")};

    // Charge related fields
    private boolean addOneArrow;
    private Projectile arrow;
    private int recoverTimer = 0;
    private static int RECOVER_TIME = 30;
    private int chargeValue;
    private SuperStatBar chargeBar;
    private boolean addOne;
    private GreenfootSound charging;

    /**
     * Constructor of Bow to initialize its image frames.
     */
    public Bow(){
        super(0); // Call superclass constructor
        setImage(bowRightFrames[0]); // Set initial image
        addOneArrow = true; // Flag to control arrow addition
        chargeValue = 0; // Initial charge value
        addOne = true; // Flag to control charge bar addition
        
        // Mirror the left frames for consistency
        for(int i = 0; i < bowLeftFrames.length; i++){
            bowLeftFrames[i].mirrorHorizontally();
        }
        energyUsage = 3;
    }

    /**
     * Act method controls the behavior of the bow each frame.
     */
    public void act()
    {
        super.act(); // Call superclass act method
        animation(); // Update bow animation
        attack(); // Handle bow attack logic
        
        // Reduce recovery timer if active
        if(recoverTimer > 0 && addOneArrow) {
            recoverTimer--;
        }
        
        // Update arrow position if mouse is held and arrow exists
        if(GameWorld.isMouseHolding() && arrow != null && beingUsed) updateProjectilePosition(arrow);
        
        // Handle charge bar creation and update
        if(GameWorld.isMouseHolding() && beingUsed && recoverTimer == 0 && getHolder() instanceof Hero && addOne && !addOneArrow) {
            Hero hero = (Hero) getHolder();
            
            chargeBar = new SuperStatBar(100, chargeValue, hero, 25, 3, hero.getImage().getHeight() / 2 + 10);
            getWorld().addObject(chargeBar, hero.getX(), hero.getY());
            addOne = false;
            
        }
        
        // Update charge value and charge bar
        if(chargeBar != null) {
            if(GameWorld.isMouseHolding() && beingUsed && recoverTimer == 0) {
                chargeValue++;
                if(chargeValue >= 100) chargeValue = 100;
                chargeBar.update(chargeValue);
            } else {
                // Reset charge value and remove charge bar when not charging
                chargeValue = 0;
                getWorld().removeObject(chargeBar);
                addOne = true;
            }
        }
    }

    /**
     * Perform attack action based on current holder (Hero or Enemy).
     */
    public void attack() {
        if(!beingUsed) return; // Exit if weapon is not being used
        Actor holder = getHolder(); // Get current holder
        
        if (holder instanceof Hero) {
            // Hero logic: shoot arrow if conditions are met
            if (GameWorld.isMouseHolding() && addOneArrow && recoverTimer == 0) {
                Hero hero = (Hero)holder;
                if (hero.getEnergyAmount() >= energyUsage) {
                    arrow = new Projectile(GameWorld.getMouseX(), GameWorld.getMouseY(), 1, 10, true);
                    getWorld().addObject(arrow, getX(), getY());
                    updateProjectilePosition(arrow);
                    addOneArrow = false;
                    hero.useEnergy(energyUsage);
                }
            }
            if(!GameWorld.isMouseHolding() && !addOneArrow){
                addOneArrow = true;
                recoverTimer = RECOVER_TIME; // Set recovery time
                // checks for the buff
                if (getHolder() instanceof Hero && ((Hero)getHolder()).getPowerList().contains("More Attack Speed")) {
                    recoverTimer /= 2;
                }
                arrow = null;
            }
        } else if (holder instanceof Enemy) {
            // Enemy logic: shoot arrow towards the nearest Hero
            Enemy e = (Enemy) holder;
            ArrayList<Hero> heroPosition = (ArrayList<Hero>) getObjectsInRange(150, Hero.class);
            if(heroPosition.size() != 0) {
                Hero current = heroPosition.get(0);
                arrow = new Projectile(current.getX(), current.getY());
            }
        }
    }

    /**
     * Update the bow's animation based on its holder's direction.
     */
    private void animation(){
        if (!beingUsed) return; // Exit if weapon is not being used
        
        // Check the direction of the holder (Hero or Enemy) and update animation accordingly
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
    
    /**
     * Update the position of the projectile relative to the bow's holder.
     * 
     * @param p The projectile to update.
     */
    private void updateProjectilePosition(Projectile p) {
        Actor holder = getHolder();
        // Update projectile position based on holder's direction
        if(holder instanceof Hero) {
            Hero hero = (Hero) holder;
            p.setLocation(getX() + (hero.right? 5 : -5), getY());
        }
        if(holder instanceof Enemy) {
            Enemy e = (Enemy) holder;
            p.setLocation(getX() + (e.right? 5 : -5), getY());
        }
    }
}
