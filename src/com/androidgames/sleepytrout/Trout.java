package com.androidgames.sleepytrout;

import com.androidgames.framework.DynamicGameObject;

public class Trout extends DynamicGameObject{
    public static final int TROUT_STATE_UP = 0;
    public static final int TROUT_STATE_DOWN = 1;
    public static final int TROUT_STATE_HIT = 2;
    public static final float TROUT_UP_VELOCITY = 1.5f;    
    public static final float TROUT_MOVE_VELOCITY = 2.5f;
    public static final float TROUT_WIDTH = 0.8f;
    public static final float TROUT_HEIGHT = 0.8f;
    
    public static final int TROUT_TYPE_SMALL = 0;
    public static final int TROUT_TYPE_BIG = 1;
    public static final int TROUT_TYPE_TRANSFORM_BIG = 2;
    public static final int TROUT_TYPE_TRANSFORM_SMALL = 3;
    
    public static final int TROUT_END_TRANSFORM_KEYFRAME = 4;
    
    int type;
    int state;
    float stateTime;    
    float transformTime;

    public Trout(float x, float y) {
        super(x, y, TROUT_WIDTH, TROUT_HEIGHT);
        type = TROUT_TYPE_SMALL;
        state = TROUT_STATE_DOWN;
        stateTime = 0;        
        transformTime = 0;
        velocity.set(TROUT_MOVE_VELOCITY, 0);
    }

    public void update(float deltaTime) {     
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
        
        if(velocity.y > 0 && state != TROUT_STATE_HIT) {
            if(state != TROUT_STATE_UP) {
                state = TROUT_STATE_UP;
            }
        }
        
        if(velocity.y < 0 && state != TROUT_STATE_HIT) {
            if(state != TROUT_STATE_DOWN) {
                state = TROUT_STATE_DOWN;
            }
        }
        
        if(position.y < TROUT_HEIGHT)
        {
            velocity.set(0,0);
            state = TROUT_STATE_HIT;        
            stateTime = 0;
        }
        
        if(position.y > World.WORLD_HEIGHT - TROUT_HEIGHT)
        {
            velocity.set(0,0);
            state = TROUT_STATE_HIT;        
            stateTime = 0;
        }
        
        stateTime += deltaTime;
        if (type == TROUT_TYPE_BIG) {
        	if (stateTime >= transformTime) {
        		type = TROUT_TYPE_TRANSFORM_SMALL;
        	}
        }
    }

    public void hit() {
        velocity.set(0,0);
        state = TROUT_STATE_HIT;        
        stateTime = 0;
    }
    
    public void eat() {
    	if (type == TROUT_TYPE_SMALL) {
    		type = TROUT_TYPE_TRANSFORM_BIG;
    	}
    	
    	transformTime = 12.0f;
    	stateTime = 0;
    }
}
