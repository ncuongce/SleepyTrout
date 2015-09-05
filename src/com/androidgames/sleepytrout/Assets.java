package com.androidgames.sleepytrout;

import com.androidgames.framework.Music;
import com.androidgames.framework.Sound;
import com.androidgames.framework.gl.Animation;
import com.androidgames.framework.gl.Font;
import com.androidgames.framework.gl.Texture;
import com.androidgames.framework.gl.TextureRegion;
import com.androidgames.framework.impl.GLGame;
import com.androidgames.sleepytrout.Settings;

public class Assets {
    public static Texture background;
    public static TextureRegion backgroundRegion;
    
    public static Texture items;
    public static TextureRegion tittle;
    public static TextureRegion author;
    public static TextureRegion menuButton;
    public static TextureRegion playButton;
    public static TextureRegion shareButton;
    public static TextureRegion pauseButton;
    public static TextureRegion resumeButton;
    public static TextureRegion refreshButton;
    
    public static Animation troutAnim;
    public static TextureRegion troutHit;
    public static Animation pufferAnim;
    public static TextureRegion pufferHit;
    public static Animation troutTransformBig;
    public static Animation troutTransformSmall;
    public static Animation jellyAnim;
    public static TextureRegion jellyHitTrout;
    public static TextureRegion jellyHitPuffer;
    public static TextureRegion pipeHead;
    public static TextureRegion pipeBody;
    public static Animation poisonFood;
    public static TextureRegion empty;
    
    public static Font font;
    
    
    public static Music music;
    public static Sound upSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;
    public static Sound eatSound;
	
    public static void load(GLGame game) {
        background = new Texture(game, "background.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 480, 320);
        
        items = new Texture(game, "items.png");
        
        tittle = new TextureRegion(items, 0, 160, 256, 96);
        author = new TextureRegion(items, 256, 160, 160, 32);
        
        playButton = new TextureRegion(items, 0, 96, 96, 32);
        menuButton = new TextureRegion(items, 96, 96, 96, 32);
        shareButton = new TextureRegion(items, 0, 128, 96, 32);
        pauseButton = new TextureRegion(items, 96, 128, 32, 32);
        resumeButton = new TextureRegion(items, 128, 128, 32, 32);
        refreshButton = new TextureRegion(items, 160, 128, 32, 32);
        
        troutAnim = new Animation( 0.08f,
                new TextureRegion(items, 0, 0, 32, 32),
                new TextureRegion(items, 32, 0, 32, 32),
                new TextureRegion(items, 64, 0, 32, 32),
                new TextureRegion(items, 96, 0, 32, 32),
                new TextureRegion(items, 128, 0, 32, 32),
                new TextureRegion(items, 160, 0, 32, 32),
                new TextureRegion(items, 192, 0, 32, 32),
                new TextureRegion(items, 224, 0, 32, 32));
        
        troutHit = new TextureRegion(items, 256, 0, 32, 32);
        
        pufferAnim = new Animation( 0.08f,
                new TextureRegion(items, 0, 32, 32, 64),
                new TextureRegion(items, 32, 32, 32, 64),
                new TextureRegion(items, 64, 32, 32, 64),
                new TextureRegion(items, 96, 32, 32, 64),
                new TextureRegion(items, 128, 32, 32, 64),
                new TextureRegion(items, 160, 32, 32, 64));
        
        pufferHit = new TextureRegion(items, 352, 32, 32, 64);
        
        troutTransformBig = new Animation (0.16f, 
                new TextureRegion(items, 192, 32, 32, 64),
                new TextureRegion(items, 224, 32, 32, 64),
                new TextureRegion(items, 256, 32, 32, 64),
                new TextureRegion(items, 288, 32, 32, 64),
                new TextureRegion(items, 320, 32, 32, 64));
        
        troutTransformSmall = new Animation (0.16f,
        		new TextureRegion(items, 320, 32, 32, 64),
        		new TextureRegion(items, 288, 32, 32, 64),
        		new TextureRegion(items, 256, 32, 32, 64),
        		new TextureRegion(items, 224, 32, 32, 64),
        		new TextureRegion(items, 192, 32, 32, 64));
        		
        jellyAnim = new Animation( 0.08f,
                new TextureRegion(items, 192, 96, 32, 32),
                new TextureRegion(items, 224, 96, 32, 32),
                new TextureRegion(items, 256, 96, 32, 32),
                new TextureRegion(items, 288, 96, 32, 32));
        
        jellyHitTrout = new TextureRegion(items, 192, 128, 32, 32);
        jellyHitPuffer = new TextureRegion(items, 224, 128, 32, 32);
        
        pipeHead = new TextureRegion(items, 320, 96, 32, 32);
        pipeBody = new TextureRegion(items, 320, 96 + 16, 32, 32);
        
        poisonFood = new Animation(0.2f,  
        		new TextureRegion(items, 256, 128, 32, 32),
        		new TextureRegion(items, 288, 128, 32, 32));
        
        empty = new TextureRegion(items, 320, 0, 32, 32);
        
        font = new Font(items, 0, 256, 16, 16, 20);
        
        music = game.getAudio().newMusic("music.mp3");
        music.setLooping(true);
        music.setVolume(0.5f);
        if(Settings.soundEnabled)
            music.play();
        upSound = game.getAudio().newSound("up.wav");
        hitSound = game.getAudio().newSound("hit.ogg");
        coinSound = game.getAudio().newSound("coin.wav");
        clickSound = game.getAudio().newSound("click.ogg");
        eatSound = game.getAudio().newSound("eat.wav");
    }       

    public static void reload() {
    	background.reload();
    	items.reload();
    	
        if(Settings.soundEnabled)
            music.play();
    }

    public static void playSound(Sound sound) {
        if(Settings.soundEnabled)
            sound.play(1);
    }
}
