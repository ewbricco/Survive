import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

/**
 * Created by ebricco on 9/30/17.
 */
public class Sound {

    static long context;
    static long device;
    static List<Pointers> pointers;

    public static void main(String[] args) {

        String fileName = "reload.ogg";

        pointers = new ArrayList<>();

        initialize();

        /*long start = System.currentTimeMillis();
        for(int i=0; i<100; i++) {
            playSound(fileName);

            try {
                //Wait for a second
                Thread.sleep(40);
            } catch(InterruptedException ex) {}
        }
        System.out.println(System.currentTimeMillis() - start);*/

        for(int i=0; i<256; i++) {
            System.out.println(i);
            playSound(fileName);
            try {
                //Wait for a second
                Thread.sleep(10);
            } catch(InterruptedException ex) {}
        }

        long start = System.currentTimeMillis();
        reinitialize();
        System.out.println("time: " + (System.currentTimeMillis() - start));

        for(int i=0; i<256; i++) {
            System.out.println(i);
            playSound(fileName);
            try {
                //Wait for a second
                Thread.sleep(10);
            } catch(InterruptedException ex) {}
        }

        end();

        try {
            //Wait for a second
            Thread.sleep(3000);
        } catch(InterruptedException ex) {}
    }

    public static void initialize() {
        //Initialization
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
    }

    public static void reinitialize() {
        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
    }

    public static void end() {
        pointers.forEach(p -> p.delete());

        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public static void playSound(String fileName) {
        //Allocate space to store return information from the function
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(fileName, channelsBuffer, sampleRateBuffer);

        //Retreive the extra information that was stored in the buffers by the function
        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();
        //Free the space we allocated earlier
        stackPop();
        stackPop();

        //Find the correct OpenAL format
        int format = -1;
        if(channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if(channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        //Request space for the buffer
        int bufferPointer = alGenBuffers();

        //Send the data to OpenAL
        alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);

        //Free the memory allocated by STB
        free(rawAudioBuffer);


        //Request a source
        int sourcePointer = alGenSources();

        //Assign the sound we just loaded to the source
        alSourcei(sourcePointer, AL_BUFFER, bufferPointer);

        //Play the sound
        alSourcePlay(sourcePointer);
    }

    public static class Pointers {
        int sourcePointer;
        int bufferPointer;
        long createdAt;
        boolean toRemove;

        public Pointers(int source, int buffer) {
            sourcePointer = source;
            bufferPointer = buffer;
            createdAt = System.currentTimeMillis();
        }

        public void delete() {
            alDeleteSources(sourcePointer);
            alDeleteBuffers(bufferPointer);
            toRemove = true;
        }
    }
}
