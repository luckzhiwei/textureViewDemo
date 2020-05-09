package com.opengl.learn.openglview.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.opengl.learn.util.GLUtil;
import com.opengl.learn.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Cylinder extends Shape {


    private float cylinderHeight;
    private float[] mVerticesData;
    private float[] mVerticesDataBottom;
    private float[] mVerticesDataTop;
    private FloatBuffer mVerticesBuffer;
    private FloatBuffer mVerticesBufferBottom;
    private FloatBuffer mVerticesBufferTop;


    public Cylinder(Context context, float width, float height) {
        super(context, width, height);
    }

    @Override
    protected void init(Context ctx) {
        this.cylinderHeight = 2.0f;
        this.initmVertexData();
        mVerticesBuffer = ByteBuffer.allocateDirect(mVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVerticesBuffer.put(mVerticesData).position(0);
        mVerticesBufferBottom = ByteBuffer.allocate(mVerticesDataBottom.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVerticesBufferBottom.put(mVerticesDataBottom).position(0);
    }

    private void initmVertexData() {
        ArrayList<Float> data = new ArrayList<>();
        float radius = 1.0f;
        float angDegSpan = 360f / 360;
        for (float i = 0.0f; i < 360.0 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(cylinderHeight);
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(0.0f);
        }
        this.mVerticesData = new float[data.size()];
        for (int i = 0; i < mVerticesData.length; i++) {
            mVerticesData[i] = data.get(i);
        }
        data.clear();
        data.add(0.0f);
        data.add(0.0f);
        data.add(cylinderHeight);
        for (float i = 0.0f; i < 360.0 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(cylinderHeight);
        }
        this.mVerticesDataBottom = new float[data.size()];
        for (int i = 0; i < mVerticesDataBottom.length; i++) {
            mVerticesDataBottom[i] = data.get(i);
        }


    }

    @Override
    public void onSurfaceCreated() {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mProgramObject = GLUtil.initGLProram(ShaderUtil.loadShaderFromAsset(GLES20.GL_VERTEX_SHADER, mContext, "vShader/cylinder.sh"),
                ShaderUtil.loadShaderFromAsset(GLES20.GL_FRAGMENT_SHADER, mContext, "fShader/cylinder.sh"));
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.initMVP();
    }

    private void initMVP() {
        float ratio = 1.0f * mWidth / mHeight;
        //设置视角范围
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 10.0f, 0.0f, 0.0f, 0f, 0f, 0f, 0.0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {

    }

    @Override
    public void onDrawFrame() {// 绘制一frame
        // 清除深度缓冲与颜色缓冲（就是清除缓存）
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        // Clear the color buffer
        GLES20.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // Use the program object
        GLES20.glUseProgram(mProgramObject);

        int mPosHandle = GLUtil.bindBuffer(mProgramObject, "vPosition");
        GLES20.glVertexAttribPointer(mPosHandle, 3, GLES20.GL_FLOAT, false, 0, mVerticesBuffer);

        int matrixHandle = GLES20.glGetUniformLocation(mProgramObject, "vMatrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, mVerticesData.length / 3);

        mPosHandle = GLUtil.bindBuffer(mProgramObject, "vPosition");
//        GLES20.glVertexAttribPointer(mPosHandle, 3, GLES20.GL_FLOAT, false, 0, mVerticesBufferBottom);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mVerticesDataBottom.length / 3);

        super.onDrawFrame();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
