package com.opengl.learn;

import android.os.Bundle;
import android.view.Choreographer;
import android.widget.LinearLayout;

import com.opengl.learn.openglview.BlockingGLTextureView;
import com.opengl.learn.openglview.GLESView;
import com.opengl.learn.openglview.GLRender;
import com.opengl.learn.openglview.exception.GLESSurfaceView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private BlockingGLTextureView view;

    private Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long l) {
            view.render();
            Choreographer.getInstance().postFrameCallback(frameCallback);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLESView view1 = new GLESView(this);
        view1.setLayoutParams(new LinearLayout.LayoutParams(1000, 1000));
        setContentView(view1);
//        GLESSurfaceView view1 = new GLESSurfaceView(this);
//        view1.setLayoutParams(new LinearLayout.LayoutParams(1000, 1000));
//        setContentView(view1);
//        view = new BlockingGLTextureView(this);
//        GLRender render = new GLRender(this);
//        view.setRenderer(render);
//        view.setLayoutParams(new LinearLayout.LayoutParams(1000, 1000));
//        setContentView(view);
//        Choreographer.getInstance().postFrameCallback(frameCallback);
    }

}
