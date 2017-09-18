package net.sqfix.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;

import net.sqfix.Mario;
import net.sqfix.Scenes.Hud;
import net.sqfix.Screens.PlayScreen;

/**
 * Created by Makc on 28.12.2016.
 */

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategotyFilter(Mario.BRICK_BIT);
    }

    //РАЗРУШЕНИЕ БЛОКОВ
    @Override
    public void onHeadHit(MarioS mario) {
        if(mario.isBig()) {
            Gdx.app.log("Brick", "Collision");
            setCategotyFilter(Mario.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScrore(200);

            Mario.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }
        else
            Mario.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }
}
