package com.opengl.learn.openglview;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.opengl.learn.openglview.base.SimpleTriangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRender implements GLSurfaceView.Renderer {

    IGLESRenderer renderer;
    private Context mContext;

    public GLRender(Context ctx) {
        this.mContext = ctx;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        renderer = new SimpleTriangle(this.mContext);
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
