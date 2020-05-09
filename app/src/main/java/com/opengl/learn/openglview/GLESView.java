package com.opengl.learn.openglview;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.TextureView;

import com.opengl.learn.openglview.base.SimpleTriangle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GLESView extends TextureView implements TextureView.SurfaceTextureListener {

    private GLESThread mThread;
    private Context context;
    private IGLESRenderer renderer;
    public AtomicBoolean flag = new AtomicBoolean(false);
    //    public volatile boolean flag = false;
    public Object lock = new Object();
    //    public volatile boolean flag;
    private volatile int callCount = 0;
    private int waitCount = 0;
    volatile boolean hasPendingFrame = false;

    private long lastTime = 0;
    private volatile int swapIndex = 0;

    private HandlerThread thread;
    private android.os.Handler handler;
    private long currentTime = SystemClock.currentThreadTimeMillis();
    private long currentTime2 = SystemClock.currentThreadTimeMillis();
    private AtomicInteger index = new AtomicInteger(0);

    private long frames = 0;

    public GLESView(Context context) {
        super(context);
        init(context);
    }

    public GLESView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context ctx) {
        this.context = ctx;
        this.setSurfaceTextureListener(this);
        this.thread = new HandlerThread("");
        this.thread.start();
        this.handler = new android.os.Handler(this.thread.getLooper());
//        this.handler = new android.os.Handler(Looper.getMainLooper());

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        renderer = new SimpleTriangle(this.context);
        renderer.setWidth(width);
        renderer.setHeight(height);
        currentTime = SystemClock.currentThreadTimeMillis();
        frames = 0;

//        this.mThread = new GLESThread(surface, renderer, this);
//        this.mThread.run();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mThread.initEGLContext();
                    renderer.onSurfaceCreated();
                    renderer.onDrawFrame();
                    mThread.swapBuffer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
//            @Override
//            public void doFrame(long l) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            synchronized (GLESView.class) {
//                                if (frames == 0) {
//                                    currentTime = System.currentTimeMillis();
//                                }
//
//                                frames++;
//
//
//                                Log.e("Alan", "VSYNC call in");
//                                if (!hasPendingFrame) {
//                                    Log.e("Alan", "VYSNC Direct render");
//                                    renderer.onDrawFrame();
//                                    mThread.swapBuffer();
//
//                                    renderer.onDrawFrame();
//                                    mThread.swapBuffer();
////                                    hasPendingFrame = true;
//                                } else {
//                                    Log.e("Alan", "VYSNC hasPendingFrame is " + hasPendingFrame);
//                                    callback = new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            renderer.onDrawFrame();
//                                            mThread.swapBuffer();
//                                            hasPendingFrame = true;
//                                        }
//                                    };
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
////                Choreographer.getInstance().postFrameCallback(this);
//            }
//        };
//
//        Choreographer.getInstance().postFrameCallback(frameCallback);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, final int width,
                                            final int height) {
        mThread.onSurfaceChanged(width, height);
    }


    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//        Log.e("Alan", "onSurfaceTextureUpdated " + getCurrentTimeStamp());
        handler.post(new Runnable() {
            @Override
            public void run() {
                renderer.onDrawFrame();
                mThread.swapBuffer();
            }
        });
//        synchronized (GLESView.class) {
//            post(new Runnable() {
//                @Override
//                public void run() {
//                    if (!hasPendingFrame) {
//                        return;
//                    }
//                    updateFrames++;
//                    Log.e("Alan", "onSurfaceTextureUpdated in hasPendingFrame value is" + hasPendingFrame);
//                    hasPendingFrame = false;
//                    if (callback != null) {
//                        Log.e("Alan", "has callback");
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                synchronized (GLESView.class) {
//                                    callback.run();
//                                }
//                            }
//                        });
//                    }
//                    Log.e("Alan", "onSurfaceTextureUpdated call finish hasPendingFrame  value is" + hasPendingFrame);
//                }
//            });
//
//        }
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

}