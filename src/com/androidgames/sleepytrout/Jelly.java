package com.androidgames.sleepytrout;

import com.androidgames.framework.DynamicGameObject;

public class Jelly extends DynamicGameObject {
    public static final float JELLY_WIDTH = 0.8f;
    public static final float JELLY_HEIGHT = 0.8f;
    public static final float JELLY_VELOCITY = 1.0f;
    public static final int JELLY_STATE_UP = 0;
    public static final int JELLY_STATE_DOWN = 1;
    public static final int JELLY_STATE_HIT_TROUT = 2;
    public static final int JELLY_STATE_HIT_PUFFER = 3;
    public static final int JELLY_STATE_DIED = 4;
    
    int state;
    float stateTime = 0;
    
    public Jelly(float x, float y) {
        super(x, y, JELLY_WIDTH, JELLY_HEIGHT);
        state = JELLY_STATE_DOWN;
        stateTime = 0;
        velocity.set(0, JELLY_VELOCITY);
    }
    
    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(JELLY_WIDTH / 2, JELLY_HEIGHT / 2);
        
        if(position.y < JELLY_HEIGHT / 2 ) {
        	state = JELLY_STATE_UP;
            position.y = JELLY_HEIGHT / 2;
            velocity.y = JELLY_VELOCITY;
        }
        if(position.y > World.WORLD_HEIGHT - JELLY_HEIGHT / 2) {
        	state = JELLY_STATE_DOWN;
            position.y = World.WORLD_HEIGHT - JELLY_HEIGHT / 2;
            velocity.y = -JELLY_VELOCITY;
        }
        stateTime += deltaTime;
        
        if (state == JELLY_STATE_HIT_PUFFER)
        {
        	if (stateTime >= 0.8f)
        		state = JELLY_STATE_DIED;
        }
    }
    
    public void hitTrout() {
    	state = JELLY_STATE_HIT_TROUT;
    	stateTime = 0;
    	velocity.set(0,0);
    }
    
    public void hitPuffer() {
    	state = JELLY_STATE_HIT_PUFFER;
    	stateTime = 0;
    	velocity.set(0,0);
    }
}
