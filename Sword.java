import greenfoot.*;  
import java.util.ArrayList;

/**
 * One of the weapons: bow. This is coupled with an arrow.
 * 
 * @author Andy Feng
 * @version June 10th, 2024
 */
public class Sword extends Weapon {
    private static final String IMAGE_PATH = "images/sword/sword";
    private static final int DAMAGE = 10;
    private static final int RECOVER_TIME = 60;

    protected GreenfootImage[] swordRightFrames = new GreenfootImage[6];
    protected GreenfootImage[] swordLeftFrames = new GreenfootImage[6];
    //protected GreenfootImage[] slashFrames = new GreenfootImage[4];
    private int frameNumber = 0;
    private boolean isSwinging;
    private int recoverCounter;
    private SimpleTimer animationTimer = new SimpleTimer();
    private SimpleHitbox hitbox;
    private Overlay overlay;
    //private Image effect;
    
    public Sword() {
        super(DAMAGE);
        loadImages();
        isSwinging = false;
        setImage(swordRightFrames[0]);
        recoverCounter = 0;
        //effect = new Image("slasheffect/slash0.png");
        hitbox = new SimpleHitbox(this, getImage().getWidth() / 2, getImage().getHeight() / 2, 0, 0);
        overlay = new Overlay(this, hitbox);
    }
    
    /**
     * Override addedToWorld method
     */
    public void addedToWorld(World w) {
        //w.addObject(overlay, getX(), getY());
        SimpleHitbox.allHitboxesInWorld.add(hitbox);
    }

    /**
     * load all images of a Sword object, left and right
     */
    private void loadImages() {
        for (int i = 0; i < swordRightFrames.length; i++) {
            swordRightFrames[i] = new GreenfootImage(IMAGE_PATH + i + ".png");
            swordRightFrames[i].scale(60, 60);
            swordLeftFrames[i] = new GreenfootImage(IMAGE_PATH + i + ".png");
            swordLeftFrames[i].mirrorHorizontally();
            swordLeftFrames[i].scale(60, 60);
        }
        
        // for (int i = 0; i < slashFrames.length; i++) {
            // slashFrames[i] = new GreenfootImage("slasheffect/slash"+ i + ".png");
            // slashFrames[i].scale(60, 60);
        // }
    }

    public void act() {
        super.act();
        Actor holder = getHolder();
        
        if (recoverCounter > 0) {
            recoverCounter--;
        }
        
        if (holder instanceof Hero || holder instanceof Enemy) {
            boolean right = holder instanceof Hero ? ((Hero) holder).right : ((Enemy) holder).right;
            animateSword(right);
        }
        
        if(isSwinging) attack();
    }

    /**
     * handle sword object's animation. If the all conditions are met, swing the sword and set isSwinging to true
     * 
     * @ right: the drection of the holder, either Hero or Enemy, to update the 
     * 
     */
    private void animateSword(boolean right) {
        if(getHolder() instanceof Hero) {
            if (isAttacking && beingUsed && recoverCounter == 0) {
                isSwinging = true;
                swing(right ? swordRightFrames : swordLeftFrames);
            } else if (beingUsed && !isSwinging && recoverCounter != 0) {
                setImage(right ? swordRightFrames[0] : swordLeftFrames[0]);
            }
        }
    }

    private void swing(GreenfootImage[] swingFrames) {
        if(animationTimer.millisElapsed() > 1200) {
            setImage(swingFrames[frameNumber]);
            frameNumber++;
        } else {
            animationTimer.mark();
        }
        if(frameNumber >= swingFrames.length) {
            frameNumber = 0;
            recoverCounter = RECOVER_TIME;
            isSwinging = false;
        }
    }

    public void attack() {
        ArrayList<SimpleHitbox> hitboxes = SimpleHitbox.allHitboxesInWorld;
        if(getHolder() instanceof Hero) {
            for(SimpleHitbox hit : hitboxes) {
                if(hit.getActor() instanceof Enemy && hitbox.isHitBoxesIntersecting(hit)) {
                    Enemy e = (Enemy) hit.getActor();
                    e.takeDamage(DAMAGE);
                }
            }
        } else if(getHolder() instanceof Enemy) {
            for(SimpleHitbox hit : hitboxes) {
                if(hit.getActor() instanceof Enemy && hitbox.isHitBoxesIntersecting(hit)) {
                    Hero h = (Hero) hit.getActor();
                    h.takeDamage(DAMAGE);
                }
            }
        }
    }
}
