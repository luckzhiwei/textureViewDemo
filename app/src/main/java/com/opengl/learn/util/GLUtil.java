package com.opengl.learn.util;

import android.opengl.GLES20;
import android.util.Log;

public class GLUtil {

    public static final String TAG = GLUtil.class.getName();

    public static int initGLProram(int vertexShader, int fragmentShader) {
        if (vertexShader == -1 || fragmentShader == -1)
            return -1;
        int programObject;
        programObject = GLES20.glCreateProgram();
        int[] linked = new int[1];
        if (programObject == 0) {
            return -1;
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
            return -1;
        }
        return programObject;
    }

    public static int bindBuffer(int programObject, String attrName) {
        int handle = GLES20.glGetAttribLocation(programObject, attrName);
        GLES20.glEnableVertexAttribArray(handle);
        return handle;
    }
}
