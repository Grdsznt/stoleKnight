import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MainCharacter here.
 * 
 * @author Andy Feng, Jean Pan
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
    private int Hp;
    private int shield;
    private int speed;
    protected int energy;
    protected boolean attack;
    protected GreenfootImage[] frames;
    //protected Weapon;
    
    public Hero(int Hp, int shieldValue, int speed, int initialEnergy, GreenfootImage[] frames) {
        this.Hp = Hp;
        this.shield = shieldValue;
        this.speed = speed;
        this.energy = initialEnergy;
        attack = false;
        this.frames = frames;
    }
    
    public void act()
    {
        // Add your action code here.
        control();
        takeDamage();
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
    
    private void attack() {
        
    }
    
    public abstract void ability();
    
    public abstract void animation();
}
