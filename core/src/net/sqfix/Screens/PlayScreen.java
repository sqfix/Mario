package net.sqfix.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.sqfix.Mario;
import net.sqfix.Scenes.Hud;
import net.sqfix.Sprites.Enemy;
import net.sqfix.Sprites.Items.Flag;
import net.sqfix.Sprites.Items.Item;
import net.sqfix.Sprites.Items.ItemDef;
import net.sqfix.Sprites.Items.Mushroom;
import net.sqfix.Sprites.MarioS;
import net.sqfix.Tools.B2WorldCreator;
import net.sqfix.Tools.Controller;
import net.sqfix.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Makc on 28.12.2016.
 */

public class PlayScreen implements Screen{
    private Mario game;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //TILET MAP VARIABLES
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    //BOX 2D VARIABLES
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //Sprites
    private MarioS player;

    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    //private Screen_inputs inputs;
    private Controller controller;


    public PlayScreen(Mario game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new StretchViewport(Mario.V_WIDTH / Mario.PPM, Mario.V_HEIGHT / Mario.PPM, gamecam);
        hud = new Hud(game.batch);



        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Mario.PPM);
        gamecam.position.set(Mario.V_WIDTH/2 / Mario.PPM, Mario.V_HEIGHT/2 / Mario.PPM, 0);
        //gamecam.position.set(gamePort.getScreenWidth()/2, gamePort.getScreenHeight()/2,0);

        world = new World(new Vector2(0,-10 ), true);
        //ЛИНИИ ОТЛАДКИ
        b2dr  = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        //СОЗДАЕМ ИГРОКА В МИРЕ
        player = new MarioS(this);


        //CОЗДАЕМ ЭЛЕМЕНТЫ УПРАВЛЕНИЯ


        //ОБРАБОТКА СТОЛКНОВЕНИЙ
        world.setContactListener(new WorldContactListener());

        //SOUNDS
        music = Mario.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        //music.play();

        //CОЗДАЕМ МУНСТРА 5.64f, .32f - КООРДИНАТЫ
        //goomba = new Goomba(this, 5.64f, .32f);


        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

        controller = new Controller();

    }

    public void spawnItem(ItemDef iDef){
        itemsToSpawn.add(iDef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {}

    public void handleInput(float dt) {
        /*if(Gdx.input.isTouched())
            gamecam.position.x += 100 * dt;*/
        if(player.currentState != MarioS.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y == 0
                    || controller.isUpPressed() && player.b2body.getLinearVelocity().y == 0)
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2
                    || controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2
                    || controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }

        //TEMP INPUT
        /*if(Gdx.input.isTouched() ){
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }*/
    }

    public void update(float dt){
        handleInput(dt);
        handleSpawningItems();


        world.step(1/60f, 6, 2);

        player.update(dt);
        for(Enemy enemy : creator.getEnemies()){
            enemy.update(dt);
            //РАЗМОРОЗКА при условии
            if(enemy.getX() < player.getX() + 224 / Mario.PPM);
                enemy.b2body.setActive(true);
        }



        for(Item item: items)
            item.update(dt);


        //for(Flag flag: flags)
        for(Flag flag : creator.getFlags()){
            flag.update(dt);
        }


        hud.update(dt);

        //КАМЕРА СЛЕДИТ ЗА ИГРОКОМ ЕСЛИ ОН ЖИВ
        if(player.currentState != MarioS.State.DEAD)
            gamecam.position.x = player.b2body.getPosition().x;

        if(player.getY() < -20/Mario.PPM)
            player.setMarioIsDead(true);

        gamecam.update();
        renderer.setView(gamecam);
    }


    @Override
    public void render(float delta) {

        //separate our update logic from render
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);



        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        player.draw(game.batch);

        //РИСУЕМ ГУМБАСОВ
        for(Enemy enemy : creator.getEnemies()){
            enemy.draw(game.batch);
        }


        for (Item item : items)
            item.draw(game.batch);

        for(Flag flag : creator.getFlags()){
            flag.draw(game.batch);
        }




        game.batch.end();

        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

        controller.draw();
    }


    public TiledMap getMap(){
        return map;
    }

    public World getWorld() {
        return world;
    }

    public boolean gameOver(){
        if(player.currentState == MarioS.State.DEAD && player.getStateTimer() > 3){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
        controller.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }
}
