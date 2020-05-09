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

public class Quad extends IGLESRenderer {
    private static final String TAG = Quad.class.getName();
    private Context mContext;
    private int mProgramObject;
    private FloatBuffer mVertices;
    private FloatBuffer mColors;
    private final float[] mVerticesData =
            {0.5f, 0.5f, 0.0f,
                    0.5f, -0.5f, 0.0f,
                    -0.5f, -0.5f, 0.0f,
                    -0.5f, 0.5f, 0.0f
            };

    private final float[] mColorsData = {
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f
    };


    public Quad(Context context) {
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);
        mColors = ByteBuffer.allocateDirect(mColorsData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mColors.put(mColorsData).position(0);
    }

    @Override
    public void onSurfaceCreated() {
        // 设置屏幕背景色RGBA
        GLES20.glClearColor(1, 0, 0, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        this.mProgramObject = GLUtil.initGLProram(ShaderUtil.loadShaderFromAsset(GLES20.GL_VERTEX_SHADER, mContext, "vShader/quard.sh"),
                ShaderUtil.loadShaderFromAsset(GLES20.GL_FRAGMENT_SHADER, mContext, "fShader/quard.sh"));
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
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

        int posHandle = GLUtil.bindBuffer(mProgramObject, "vPosition");
        GLES20.glVertexAttribPointer(posHandle, 3, GLES20.GL_FLOAT, false, 0, mVertices);
        int aColorHandle = GLUtil.bindBuffer(mProgramObject, "aColor");
        GLES20.glVertexAttribPointer(aColorHandle, 4, GLES20.GL_FLOAT, false, 0, mColors);

        GLES20.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 4);
    }

    @Override
    public void onDestroy() {

    }
}
