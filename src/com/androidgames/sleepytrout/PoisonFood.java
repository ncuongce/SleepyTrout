package com.androidgames.sleepytrout;

import com.androidgames.framework.GameObject;

public class PoisonFood extends GameObject {
    public static float POISONFOOD_WIDTH = 0.8f;
    public static float POISONFOOD_HEIGHT = 0.8f;
    public static int POISONFOOD = 0;
    public static int EMPTYFOOD = 1;
    
    int type;
    
    public PoisonFood(float x, float y) {
        super(x, y, POISONFOOD_WIDTH, POISONFOOD_HEIGHT);
        type = POISONFOOD;
    }

    public void empty() {
    	type = EMPTYFOOD;
    }
    
}
