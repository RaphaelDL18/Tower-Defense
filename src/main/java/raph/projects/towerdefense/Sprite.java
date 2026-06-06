package raph.projects.towerdefense;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;

public class Sprite
{
    private ImageView currentFrame; //Frame showed on screen
    private Timeline animation; //Changes frames
    private int frameWidth;
    private int frameHeight;
    private int frames;

    public Sprite(String path,int fps, int w, int h, int f)
    {
        this.frameWidth = w;
        this.frameHeight = h;
        this.frames = f;

        Image spriteSheet = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        this.currentFrame = new ImageView(spriteSheet);
        this.currentFrame.setFitWidth(this.frameWidth + 1);
        this.currentFrame.setFitHeight(this.frameHeight + 1);
        this.currentFrame.setSmooth(false);

        int frame[] = {0}; //array because Timeline can only catch final variables or effective final variables
        this.animation = new Timeline(new KeyFrame(Duration.millis(1000.0/fps), e->{this.currentFrame.setViewport(new Rectangle2D(frame[0]*this.frameWidth, 0, this.frameWidth, this.frameHeight));frame[0] = (frame[0] + 1) % this.frames;}));
        this.animation.setCycleCount(Animation.INDEFINITE);
    }

    public ImageView getCurrentFrame()
    {
        return currentFrame;
    }

    public void play()
    {
        this.animation.play();
    }

    public void stop()
    {
        this.animation.stop();
    }

}
