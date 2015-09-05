package com.androidgames.sleepytrout;

import javax.microedition.khronos.opengles.GL10;

import com.androidgames.framework.gl.Animation;
import com.androidgames.framework.gl.Camera2D;
import com.androidgames.framework.gl.SpriteBatcher;
import com.androidgames.framework.gl.TextureRegion;
import com.androidgames.framework.impl.GLGraphics;

public class WorldRenderer {
    static final float FRUSTUM_WIDTH = 15;
    static final float FRUSTUM_HEIGHT = 10;    
    GLGraphics glGraphics;
    World world;
    Camera2D cam;
    SpriteBatcher batcher;    
    
    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;        
    }
    
    public void render() {
        if(world.trout.position.x > cam.position.x)
            cam.position.x = world.trout.position.x;
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();        
    }

    public void renderBackground() {
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(cam.position.x, cam.position.y,
                           FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                           Assets.backgroundRegion);
        batcher.endBatch();
    }

    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.items);
        renderTrout();
        renderPipes();
        renderJellies();
        renderPoisonFoods();
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void renderTrout() {
        TextureRegion keyFrame;
        int frameNumber;
        if (world.trout.type == Trout.TROUT_TYPE_SMALL) {
	        switch(world.trout.state) {
	        case Trout.TROUT_STATE_DOWN:
	        case Trout.TROUT_STATE_UP:
	            keyFrame = Assets.troutAnim.getKeyFrame(world.trout.stateTime, Animation.ANIMATION_LOOPING);
	            break;
	        case Trout.TROUT_STATE_HIT:
	        default:
	            keyFrame = Assets.troutHit;                       
	        }
	        float direction = world.trout.velocity.y < 0? -1: 1;     
	        batcher.drawSprite(world.trout.position.x, world.trout.position.y, 1, 1, 20 * direction, keyFrame);  
        }
        else if (world.trout.type == Trout.TROUT_TYPE_BIG)
        {
	        switch(world.trout.state) {
	        case Trout.TROUT_STATE_DOWN:
	        case Trout.TROUT_STATE_UP:
	            keyFrame = Assets.pufferAnim.getKeyFrame(world.trout.stateTime, Animation.ANIMATION_LOOPING);
	            break;
	        case Trout.TROUT_STATE_HIT:
	        default:
	            keyFrame = Assets.pufferHit;                       
	        }
	        float direction = world.trout.velocity.y < 0? -1: 1;     
	        batcher.drawSprite(world.trout.position.x, world.trout.position.y, 1, 2, 20 * direction, keyFrame);  
        }
        /* Transform */
        else if (world.trout.type == Trout.TROUT_TYPE_TRANSFORM_BIG)
        {
	        switch(world.trout.state) {
	        case Trout.TROUT_STATE_DOWN:
	        case Trout.TROUT_STATE_UP:
	            keyFrame = Assets.troutTransformBig.getKeyFrame(world.trout.stateTime, Animation.ANIMATION_LOOPING);
	            break;
	        case Trout.TROUT_STATE_HIT:
	        default:
	            keyFrame = Assets.pufferHit;                       
	        }
	        float direction = world.trout.velocity.y < 0? -1: 1;     
	        batcher.drawSprite(world.trout.position.x, world.trout.position.y, 1, 2, 20 * direction, keyFrame);  
	        
	        frameNumber = Assets.troutTransformBig.getFrameNumber(world.trout.stateTime, Animation.ANIMATION_LOOPING);
	        /* last frame */
	        if (frameNumber == Trout.TROUT_END_TRANSFORM_KEYFRAME)
	        {
	        	world.trout.type = Trout.TROUT_TYPE_BIG;
	        }
        }
        else
        {
	        switch(world.trout.state) {
	        case Trout.TROUT_STATE_DOWN:
	        case Trout.TROUT_STATE_UP:
	            keyFrame = Assets.troutTransformSmall.getKeyFrame(world.trout.stateTime, Animation.ANIMATION_LOOPING);
	            break;
	        case Trout.TROUT_STATE_HIT:
	        default:
	            keyFrame = Assets.troutHit;                       
	        }
	        float direction = world.trout.velocity.y < 0? -1: 1;     
	        batcher.drawSprite(world.trout.position.x, world.trout.position.y, 1, 2, 20 * direction, keyFrame);  
	        
	        frameNumber = Assets.troutTransformSmall.getFrameNumber(world.trout.stateTime, Animation.ANIMATION_LOOPING);
	        /* last frame */
	        if (frameNumber == Trout.TROUT_END_TRANSFORM_KEYFRAME)
	        {
	        	world.trout.type = Trout.TROUT_TYPE_SMALL;
	        }
        }
    }
    
    private void renderPoisonFoods() {
        int len = world.poisonfoods.size();
        TextureRegion keyFrame;
        for(int i = 0; i < len; i++) {
            PoisonFood poisonfood = world.poisonfoods.get(i);
            if (poisonfood.type == PoisonFood.POISONFOOD) {
            	keyFrame = Assets.poisonFood.getKeyFrame(world.trout.stateTime, Animation.ANIMATION_LOOPING);
            }
            else
            {
            	keyFrame = Assets.empty;
            }
            batcher.drawSprite(poisonfood.position.x, poisonfood.position.y, 1, 1, keyFrame);
        }
    }
    
    private void renderJellies() {
    	TextureRegion keyFrame;
        int len = world.jellies.size();
        for(int i = 0; i < len; i++) {
            Jelly jelly = world.jellies.get(i);
            if (jelly.state == Jelly.JELLY_STATE_HIT_TROUT) {
            	keyFrame = Assets.jellyHitTrout;
            }
            else if (jelly.state == Jelly.JELLY_STATE_HIT_PUFFER)
            {
            	keyFrame = Assets.jellyHitPuffer;
            }
            else if (jelly.state == Jelly.JELLY_STATE_DIED)
            {
            	keyFrame = Assets.empty;
            }
            else
            {
            	keyFrame = Assets.jellyAnim.getKeyFrame(jelly.stateTime, Animation.ANIMATION_LOOPING);
            }
            batcher.drawSprite(jelly.position.x, jelly.position.y, 1, 1, keyFrame);
        }
    }
    
    private void renderPipes() {
    	TextureRegion keyFrame;
        int len = world.pipes.size();
        for(int i = 0; i < len; i++) {
            Pipe pipe = world.pipes.get(i);
            switch(pipe.type) {
            	case Pipe.PIPE_HEAD:
            		keyFrame = Assets.pipeHead;
            		break;
            	case Pipe.PIPE_BODY:
            	default:
            		keyFrame = Assets.pipeBody;
            }
            batcher.drawSprite(pipe.position.x, pipe.position.y, 1, pipe.direction * 1, keyFrame);
        }
    }

}

