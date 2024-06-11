import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

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
    private boolean addOneArrow;
    private Projectile arrow;
    private int recoverTimer = 0;
    private static int RECOVER_TIME = 30;
    private int chargeValue;
    private SuperStatBar chargeBar;
    private boolean addOne;

    /**
     * Constructor of Bow to set its original image.
     */
    public Bow(){
        super(0);
        setImage(bowRightFrames[0]);
        addOneArrow = true;
        chargeValue = 0;
        addOne = true;
        
        for(int i = 0; i < bowLeftFrames.length; i++){
            bowLeftFrames[i].mirrorHorizontally();
        }
    }

    public void act()
    {
        super.act();
        animation();
        attack();
        if(recoverTimer > 0 && addOneArrow) {
            recoverTimer--;
        }
        
        if(GameWorld.isMouseHolding() && arrow != null && beingUsed) updateProjectilePosition(arrow);
        
        if(GameWorld.isMouseHolding() && beingUsed && recoverTimer == 0 && getHolder() instanceof Hero && addOne) {
            Hero hero = (Hero) getHolder();
            chargeBar = new SuperStatBar(100, chargeValue, hero, 25, 3, hero.getImage().getHeight() / 2 - 10);
            getWorld().addObject(chargeBar, hero.getX(), hero.getY());
            addOne = false;
        }
        
        if(chargeBar != null) {
            //System.out.println(chargeBar);
            if(GameWorld.isMouseHolding() && beingUsed && recoverTimer == 0) {
                chargeValue++;
                if(chargeValue >= 100) chargeValue = 100;
                chargeBar.update(chargeValue);
            } else {
                chargeValue = 0;
                getWorld().removeObject(chargeBar);
                addOne = true;
            }
        }
    }

    /**
     * Animate it to attack.
     */
    public void attack() {
        if(!beingUsed) return;
        Actor holder = getHolder();
        if (holder instanceof Hero) {
            if (GameWorld.isMouseHolding() && addOneArrow && recoverTimer == 0) {
                arrow = new Projectile(GameWorld.getMouseX(), GameWorld.getMouseY(), 1, 10, true);
                getWorld().addObject(arrow, getX(), getY());
                updateProjectilePosition(arrow);
                addOneArrow = false;
            }
            if(!GameWorld.isMouseHolding()){
                addOneArrow = true;
            }
        }
        if (holder instanceof Enemy) {
            // Handle enemy's attack logic here
            Enemy e = (Enemy) holder;
            ArrayList<Hero> heroPosition = (ArrayList<Hero>) getObjectsInRange(150, Hero.class);
            if(heroPosition.size() != 0) {
                Hero current = heroPosition.get(0);
                arrow = new Projectile(current.getX(), current.getY());
            }
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
        Actor holder = getHolder();
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
