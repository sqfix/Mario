package net.sqfix.Sprites.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import net.sqfix.Mario;
import net.sqfix.Screens.PlayScreen;
import net.sqfix.Sprites.Enemy;
import net.sqfix.Sprites.MarioS;

/**
 * Created by Makc on 08.01.2017.
 */

public class Flag extends Enemy{
    private TextureRegion frame;
    private boolean setToStart;

    public Flag(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frame = new TextureRegion(new Texture(Gdx.files.internal("flag.png")));
        setToStart = false;
    }



    @Override
    protected void defineEnemy() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Mario.PPM);
        fDef.filter.categoryBits = Mario.FLAG_BIT;
        fDef.filter.maskBits = Mario.GROUND_BIT |
                Mario.COIN_BIT |
                Mario.BRICK_BIT |
                Mario.ENEMY_BIT |
                Mario.OBJECT_BIT |
                Mario.MARIO_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);
    }

    @Override
    public void hitOnHead(MarioS mario) {

    }

    @Override
    public void update(float dt) {
        if(!setToStart){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(frame);
        }
    }

    public void draw(Batch batch){
            super.draw(batch);
    }

    @Override
    public void onEnemyHit(Enemy enemy) {

    }
}
