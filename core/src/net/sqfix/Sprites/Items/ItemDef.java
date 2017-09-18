package net.sqfix.Sprites.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Makc on 29.12.2016.
 */

public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type){
        this.position = position;
        this.type = type;
    }
}
