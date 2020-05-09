package com.opengl.learn.openglview;

import android.graphics.SurfaceTexture;
import android.opengl.GLException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;


import com.opengl.learn.openglview.exception.OpenGLException;


import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import static android.opengl.EGL14.EGL_CONTEXT_CLIENT_VERSION;
import static android.opengl.EGL14.EGL_OPENGL_ES2_BIT;
import static com.opengl.learn.openglview.GLESView.getCurrentTimeStamp;

public class GLESThread {
    public static volatile int count = 0;

    private SurfaceTexture texture;

    private EGL10 mEGL;

    private EGLDisplay mDisplay;

    private EGLContext mEGLContext;

    private EGLSurface mEglSurface;

    private IGLESRenderer mRender;

    private Choreographer.FrameCallback frameCallback;

    private HandlerThread renderThread;
    private Handler renderHandler;

    private static int[] configAttribs = new int[]{ //
            EGL10.EGL_BUFFER_SIZE, 32,//
            EGL10.EGL_ALPHA_SIZE, 8, // 指定Alpha大小，以下四项实际上指定了像素格式
            EGL10.EGL_BLUE_SIZE, 8, // 指定B大小
            EGL10.EGL_GREEN_SIZE, 8,// 指定G大小
            EGL10.EGL_RED_SIZE, 8,// 指定RGB中的R大小（bits）
            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,// 指定渲染api类别,这里或者是硬编码的4，或者是EGL14.EGL_OPENGL_ES2_BIT
            EGL10.EGL_SURFACE_TYPE, EGL10.EGL_WINDOW_BIT,
            EGL10.EGL_NONE// 总是以EGL10.EGL_NONE结尾
    };

    private GLESView view;


    public GLESThread(SurfaceTexture texture, IGLESRenderer mRender, GLESView view) {
        this.texture = texture;
        this.mRender = mRender;
        this.renderThread = new HandlerThread("handlerThread");
        this.renderThread.start();
        this.renderHandler = new Handler(renderThread.getLooper());
        this.view = view;
    }


    public void initEGLContext() throws Exception {
        this.mEGL = (EGL10) EGLContext.getEGL();
        this.mDisplay = this.mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (this.mDisplay == EGL10.EGL_NO_DISPLAY)
            throw OpenGLException.newException(OpenGLException.EGL_NO_DISPLAY);
        int[] ver = new int[2];
        if (!this.mEGL.eglInitialize(this.mDisplay, ver))
            throw OpenGLException.newException(OpenGLException.EGL_INIT_FAIL);

        EGLConfig[] config = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!this.mEGL.eglChooseConfig(this.mDisplay, configAttribs, config, 1, numConfigs))
            throw OpenGLException.newException(OpenGLException.EGL_CHOOSE_CONFIG_FAILED);

        int[] contextAttribs = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};

        this.mEGLContext = mEGL.eglCreateContext(this.mDisplay, config[0], EGL10.EGL_NO_CONTEXT, contextAttribs);
        this.mEglSurface = mEGL.eglCreateWindowSurface(this.mDisplay, config[0], this.texture, null);

        if (this.mEglSurface == EGL10.EGL_NO_SURFACE || this.mEGLContext == EGL10.EGL_NO_CONTEXT) {
            throw OpenGLException.newException(OpenGLException.EGL_SURFACE_OR_CONTEXT_INIT_FAIL);
        }
        this.makecurrent();
    }

    private void makecurrent() throws Exception {
        if (!this.mEGL.eglMakeCurrent(this.mDisplay, this.mEglSurface, this.mEglSurface, this.mEGLContext)) {
            throw OpenGLException.newException(OpenGLException.EGL_MAKECURRENT_FAIL);
        }
    }

    public void swapBuffer() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        egl.eglSwapBuffers(mDisplay, mEglSurface);
    }

    public void run() {

        renderHandler.post(new Runnable() {
            @Override
            public void run() {
                initChoreographer();
            }
        });

        renderHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    initEGLContext();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        renderHandler.post(new Runnable() {
            @Override
            public void run() {
                mRender.onSurfaceCreated();
                mRender.onDrawFrame();
                swapBuffer();
                Choreographer.getInstance().postFrameCallback(frameCallback);
            }
        });

    }

    private void initChoreographer() {
        frameCallback = new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long l) {
                mRender.onDrawFrame();
                swapBuffer();
                Log.e("Alan", "on swapBuffer()" + getCurrentTimeStamp());
                Choreographer.getInstance().postFrameCallback(frameCallback);
            }
        };
    }

    public void onSurfaceChanged(int width, int height) {
        mRender.onSurfaceChanged(width, height);
    }


}
