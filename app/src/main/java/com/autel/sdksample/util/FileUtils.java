package com.autel.sdksample.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Created by A13087 on 2017/5/19.
 */

public class FileUtils {

    public static void Initialize(Context context, String path, String fileName){
        File modelFile = new File(path);
        if(!modelFile.exists()) {
            try {
                if(!Objects.requireNonNull(modelFile.getParentFile()).exists()){
                    modelFile.getParentFile().mkdirs();
                }
                modelFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] buffer = new byte[2048];
        AssetManager assets = context.getAssets();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try (InputStream modelStream = assets.open(fileName); BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(modelFile.toPath()))) {

                int count;
                while ((count = modelStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
