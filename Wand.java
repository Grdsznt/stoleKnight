import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Wand here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wand extends Weapon
{
    /**
     * Act - do whatever the Wand wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    GreenfootImage wand = new GreenfootImage("weapon_red_magic_staff.png");
    private int damage, actNum;
    private BallProjectile proj;
    public void act()
    {
        super.act();
        animate();
        if (isAttacking == true && actNum % 22 == 0) isAttacking = false;
        actNum++;
    }
    
    public Wand(int damage) {
        super(damage);
        this.damage = damage;
        setImage(wand);
        actNum = 0;
    }
    
    public void shoot(int dx, int dy) {
        isAttacking = true;
        // Calculate angle in radians from enemy to hero
        double angleRadians = Math.atan2(dy, dx);

        // Convert radians to degrees
        double angleDegrees = Math.toDegrees(angleRadians);

        // Adjust for Greenfoot's coordinate system (clockwise and 0-360)
        double clockwiseAngle = (angleDegrees + 360) % 360;
        
        proj = new BallProjectile(20, 3, (int)clockwiseAngle); 
        getWorld().addObject(proj, getX(), getY());
    }
    
    public void attack() {
        
    }
    
    private void animate(){
        if(isAttacking)  {
            setRotation(20); 
        }
        else {
            setRotation(0);
        }
    }
    
    public void followWizard(Wizard w){
        setLocation(w.getX(), w.getY());
    }
}
