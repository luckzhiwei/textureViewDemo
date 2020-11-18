package com.opengl.learn.openglview.exception;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.opengl.learn.openglview.IGLESRenderer;
import com.opengl.learn.openglview.base.SimpleScissor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLESSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {


    private Context context;
    IGLESRenderer renderer;

    public GLESSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public GLESSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context ctx) {
        this.context = ctx;
        this.setEGLContextClientVersion(2);
        this.setRenderer(this);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        renderer = new SimpleScissor();
        renderer.setWidth(1000);
        renderer.setHeight(1000);
        renderer.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        renderer.onSurfaceChanged(i, i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        renderer.onDrawFrame();
    }
}
