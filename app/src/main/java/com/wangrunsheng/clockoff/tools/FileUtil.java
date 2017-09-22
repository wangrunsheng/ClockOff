package com.wangrunsheng.clockoff.tools;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Russell on 2017/9/20.
 */

public class FileUtil {
    public static void saveLocation(String locationStr) {
        String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder = diskPath + File.separatorChar + "wangrunsheng" + File.separatorChar + "location";

        File jsonFolder = new File(folder);
        if (!jsonFolder.exists()) {
            jsonFolder.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File jsonFile = new File(jsonFolder, timeStamp + ".txt");
        try {
            FileOutputStream outputStream = new FileOutputStream(jsonFile);
            outputStream.write(locationStr.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(String string) {

    }
}
