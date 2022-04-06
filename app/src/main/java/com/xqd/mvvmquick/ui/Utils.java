package com.xqd.mvvmquick.ui;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: XieQD
 * @Date: 2022/4/6 14:42
 * @Description: Powered by GWM
 */

public class Utils {
    public static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().toString();

    public static boolean getRotationMatrix(float[] R,float[] gravity) {
        // TODO: move this to native code for efficiency
        float Ax = gravity[0];
        float Ay = gravity[1];
        float Az = gravity[2];

        final float normsqA = (Ax * Ax + Ay * Ay + Az * Az);
        final float g = 9.81f;
        final float freeFallGravitySquared = 0.01f * g * g;
        if (normsqA < freeFallGravitySquared) {
            // gravity less than 10% of normal value
            return false;
        }

//        final float Ex = geomagnetic[0];
//        final float Ey = geomagnetic[1];
//        final float Ez = geomagnetic[2];
//        float Hx = Ey * Az - Ez * Ay;
//        float Hy = Ez * Ax - Ex * Az;
//        float Hz = Ex * Ay - Ey * Ax;
//        final float normH = (float) Math.sqrt(Hx * Hx + Hy * Hy + Hz * Hz);
//
//        if (normH < 0.1f) {
//            // device is close to free fall (or in space?), or close to
//            // magnetic north pole. Typical values are  > 100.
//            return false;
//        }
//        final float invH = 1.0f / normH;
//        Hx *= invH;
//        Hy *= invH;
//        Hz *= invH;
        final float invA = 1.0f / (float) Math.sqrt(Ax * Ax + Ay * Ay + Az * Az);
        Ax *= invA;
        Ay *= invA;
        Az *= invA;
//        final float Mx = Ay * Hz - Az * Hy;
//        final float My = Az * Hx - Ax * Hz;
//        final float Mz = Ax * Hy - Ay * Hx;
        if (R != null) {
            if (R.length == 9) {
//                R[0] = Hx;     R[1] = Hy;     R[2] = Hz;
//                R[3] = Mx;     R[4] = My;     R[5] = Mz;
                R[6] = Ax;     R[7] = Ay;     R[8] = Az;
            } else if (R.length == 16) {
//                R[0]  = Hx;    R[1]  = Hy;    R[2]  = Hz;   R[3]  = 0;
//                R[4]  = Mx;    R[5]  = My;    R[6]  = Mz;   R[7]  = 0;
                R[8]  = Ax;    R[9]  = Ay;    R[10] = Az;   R[11] = 0;
                R[12] = 0;     R[13] = 0;     R[14] = 0;    R[15] = 1;
            }
        }

        return true;
    }

    /**
     * 五点三次平滑
     * @param degrees
     * @param n
     * @return
     */
    public static ArrayList<Double> optimizePointsThree(List<Double> degrees, int n) {
        ArrayList<Double> latLngsAfter = new ArrayList<>();
        int i;
        if (n < 5) {
            for (i = 0; i < n; i++) {
                latLngsAfter.add(degrees.get(i));
            }

        } else {

            latLngsAfter.add((69.0 * degrees.get(0)+ 4.0 * degrees.get(1)- 6.0 * degrees.get(2)+ 4.0 * degrees.get(3) - degrees.get(4)) / 70.0);
            latLngsAfter.add((2.0 * degrees.get(0) + 27.0 * degrees.get(1) + 12.0 * degrees.get(2) - 8.0 * degrees.get(3) + 2.0 * degrees.get(4)) / 35.0);

            for (i = 2; i <= n - 3; i++) {
                latLngsAfter.add((-3.0 * degrees.get(i - 2) + 12.0 * degrees.get(i - 1) + 17.0 * degrees.get(i) + 12.0 * degrees.get(i + 1) - 3.0 * degrees.get(i + 2)) / 35.0);
            }

            latLngsAfter.add((2.0 * degrees.get(n - 5) - 8.0 * degrees.get(n - 4) + 12.0 * degrees.get(n - 3) + 27.0 * degrees.get(n - 2) + 2.0 * degrees.get(n - 1)) / 35.0);
            latLngsAfter.add((-degrees.get(n - 5) + 4.0 * degrees.get(n - 4) - 6.0 * degrees.get(n - 3) + 4.0 * degrees.get(n - 2) + 69.0 * degrees.get(n - 1)) / 70.0);
        }

        return latLngsAfter;

    }

    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }

            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
}
