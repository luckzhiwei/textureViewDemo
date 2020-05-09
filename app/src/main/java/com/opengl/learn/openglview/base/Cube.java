package com.opengl.learn.openglview.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import com.opengl.learn.openglview.IGLESRenderer;
import com.opengl.learn.util.GLUtil;
import com.opengl.learn.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Cube extends IGLESRenderer {

    private Context mContext;
    private int mProgramObject;
    private FloatBuffer mVertices;
    private FloatBuffer mColors;
    private ShortBuffer mIndexBuffer;
    private int mWidth;
    private int mHeight;
    private final float[] mVerticesData = {
            -1.0f, 1.0f, 1.0f,//0
            -1.0f, -1.0f, 1.0f,//1
            1.0f, -1.0f, 1.0f,//2
            1.0f, 1.0f, 1.0f,//3
            -1.0f, 1.0f, -1.0f,//4
            -1.0f, -1.0f, -1.0f,//5
            1.0f, -1.0f, -1.0f,//6
            1.0f, 1.0f, -1.0f,//7
    };

    private short indexes[] = {
            6, 7, 4, 6, 4, 5,//back
            6, 7, 3, 6, 2, 3,//right
            6, 5, 1, 6, 2, 1,//bottom
            0, 1, 2, 0, 2, 3,//forward
            0, 1, 5, 0, 4, 5,//left
            0, 4, 7, 0, 7, 3//top

    };

    private final float[] mColorsData = {
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
    };


    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    public Cube(Context context, int width, int height) {
        init(context);
        this.mWidth = width;
        this.mHeight = height;
    }

    private void init(Context context) {
        this.mContext = context;
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);
        mColors = ByteBuffer.allocateDirect(mColorsData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mColors.put(mColorsData).position(0);
        mIndexBuffer = ByteBuffer.allocateDirect(indexes.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndexBuffer.put(indexes).position(0);
    }


    @Override
    public void onSurfaceCreated() {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glViewport(0, 0, mWidth, mHeight);
        mProgramObject = GLUtil.initGLProram(ShaderUtil.loadShaderFromAsset(GLES20.GL_VERTEX_SHADER, mContext, "vShader/cube.sh"),
                ShaderUtil.loadShaderFromAsset(GLES20.GL_FRAGMENT_SHADER, mContext, "fShader/cube.sh"));

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.initMVP();
    }

    private void initMVP() {
        float ratio = 1.0f * mWidth / mHeight;
        //设置视角范围
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 5.0f, 0.0f, 10.0f, 0f, 0f, 0f, 0.f, 0.0f, 1.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }


    @Override
    public void onSurfaceChanged(int width, int height) {
        // 设置视窗大小及位置

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
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Use the program object
        GLES20.glUseProgram(mProgramObject);

        int posHandle = GLUtil.bindBuffer(mProgramObject, "vPosition");
        GLES20.glVertexAttribPointer(posHandle, 3, GLES20.GL_FLOAT, false, 0, mVertices);

        int colorHandle = GLUtil.bindBuffer(mProgramObject, "aColor");
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, mColors);

        int matrixHandle = GLES20.glGetUniformLocation(mProgramObject, "vMatrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexes.length, GLES20.GL_UNSIGNED_SHORT, this.mIndexBuffer);
    }

    @Override
    public void onDestroy() {
        // do something
    }
}
