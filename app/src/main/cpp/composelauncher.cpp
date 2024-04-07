#include <dlfcn.h>
#include "jni.h"
#include <android/log.h>
#include <streambuf>
#include <pthread.h>
#include <unistd.h>
#include <iostream>

using namespace std;
#define TAG "projectname" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

int JLI_Launch(int argc, char **argv,              /* main argc, argv */
               int jargc, const char **jargv,          /* java args */
               int appclassc, const char **appclassv,  /* app classpath */
               const char *fullversion,                /* full version defined */
               const char *dotversion,                 /* UNUSED dot version defined */
               const char *pname,                      /* program name */
               const char *lname,                      /* launcher name */
               jboolean javaargs,                      /* JAVA_ARGS */
               jboolean cpwildcard,                    /* classpath wildcard*/
               jboolean javaw,                         /* windows-only javaw */
               jint ergo                               /* unused */
);

#define FULL_VERSION "1.8.0-internal"
#define DOT_VERSION "1.8"
#define PROGNAME "java"
#define LAUNCHER_NAME "openjdk"

void *handle;
extern "C" JNIEXPORT jstring JNICALL
Java_lan_jing_composelauncher_viewmodel_home_HomeViewModel_startCompose(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("Hello from C++");
}
extern "C" JNIEXPORT void JNICALL
Java_lan_jing_composelauncher_viewmodel_home_HomeViewModel_dlopen(JNIEnv *env, jobject thiz,
                                                                  jstring path) {
    dlerror();
    dlopen(env->GetStringUTFChars(path, 0), RTLD_GLOBAL | RTLD_LAZY);
    auto error = dlerror();
    if (error) {
        LOGE("dlopen error: %s", error);
    }
}
extern "C" JNIEXPORT void JNICALL
Java_lan_jing_composelauncher_viewmodel_home_HomeViewModel_setupJli(JNIEnv *env, jobject thiz,
                                                                    jstring jli) {
    dlerror();
    handle = dlopen(env->GetStringUTFChars(jli, nullptr), RTLD_GLOBAL | RTLD_LAZY);
    auto error = dlerror();
    if (error) {
        LOGE("dlopen error: %s", error);
    }
}
extern "C" JNIEXPORT void JNICALL
Java_lan_jing_composelauncher_viewmodel_home_HomeViewModel_jliLaunch(JNIEnv *env, jobject thiz) {
    void *func = dlsym(handle, "JLI_Launch");
    if (func == nullptr) {
        LOGE("dlsym error: %s", dlerror());
        return;
    }
    int argc = 2;
    char *argv[] = {
            "java",
            "--version"
//            "-cp",
//            "/data/user/0/lan.jing.composelauncher",
//            "Hello"
    };
    int result = ((int (*)(int, char **, int, const char **, int, const char **, const char *,
                           const char *,
                           const char *, const char *, jboolean, jboolean, jboolean, jint)) func)(
            argc, argv,
            0, nullptr,
            0, nullptr,
            FULL_VERSION,
            DOT_VERSION,
            nullptr,
            nullptr,
            JNI_FALSE,
            JNI_FALSE, JNI_FALSE, 0);

    LOGD("JLI_Launch result: %d", result);
}
extern "C" JNIEXPORT void JNICALL
Java_lan_jing_composelauncher_viewmodel_home_HomeViewModel_redirectIO(JNIEnv *env, jobject thiz) {
    cout << "你好" << endl;
}

int start_logger(const char *app_name);

extern "C" JNIEXPORT void JNICALL
Java_lan_jing_composelauncher_viewmodel_home_HomeViewModel_startLogger(JNIEnv *env, jobject thiz) {
    start_logger("ComposeLauncherJJJJJJJJJJJJJJJJJJJJJJ");
}
static int pfd[2];
static pthread_t thr;
static const char *tag = "myapp";

static void *thread_jni2logcat(void *);

int start_logger(const char *app_name) {
    tag = app_name;

    /* make stdout line-buffered and stderr unbuffered */
    setvbuf(stdout, 0, _IOLBF, 0);
    setvbuf(stderr, 0, _IONBF, 0);

    /* create the pipe and redirect stdout and stderr */
    pipe(pfd);
    dup2(pfd[1], 1);
    dup2(pfd[1], 2);

    /* spawn the logging thread */
    if (pthread_create(&thr, 0, thread_jni2logcat, 0) == -1)
        return -1;
    pthread_detach(thr);
    return 0;
}

static void *thread_jni2logcat(void *) {
    ssize_t rdsz;
    char buf[128];
    while ((rdsz = read(pfd[0], buf, sizeof buf - 1)) > 0) {
        if (buf[rdsz - 1] == '\n') --rdsz;
        buf[rdsz] = 0;  /* add null-terminator */
        __android_log_write(ANDROID_LOG_DEBUG, tag, buf);
    }
    return 0;
}

extern "C"
JNIEXPORT void JNICALL
Java_lan_jing_composelauncher_viewmodel_home_HomeViewModel_startMinecraft(JNIEnv *env,jobject thiz,jstring args) {
    char* str= const_cast<char *>(env->GetStringUTFChars(args, nullptr));
    LOGI("args: %s",str);
    void *func = dlsym(handle, "JLI_Launch");
    if (func == nullptr) {
        LOGE("dlsym error: %s", dlerror());
        return;
    }
     char *argv[] = {
            "java",
            "-cp",
            str,
            "net.minecraft.client.main.Main"
    };
    int result = ((int (*)(int, char **, int, const char **, int, const char **, const char *,
                           const char *,
                           const char *, const char *, jboolean, jboolean, jboolean, jint)) func)(
            4, argv,
            0, nullptr,
            0, nullptr,
            FULL_VERSION,
            DOT_VERSION,
            nullptr,
            nullptr,
            JNI_FALSE,
            JNI_FALSE, JNI_FALSE, 0);
}