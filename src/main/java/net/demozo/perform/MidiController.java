package net.demozo.perform;

import javax.sound.midi.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MidiController {
    private Robot robot;
    private MidiToKeyTranslator translator;
    private Transmitter transmitter;
    private MidiDevice device;
    private List<KeyPress> keysToPress;

    public MidiController() {
        keysToPress = Collections.synchronizedList(new ArrayList<>());

        try {
            robot = new Robot();
        } catch (AWTException e) {
            Perform.LOGGER.error(e.getMessage());
        }

        translator = new MidiToKeyTranslator();
    }

    public void listen(MidiDevice device) throws MidiUnavailableException {
        var presser = new KeyPresser();
        presser.start();

        this.device = device;
        this.device.open();

        transmitter = device.getTransmitter();
        transmitter.setReceiver(new Receiver());
    }

    private void close() {
        transmitter.close();
        device.close();
    }

    public final class KeyPresser extends Thread {
        private final int PRESS_DELAY = 7;

        @Override
        public void run() {
            this.setName("KeyPresser");

            while(true) {
                if(keysToPress.size() > 0) {
                    var keyPress = keysToPress.remove(0);

                    if(keyPress.keyCode != -1) {
                        if (keyPress.shouldPress) {
                            robot.keyPress(keyPress.keyCode);
                            Perform.LOGGER.info("Pressing: {}", keyPress.keyCode);
                        } else {
                            robot.keyRelease(keyPress.keyCode);
                            Perform.LOGGER.info("Releasing: {}", keyPress.keyCode);
                        }
                    }
                }

                try {
                    sleep(PRESS_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public final class KeyPress {
        private final int keyCode;
        private final boolean shouldPress;

        public KeyPress(int keyCode, boolean shouldPress) {
            this.keyCode = keyCode;
            this.shouldPress = shouldPress;
        }
    }

    public final class Receiver implements javax.sound.midi.Receiver {

        @Override
        public void send(MidiMessage message, long timeStamp) {
            if(message.getLength() == 6) {
                return;
            }

            if(message instanceof ShortMessage shortMessage) {
                var note = shortMessage.getData1();
                var velocity = shortMessage.getData2();
                var keyCode = translator.translate(note);

                keysToPress.add(new KeyPress(keyCode, velocity > 0));
            }
        }

        @Override
        public void close() {
            MidiController.this.close();
        }
    }
}
