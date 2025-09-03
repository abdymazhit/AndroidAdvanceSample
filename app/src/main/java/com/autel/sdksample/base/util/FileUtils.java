package com.autel.sdksample.base.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by A13087 on 2017/5/19.
 */

public class FileUtils {

    public static void Initialize(Context context, String path, String fileName){
        File modelFile = new File(path);
        if(!modelFile.exists()) {
            try {
                if(!modelFile.getParentFile().exists()){
                    modelFile.getParentFile().mkdirs();
                }
                modelFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] buffer = new byte[2048];
        AssetManager assets = context.getAssets();

        try (InputStream modelStream = assets.open(fileName); BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(modelFile))) {

            int count;
            while ((count = modelStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
