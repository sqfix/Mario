package net.sqfix.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import net.sqfix.Mario;
import net.sqfix.Scenes.Hud;
import net.sqfix.Screens.PlayScreen;
import net.sqfix.Sprites.Items.ItemDef;
import net.sqfix.Sprites.Items.Mushroom;

/**
 * Created by Makc on 28.12.2016.
 */

public class Coin extends InteractiveTileObject {

    private static TiledMapTileSet tileset;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileset = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategotyFilter(Mario.COIN_BIT);
    }

    @Override
    public void onHeadHit(MarioS mario) {
        Gdx.app.log("Coin", "Collision");
        if(getCell().getTile().getId() == BLANK_COIN)
            Mario.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else {
            //КАСТОМНЫЕ ПАРАМЕТРЫ
            if(object.getProperties().containsKey("mushroom")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / Mario.PPM),
                        Mushroom.class));
                Mario.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            }
            else
                Mario.manager.get("audio/sounds/coin.wav", Sound.class).play();
            Hud.addScrore(100);
        }
        getCell().setTile(tileset.getTile(BLANK_COIN));
    }


}
