import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Write a description of class Weapon here.
 * 
 * @author Andy Feng
 * @version 1.0
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
        isAttacking = GameWorld.isMouseHolding();
        if(enemy == null && hero != null) hero = (Hero) getOneIntersectingObject(Hero.class);
        if(hero == null && enemy != null) enemy = (Enemy) getOneIntersectingObject(Enemy.class);
    }

    public abstract void attack();

    public void causeDamage() {
        ArrayList<Enemy> e = (ArrayList<Enemy>) getIntersectingObjects(Enemy.class);
        for (Enemy enemy : e) {
            enemy.setHealth(enemy.getHealth() - damage);
            e.remove(enemy);
        }
    }
}
