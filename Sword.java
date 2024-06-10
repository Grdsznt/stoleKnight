import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Represents a Sword weapon with swinging animation and attack functionality.
 * 
 * Author: Andy Feng
 * Version: June 7th, 2024
 */
public class Sword extends Weapon {
    private static final String IMAGE_PATH = "images/sword/sword";
    private static final int DAMAGE = 1;
    private static final int RECOVER_TIME = 20;

    protected GreenfootImage[] swordRightFrames = new GreenfootImage[6];
    protected GreenfootImage[] swordLeftFrames = new GreenfootImage[6];
    private int frameNumber = 0;
    private boolean isSwinging;
    private int recoverCounter = 0;
    private SimpleTimer timer = new SimpleTimer();

    public Sword() {
        super(DAMAGE);
        loadImages();
        isSwinging = false;
        setImage(swordRightFrames[0]);
    }

    private void loadImages() {
        for (int i = 0; i < swordRightFrames.length; i++) {
            swordRightFrames[i] = new GreenfootImage(IMAGE_PATH + i + ".png");
            swordRightFrames[i].scale(50, 50);
            swordLeftFrames[i] = new GreenfootImage(IMAGE_PATH + i + ".png");
            swordLeftFrames[i].mirrorHorizontally();
            swordLeftFrames[i].scale(50, 50);
        }
    }

    public void act() {
        super.act();
        handleAttack();
        System.out.println(beingUsed);
        Actor holder = (Actor) getOneIntersectingObject(Actor.class);
        if (holder instanceof Hero || holder instanceof Enemy) {
            boolean right = holder instanceof Hero ? ((Hero) holder).right : ((Enemy) holder).right;
            animateSword(right);
        }
    }

    private void animateSword(boolean right) {
        if (isAttacking && beingUsed && recoverCounter == 0) {
            isSwinging = true;
            swing(right ? swordRightFrames : swordLeftFrames);
        } else if (beingUsed && !isSwinging) {
            setImage(right ? swordRightFrames[0] : swordLeftFrames[0]);
        }
    }

    private void swing(GreenfootImage[] swingFrames) {
        if (timer.millisElapsed() < 100) return;
        timer.mark();
        System.out.println(1);
        if(frameNumber < swingFrames.length) {
            setImage(swingFrames[frameNumber]);
            frameNumber++;
        } else {
            resetSwingState();
        }
    }

    private void resetSwingState() {
        frameNumber = 0;
        isSwinging = false;
        recoverCounter = RECOVER_TIME;
    }

    private void handleAttack() {
        if (isAttacking && beingUsed && isSwinging && recoverCounter == 0) {
            attack();
        } else {
            if (recoverCounter > 0) {
                recoverCounter--;
            }
        }
    }

    public void attack() {
        List<Enemy> enemies = getIntersectingObjects(Enemy.class);
        for (Enemy enemy : enemies) {
            //enemy.takeDamage(DAMAGE);
        }
    }
}
