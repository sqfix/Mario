package net.sqfix.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import net.sqfix.Mario;
import net.sqfix.Screens.PlayScreen;
import net.sqfix.Sprites.Brick;
import net.sqfix.Sprites.Coin;
import net.sqfix.Sprites.Enemy;
import net.sqfix.Sprites.Goomba;
import net.sqfix.Sprites.Items.Flag;
import net.sqfix.Sprites.Turtle;

/**
 * Created by Makc on 28.12.2016.
 */

public class B2WorldCreator {
    //СОЗДАЁМ ГУМБ
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;
    private Array<Flag> flags;



    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //CREATE GROUND BODIES/FIXTURES
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ Mario.PPM, (rect.getY() + rect.getHeight()/2)/ Mario.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Mario.PPM, rect.getHeight()/2 / Mario.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //CREATE PIPE
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ Mario.PPM, (rect.getY() + rect.getHeight()/2)/ Mario.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Mario.PPM, rect.getHeight()/2 / Mario.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Mario.OBJECT_BIT;
            body.createFixture(fdef);
        }
        //CREATE BRICK
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class))
        {

            new Brick(screen, object);

            //***OLD CREATE***//
           /* Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Brick(screen, rect);*/
        }
        //CREATE COIN
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class))
        {
            new Coin(screen, object);
        }

        //CREATE ALL GUMBAS
        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / Mario.PPM, rect.getY() / Mario.PPM));
        }

        turtles = new Array<Turtle>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX() / Mario.PPM, rect.getY() / Mario.PPM));
        }

        flags = new Array<Flag>();
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            flags.add(new Flag(screen, rect.getX() / Mario.PPM, rect.getY() / Mario.PPM));
        }



    }

    /*public Array<Goomba> getGoombas() {
        return goombas;
    }*/
    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }

    public Array<Flag> getFlags(){
        return flags;
    }


}
