package com.androidgames.sleepytrout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.math.OverlapTester;
import com.androidgames.framework.math.Vector2;

public class World {
    public interface WorldListener {
        public void up();
        public void hit();
        public void coin();
		public void eat();
    }
	
	public static final float WORLD_WIDTH_UNIT = 15;
    public static final float WORLD_WIDTH = WORLD_WIDTH_UNIT * 50;
    public static final float WORLD_HEIGHT = 10;    
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(0, -3);

    public final Trout trout;
    public final List<Jelly> jellies;
    public final List<Pipe> pipes;
    public final List<PoisonFood> poisonfoods;
    public final Random rand;
    
    public final WorldListener listener;
    
    public float widthSoFar;
    public int score;    
    public int state;

    public World(WorldListener listener) {
        this.trout = new Trout(1, 5);        
        this.jellies = new ArrayList<Jelly>();
        this.pipes = new ArrayList<Pipe>();
        this.poisonfoods = new ArrayList<PoisonFood>();
        
        this.listener = listener;
        
        rand = new Random();
        generateLevel();
        
        this.widthSoFar = 9;
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;
    }

    private void generateLevel() {
    	int i;
    	Pipe pipe;
    	
        float x1 = 10;
        while (x1 < WORLD_WIDTH - WORLD_WIDTH_UNIT / 2) {
        	float x2 = x1 + 2;
        	float x3 = x2;
        	
        	int numPipes = rand.nextInt(5) + 1;
        	
        	for (i = 0; i < numPipes - 1; i++) {
        		pipe = new Pipe(x1, i * Pipe.PIPE_HEIGHT + Pipe.PIPE_HEIGHT / 2);
        		pipes.add(pipe);
        	}
        	pipe = new Pipe(x1, i * Pipe.PIPE_HEIGHT + Pipe.PIPE_HEIGHT / 2);
        	pipe.type = Pipe.PIPE_HEAD;
        	pipes.add(pipe);

        	float y3 = (numPipes + 2) * Pipe.PIPE_HEIGHT + PoisonFood.POISONFOOD_HEIGHT / 2;
        	
        	for (i = 0; i < 7 - numPipes; i++) {
        		pipe = new Pipe(x1, World.WORLD_HEIGHT - i * Pipe.PIPE_HEIGHT - Pipe.PIPE_HEIGHT / 2);
        		pipe.direction = -1;
        		pipes.add(pipe);
        	}
        	pipe = new Pipe(x1, World.WORLD_HEIGHT - i * Pipe.PIPE_HEIGHT - Pipe.PIPE_HEIGHT / 2);
        	pipe.direction = -1;
        	pipe.type = Pipe.PIPE_HEAD;
        	pipes.add(pipe);
        	
        	float y2 = rand.nextFloat()
                    * (WORLD_HEIGHT - Jelly.JELLY_HEIGHT)
                    + Jelly.JELLY_HEIGHT / 2;

            
            Jelly jelly = new Jelly(x2, y2);
            jellies.add(jelly);
            
            PoisonFood poisonfood = new PoisonFood(x3, y3);  
            
            if (OverlapTester.overlapRectangles(jelly.bounds, poisonfood.bounds)) 
            {
            	poisonfoods.add(poisonfood);
            }
            
            x1 += 4;
        }
    }

    public void update(float deltaTime, List<TouchEvent> touchEvents) {
        updateTrout(deltaTime, touchEvents);
        updateJellies(deltaTime);
        
        if (trout.state != Trout.TROUT_STATE_HIT)
            checkCollisions();
        checkGameOver();
    }

    private void updateTrout(float deltaTime, List<TouchEvent> touchEvents) {
        if (trout.state != Trout.TROUT_STATE_HIT)
        {
            int len = touchEvents.size();
            for(int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);
                if(event.type == TouchEvent.TOUCH_UP) {
                	trout.velocity.add(gravity.x, Trout.TROUT_UP_VELOCITY);
                	listener.up();
                }
            }
        }
        
        
        trout.update(deltaTime);
        
        widthSoFar = Math.max(trout.position.x, widthSoFar);
        int newScore = (int) ((widthSoFar - 6) / 4);
        
        if (newScore != score) {
        	score = newScore;
        	listener.coin();
        }
    }
    
    private void updateJellies(float deltaTime) {
        int len = jellies.size();
        for (int i = 0; i < len; i++) {
            Jelly jelly = jellies.get(i);
            jelly.update(deltaTime);
            
            if (jelly.position.x < trout.position.x - WORLD_WIDTH_UNIT)
            {
            	jellies.remove(jelly);
            	len = jellies.size();
            }
        }
    }
    
    private void checkCollisions() {
    	checkPipesCollisions();
    	checkJelliesCollisions();
    	checkPoisonFoodCollisions();
    }

    private void checkPoisonFoodCollisions() {
        int len = poisonfoods.size();
        for (int i = 0; i < len; i++) {
            PoisonFood poisonfood = poisonfoods.get(i);
            if (OverlapTester.overlapRectangles(poisonfood.bounds, trout.bounds)) {
                trout.eat();
                listener.eat();
                poisonfood.empty();
            }
            
            if ((poisonfood.position.x < trout.position.x - WORLD_WIDTH_UNIT) || (poisonfood.type == PoisonFood.EMPTYFOOD))
            {
            	poisonfoods.remove(poisonfood);
            	len = poisonfoods.size();
            }
        }
    }
    
    private void checkJelliesCollisions() {
        int len = jellies.size();
        
    	for (int i = 0; i < len; i++) {
        	Jelly jelly = jellies.get(i);
        	if (OverlapTester.overlapRectangles(jelly.bounds, trout.bounds)) {
        		if ((trout.type == Trout.TROUT_TYPE_SMALL) && (jelly.state != Jelly.JELLY_STATE_HIT_PUFFER)) {
        			trout.hit();
        			jelly.hitTrout();
        		}
        		else
        		{
        			jelly.hitPuffer();
        		}
        		listener.hit();
        	}
    	}
    }
    
    private void checkPipesCollisions() {
        int len = pipes.size();
        for (int i = 0; i < len; i++) {
            Pipe pipe = pipes.get(i);
            if (OverlapTester.overlapRectangles(pipe.bounds, trout.bounds)) {
                trout.hit();
                listener.hit();
            }
            
            if (pipe.position.x < trout.position.x - WORLD_WIDTH_UNIT)
            {
            	pipes.remove(pipe);
            	len = pipes.size();
            }
        }
    }
    
    private void checkGameOver() {
        if (widthSoFar > WORLD_WIDTH) {
            state = WORLD_STATE_NEXT_LEVEL;
        }
        
        if (trout.state == Trout.TROUT_STATE_HIT) {
            state = WORLD_STATE_GAME_OVER;
        }
    }
}
