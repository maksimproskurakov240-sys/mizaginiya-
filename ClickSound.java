package com.myapp.ui;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/** Генерирует звук "тык" без внешних файлов */
public class ClickSound {

    public static void play() {
        new Thread(() -> {
            try {
                int sampleRate = 44100;
                int durationMs = 80;
                int numSamples = sampleRate * durationMs / 1000;
                short[] buf = new short[numSamples];

                for (int i = 0; i < numSamples; i++) {
                    double t = (double) i / sampleRate;
                    double env = Math.exp(-t * 40.0);       // быстрое затухание
                    buf[i] = (short) (Short.MAX_VALUE * 0.85 * env
                            * Math.sin(2 * Math.PI * 180 * t)); // 180 Гц — низкий «тык»
                }

                int minBuf = AudioTrack.getMinBufferSize(sampleRate,
                        AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

                AudioTrack track = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        sampleRate,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        Math.max(numSamples * 2, minBuf),
                        AudioTrack.MODE_STATIC);

                track.write(buf, 0, numSamples);
                track.play();
                Thread.sleep(durationMs + 50);
                track.stop();
                track.release();
            } catch (Exception ignored) { }
        }).start();
    }
}
