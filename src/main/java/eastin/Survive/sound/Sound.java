package eastin.Survive.sound;

import eastin.Survive.World;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.Stdlib.free;

/**
 * Created by ebricco on 8/23/18.
 */
public class Sound implements Runnable {

    String fileName;
    //int sourcePointer;
    int bufferPointer;
    long playedAt;
    int duration; //duration of sound in ms
    boolean toRemove;

    static long device;
    static long context;

    int sourcePointer;

    public Sound(String fileName) {
        this.fileName = fileName;
        toRemove = false;
        duration = 5000;

        if(!fileName.equals("gunshot.ogg")) {
            //Request a source
            sourcePointer = alGenSources();
        }

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
        bufferPointer = alGenBuffers();

        //Send the data to OpenAL
        alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);

        //Free the memory allocated by STB
        free(rawAudioBuffer);

        if(fileName.equals("gunshot.ogg")) {
            for(int i=0; i<World.sounds.NUMBERFIRESOURCES; i++) {
                //Assign the sound we just loaded to the source
                alSourcei(World.sounds.getNextFireSource(), AL_BUFFER, bufferPointer);
            }
        } else {
            alSourcei(sourcePointer, AL_BUFFER, bufferPointer);
        }
    }

    public static void initializeAbilityToPlaySound() {
        //Initialization
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
    }

    public static void shutdown() {
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public void run() {
        if(fileName.equals("gunshot.ogg")) {
            sourcePointer = World.sounds.getNextFireSource();
        }

        //Play the sound
        alSourcePlay(sourcePointer);
        playedAt = System.currentTimeMillis();
    }

    public boolean checkIfDone() {
        if(System.currentTimeMillis() - playedAt > duration) {
            delete();
            toRemove = true;
            return true;
        }

        return false;
    }

    public void delete() {
        alDeleteSources(sourcePointer);
        alDeleteBuffers(bufferPointer);
    }
}
