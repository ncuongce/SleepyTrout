package com.androidgames.sleepytrout;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.androidgames.framework.Game;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.gl.Camera2D;
import com.androidgames.framework.gl.FPSCounter;
import com.androidgames.framework.gl.SpriteBatcher;
import com.androidgames.framework.impl.GLScreen;
import com.androidgames.framework.math.OverlapTester;
import com.androidgames.framework.math.Rectangle;
import com.androidgames.framework.math.Vector2;
import com.androidgames.sleepytrout.World;
import com.androidgames.sleepytrout.WorldRenderer;
import com.androidgames.sleepytrout.Assets;
import com.androidgames.sleepytrout.Settings;
import com.androidgames.sleepytrout.MainMenuScreen;
import com.androidgames.sleepytrout.World.WorldListener;


public class GameScreen extends GLScreen {
    static final int GAME_READY = 0;    
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
  
    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;    
    WorldListener worldListener;
    World world;
    WorldRenderer renderer;
    
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle menuBounds;
    boolean menuButtonDown;
    Rectangle refreshBounds;
    boolean refreshButtonDown;
    
    int lastScore;
    String scoreString;
    
    public GameScreen(Game game) {
        super(game);
        state = GAME_READY;
        guiCam = new Camera2D(glGraphics, 480, 320);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 2000);
        worldListener = new WorldListener() {
            public void up() {            
                Assets.playSound(Assets.upSound);
            }
            
            public void hit() {
                Assets.playSound(Assets.hitSound);
            }

            public void coin() {
                Assets.playSound(Assets.coinSound);
            }                      
            
            public void eat() {
            	Assets.playSound(Assets.eatSound);
            }
        };      
        
        world = new World(worldListener);
        renderer = new WorldRenderer(glGraphics, batcher, world);
        
        pauseBounds = new Rectangle(480 - 32, 0, 32, 32);
        resumeBounds = new Rectangle(480 - 32, 0, 32, 32);
        menuBounds = new Rectangle(240 - 48, 160 - 16, 96, 32);
        menuButtonDown = false;
        
        refreshBounds = new Rectangle(240 - 16, 160 - 40 - 16, 32, 32);
        refreshButtonDown = false;
        
        lastScore = 0;
        scoreString = "0";
    }

    @Override
    public void update(float deltaTime) {
        if(deltaTime > 0.1f)
            deltaTime = 0.1f;
        
        switch(state) {
        case GAME_READY:
            updateReady();
            break;
        case GAME_RUNNING:
            updateRunning(deltaTime);
            break;
        case GAME_PAUSED:
            updatePaused();
            break;
        case GAME_LEVEL_END:
            updateLevelEnd();
            break;
        case GAME_OVER:
            updateGameOver();
            break;
        }
    }

    private void updateReady() {
        if(game.getInput().getTouchEvents().size() > 0) {
            state = GAME_RUNNING;
        }
    }

    private void updateRunning(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_PAUSED;
                return;
            }  
                     
        }
        
        world.update(deltaTime, touchEvents);
        if(world.score != lastScore) {
            lastScore = world.score;
            scoreString = "" + lastScore;
        }

        if(world.state == World.WORLD_STATE_NEXT_LEVEL) {
            state = GAME_LEVEL_END;        
        }
        if(world.state == World.WORLD_STATE_GAME_OVER) {
            state = GAME_OVER;
            if(lastScore > Settings.highscores[1]) 
                scoreString = "new highscore: " + lastScore;
            else
                scoreString = "score: " + lastScore;
            Settings.addScore(lastScore);
            Settings.save(game.getFileIO());
        }
    }

    private void updatePaused() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(OverlapTester.pointInRectangle(resumeBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
                return;
            }
        }
    }

    private void updateLevelEnd() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {                   
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            world = new World(worldListener);
            renderer = new WorldRenderer(glGraphics, batcher, world);
            world.score = lastScore;
            state = GAME_READY;
        }
    }

    private void updateGameOver() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {                   
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            if(OverlapTester.pointInRectangle(menuBounds, touchPoint)) {
                if(event.type == TouchEvent.TOUCH_UP) { 
                	Assets.playSound(Assets.clickSound);
            		game.setScreen(new MainMenuScreen(game));
            		Assets.music.play();
            		menuButtonDown = false;
            		return;
            	}
                else if (event.type == TouchEvent.TOUCH_DOWN) {
                	menuButtonDown = true;
                }
            }
            
            if(OverlapTester.pointInRectangle(refreshBounds, touchPoint)) {
                if(event.type == TouchEvent.TOUCH_UP) { 
                	Assets.playSound(Assets.clickSound);
                	game.setScreen(new GameScreen(game));
            		refreshButtonDown = false;
            		return;
            	}
                else if (event.type == TouchEvent.TOUCH_DOWN) {
                	refreshButtonDown = true;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        renderer.render();
        
        guiCam.setViewportAndMatrices();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.items);
        switch(state) {
        case GAME_READY:
            presentReady();
            break;
        case GAME_RUNNING:
            presentRunning();
            break;
        case GAME_PAUSED:
            presentPaused();
            break;
        case GAME_LEVEL_END:
            presentLevelEnd();
            break;
        case GAME_OVER:
            presentGameOver();
            break;
        }
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void presentReady() {
    	Assets.font.drawText(batcher, scoreString, 480 - 48, 320 - 10);
    	Assets.font.drawText(batcher, "Ready?", 240 - 48, 160);
    }

    private void presentRunning() {
        batcher.drawSprite(480 - 16, 16, 32, 32, Assets.pauseButton);
        Assets.font.drawText(batcher, scoreString, 480 - 48, 320 - 10);
    }

    private void presentPaused() {        
    	Assets.font.drawText(batcher, scoreString, 480 - 48, 320 - 10);
    	batcher.drawSprite(480 - 16, 16, 32, 32, Assets.resumeButton);
    }

    private void presentLevelEnd() {
    	Assets.font.drawText(batcher, scoreString, 480 - 48, 320 - 10);
    	Assets.font.drawText(batcher, "You win!", 240 - 48, 160);
    }

    private void presentGameOver() {
    	Assets.font.drawText(batcher, "Ops, try again!", 240 - 104, 160 + 32 + 32);
    	Assets.font.drawText(batcher, scoreString, 240 - 56, 160 + 32);
    	if (!menuButtonDown) {
    		batcher.drawSprite(240, 160, 96, 32, Assets.menuButton);
    	}
    	else
    	{
    		batcher.drawSprite(240, 160 - 2, 96, 32, Assets.menuButton);
    	}
    	
    	if (!refreshButtonDown) {
    		batcher.drawSprite(240, 160 - 40, 32, 32, Assets.refreshButton);
    	}
    	else
    	{
    		batcher.drawSprite(240, 160 - 40, 32, 32, -45, Assets.refreshButton);
    	}
    }

    @Override
    public void pause() {
        if(state == GAME_RUNNING)
            state = GAME_PAUSED;
    }

    @Override
    public void resume() {        
    }

    @Override
    public void dispose() {       
    }
}
