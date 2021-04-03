package com.pedro.rtplibrary.rtmp;

import android.app.Activity;
import android.content.Context;
import android.media.MediaCodec;
import android.os.Build;
import android.view.SurfaceView;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.pedro.rtplibrary.base.Camera1Base;
import com.pedro.rtplibrary.view.LightOpenGlView;
import com.pedro.rtplibrary.view.OpenGlView;

import net.ossrs.rtmp.ConnectCheckerRtmp;
import net.ossrs.rtmp.SrsFlvMuxer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * More documentation see:
 * {@link Camera1Base}
 * <p>
 * Created by pedro on 25/01/17.
 */

public class MultipleRtmpCamera1 extends Camera1Base {
    private Map<String, SrsFlvMuxer> mapStream;
    private List<SrsFlvMuxer> srsFlvMuxers;
    private Activity activity;
    private long disconnectTime;

    public MultipleRtmpCamera1(SurfaceView surfaceView, ConnectCheckerRtmp connectChecker, Activity activity) {
        super(surfaceView);
        srsFlvMuxers = new ArrayList();
        mapStream = new HashMap<String, SrsFlvMuxer>();
        this.activity = activity;
    }

    public MultipleRtmpCamera1(TextureView textureView, ConnectCheckerRtmp connectChecker, Activity activity) {
        super(textureView);
        srsFlvMuxers = new ArrayList();
        mapStream = new HashMap<String, SrsFlvMuxer>();
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public MultipleRtmpCamera1(OpenGlView openGlView, ConnectCheckerRtmp connectChecker, Activity activity) {
        super(openGlView);
        srsFlvMuxers = new ArrayList();
        mapStream = new HashMap<String, SrsFlvMuxer>();
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public MultipleRtmpCamera1(LightOpenGlView lightOpenGlView, ConnectCheckerRtmp connectChecker, Activity activity) {
        super(lightOpenGlView);
        srsFlvMuxers = new ArrayList();
        mapStream = new HashMap<String, SrsFlvMuxer>();
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public MultipleRtmpCamera1(Context context, ConnectCheckerRtmp connectChecker, Activity activity) {
        super(context);
        srsFlvMuxers = new ArrayList();
        mapStream = new HashMap<String, SrsFlvMuxer>();
        this.activity = activity;
    }

    private SrsFlvMuxer createSrsFlvMuxer(final String url) {
        final SrsFlvMuxer srsFlvMuxer = new SrsFlvMuxer(
                new ConnectCheckerRtmp() {
                    @Override
                    public void onConnectionSuccessRtmp() {

                    }

                    @Override
                    public void onConnectionFailedRtmp(@NonNull String reason) {
//                        activity.runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                SrsFlvMuxer old  = mapStream.get(url);
//                                mapStream.remove(url);
//                                SrsFlvMuxer srsFlvMuxer = createSrsFlvMuxer(url);
//                                srsFlvMuxer.start(url);
//                                mapStream.put(url, srsFlvMuxer);
//                                srsFlvMuxers.add(srsFlvMuxer);
//                                old.stop();
//                            }
//                        });
                        SrsFlvMuxer srsFlvMuxer = mapStream.get(url);
                        if (srsFlvMuxer != null) {
                            srsFlvMuxer.reConnect(10000);
                        }

                    }

                    @Override
                    public void onNewBitrateRtmp(long bitrate) {

                    }

                    @Override
                    public void onDisconnectRtmp() {

                    }

                    @Override
                    public void onAuthErrorRtmp() {

                    }

                    @Override
                    public void onAuthSuccessRtmp() {

                    }
                }
        );
        if (videoEncoder.getRotation() == 90 || videoEncoder.getRotation() == 270) {
            srsFlvMuxer.setVideoResolution(videoEncoder.getHeight(), videoEncoder.getWidth());
        } else {
            srsFlvMuxer.setVideoResolution(videoEncoder.getWidth(), videoEncoder.getHeight());
        }
        srsFlvMuxer.setReTries(10);
        return srsFlvMuxer;
    }

    private Boolean start = false;

    public void addUrl(final String url) {
        final SrsFlvMuxer srsFlvMuxer = new SrsFlvMuxer(
                new ConnectCheckerRtmp() {
                    @Override
                    public void onConnectionSuccessRtmp() {
                        if (!start) {
                            boolean check = false;
                            for (int i = 0; i < srsFlvMuxers.size(); i++) {
                                if (!srsFlvMuxers.get(i).isConnected()) {
                                    check = true;
                                    break;
                                }
                            }

                            if (!check) {
                                start = true;
                                startStream("");
                            }
                        }
                    }

                    @Override
                    public void onConnectionFailedRtmp(@NonNull String reason) {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
//                                SrsFlvMuxer old = mapStream.get(url);
//                                mapStream.remove(url);
//                                SrsFlvMuxer srsFlvMuxer = createSrsFlvMuxer(url);
//                                srsFlvMuxer.start(url);
//                                mapStream.put(url, srsFlvMuxer);
//                                srsFlvMuxers.add(srsFlvMuxer);
//                                old.stop();

                                SrsFlvMuxer srsFlvMuxer = mapStream.get(url);
                                if (srsFlvMuxer != null) {
                                    srsFlvMuxer.reConnect(15000);
                                }

                            }
                        });
                    }

                    @Override
                    public void onNewBitrateRtmp(long bitrate) {

                    }

                    @Override
                    public void onDisconnectRtmp() {

                    }

                    @Override
                    public void onAuthErrorRtmp() {

                    }

                    @Override
                    public void onAuthSuccessRtmp() {

                    }
                }
        );
        if (videoEncoder.getRotation() == 90 || videoEncoder.getRotation() == 270) {
            srsFlvMuxer.setVideoResolution(videoEncoder.getHeight(), videoEncoder.getWidth());
        } else {
            srsFlvMuxer.setVideoResolution(videoEncoder.getWidth(), videoEncoder.getHeight());
        }
        mapStream.put(url, srsFlvMuxer);
        srsFlvMuxers.add(srsFlvMuxer);
//    srsFlvMuxer.start(url);
    }

    /**
     * H264 profile.
     *
     * @param profileIop Could be ProfileIop.BASELINE or ProfileIop.CONSTRAINED
     */
    public void setProfileIop(byte profileIop) {

    }

    @Override
    public void resizeCache(int newSize) throws RuntimeException {

    }

    @Override
    public int getCacheSize() {
        return 0;
    }

    @Override
    public long getSentAudioFrames() {
        return 0;
    }

    @Override
    public long getSentVideoFrames() {
        return 0;
    }

    @Override
    public long getDroppedAudioFrames() {
        return 0;
    }

    @Override
    public long getDroppedVideoFrames() {
        return 0;
    }

    @Override
    public void resetSentAudioFrames() {

    }

    @Override
    public void resetSentVideoFrames() {

    }

    @Override
    public void resetDroppedAudioFrames() {

    }

    @Override
    public void resetDroppedVideoFrames() {

    }

    @Override
    public void setAuthorization(String user, String password) {

    }

    /**
     * Some Livestream hosts use Akamai auth that requires RTMP packets to be sent with increasing
     * timestamp order regardless of packet type.
     * Necessary with Servers like Dacast.
     * More info here:
     * https://learn.akamai.com/en-us/webhelp/media-services-live/media-services-live-encoder-compatibility-testing-and-qualification-guide-v4.0/GUID-F941C88B-9128-4BF4-A81B-C2E5CFD35BBF.html
     */
    public void forceAkamaiTs(boolean enabled) {

    }

    @Override
    protected void prepareAudioRtp(boolean isStereo, int sampleRate) {

    }

    @Override
    protected void startStreamRtp(String url) {
//    if (videoEncoder.getRotation() == 90 || videoEncoder.getRotation() == 270) {
//      srsFlvMuxer.setVideoResolution(videoEncoder.getHeight(), videoEncoder.getWidth());
//    } else {
//      srsFlvMuxer.setVideoResolution(videoEncoder.getWidth(), videoEncoder.getHeight());
//    }
//    srsFlvMuxer.start(url);

//        for (int i = 0; i < srsFlvMuxers.size(); i++) {
//            srsFlvMuxers.get(i).start(url);
//        }
//        startStream("");

    }

    public void startStreamRtp() throws InterruptedException {
        for (Map.Entry<String, SrsFlvMuxer> set : mapStream.entrySet()) {
            set.getValue().start(set.getKey());
        }


    }

    @Override
    protected void stopStreamRtp() {

    }

    @Override
    public void setReTries(int reTries) {

    }

    @Override
    public boolean shouldRetry(String reason) {
        return true;
    }

    @Override
    public void reConnect(long delay) {

    }

    @Override
    public boolean hasCongestion() {
        return false;
    }

    @Override
    protected void getAacDataRtp(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
        for (int i = 0; i < srsFlvMuxers.size(); i++) {
            srsFlvMuxers.get(i).sendAudio(aacBuffer.duplicate(), info);

        }
    }

    @Override
    protected void onSpsPpsVpsRtp(ByteBuffer sps, ByteBuffer pps, ByteBuffer vps) {
        for (int i = 0; i < srsFlvMuxers.size(); i++) {
            srsFlvMuxers.get(i).setSpsPPs(sps.duplicate(), pps.duplicate());
        }
    }

    @Override
    protected void getH264DataRtp(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
        for (int i = 0; i < srsFlvMuxers.size(); i++) {
            srsFlvMuxers.get(i).sendVideo(h264Buffer.duplicate(), info);
        }
    }

    @Override
    public void setLogs(boolean enable) {
        for (int i = 0; i < srsFlvMuxers.size(); i++) {
            srsFlvMuxers.get(i).setLogs(enable);
        }

    }
}
