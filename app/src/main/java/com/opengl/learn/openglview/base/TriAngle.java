package com.opengl.learn.openglview.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import com.opengl.learn.openglview.IGLESRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TriAngle extends IGLESRenderer {

    private static final String TAG = "SimpleTriangle";
    private Context mContext;
    private int mProgramObject;
    private FloatBuffer mVertices;
    private FloatBuffer mColors;
    private final float[] mVerticesData =
            {0.5f, 0.5f, 0.0f,
                    -0.5f, -0.5f, 0.0f,
                    0.5f, -0.5f, 0.0f};

    private final float[] mColorsData = {
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
    };

    private String mVShaderStr = "			  \n"
            + "attribute vec4 aColor;              \n"
            + "attribute vec4 vPosition;           \n"
            + "varying vec4 vColor;              \n"
            + "void main()                  \n"
            + "{                            \n"
            + "   gl_Position = vPosition;    \n"
            + "   vColor = aColor;    \n"
            + "}                            \n";

    private String mFShaderStr = "\n"
            + "precision mediump float;					  	\n"
            + "varying vec4 vColor;	 			 		  	\n"
            + "void main()                                  \n"
            + "{                                            \n"
            + "  gl_FragColor = vColor;	\n"
            + "}                                            \n";

    public TriAngle(Context context) {
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
        int vertexShader;
        int fragmentShader;
        int programObject;
        int[] linked = new int[1];

        // Load the vertex/fragment shaders
        vertexShader = LoadShader(GLES20.GL_VERTEX_SHADER, mVShaderStr);
        fragmentShader = LoadShader(GLES20.GL_FRAGMENT_SHADER, mFShaderStr);

        // Create the program object
        programObject = GLES20.glCreateProgram();

        if (programObject == 0) {
            return;
        }

        GLES20.glAttachShader(programObject, vertexShader);
        GLES20.glAttachShader(programObject, fragmentShader);

        // Bind vPosition to attribute 0
        GLES20.glBindAttribLocation(programObject, 0, "vPosition");

        // Link the program
        GLES20.glLinkProgram(programObject);

        // Check the link status
        GLES20.glGetProgramiv(programObject, GLES20.GL_LINK_STATUS, linked, 0);

        if (linked[0] == 0) {
            Log.e(TAG, "Error linking program:");
            Log.e(TAG, GLES20.glGetProgramInfoLog(programObject));
            GLES20.glDeleteProgram(programObject);
            return;
        }

        // Store the program object
        mProgramObject = programObject;

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    }

    private int LoadShader(int type, String shaderSrc) {
        int shader;
        int[] compiled = new int[1];

        // Create the shader object
        shader = GLES20.glCreateShader(type);

        if (shader == 0) {
            return 0;
        }

        // Load the shader source
        GLES20.glShaderSource(shader, shaderSrc);

        // Compile the shader
        GLES20.glCompileShader(shader);

        // Check the compile status
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0) {
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }

        return shader;
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        // 设置视窗大小及位置
        GLES20.glViewport(0, 0, width, height);
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


        GLES20.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
    }

    @Override
    public void onDestroy() {
        // do something
    }
}
