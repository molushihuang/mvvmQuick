package com.xqd.mylibrary.utlis;

import android.graphics.Color;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/6/9.
 * 数据类型转换工具
 */

public class ConvertToUtils {

    public ConvertToUtils() {
    }

    public static String toString(String var0) {
        return isNullOrEmpty(var0) ? "" : var0;
    }

    public static String toString(Object var0) {
        return isNullOrEmpty(var0) ? "" : var0.toString();
    }

    public static int toInt(String var0) {
        return toInt(var0, 0);
    }

    public static int toInt(String var0, int var1) {
        if (isNullOrEmpty(var0)) {
            return var1;
        } else {
            try {
                return Integer.parseInt(var0);
            } catch (NumberFormatException var2) {
                return var1;
            }
        }
    }

    public static boolean toBoolean(String var0) {
        return toBoolean(var0, false);
    }

    public static boolean toBoolean(String var0, boolean var1) {
        return isNullOrEmpty(var0) ? var1 : (!"false".equalsIgnoreCase(var0) && !"0".equals(var0) ? (!"true".equalsIgnoreCase(var0) && !"1".equals(var0) ? var1 : true) : false);
    }

    public static float toFloat(String var0) {
        return toFloat(var0, 0.0F);
    }

    public static float toFloat(String var0, float var1) {
        if (isNullOrEmpty(var0)) {
            return var1;
        } else {
            try {
                return Float.parseFloat(var0);
            } catch (NumberFormatException var2) {
                return var1;
            }
        }
    }

    public static double toDouble(String var0, double var1) {
        if (isNullOrEmpty(var0)) {
            return var1;
        } else {
            try {
                return Double.parseDouble(var0);
            } catch (NumberFormatException var2) {
                return var1;
            }
        }
    }

    public static long toLong(String var0) {
        return toLong(var0, 0L);
    }

    public static long toLong(String var0, long var1) {
        if (isNullOrEmpty(var0)) {
            return var1;
        } else {
            try {
                return Long.parseLong(var0);
            } catch (NumberFormatException var3) {
                return var1;
            }
        }
    }


    public static short toShort(String var0, short var1) {
        if (isNullOrEmpty(var0)) {
            return var1;
        } else {
            try {
                return Short.parseShort(var0);
            } catch (NumberFormatException var2) {
                return var1;
            }
        }
    }

    public static int toColor(String var0, int var1) {
        if (isNullOrEmpty(var0)) {
            return var1;
        } else {
            try {
                return Color.parseColor(var0);
            } catch (Exception var2) {
                return var1;
            }
        }
    }

    /**
     * 返回两位小数的double值
     *
     * @param fordouble
     * @return
     */
    public static double doubleTow(double fordouble) {
        try {
            return new BigDecimal(fordouble).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }

    }


    public static double doubleTow(double fordouble,int newScale) {
        try {
            return new BigDecimal(fordouble).setScale(newScale, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }

    }

    /**
     * double类型转为两位小数点的字符串形式
     *
     * @param x1
     * @return
     */
    public static String doubleToString(double x1) {
        BigDecimal b = null;
        try {
            b = new BigDecimal(x1);
            return b.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue() + "";
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    public static String doubleToString(double x1, int newScale) {
        BigDecimal b = null;
        try {
            b = new BigDecimal(x1);
            return b.setScale(newScale, BigDecimal.ROUND_HALF_DOWN).doubleValue() + "";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static boolean isNullOrEmpty(String var0) {
        return var0 == null || var0.length() == 0;
    }

    public static boolean isNullOrEmpty(Object var0) {
        return var0 == null || var0.toString().length() == 0;
    }

}
