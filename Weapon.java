import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * One of the weapons: bow. This is coupled with an arrow.
 * 
 * @author Andy Feng
 * @version June 10th, 2024
 */
public abstract class Weapon extends Actor {
    protected boolean isAttacking;
    protected int damage;
    protected Enemy enemy;
    protected boolean beingUsed;
    protected Hero hero;
    protected int energyUsage;
    protected int id;

    public Weapon(int damage) {
        isAttacking = false;
        this.damage = damage;
        beingUsed = false;
    }

    public void act() {
        // Add your action code here.
        if (this instanceof Wand) {

        } else {
            isAttacking = GameWorld.isMouseHolding();
        }
        //causeDamage();

        //hero = (Hero) getOneIntersectingObject(Hero.class);
        //enemy = (Enemy) getOneIntersectingObject(Enemy.class);
    }

    public abstract void attack();

    
    /**
     * get the current user of the weapon, if the enemy is holding the weapon, return enemy. 
     * Otherwise, return hero has the user.
     * 
     * @return Actor: the current user of the weapon
     */
    protected Actor getHolder() {
        if (getWorld().getObjects(Hero.class).size() > 0) {
            if (getWorld().getObjects(Hero.class).get(0).getWeapons().contains(this)) {
                return getWorld().getObjects(Hero.class).get(0);
            }
        }
        Actor holder = getOneIntersectingObject(Enemy.class);
        return holder;
        // if (this instanceof Wand) {
            // Actor holder = getOneIntersectingObject(Wizard.class);
            // if (holder == null) {
                // holder = getOneIntersectingObject(Hero.class);
            // }
            // return holder;
        // } else {
            // Actor holder = getOneIntersectingObject(Hero.class);
            // if (holder == null) {
                // holder = getOneIntersectingObject(Enemy.class);
            // }
            // return holder;
        // }
    }
    
    /**
     * Returns how much energy the weapon uses
     *
     * @return Returns how much energy the weapon uses
     */
    public int getEnergyUsage() {
        return energyUsage;
    }
    
    /**
     * Sets the state if the weapon is being used or not
     *
     * @param state The state of being used or not
     */
    public void setBeingUsed(boolean state) {
        beingUsed = state;
    }
    
    /**
     * Returns the id number of the weapon
     *
     * @return Returns the id number of the weapon
     */
    public int getID() {
        return id;
    }
}

