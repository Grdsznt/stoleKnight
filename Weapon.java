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

    protected Actor getHolder() {
        Actor holder = getOneIntersectingObject(Hero.class);
        if (holder == null) {
            holder = getOneIntersectingObject(Enemy.class);
        }
        return holder;
    }
}

