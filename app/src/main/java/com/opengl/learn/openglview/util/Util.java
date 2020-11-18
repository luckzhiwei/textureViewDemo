package com.opengl.learn.openglview.util;

import android.text.TextUtils;

import java.lang.reflect.Field;

public class Util {

    public static Field reflectGetFieldByName(Field[] fields, String name) {
        Field targetField = null;
        for (int i = 0; i < fields.length; i++) {
            if (TextUtils.equals(name, fields[i].getName())) {
                targetField = fields[i];
                break;
            }
        }
        return targetField;
    }

}
