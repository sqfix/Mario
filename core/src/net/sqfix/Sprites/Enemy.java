package net.sqfix.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import net.sqfix.Screens.PlayScreen;

/**
 * Created by Makc on 29.12.2016.
 */

public abstract class Enemy extends Sprite{
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        //ДВИГАЕТСЯ 1- знач ПО X, 2-е ПО Y
        velocity = new Vector2(1, 0);
        //ЗАМОРОЗКА ЦЕЛЕЙ
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void hitOnHead(MarioS mario);
    public abstract void update(float dt);
    public abstract void onEnemyHit(Enemy enemy);

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }


}
