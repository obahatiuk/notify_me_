package com.company.services.notification.sound;

import com.company.services.notification.IObserver;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import javax.sound.sampled.*;


public class ServerSoundNotificationService  implements IObserver {

    public void playSound() {
        try {
            InputStream is = getClass().getResourceAsStream("/Lion-Roar.wav");
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(is);
            clip.open(inputStream);
            clip.getLineInfo();

            final CountDownLatch clipDone = new CountDownLatch(1);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        event.getLine().close();
                        clipDone.countDown();
                    }
                }
            });
            clip.start();
        } catch (Throwable t) {
            System.out.println("ERROR: playing sound");
        }
    }

    @Override
    public void update() {
        playSound();
    }
}
