package com.opengl.learn.openglview.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;


import com.opengl.learn.openglview.IGLESRenderer;
import com.opengl.learn.util.GLUtil;
import com.opengl.learn.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Circle extends IGLESRenderer {

    private Context mContext;
    private int mProgramObject;
    private FloatBuffer mVertices;
    private FloatBuffer mColors;
    private float[] mVerticesData = null;

    private final float[] mColorsData = {
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
    };

    public Circle(Context context) {
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        initmVerticData();
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);
        mColors = ByteBuffer.allocateDirect(mColorsData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mColors.put(mColorsData).position(0);
    }

    private void initmVerticData() {
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);             //设置圆心坐标
        data.add(0.0f);
        data.add(0.0f);
        float radius = 1.0f;
        float angDegSpan = 360f / 360;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(0.0f);
        }
        this.mVerticesData = new float[data.size()];
        for (int i = 0; i < mVerticesData.length; i++) {
            mVerticesData[i] = data.get(i);
        }
    }

    @Override
    public void onSurfaceCreated() {
        // 设置屏幕背景色RGBA
        GLES20.glClearColor(1, 0, 0, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mProgramObject = GLUtil.initGLProram(ShaderUtil.loadShaderFromAsset(GLES20.GL_VERTEX_SHADER, mContext, "vShader/circle.sh"), ShaderUtil.loadShaderFromAsset(GLES20.GL_FRAGMENT_SHADER, mContext, "fShader/circle.sh"));
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }


    @Override
    public void onSurfaceChanged(int width, int height) {
        // 设置视窗大小及位置
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDrawFrame() {// 绘制一frame
        // 清除深度缓冲与颜色缓冲（就是清除缓存）
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        // Clear the color buffer
        GLES20.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // Use the program object
        GLES20.glUseProgram(mProgramObject);

        int mPosHandle = GLES20.glGetAttribLocation(mProgramObject, "vPosition");
        GLES20.glEnableVertexAttribArray(mPosHandle);
        GLES20.glVertexAttribPointer(mPosHandle, 3, GLES20.GL_FLOAT, false, 0, mVertices);

        int maColorHandle = GLES20.glGetAttribLocation(mProgramObject, "aColor");
        GLES20.glEnableVertexAttribArray(maColorHandle);
        GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false, 0, mColors);

        GLES20.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, mVerticesData.length / 3);
    }

    @Override
    public void onDestroy() {
        // do something
    }
}
