import greenfoot.*;  
import java.util.ArrayList;

/**
 * One of the weapons: sword. This class handles the behavior of the sword,
 * including animation of attacks and damage calculation upon hitting enemies.
 * Extends from Weapon class.
 * 
 * Author: Andy Feng
 * Version: June 10th, 2024
 */
public class Sword extends Weapon {
    private static final String IMAGE_PATH = "images/sword/sword"; // Path to sword images
    private static final int DAMAGE = 10; // Damage inflicted by sword
    private static final int RECOVER_TIME = 60; // Recovery time after swinging

    // Image frames for sword animations
    protected GreenfootImage[] swordRightFrames = new GreenfootImage[6];
    protected GreenfootImage[] swordLeftFrames = new GreenfootImage[6];

    private int frameNumber = 0; // Current frame number for animation
    private boolean isSwinging; // Flag indicating if sword is currently swinging
    private int recoverCounter; // Counter for recovery time after swinging
    private SimpleTimer animationTimer = new SimpleTimer(); // Timer for animation timing
    private SimpleHitbox hitbox; // Hitbox for sword collision detection
    // private Overlay overlay; // Overlay object for visual effects
    
    /**
     * Constructor for Sword class. Initializes images, flags, and hitbox.
     */
    public Sword() {
        super(DAMAGE); // Call superclass constructor with damage value
        loadImages(); // Load sword images
        isSwinging = false; // Initially, sword is not swinging
        setImage(swordRightFrames[0]); // Set initial image
        recoverCounter = 0; // Initialize recover counter
        
        // Initialize hitbox for collision detection
        hitbox = new SimpleHitbox(this, getImage().getWidth() / 2 - 4, getImage().getHeight() / 2, 0, 0);
        // overlay = new Overlay(this, hitbox); // Initialize overlay (if used)
    }
    
    /**
     * Method called when the sword is added to the world.
     * Adds the hitbox to the list of all hitboxes in the world.
     */
    public void addedToWorld(World w) {
        SimpleHitbox.allHitboxesInWorld.add(hitbox); // Add hitbox to world's hitboxes list
    }

    /**
     * Load all images of a Sword object, for both left and right orientations.
     */
    private void loadImages() {
        for (int i = 0; i < swordRightFrames.length; i++) {
            swordRightFrames[i] = new GreenfootImage(IMAGE_PATH + i + ".png");
            swordRightFrames[i].scale(60, 60); // Scale image for consistency
            swordLeftFrames[i] = new GreenfootImage(IMAGE_PATH + i + ".png");
            swordLeftFrames[i].mirrorHorizontally(); // Mirror image for left orientation
            swordLeftFrames[i].scale(60, 60); // Scale image for consistency
        }
    }

    /**
     * Act method for sword behavior.
     * Controls animation and attack logic based on holder (Hero or Enemy).
     */
    public void act() {
        super.act(); // Call superclass act method
        Actor holder = getHolder(); // Get current holder
        
        if (recoverCounter > 0) {
            recoverCounter--; // Decrease recover counter if swinging
        }
        
        // Animate sword if held by Hero or Enemy
        if (holder instanceof Hero || holder instanceof Enemy) {
            boolean right = holder instanceof Hero ? ((Hero) holder).right : ((Enemy) holder).right;
            animateSword(right); // Animate sword based on holder's direction
        }
        
        if (isSwinging) {
            attack(); // Perform attack logic if sword is swinging
        }
    }

    /**
     * Handle sword object's animation.
     * If conditions are met, swing the sword and set isSwinging to true.
     * 
     * @param right Whether the holder (Hero or Enemy) is facing right.
     */
    private void animateSword(boolean right) {
        if (getHolder() instanceof Hero) {
            if (isAttacking && beingUsed && recoverCounter == 0) {
                isSwinging = true; // Start swinging animation
                swing(right ? swordRightFrames : swordLeftFrames); // Swing sword frames based on direction
            } else if (beingUsed && !isSwinging && recoverCounter != 0) {
                setImage(right ? swordRightFrames[0] : swordLeftFrames[0]); // Set default image
            }
        }
    }

    /**
     * Perform swinging animation with given frames.
     * Reset animation and recovery counters after animation completes.
     * 
     * @param swingFrames Array of frames for swinging animation.
     */
    private void swing(GreenfootImage[] swingFrames) {
        if (animationTimer.millisElapsed() > 1200) { // Control animation speed
            setImage(swingFrames[frameNumber]); // Set current animation frame
            frameNumber++; // Move to next frame
        } else {
            animationTimer.mark(); // Reset animation timer
        }
        
        if (frameNumber >= swingFrames.length) { // Check if end of animation frames
            frameNumber = 0; // Reset frame number for next swing
            recoverCounter = RECOVER_TIME; // Set recovery time
            isSwinging = false; // Stop swinging flag
        }
    }

    /**
     * Perform attack logic when sword hits enemies.
     * Checks collision with hitboxes and deals damage accordingly.
     */
    public void attack() {
        ArrayList<SimpleHitbox> hitboxes = SimpleHitbox.allHitboxesInWorld; // Get all hitboxes in world
        
        if (getHolder() instanceof Hero) { // If held by Hero
            for (SimpleHitbox hit : hitboxes) {
                if (hit.getActor() instanceof Enemy && hitbox.isHitBoxesIntersecting(hit)) {
                    Enemy e = (Enemy) hit.getActor(); // Get intersecting Enemy
                    e.takeDamage(DAMAGE); // Deal damage to enemy
                }
            }
        } else if (getHolder() instanceof Enemy) { // If held by Enemy
            for (SimpleHitbox hit : hitboxes) {
                if (hit.getActor() instanceof Hero && hitbox.isHitBoxesIntersecting(hit)) {
                    Hero h = (Hero) hit.getActor(); // Get intersecting Hero
                    h.takeDamage(DAMAGE); // Deal damage to hero
                }
            }
        }
    }
}
