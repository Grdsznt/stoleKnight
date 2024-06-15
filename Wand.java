import greenfoot.*;  // Greenfoot classes

/**
 * Wand weapon, shoots a red ball projectile.
 * This class handles the behavior of the wand, including animation and shooting projectiles.
 * Extends from Weapon class.
 * 
 * @author Edwin Dong
 * @author Andy Feng
 * @version 0.1
 */
public class Wand extends Weapon
{
    GreenfootImage wand = new GreenfootImage("weapon_red_magic_staff.png"); // Image of the wand
    private int damage, actNum; // Damage value and action number for timing
    private BallProjectile proj; // Projectile object
    
    private static final int RECOVER_TIME = 20;
    private int recoverTimer;
    /**
     * Constructor for Wand class.
     * Initializes damage, image, and other variables.
     * 
     * @param damage The damage value inflicted by the wand.
     */
    public Wand(int damage) {
        super(damage); // Call superclass constructor with damage value
        this.damage = damage; // Initialize damage
        setImage(wand); // Set wand image
        actNum = 0; // Initialize action number
        
        energyUsage = 1;
    }
    
    /**
     * Act method for wand behavior.
     * Controls animation, shooting, and attack logic based on holder (Hero or Enemy).
     */
    public void act()
    {
        super.act(); // Call superclass act method
        animate(); // Animate wand based on attack state
        // Prevent continuous attack for enemies, ensure attack once for heroes
        if (getHolder() instanceof Enemy && isAttacking == true && actNum % 22 == 0) {
            isAttacking = false;
        }
        if (getHolder() instanceof Hero) {
            isAttacking = GameWorld.isMouseHolding(); // Set attack state based on mouse click
                if(recoverTimer > 0) {
                recoverTimer--;
            }
        }
        
        attack(); // Perform attack logic
        actNum++; // Increment action number
    }
    
    /**
     * Method to shoot a projectile from the wand.
     * Calculates direction and creates a projectile object accordingly.
     * 
     * @param dx Horizontal distance from wand to target
     * @param dy Vertical distance from wand to target
     */
    public void shoot(int dx, int dy) {
        isAttacking = true; // Set attacking flag
        double clockwiseAngle = 0;
        // Calculate angle in radians from enemy to hero
        double angleRadians = Math.atan2(dy, dx);
        
        // Convert radians to degrees
        double angleDegrees = Math.toDegrees(angleRadians);
        
        // Get the clockwise angle
        clockwiseAngle = (angleDegrees + 360) % 360;
        
        if (getHolder() instanceof Hero) {
            // Create projectile for Hero
            recoverTimer = RECOVER_TIME; // Set recovery time
            // checks for the buff
            if (getHolder() instanceof Hero && ((Hero)getHolder()).getPowerList().contains("More Attack Speed")) {
                recoverTimer /= 2;
            }
            ((Hero)getHolder()).useEnergy(energyUsage);
            BallProjectileHero b = new BallProjectileHero(20, damage, (int)clockwiseAngle); 
            getWorld().addObject(b, getX(), getY()-15); // Add projectile to world
        } else {
            // Create projectile for Enemy
            proj = new BallProjectile(20, 3, (int)clockwiseAngle); 
            getWorld().addObject(proj, getX(), getY()-15); // Add projectile to world
        }
    }
    
    /**
     * Perform attack logic when the wand is used.
     * Trigger shooting if held by Hero and attack condition is met.
     */
    public void attack() {
        if (getHolder() instanceof Hero && beingUsed && isAttacking && recoverTimer == 0) {
            if (((Hero)getHolder()).getEnergyAmount() < energyUsage) {
                return;
            }
            shoot(GameWorld.getMouseX() - getX(), GameWorld.getMouseY() - getY()); // Shoot towards mouse position
        }
    }
    
    /**
     * Animate the wand based on its attack state.
     * Adjust rotation for Hero's direction.
     */
    private void animate() {
        if (isAttacking) {
            if (getHolder() instanceof Hero && beingUsed) {
                setRotation(((Hero)getHolder()).right ? 20 : -20); // Rotate for Hero's direction
            } else {
                setRotation(20); // Default rotation for other holders
            }
        } else {
            setRotation(0); // Reset rotation when not attacking
        }
    }
    
    /**
     * Method to make the wand follow a Wizard actor.
     * Adjusts the wand's position relative to the Wizard.
     * 
     * @param w The Wizard actor to follow.
     */
    public void followWizard(Wizard w) {
        setLocation(w.getX()-4, w.getY()+17); // Adjust wand's position relative to Wizard
    }
}
