package com.opengl.learn.openglview.base;

import android.content.Context;

import com.opengl.learn.openglview.IGLESRenderer;

public abstract class Shape extends IGLESRenderer {

    protected float[] mViewMatrix = new float[16];
    protected float[] mProjectMatrix = new float[16];
    protected float[] mMVPMatrix = new float[16];
    protected Context mContext;
    protected int mProgramObject;
    protected float mWidth;
    protected float mHeight;

    public Shape(Context context, float width, float height) {
        this.mContext = context;
        this.mWidth = width;
        this.mHeight = height;
        this.init(context);
    }

    protected abstract void init(Context ctx);

}
