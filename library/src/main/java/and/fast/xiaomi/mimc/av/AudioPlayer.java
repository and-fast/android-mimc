package and.fast.xiaomi.mimc.av;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import and.fast.xiaomi.mimc.common.Constant;

import static android.media.AudioTrack.*;

/**
 * Created by houminjiang on 18-6-6.
 */

public class AudioPlayer implements Player {
    private int minBufferSize = 0;
    private AudioTrack audioTrack;
    private boolean isPlayStarted = false;
    private Context context;
    private int defaultAudioMode;
    private static final String TAG = "AudioPlayer";


    public AudioPlayer(Context context, int defaultAudioMode) {
        this.context = context;
        this.defaultAudioMode = defaultAudioMode;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean start() {
        return startPlayer(Constant.DEFAULT_PLAY_STREAM_TYPE, Constant.DEFAULT_AUDIO_SAMPLE_RATE, Constant.DEFAULT_PLAY_CHANNEL_CONFIG, Constant.DEFAULT_AUDIO_FORMAT);
    }

    @Override
    public void stop() {
        stopPlayer();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean startPlayer(int streamType, int sampleRateInHz, int channelConfig, int audioFormat) {
        if (isPlayStarted) {
            Log.w(TAG, "Audio player started.");
            return false;
        }

        minBufferSize = 2 * AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        if (minBufferSize == AudioTrack.ERROR_BAD_VALUE) {
            Log.w(TAG, "Invalid parameters.");
            return false;
        }

        audioTrack = new AudioTrack((new AudioAttributes.Builder())
            .setLegacyStreamType(streamType)
            .build(),
            (new AudioFormat.Builder())
                .setChannelMask(channelConfig)
                .setEncoding(audioFormat)
                .setSampleRate(sampleRateInHz)
                .build(),
            minBufferSize,
            Constant.DEFAULT_PLAY_MODE, AudioManager.AUDIO_SESSION_ID_GENERATE);
        if (audioTrack.getState() == AudioTrack.STATE_UNINITIALIZED) {
            Log.w(TAG, "AudioTrack initialize fail.");
            return false;
        }
        isPlayStarted = true;
        audioTrack.play();
        setAudioMode(defaultAudioMode);
        Log.i(TAG, "Start audio player success.");

        return true;
    }

    private void stopPlayer() {
        if (!isPlayStarted) {
            return;
        }

        isPlayStarted = false;
        if (audioTrack.getState() == AudioTrack.PLAYSTATE_PLAYING) {
            audioTrack.stop();
        }
        audioTrack.release();
        setAudioMode(AudioManager.MODE_NORMAL);
        Log.i(TAG, "Stop audio player success.");
    }

    public boolean play(byte[] audioData, int offsetInBytes, int sizeInBytes) {
        if (!isPlayStarted) {
            Log.w(TAG, "Audio player not started.");
            return false;
        }

        int result = audioTrack.write(audioData, offsetInBytes, sizeInBytes);
        if (result == ERROR_INVALID_OPERATION) {
            Log.w(TAG, "The track isn't properly initialized.");
        } else if (result == ERROR_BAD_VALUE) {
            Log.w(TAG, "The parameters don't resolve to valid data and indexes.");
        } else if (result == ERROR_DEAD_OBJECT) {
            Log.w(TAG, "The AudioTrack is not valid anymore and needs to be recreated.");
        } else if (result == ERROR) {
            Log.w(TAG, "Other error.");
        }
        Log.d(TAG, "Played:" + result + " bytes.");

        return true;
    }

    private void setAudioMode(int mode) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if (mode == AudioManager.MODE_NORMAL) {
            audioManager.setSpeakerphoneOn(true);
        } else if (mode == AudioManager.MODE_IN_COMMUNICATION) {
            audioManager.setSpeakerphoneOn(false);
        }
        audioManager.setMode(mode);
    }
}
