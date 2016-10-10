package com.simple.bubbleviewlibrary;

import java.util.Random;

/**
 * 颗粒类
 */
public class Particle {

    float cx; //center x of circle
    float cy; //center y of circle
    float radius;

    int color;
    float alpha;

    public static int particleCount = 10;

    Random random = new Random();

    public Particle(float cx, float cy, float radius,int color) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.color = color;
    }

    public void broken(float factor, int width, int height) {
        cx = cx + factor * random.nextInt(width) * (random.nextFloat() - 0.5f);
        cy = cy + factor * random.nextInt(height / 2);

        radius = radius - factor * random.nextInt(2);

        alpha = (1f - factor) * (1 + random.nextFloat());
    }

}
