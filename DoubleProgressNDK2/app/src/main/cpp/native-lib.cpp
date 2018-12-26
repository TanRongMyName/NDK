#include <jni.h>
#include <string>
#include "native_lib.h"

const char *userid;
int m_child;
const char *PATH="/data/data/com.coffe.shentao.doubleprogressndk/my.sock";

extern "C" JNIEXPORT jstring JNICALL
Java_com_coffe_shentao_doubleprogressndk_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_coffe_shentao_doubleprogressndk_Watcher_createWatcher(JNIEnv *env, jobject instance,
                                                               jstring userid_) {
    userid = env->GetStringUTFChars(userid_, 0);

    // TODO   打开双进程
        pid_t pid=fork()   ;
        if(pid<0){//fock 失败

        }else if(pid==0){//子进程
            //守护进程  服务端
            child_do_work();
        }else if(pid>0){
            //父进程
        }

    env->ReleaseStringUTFChars(userid_, userid);
}

void child_do_work() {
    //开启socket 服务端  分两步骤
    if(child_create_channel()){ //建立socket 建立连接
        child_listen_msg();//读取消息
    }
}



//创建服务端的socket
void child_listen_msg() {
    fd_set rfds;
    struct timeval timeout={3,0};
   while(1){
        //清空 内容 缓存
        FD_ZERO(&rfds);
        //设置监听的 socket  内容 集合
        FD_SET(m_child,&rfds);
         //一般客户端+1  监视文件的句柄数
        int r= select(m_child+1,&rfds,NULL,NULL,&timeout);//选择监听
        LOGE("读取前消息 %d ",r);
        if(r>0){//选择了一个客户端
           char pkg[256]={0};//缓冲区
           //保证读取到的信息是，指定的apk 客户端
           if(FD_ISSET(m_child,&rfds)){
               //阻塞式函数  什么都不去做
              int result= read(m_child,pkg,sizeof(pkg));
               LOGE("执行了---重启 服务 userid %s ",userid);
              //开启服务                                        包名+全类名
              execlp("am","am","startservice","--user",userid,
                     "com.coffe.shentao.doubleprogressndk/com.coffe.shentao.doubleprogressndk.ProcessService",(char*)NULL);
               LOGE("执行了---重启 服务");
              break;
                      //Java 中同样可以执行 am 命令
                      //am 命令 执行打电话
                      //am start -a android.intent.action.CALL -d tel:10086
           }
        }

   }
}

/**
 * 服务端读取信息
 * 客户端
 * socket 跨进程 liunx 文件
 * ip address  file
 */

int child_create_channel() {
    int listenfd=socket(AF_LOCAL,SOCK_STREAM,0);
    //addr ---内存区域
    struct sockaddr_un addr;
    unlink(PATH);//清空文件连接
    //清空内存
    memset(&addr,0,sizeof(sockaddr_un));
    addr.sun_family=AF_LOCAL;//协议类型
    strcpy(addr.sun_path,PATH);

    if(bind(listenfd, (const sockaddr *)(&addr), sizeof(sockaddr_un))<0){
        LOGE("绑定错误");
        return 0;
    }
    //开始Listener
    listen(listenfd,5);//可以同时守护五个客户端进程
    //保证 宿主 进程连接成功
    int connfd=0;

    while(1){
        //返回客户端的引用   柱塞式函数  连接失败不停重复连接
        if(connfd=accept(listenfd,NULL,NULL)<0){
            if(errno==EINTR){
                continue;  //连接失败
            }else{
                LOGE("读取失败");
                return 0;
            }
        }
        m_child=connfd;
        LOGE("apk 父进程连接上了 %d ",m_child);
        break;
    }
    return 1;
}
//
//
//
//3.1、socket()函数
//int socket(int domain, int type, int protocol);
//
//
//socket函数对应于普通文件的打开操作。普通文件的打开操作返回一个文件描述字，而socket()用于创建一个socket描述符（socket descriptor），它唯一标识一个socket。这个socket描述字跟文件描述字一样，后续的操作都有用到它，把它作为参数，通过它来进行一些读写操作。
//
//正如可以给fopen的传入不同参数值，以打开不同的文件。创建socket的时候，也可以指定不同的参数创建不同的socket描述符，socket函数的三个参数分别为：
//
//domain：即协议域，又称为协议族（family）。常用的协议族有，AF_INET、AF_INET6、AF_LOCAL（或称AF_UNIX，Unix域socket）、AF_ROUTE等等。协议族决定了socket的地址类型，在通信中必须采用对应的地址，如AF_INET决定了要用ipv4地址（32位的）与端口号（16位的）的组合、AF_UNIX决定了要用一个绝对路径名作为地址。
//type：指定socket类型。常用的socket类型有，SOCK_STREAM、SOCK_DGRAM、SOCK_RAW、SOCK_PACKET、SOCK_SEQPACKET等等（socket的类型有哪些？）。
//protocol：故名思意，就是指定协议。常用的协议有，IPPROTO_TCP、IPPTOTO_UDP、IPPROTO_SCTP、IPPROTO_TIPC等，它们分别对应TCP传输协议、UDP传输协议、STCP传输协议、TIPC传输协议。
//注意：并不是上面的type和protocol可以随意组合的，如SOCK_STREAM不可以跟IPPROTO_UDP组合。当protocol为0时，会自动选择type类型对应的默认协议。
//
//当我们调用socket创建一个socket时，返回的socket描述字它存在于协议族（address family，AF_XXX）空间中，但没有一个具体的地址。如果想要给它赋值一个地址，就必须调用bind()函数，否则就当调用connect()、listen()时系统会自动随机分配一个端口。
//
//3.2、bind()函数
//正如上面所说bind()函数把一个地址族中的特定地址赋给socket。例如对应AF_INET、AF_INET6就是把一个ipv4或ipv6地址和端口号组合赋给socket。
//
//int bind(int sockfd, const struct sockaddr *addr, socklen_t addrlen);
//函数的三个参数分别为：
//
//sockfd：即socket描述字，它是通过socket()函数创建了，唯一标识一个socket。bind()函数就是将给这个描述字绑定一个名字。
//addr：一个const struct sockaddr *指针，指向要绑定给sockfd的协议地址。这个地址结构根据地址创建socket时的地址协议族的不同而不同，如ipv4对应的是：
//struct sockaddr_in {
//    sa_family_t    sin_family; /* address family: AF_INET */
//    in_port_t      sin_port;   /* port in network byte order */
//    struct in_addr sin_addr;   /* internet address */
//};
//
///* Internet address. */
//struct in_addr {
//    uint32_t       s_addr;     /* address in network byte order */
//};
//ipv6对应的是：
//struct sockaddr_in6 {
//    sa_family_t     sin6_family;   /* AF_INET6 */
//    in_port_t       sin6_port;     /* port number */
//    uint32_t        sin6_flowinfo; /* IPv6 flow information */
//    struct in6_addr sin6_addr;     /* IPv6 address */
//    uint32_t        sin6_scope_id; /* Scope ID (new in 2.4) */
//};
//
//struct in6_addr {
//    unsigned char   s6_addr[16];   /* IPv6 address */
//};
//Unix域对应的是：
//#define UNIX_PATH_MAX    108
//
//struct sockaddr_un {
//    sa_family_t sun_family;               /* AF_UNIX */
//    char        sun_path[UNIX_PATH_MAX];  /* pathname */
//};
//addrlen：对应的是地址的长度。
//通常服务器在启动的时候都会绑定一个众所周知的地址（如ip地址+端口号），用于提供服务，客户就可以通过它来接连服务器；而客户端就不用指定，有系统自动分配一个端口号和自身的ip地址组合。这就是为什么通常服务器端在listen之前会调用bind()，而客户端就不会调用，而是在connect()时由系统随机生成一个。
//
//网络字节序与主机字节序
//        主机字节序就是我们平常说的大端和小端模式：不同的CPU有不同的字节序类型，这些字节序是指整数在内存中保存的顺序，这个叫做主机序。引用标准的Big-Endian和Little-Endian的定义如下：
//
//　　a) Little-Endian就是低位字节排放在内存的低地址端，高位字节排放在内存的高地址端。
//
//　　b) Big-Endian就是高位字节排放在内存的低地址端，低位字节排放在内存的高地址端。
//
//网络字节序：4个字节的32 bit值以下面的次序传输：首先是0～7bit，其次8～15bit，然后16～23bit，最后是24~31bit。这种传输次序称作大端字节序。由于TCP/IP首部中所有的二进制整数在网络中传输时都要求以这种次序，因此它又称作网络字节序。字节序，顾名思义字节的顺序，就是大于一个字节类型的数据在内存中的存放顺序，一个字节的数据没有顺序的问题了。
//
//所以：在将一个地址绑定到socket的时候，请先将主机字节序转换成为网络字节序，而不要假定主机字节序跟网络字节序一样使用的是Big-Endian。由于这个问题曾引发过血案！公司项目代码中由于存在这个问题，导致了很多莫名其妙的问题，所以请谨记对主机字节序不要做任何假定，务必将其转化为网络字节序再赋给socket。
//
//3.3、listen()、connect()函数
//        如果作为一个服务器，在调用socket()、bind()之后就会调用listen()来监听这个socket，如果客户端这时调用connect()发出连接请求，服务器端就会接收到这个请求。
//
//int listen(int sockfd, int backlog);
//int connect(int sockfd, const struct sockaddr *addr, socklen_t addrlen);
//listen函数的第一个参数即为要监听的socket描述字，第二个参数为相应socket可以排队的最大连接个数。socket()函数创建的socket默认是一个主动类型的，listen函数将socket变为被动类型的，等待客户的连接请求。
//
//connect函数的第一个参数即为客户端的socket描述字，第二参数为服务器的socket地址，第三个参数为socket地址的长度。客户端通过调用connect函数来建立与TCP服务器的连接。
//
//3.4、accept()函数
//TCP服务器端依次调用socket()、bind()、listen()之后，就会监听指定的socket地址了。TCP客户端依次调用socket()、connect()之后就想TCP服务器发送了一个连接请求。TCP服务器监听到这个请求之后，就会调用accept()函数取接收请求，这样连接就建立好了。之后就可以开始网络I/O操作了，即类同于普通文件的读写I/O操作。
//
//int accept(int sockfd, struct sockaddr *addr, socklen_t *addrlen);
//accept函数的第一个参数为服务器的socket描述字，第二个参数为指向struct sockaddr *的指针，用于返回客户端的协议地址，第三个参数为协议地址的长度。如果accpet成功，那么其返回值是由内核自动生成的一个全新的描述字，代表与返回客户的TCP连接。
//
//注意：accept的第一个参数为服务器的socket描述字，是服务器开始调用socket()函数生成的，称为监听socket描述字；而accept函数返回的是已连接的socket描述字。一个服务器通常通常仅仅只创建一个监听socket描述字，它在该服务器的生命周期内一直存在。内核为每个由服务器进程接受的客户连接创建了一个已连接socket描述字，当服务器完成了对某个客户的服务，相应的已连接socket描述字就被关闭。
//
//3.5、read()、write()等函数
//        万事具备只欠东风，至此服务器与客户已经建立好连接了。可以调用网络I/O进行读写操作了，即实现了网咯中不同进程之间的通信！网络I/O操作有下面几组：
//
//read()/write()
//recv()/send()
//readv()/writev()
//recvmsg()/sendmsg()
//recvfrom()/sendto()
//我推荐使用recvmsg()/sendmsg()函数，这两个函数是最通用的I/O函数，实际上可以把上面的其它函数都替换成这两个函数。它们的声明如下：



//
//
//#include <sys/select.h>
//
//int FD_ZERO(int fd, fd_set *fdset);
//
//int FD_CLR(int fd, fd_set *fdset);
//
//int FD_SET(int fd, fd_set *fd_set);
//
//int FD_ISSET(int fd, fd_set *fdset);</span>
//
//FD_ZERO宏将一个 fd_set类型变量的所有位都设为 0，使用FD_SET将变量的某个位置位。清除某个位时可以使用 FD_CLR，我们可以使用 FD_SET来测试某个位是否被置位。
//
//当声明了一个文件描述符集后，必须用FD_ZERO将所有位置零。之后将我们所感兴趣的描述符所对应的位置位，操作如下：
//
//
//
//fd_set rset;
//
//int fd;
//
//FD_ZERO(&rset);
//
//FD_SET(fd, &rset);
//
//FD_SET(stdin, &rset);</span>
//
//
//
//select返回后，用FD_ISSET测试给定位是否置位：
//
//
//
//if(FD_ISSET(fd, &rset)
//
//{ ... }</span>
//
//具体解释select的参数：
//
//（1）intmaxfdp是一个整数值，是指集合中所有文件描述符的范围，即所有文件描述符的最大值加1，不能错。
//
//说明：对于这个原理的解释可以看上边fd_set的详细解释，fd_set是以位图的形式来存储这些文件描述符。maxfdp也就是定义了位图中有效的位的个数。
//
//（2）fd_set*readfds是指向fd_set结构的指针，这个集合中应该包括文件描述符，我们是要监视这些文件描述符的读变化的，即我们关心是否可以从这些文件中读取数据了，如果这个集合中有一个文件可读，select就会返回一个大于0的值，表示有文件可读；如果没有可读的文件，则根据timeout参数再判断是否超时，若超出timeout的时间，select返回0，若发生错误返回负值。可以传入NULL值，表示不关心任何文件的读变化。
//
//（3）fd_set*writefds是指向fd_set结构的指针，这个集合中应该包括文件描述符，我们是要监视这些文件描述符的写变化的，即我们关心是否可以向这些文件中写入数据了，如果这个集合中有一个文件可写，select就会返回一个大于0的值，表示有文件可写，如果没有可写的文件，则根据timeout参数再判断是否超时，若超出timeout的时间，select返回0，若发生错误返回负值。可以传入NULL值，表示不关心任何文件的写变化。
//
//（4）fd_set*errorfds同上面两个参数的意图，用来监视文件错误异常文件。
//
//（5）structtimeval* timeout是select的超时时间，这个参数至关重要，它可以使select处于三种状态，第一，若将NULL以形参传入，即不传入时间结构，就是将select置于阻塞状态，一定等到监视文件描述符集合中某个文件描述符发生变化为止；第二，若将时间值设为0秒0毫秒，就变成一个纯粹的非阻塞函数，不管文件描述符是否有变化，都立刻返回继续执行，文件无变化返回0，有变化返回一个正值；第三，timeout的值大于0，这就是等待的超时时间，即 select在timeout时间内阻塞，超时时间之内有事件到来就返回了，否则在超时后不管怎样一定返回，返回值同上述。
//
//说明：
//
//函数返回：
//
//（1）当监视的相应的文件描述符集中满足条件时，比如说读文件描述符集中有数据到来时，内核(I/O)根据状态修改文件描述符集，并返回一个大于0的数。
//
//（2）当没有满足条件的文件描述符，且设置的timeval监控时间超时时，select函数会返回一个为0的值。
//
//（3）当select返回负值时，发生错误。
 extern "C"
JNIEXPORT void JNICALL
Java_com_coffe_shentao_doubleprogressndk_Watcher_connectMonitor(JNIEnv *env, jobject instance) {
    //子进程   客户端 apk
    //连接
    int socked;
    struct sockaddr_un addr;
    while(1){
        LOGE("客户端  父进程开始连接");
        socked=socket(AF_LOCAL,SOCK_STREAM,0);
        if(socked<0){
            LOGE("创建socked 失败");
            return ;
        }
        memset(&addr,0,sizeof(sockaddr));
        addr.sun_family=AF_LOCAL;
        strcpy(addr.sun_path,PATH);
        if(connect(socked, (const sockaddr *)(&addr), sizeof(sockaddr_un)) < 0){
            //连接失败  开始重新连接
            LOGE("连接socked失败");
            close(socked);
            sleep(1);
            //在来下一次尝试连接
            continue;
        }
        LOGE("连接成功---");
        break;
    }
    // TODO

}