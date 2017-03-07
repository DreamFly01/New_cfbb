#include <jni.h>
#include <memory.h>
#include <string.h>
#include <stdlib.h>
#include "md5.h"

#include <stdio.h>
#include <sys/ptrace.h>
#include <unistd.h>
#include <signal.h>
#include <errno.h>

#define MAX 128
#define CHECK_TIME 10

void AntiDebug() {

    int pid;
    FILE *fd;
    char filename[MAX];
    char line[MAX];
    pid = getpid();
    sprintf(filename,"/proc/%d/status",pid);// 读取proc/pid/status中的TracerPid
    if(fork()==0)
    {
        int pt;
        pt = ptrace(PTRACE_TRACEME, 0, 0, 0); //子进程反调试
        while(1)
        {
            fd = fopen(filename,"r");
            while(fgets(line,MAX,fd))
            {
                if(strncmp(line,"TracerPid",9) == 0)
                {
                    int statue = atoi(&line[10]);
                  //  LOGD("########## statue = %d,%s", statue,line);
                    fclose(fd);
                    if(statue != 0)
                    {
                       // LOGD("########## here");
                        int ret = kill( pid,SIGKILL);
                      //  LOGD("########## kill = %d", ret);
                        return ;
                    }
                    break;
                }
            }
            sleep(CHECK_TIME);
        }
    }
}


/*
 * Set some test stuff up.
 *
 * Returns the JNI version on success, -1 on failure.
 */
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    AntiDebug();
    int result = JNI_VERSION_1_6;
    return result;
}


JNIEXPORT jstring

JNICALL Java_com_cfbb_android_commom_utils_others_MyJni_createSign(JNIEnv *env, jobject instance,
                                                      jstring waitForsign) {
    char *szText = (char *) (*env)->GetStringUTFChars(env, waitForsign, 0);

    MD5_CTX context = {0};
    MD5Init(&context);
    MD5Update(&context, szText, strlen(szText));
    unsigned char dest[16] = {0};
    MD5Final(dest, &context);

    (*env)->ReleaseStringUTFChars(env, waitForsign, szText);

    int i = 0;
    char szMd5[32] = {0};
    for (i = 0; i < 16; i++) {
        sprintf(szMd5, "%s%02x", szMd5, dest[i]);
    }

    return (*env)->NewStringUTF(env, szMd5);

}

JNIEXPORT jstring
        JNICALL
        Java_com_cfbb_android_commom_utils_others_MyJni_getSecretStr(JNIEnv *env, jobject instance)
{

    return (*env)->NewStringUTF(env, "p2papp");
}

JNIEXPORT jstring
        JNICALL
        Java_com_cfbb_android_commom_utils_others_MyJni_getHostAdd(JNIEnv
                                                      *env,
                                                      jobject instance
        )
{
   // public static final String API_HOST_ADDR = "http://appapi.cfbb.jinzejiayuan.com"; 测试
//    public static final String API_HOST_ADDR = "http://appapi3.cfbb.com.cn";正式
    //"http://www.app.cfbb.com.cn" 李文军个人站
    return (*env)->NewStringUTF(env, "http://appapi3.cfbb.com.cn");
}

JNIEXPORT jstring
        JNICALL
        Java_com_cfbb_android_commom_utils_others_MyJni_getSignStrForSecure(JNIEnv
                                                               *env,
                                                               jobject instance
        )
{
    return (*env)->
            NewStringUTF(env,
                         "mrchang45"
    );
}


