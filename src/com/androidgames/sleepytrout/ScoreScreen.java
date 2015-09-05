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

public class ScoreScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle menuBounds;
    Rectangle shareBounds;
    boolean menuButtonDown;
    boolean shareButtonDown;
    Vector2 touchPoint;
    int highScore;  

    public ScoreScreen(Game game) {
        super(game);
        
        guiCam = new Camera2D(glGraphics, 480, 320);
        menuBounds = new Rectangle(128, 160 - 16, 96, 32);
        shareBounds = new Rectangle(256, 160 - 16, 96, 32);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 100);
        highScore = Settings.highscores[1];
        menuButtonDown = false;
        shareButtonDown = false;
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
            	if(OverlapTester.pointInRectangle(menuBounds, touchPoint)) {
            		Assets.playSound(Assets.clickSound);
            		game.setScreen(new MainMenuScreen(game));
            		menuButtonDown = false;
            		return;
            	}
            	
            	if(OverlapTester.pointInRectangle(shareBounds, touchPoint)) {
            		Assets.playSound(Assets.clickSound);
            		game.postScoreOnFacebook();
            		shareButtonDown = false;	
            		return;
            	}
        	}
            else if (event.type == TouchEvent.TOUCH_DOWN) {
                if(OverlapTester.pointInRectangle(menuBounds, touchPoint)) {
                	menuButtonDown = true;
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
    	Assets.font.drawText(batcher, "Highest Score :" + highScore, 240 - 120, 160 + 32);
    	if (!menuButtonDown) {
    		batcher.drawSprite(240 - 64, 160, 96, 32, Assets.menuButton);
    	}
    	else
    	{
    		batcher.drawSprite(240 - 64, 160 - 2, 96, 32, Assets.menuButton);
    	}
    	
    	if (!shareButtonDown) {
    		batcher.drawSprite(240 + 64, 160, 96, 32, Assets.shareButton);
    	}
    	else
    	{
    		batcher.drawSprite(240 + 64, 160 - 2, 96, 32, Assets.shareButton);
    	}
    	
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
    }
    
    @Override
    public void resume() {        
    }
    
    @Override
    public void pause() {        
    }

    @Override
    public void dispose() {
    }
}
