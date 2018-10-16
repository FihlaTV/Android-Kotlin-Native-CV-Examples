#include <jni.h>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>

using namespace std;
using namespace cv;

extern "C" {
    void JNICALL Java_com_mifly_androidkotlincv_MainActivity_gray(
            JNIEnv *env,
            jobject instance,
            jlong srcAddr,
            jlong dstAddr) {
        Mat &src = *(Mat *) srcAddr;
        Mat &dst = *(Mat *) dstAddr;

        cvtColor(src, dst, COLOR_BGR2GRAY);
    }
}
