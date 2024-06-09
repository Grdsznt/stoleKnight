import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Just an image that can be used for multiple purposes
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Image extends Actor
{
    /**
     * Creates an image using a file name 
     *
     * @param fileName The file path / file name
     */
    public Image(String fileName) {
        setImage(fileName);
    }
    
    /**
     * Creates empty image with a width and a height
     *
     * @param width A parameter
     * @param height A parameter
     */
    public Image(int width, int height) {
        setImage(new GreenfootImage(width, height));
    }
}
