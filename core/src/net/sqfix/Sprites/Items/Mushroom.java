package net.sqfix.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import net.sqfix.Mario;
import net.sqfix.Screens.PlayScreen;
import net.sqfix.Sprites.MarioS;

/**
 * Created by Makc on 29.12.2016.
 */

public class Mushroom extends Item{

    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        //ЗАДАЁМ ТЕКСТУРУ ГРИБА
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f, 0);
    }

    @Override

    public void defineItem() {
        //CОЗДАЁМ ТЕЛО
        BodyDef bdef = new BodyDef();
        //ЗАДАЁМ КООРДИНАТЫ ИЗ КОНСТРУКТОРА
        bdef.position.set(getX(), getY());
        //ТИП ТЕЛА
        bdef.type = BodyDef.BodyType.DynamicBody;
        //ДОБАВЛЯЕМ В МИР
        body = world.createBody(bdef);

        //СОЗДАЁМ МАКЕТ
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Mario.PPM);
        //ЗНАЧЕНИЕ СТОЛКНОВЕНИЙ
        fdef.filter.categoryBits = Mario.ITEM_BIT;
        fdef.filter.maskBits = Mario.MARIO_BIT|
                Mario.OBJECT_BIT|
                Mario.GROUND_BIT|
                Mario.COIN_BIT|
                Mario.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(MarioS mario) {
        destroy();
        mario.grow();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
