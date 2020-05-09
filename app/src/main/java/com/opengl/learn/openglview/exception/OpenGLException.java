package com.opengl.learn.openglview.exception;

import java.util.HashMap;

public class OpenGLException extends RuntimeException {


    private int mErrorCode;

    private String DEAFULT_EMSG = "ERROR ON GL";

    private OpenGLException(int errorCode) {
        this.mErrorCode = errorCode;
    }

    static {
        EMSG_MAP = new HashMap<>();
        init();
    }

    private static void init() {
        EMSG_MAP.put(EGL_NO_DISPLAY, "EGL_NO_DISPLAY");
        EMSG_MAP.put(EGL_INIT_FAIL, "EGL_INIT_FAIL");
        EMSG_MAP.put(EGL_CHOOSE_CONFIG_FAILED, "EGL_CHOOSE_CONFIG_FAILED");
        EMSG_MAP.put(EGL_SURFACE_OR_CONTEXT_INIT_FAIL, " EGL_SURFACE_OR_CONTEXT_INIT_FAIL ");
        EMSG_MAP.put(EGL_MAKECURRENT_FAIL, "EGL_MAKECURRENT_FAIL");
    }


    private static HashMap<Integer, String> EMSG_MAP;
    public static int EGL_NO_DISPLAY = 0x01;
    public static int EGL_INIT_FAIL = 0x02;
    public static int EGL_CHOOSE_CONFIG_FAILED = 0x03;
    public static int EGL_SURFACE_OR_CONTEXT_INIT_FAIL = 0x04;
    public static int EGL_MAKECURRENT_FAIL = 0x05;


    public static OpenGLException newException(int errCode) {
        return new OpenGLException(errCode);
    }


    @Override
    public String getMessage() {
        String ret = EMSG_MAP.get(mErrorCode);
        if (ret == null)
            return DEAFULT_EMSG;
        else
            return ret;
    }



}
