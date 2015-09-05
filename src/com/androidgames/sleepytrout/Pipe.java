package com.androidgames.sleepytrout;

import com.androidgames.framework.GameObject;

public class Pipe extends GameObject{
    public static float PIPE_WIDTH = 0.8f;
    public static float PIPE_HEIGHT = 0.8f;
    public static final int PIPE_HEAD = 0;
    public static final int PIPE_BODY = 1;
    
    int type;
    int direction;
    
    public Pipe(float x, float y) {
        super(x, y, PIPE_WIDTH, PIPE_HEIGHT);
        type = PIPE_BODY;
        direction = 1;
    }
}
