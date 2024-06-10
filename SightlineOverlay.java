import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot, and MouseInfo)

public class SightlineOverlay extends Actor {
    private Pair start;
    private Pair end;
    
    public SightlineOverlay(Pair start, Pair end) {
        this.start = start;
        this.end = end;
        GreenfootImage image = new GreenfootImage(1, 1);
        setImage(image);
    }

    public void updateLine(Pair start, Pair end) {
        this.start = start;
        this.end = end;
        drawLine();
    }

    private void drawLine() {
        if (getWorld() == null) return;
        int width = getWorld().getWidth();
        int height = getWorld().getHeight();
        GreenfootImage image = new GreenfootImage(width, height);
        image.setColor(Color.YELLOW);
        image.drawLine(start.f, start.s, end.f, end.s);
        setImage(image);
    }
}
