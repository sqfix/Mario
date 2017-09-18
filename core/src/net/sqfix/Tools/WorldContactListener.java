package net.sqfix.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import net.sqfix.Mario;
import net.sqfix.Sprites.Enemy;
import net.sqfix.Sprites.InteractiveTileObject;
import net.sqfix.Sprites.Items.Item;
import net.sqfix.Sprites.MarioS;

/**
 * Created by Makc on 29.12.2016.
 */

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        //Gdx.app.log("Begin Contact", "");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        /*if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }*/

        switch (cDef){
            case Mario.MARIO_HEAD_BIT | Mario.BRICK_BIT:
            case Mario.MARIO_HEAD_BIT | Mario.COIN_BIT:
                if(fixA.getFilterData().categoryBits == Mario.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((MarioS) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((MarioS) fixB.getUserData());
                break;
            case Mario.ENEMY_HEAD_BIT | Mario.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == Mario.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead((MarioS) fixB.getUserData());
                else
                    ((Enemy)fixB.getUserData()).hitOnHead((MarioS) fixA.getUserData());
                break;
            //CASE ENEMY СТАЛКИВАЕТСЯ С ОБЪЕКТОМ
            case Mario.ENEMY_BIT | Mario.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Mario.ENEMY_BIT)
                    //ЕСЛИ ПЕРВАЯ ЦЕЛЬ ENEMY, А ВТОРАЯ ТРУБА, ИЗМЕНИТЬ НАПРАВЛЕНИЕ ПО X
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Mario.MARIO_BIT | Mario.ENEMY_BIT:
                //MARIO DIED
                if(fixA.getFilterData().categoryBits == Mario.MARIO_BIT)
                    ((MarioS) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((MarioS) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                break;
            case Mario.ENEMY_BIT | Mario.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).onEnemyHit((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).onEnemyHit((Enemy)fixA.getUserData());
                break;
            case Mario.ITEM_BIT | Mario.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Mario.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Mario.ITEM_BIT | Mario.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == Mario.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((MarioS) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((MarioS) fixA.getUserData());
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {
        //Gdx.app.log("End Contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
