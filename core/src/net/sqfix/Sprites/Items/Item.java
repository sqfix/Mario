package net.sqfix.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import net.sqfix.Mario;
import net.sqfix.Screens.PlayScreen;
import net.sqfix.Sprites.MarioS;

/**
 * Created by Makc on 29.12.2016.
 */

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y)
    {
        this.screen = screen;
        this.world = screen.getWorld();
        toDestroy = false;
        destroyed = false;
        setPosition(x, y);
        setBounds(getX(), getY(), 16 / Mario.PPM, 16 / Mario.PPM);
        defineItem();

    }

    public abstract void defineItem();
    public abstract void use(MarioS mario);

    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy(){
        toDestroy = true;
    }

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }

}
