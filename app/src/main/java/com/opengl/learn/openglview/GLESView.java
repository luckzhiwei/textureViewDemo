package com.opengl.learn.openglview;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.TextureView;

import com.opengl.learn.openglview.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GLESView extends TextureView implements TextureView.SurfaceTextureListener {

  private GLESRender mRender;
  private HandlerThread mRenderThread;
  private android.os.Handler mMainThreadHandler;
  private android.os.Handler mRenderThreadHandler;
  private Field fieldUpdateListener = null;
  private Field fieldUpdateLayer = null;
  private final String KEY_UPDATE_LAYER = "mUpdateLayer";
  private final String KEY_UPDATE_LISTENER = "mUpdateListener";

  private SurfaceTexture.OnFrameAvailableListener canvasFrameAvaiableListener;
  private volatile boolean hasBufferNeedShow = true;

  public GLESView(Context context) {
    super(context);
    init();
  }

  public GLESView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private synchronized void setHasBufferNeedShow(boolean value) {
    this.hasBufferNeedShow = value;
  }

  private Runnable renderNextFrameCallback = new Runnable() {
    @Override public void run() {
      mRenderThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          mRender.drawFrame();
          mRender.swapBuffer();
        }
      });
    }
  };

  private void init() {
    this.setSurfaceTextureListener(this);
    this.mRenderThread = new HandlerThread("render-Thread");
    this.mRenderThread.start();
    this.mRenderThreadHandler = new android.os.Handler(mRenderThread.getLooper());
    this.mMainThreadHandler = new Handler(Looper.getMainLooper());
    initReflectGetField();
    this.canvasFrameAvaiableListener = new SurfaceTexture.OnFrameAvailableListener() {
      @Override
      public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        setHasBufferNeedShow(true);
        trySetUpdateLayerValue(true);
        invalidate();
      }
    };
    tryReplaceFrameUpdateListener();
  }

  @Override
  public void onSurfaceTextureAvailable(SurfaceTexture surface, final int width, final int height) {
    this.mRender = new GLESRender(surface, width, height);
    mRenderThreadHandler.post(new Runnable() {
      @Override
      public void run() {
        try {
          mRender.initEGLContext();
          mRender.onSurfaceCreated();
          mRender.drawFrame();
          mRender.swapBuffer();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

  }

  @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    return false;
  }

  private void tryReplaceFrameUpdateListener() {
    if (fieldUpdateListener == null) {
    } else {
      try {
        fieldUpdateListener.setAccessible(true);
        fieldUpdateListener.set(this, canvasFrameAvaiableListener);
      } catch (Exception ex) {
      }
    }
  }

  private void trySetUpdateLayerValue(boolean value) {
    if (fieldUpdateLayer == null) {

    } else {
      try {
        fieldUpdateLayer.setAccessible(true);
        fieldUpdateLayer.set(this, value);
      } catch (Exception ex) {

      }
    }
  }

  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    if (!hasBufferNeedShow) {
      renderNextFrameCallback.run();
    } else {
      mMainThreadHandler.post(renderNextFrameCallback);
    }
    setHasBufferNeedShow(false);
  }

  private void initReflectGetField() {
    try {
      if (fieldUpdateListener == null) {
        fieldUpdateListener = queryFieldByName(KEY_UPDATE_LISTENER);
      }
      if (fieldUpdateLayer == null) {
        fieldUpdateLayer = queryFieldByName(KEY_UPDATE_LAYER);
      }
    } catch (Exception e) {

    }
  }

  private Field queryFieldByName(String fieldName) {
    Field result = null;
    try {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        result = Util.reflectGetFieldByName(TextureView.class.getDeclaredFields(), fieldName);
      } else {
        Method metaGetDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredFields");
        // 系统类通过反射使用隐藏 API，检查直接通过。
        Object fields = metaGetDeclaredMethod.invoke(TextureView.class);
        if (fields instanceof Field[]) {
          Field[] fieldsArray = (Field[]) fields;
          Field f = Util.reflectGetFieldByName(fieldsArray, fieldName);
          if (f != null) {
            result = f;
          }
        }
      }
    } catch (Exception e) {

    }
    return result;
  }
}