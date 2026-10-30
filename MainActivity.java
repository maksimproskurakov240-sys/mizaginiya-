package com.myapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView bg = findViewById(R.id.bg);
        loadBlurredBg(bg);

        Button btn = findViewById(R.id.installBtn);
        btn.setOnClickListener(v -> ClickSound.play());
    }

    private void loadBlurredBg(ImageView bg) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.bg, opts);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ — аппаратное размытие
            bg.setImageBitmap(src);
            bg.setRenderEffect(
                RenderEffect.createBlurEffect(6f, 6f, Shader.TileMode.CLAMP)
            );
        } else {
            // Android 5–11 — программное размытие
            bg.setImageBitmap(softBlur(src));
        }
    }

    /** Быстрое размытие: уменьшить → увеличить с фильтрацией */
    private Bitmap softBlur(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap small = Bitmap.createScaledBitmap(src, Math.max(w / 5, 1), Math.max(h / 5, 1), false);
        return Bitmap.createScaledBitmap(small, w, h, true);
    }
}
