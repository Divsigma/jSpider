### 2020.11.22（跑通）

1. 遍历map的4种方法：

   只访问key或value、访问entrySet、使用Iterator、使用lambda表达式

> https://www.cnblogs.com/zhaoguhong/p/7074597.html?utm_source=itdadao&utm_medium=referral

2. PrintWriter继承自抽象类Writer（专门处理Unicode文本流的类，处理单位是2字节的Char而不是byte），有缓存区（？），需要flush()才能把数据写到磁盘，同时注意close()释放资源（close()之前会自动flush()，所以如果不close()也不flush()，最后留存于缓存区的数据包不会被传送到指定文件！）

### 2020.11.23（拆分+打通）

> 日记：
>
> 借鉴了webmagic和scrapy的框架流程图，看了看webmagic的Spider源码，打算从最简单的着手，将昨天跑通的流程分隔成Request、Downloader、Response、Parser、Item、Pipeline六个部分，Request、Response、Item是信息载体，由Spider在Downloader、Parser、Pipeline间进行传递，联动整个框架（暂不考虑middleware）。
>
> 今天跑通了Downloader和Spider通过url和Response交互（还没加入Request），Downloader使用的是URLConnection。

1. StringBuffer和StringBuilder异同？



### 2020.11.24-2020.11.25（拆分+打通）

1. parser定义成abstract class、interface还是class？（暂时选用了abstract class，因为一个parser**必定**包含了处理行为（abstract class中的abstract方法一旦被非abstract类继承就必须要重写），也有url、encoding等属性。interface更像是单纯对行为的封装。webmagic使用了interface，有朝一日再对比吧TAT）
2. pipeline定义成abstract class、interface还是class？（暂时选用了interface，因为我暂时还没提取出数据库、控制台、文本文件pipeline的共有属性，但它们都有process行为。webmagic使用了interface，有朝一日再对比吧TAT）

3. 为毛要用getter和setter？

   > @仲晨：https://www.zhihu.com/question/21401198

4. abstract class的构造函数能否被继承？

   

5. （https://www.cnblogs.com/twzheng/p/5923642.html）URLConnection的InputStream中获取的html缺失了想要的链接部分

   - 会不会是InputStream转为Scanner后Scanner读取不完整？——下载页面爬取，发现有链接部分。
   - 会不会是某种反爬策略过滤了<a>标签？——统计获取页面的<a>标签数目，非0；另外浏览器上看获取的页面，发现底部跳转到另外文章的链接都获取不到。所以会不会是反爬策略过滤了特定的<a>标签？尝试增加User-Agent请求头，无果。
   - 百度“java url获取网页反爬”使用另一个网站（https://www.tuicool.com/），发现不设置请求头也是可以爬取到结果的。
   - 百度“博客园爬虫”，看到有人设置cookie，F12进去看XHR部分有特定接口异步获取上下篇div元素！尝试分析这个接口的请求头看能不能直接用原始界面获取到——加个cookie（不行），去看看Initiator，获取上下篇div元素信息由js脚本发起，脚本向特定链接发送携带当前文章id的querystring的url获取，因为js是获取到文档后在本地执行的，直接爬取下来的html只有js脚本没有脚本获取的内容！（以下发现也佐证了这一点：顺着Request call back找到了最初触发的地方，是一个叫`GetPrevNextPost(...)`的函数，而在获取的html中有这个函数的调用。）
   - 于是最简单想法，百度“爬虫执行js”：@路人甲 https://www.zhihu.com/question/21471960（一般直接分析XHR或ALL找到想要信息所发送的请求直接GET，复杂的就需要模拟执行JS或直接执行JS或模拟浏览器了，三种方法都需要对所需内容参数计算、调用流程耐心分析orz。最后一种方法因消耗许多不必要的CPU和内存资源故不提倡。这部分内容是各个网站不同的，在设计框架阶段先选一些比较好对付的网站来做吧！）


### 2020.11.26（入库）

1. slf4j？？框架需要有怎样的日志系统呢？（又是个大坑吧orz）

>Nov 26, 2020 10:45:10 PM com.mongodb.diagnostics.logging.Loggers shouldUseSLF4J
>WARNING: SLF4J not found on the classpath.  Logging is disabled for the 'org.mongodb.driver' component



