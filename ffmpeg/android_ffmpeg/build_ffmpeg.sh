#!/bin/bash
export NDK=/home/tfn/Android/Sdk/ndk-bundle
export PREBUILT=$NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/linux-x86_64
export PLATFORM=$NDK/platforms/android-16/arch-arm
export FFMPEGDIR=/home/tfn/AndroidStudioProjects/FFmpeg264InLinux/ffmpeg/android_ffmpeg/ffmpegsrc
export PREFIX=/home/tfn/AndroidStudioProjects/FFmpeg264InLinux/ffmpeg/android_ffmpeg/android
export EXTRAINCLUDE=/home/tfn/AndroidStudioProjects/FFmpeg264InLinux/ffmpeg/android_x264/h264/include
export EXTRALIB=/home/tfn/AndroidStudioProjects/FFmpeg264InLinux/ffmpeg/android_x264/h264/lib
export CC=$PREBUILT/bin/arm-linux-androideabi-gcc
export CPREFIX=$PREBUILT/bin/arm-linux-androideabi-
export NM=$PREBUILT/bin/arm-linux-androideabi-nm

function build_one
{
$FFMPEGDIR/configure \
    --prefix=$PREFIX \
    --arch=arm \
    --cc=$CC \
    --nm=$NM \
    --sysroot=$PLATFORM \
    --target-os=linux \
    --cross-prefix=$CPREFIX \
    --extra-cflags="-I$EXTRAINCLUDE -fPIC -DANDROID -D__thumb__ -mthumb -Wfatal-errors -Wno-deprecated -mfloat-abi=softfp -mfpu=vfpv3-d16 -marm -march=armv7-a" \
    --extra-ldflags="-L$EXTRALIB" \
    --enable-cross-compile \
    --enable-runtime-cpudetect \
    --enable-shared \
    --enable-nonfree \
    --enable-version3 \
    --enable-gpl \
    --enable-small \
    --enable-libx264 \
    --enable-encoder=libx264 \
    --enable-zlib \
    --disable-debug \
    --disable-static \
    --disable-stripping \
    --disable-asm \
    --disable-doc \
    --disable-ffplay \
    --disable-ffserver \
    --disable-ffmpeg \
    --disable-ffprobe \
    --disable-encoder=libxfaac \
    --disable-postproc \
    --disable-symver \
    --disable-avdevice \

    make -j4 install
}

build_one

#$PREBUILT/bin/arm-linux-androideabi-ar d libavcodec/libavcodec.a inverse.o

#$PREBUILT/bin/arm-linux-androideabi-ld -rpath-link=$PLATFORM/usr/lib -L$PLATFORM/usr/lib -L$EXTRALIB -soname libffmpeg.so -shared -nostdlib -z noexecstack -Bsymbolic --whole-archive --no-undefined -o $PREFIX/libffmpeg.so libavcodec/libavcodec.a libavfilter/libavfilter.a libavresample/libavresample.a libavformat/libavformat.a libavutil/libavutil.a libswscale/libswscale.a -lc -lm -lz -ldl -llog -lx264 --dynamic-linker=/system/bin/linker $PREBUILT/lib/gcc/arm-linux-androideabi/4.9.x/libgcc.a


