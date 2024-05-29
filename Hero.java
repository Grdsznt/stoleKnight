import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Write a description of class MainCharacter here.
 * 
 * @author Andy Feng
 * @version 1.0
 * 
 * This is the main character of the game, the player can control control this character to pass the game
 * The main character will have various actions:
 * <ul>
 * <li> 
 * <ul>
 */
public abstract class Hero extends SuperSmoothMover
{
    /**
     * Act - do whatever the MainCharacter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    //ArrayList<Power> powerList;
    ArrayList<Weapon> weaponInInventory = new ArrayList<Weapon>();
    private int Hp;
    private int shield;
    private int speed;
    protected int energy;
    protected boolean attack;
    protected Weapon currentWeapon;
    
    public Hero(int Hp, int shieldValue, int speed, int initialEnergy, Weapon initialWeapon) {
        weaponInInventory.add(initialWeapon);
        this.Hp = Hp;
        this.shield = shieldValue;
        this.speed = speed;
        this.energy = initialEnergy;
        
        attack = false;
        
    }
    
    public void act()
    {
        // Add your action code here.
        control();
        takeDamage();
        if(weaponInInventory.size() < 2) pickUpWeapon();
    }
    
    private void control() {
        if(Greenfoot.isKeyDown("w")) {
            setLocation(getX(), getY() - 1);
        }
        if(Greenfoot.isKeyDown("a")) {
            move(-1);
        }
        if(Greenfoot.isKeyDown("s")) {
            setLocation(getX(), getY() + 1);
        }
        if(Greenfoot.isKeyDown("d")) {
            move(1);
        }
    }
    
    private void takeDamage() {
        // implement later
        
        if(Hp <= 0){
            //game over
        }
    }
    
    private void pickUpWeapon() {
        //pick up a new weapon if the Hero only has 1 weapon
        // action
        
    }
    
    private boolean switchWeapon(Weapon newWeapon) {
        //switch the current weapon the hero is using with another weapon
        
        return true;
    }
    
    public abstract void ability();
    
    public abstract void animation();
    
    // public abstract 
}
