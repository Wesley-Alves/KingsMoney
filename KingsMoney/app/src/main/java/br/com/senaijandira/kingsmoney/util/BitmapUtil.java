package br.com.senaijandira.kingsmoney.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class BitmapUtil {
    // Gerencia as imagens das categorias, salvando e recuperando elas dos dados do aplicativo.
    private LruCache<Long, Bitmap> bitmapCache; // Cache, usado para não precisar ficar recuperando o arquivo toda vez.
    private static BitmapUtil instance;

    private BitmapUtil() {
        bitmapCache = new LruCache<Long, Bitmap>(1024);
    }

    public static BitmapUtil getInstance() {
        if (instance == null) {
            instance = new BitmapUtil();
        }

        return instance;
    }

    public void writeImage(Context context, long id, Bitmap image) {
        try {
            FileOutputStream stream = context.openFileOutput(id + ".img", Context.MODE_PRIVATE);
            stream.write(bitmapToByteArray(image));
            stream.close();
            bitmapCache.put(id, image);
        } catch (Exception e) {
        }
    }

    public Bitmap readImage(Context context, long id) {
        Bitmap image = null;
        if (bitmapCache.get(id) != null) {
            return bitmapCache.get(id);
        } else {
            try {
                FileInputStream stream = context.openFileInput(id + ".img");
                image = BitmapFactory.decodeStream(stream);
                stream.close();
                bitmapCache.put(id, image);
            } catch (Exception e) {
            }
        }

        return image;
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}