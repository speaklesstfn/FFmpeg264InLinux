// ITranscodeAidlInterface.aidl
package com.sddd.tfn.ffmpeg264inlinux;

// Declare any non-default types here with import statements

interface ITranscodeAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int transcode(in List<String> commands);
}
