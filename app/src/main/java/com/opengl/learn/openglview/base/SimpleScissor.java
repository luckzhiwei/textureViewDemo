package com.opengl.learn.openglview.base;

import android.opengl.GLES20;
import com.opengl.learn.openglview.IGLESRenderer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SimpleScissor extends IGLESRenderer {

    private static final String TAG = "SimpleScissor";
    private FloatBuffer mVertices;
    private final float[] mVerticesData =
            {-0.5f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.0f, -0.5f, 0.0f};

    private int[] size = {60, 60};
    private int velocity = 20;
    private int[] position;


    public SimpleScissor() {
        this.init();
    }

    private void init() {
        this.preparemVertices();
    }

    private void preparemVertices() {
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);
    }


    @Override
    public void onSurfaceCreated() {
        // 设置屏幕背景色RGBA
        this.position = new int[2];
        position[0] = 0;
        position[1] = mHeight;
    }


    @Override
    public void onSurfaceChanged(int width, int height) {
        // 设置视窗大小及位置
        GLES20.glViewport(0, 0, width, height);
        mWidth = width;
        mHeight = height;
        this.position = new int[2];
        position[0] = 0;
        position[1] = mHeight;
    }

    @Override
    public void onResume() {
        // do something
    }

    @Override
    public void onPause() {
        // do something
    }

    @Override
    public void onDrawFrame() {// 绘制一frame
        // 清除深度缓冲与颜色缓冲（就是清除缓存）
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(0, 0, mWidth, mHeight);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glScissor(position[0], position[1], size[0], size[1]);
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        position[1] -= velocity;
        if (position[1] < 0) {
            position = new int[2];
            position[0] = (mWidth - size[0]);
            position[1] = mHeight;
        }
    }

    @Override
    public void onDestroy() {
    }
}
