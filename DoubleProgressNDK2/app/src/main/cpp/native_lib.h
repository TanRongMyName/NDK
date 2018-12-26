//
// Created by win on 2018/12/26.
//

#ifndef DOUBLEPROGRESSNDK2_NATIVE_LIB_H
#define DOUBLEPROGRESSNDK2_NATIVE_LIB_H

#endif //DOUBLEPROGRESSNDK2_NATIVE_LIB_H


#include <sys/select.h>
#include<unistd.h>
#include<sys/socket.h>
#include<pthread.h>
#include<signal.h>
#include<sys/wait.h>
#include<sys/types.h>
#include<sys/un.h>
#include <errno.h>
#include <stdlib.h>
#include <linux/signal.h>
#include <android/log.h>
#define  LOG_TAG "tuch"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
//开启子进程
void child_do_work();
//创建通道 连接
int child_create_channel();
//读取数据
void child_listen_msg();