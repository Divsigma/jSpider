### 2020.11.22

1. 遍历map的4种方法：

   只访问key或value、访问entrySet、使用Iterator、使用lambda表达式

> https://www.cnblogs.com/zhaoguhong/p/7074597.html?utm_source=itdadao&utm_medium=referral

2. PrintWriter继承自抽象类Writer（专门处理Unicode文本流的类，处理单位是2字节的Char而不是byte），有缓存区（？），需要flush()才能把数据写到磁盘，同时注意close()释放资源（close()之前会自动flush()，所以如果不close()也不flush()，最后留存于缓存区的数据包不会被传送到指定文件！）

### 2020.11.23

> 日记：
>
> 借鉴了webmagic和scrapy的框架流程图，看了看webmagic的Spider源码，打算从最简单的着手，将昨天跑通的流程分隔成Request、Downloader、Response、Parser、Item、Pipeline六个部分，Request、Response、Item是信息载体，由Spider在Downloader、Parser、Pipeline间进行传递，联动整个框架（暂不考虑middleware）。
>
> 今天跑通了Downloader和Spider通过url和Response交互（还没加入Request），Downloader使用的是URLConnection。

1. StringBuffer和StringBuilder异同？



### 2020.11.24

1. parser定义成abstract class、interface还是class？（暂时选用了abstract class，因为一个parser**必定**包含了处理行为（abstract class中的abstract方法一旦被非abstract类继承就必须要重写），也有url、encoding等属性。interface更像是单纯对行为的封装。webmagic使用了interface，有朝一日再对比吧TAT）
2. pipeline定义成abstract class、interface还是class？（暂时选用了interface，因为我暂时还没提取出数据库、控制台、文本文件pipeline的共有属性，但它们都有process行为。webmagic使用了interface，有朝一日再对比吧TAT）

3. 为毛要用getter和setter？

   > @仲晨：https://www.zhihu.com/question/21401198

4. abstract class的构造函数能否被继承？

   

5. 

