package com.androidgames.sleepytrout;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.androidgames.framework.Game;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.gl.Camera2D;
import com.androidgames.framework.gl.SpriteBatcher;
import com.androidgames.framework.impl.GLScreen;
import com.androidgames.framework.math.OverlapTester;
import com.androidgames.framework.math.Rectangle;
import com.androidgames.framework.math.Vector2;

public class MainMenuScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle playBounds;
    Rectangle shareBounds;
    Vector2 touchPoint;
    boolean playButtonDown;
    boolean shareButtonDown;
    
    public MainMenuScreen(Game game) {
        super(game);
        guiCam = new Camera2D(glGraphics, 480, 320);
        batcher = new SpriteBatcher(glGraphics, 100);
        playBounds = new Rectangle(128, 112, 96, 32);
        shareBounds = new Rectangle(256, 112, 96, 32);
        touchPoint = new Vector2();       
        playButtonDown = false;
    }       

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            if(event.type == TouchEvent.TOUCH_UP) {   
                if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    playButtonDown = false;
                    game.setScreen(new GameScreen(game));
                    Assets.music.pause();
                    return;
                }
                
                if(OverlapTester.pointInRectangle(shareBounds, touchPoint)) {
                	Assets.playSound(Assets.clickSound);
                	shareButtonDown = false;
                	game.setScreen(new ScoreScreen(game));
                	return;
                }

            }
            else if (event.type == TouchEvent.TOUCH_DOWN)
            {
                if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                	playButtonDown = true;
                }
                
                if(OverlapTester.pointInRectangle(shareBounds, touchPoint)) {
                	shareButtonDown = true;
                }
            }
            	
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(240, 160, 480, 320, Assets.backgroundRegion);
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);               
        
        batcher.beginBatch(Assets.items);                 
        
        batcher.drawSprite(240 , 192, 256, 96, Assets.tittle);
        batcher.drawSprite(480 - 160, 64, 160, 32, Assets.author);
        
        if (!playButtonDown) {
        	batcher.drawSprite(240 - 64, 128, 96, 32, Assets.playButton);
        }
        else
        {
        	batcher.drawSprite(240 - 64, 126, 96, 32, Assets.playButton);
        }
        
        if (!shareButtonDown) {
        	batcher.drawSprite(240 + 64, 128, 96, 32, Assets.shareButton);
        }
        else
        {
        	batcher.drawSprite(240 + 64, 126, 96, 32, Assets.shareButton);
        }
        
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
        
    }

    @Override
    public void pause() {        
    	Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {        

    }       

    @Override
    public void dispose() {        
    }
}
