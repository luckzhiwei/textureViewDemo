package com.opengl.learn.openglview;

public abstract class IGLESRenderer {

    protected int mWidth = 0;

    protected int mHeight = 0;

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    /**
     * <pre>
     * Surface创建好之后
     * </pre>
     */
    public abstract void onSurfaceCreated();

    /**
     * <pre>
     * 界面大小有更改
     * </pre>
     *
     * @param width
     * @param height
     */
    public abstract void onSurfaceChanged(int width, int height);

    /**
     * <pre>
     * 绘制每一帧
     * </pre>
     */
    public void onDrawFrame() {

    }

    /**
     * <pre>
     * Activity的onResume时的操作
     * </pre>
     */
    public abstract void onResume();

    /**
     * <pre>
     * Activity的onPause时的操作
     * </pre>
     */
    public abstract void onPause();

    /**
     * <pre>
     * Activity的onDestroy时的操作
     * </pre>
     */
    public abstract void onDestroy();


}
