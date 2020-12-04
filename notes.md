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



#### 2020.11.27（中间件）-2020.11.29

- 1、如何设计中间件？

  一开始尝试找百度“中间件设计”、“框架中间件”、“scrapy中间件”、“java中间件”等内容，发现所获甚少（原因一是自己不知道如何准确描述需求，原因二是百度上东家抄西家的着实多）。

  搜索期间萌生过直接看源码的想法（一开始有点害怕），最后还是决定看源码，所以看Scrapy的源码（`/middleware/`目录、`__init__`和`__main__`开始），看到MiddlewareManager中把middleware对象放到数组中然后用Defer.addCallback()的做法进行调用——由于对python的异步编程并不熟悉，所以不能很好的理解具体机制，而且我怀疑自己理解有误，并不确定是否需要实例化所有的中间件（**如果中间件的作用只是对pass through的东西做特定处理，为什么不能作为静态方法进行调用？**）

  考虑过看Spring或Laravel的源码，鉴于自己用过Laravel，也了解过一些Laravel的生命周期，同时它与Spring一样都是web后端的MVC框架，思想估计有相似之处（而且一开始学习，不然也dirty and quick一些吧hhhh），所以尝试看Larvavel的源码，有所收获

  - 1.1、进一步理解了`bind()`（将Contrast定义好的抽象类与实现好的具体类建立映射——放到关联数组就行啦！）和`make()`（通过Contrast定义的抽象类解析出绑定的具体类实例——用到了反射机制！通过反射获得类名从而获得类的构造函数，然后就能实例化具体的类！至于为什么要搞成这样...目前没有想法，可能是为了解耦定义与实现吧orz）

  - 2.2、Larvavel框架里，`对象通过中间件`的过程似乎类似`对象通过一个中间件handle()方法叠成的匿名函数`的过程

    打算重点去理解`carry()`和`$pipeline($this->passable);`（大致流程是：从`$middleware`数组中逆序地解析出绑定的类实例，将类中定义的`handle()`方法层层包装，最后返回成一个闭包函数`$pipeline()`，让`$pipeline()`处理需要通过中间件的请求）

    ```php
    /* Kernel.php */
    
    /* The application's global HTTP middleware stack.
       These middleware are run during every request to your application. */
    protected $middleware = [
        \App\Http\Middleware\CheckForMaintenanceMode::class,
        \Illuminate\Session\Middleware\StartSession::class,
        \Illuminate\Foundation\Http\Middleware\ValidatePostSize::class,
        \App\Http\Middleware\TrimStrings::class,
        \Illuminate\Foundation\Http\Middleware\ConvertEmptyStringsToNull::class,
        \App\Http\Middleware\TrustProxies::class,
        \App\Http\Middleware\Cors::class,
    ];
    /**
     * Send the given request through the middleware / router.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    protected function sendRequestThroughRouter($request)
    {
        $this->app->instance('request', $request);
        Facade::clearResolvedInstance('request');
        $this->bootstrap();
        // send($request)
        return (new Pipeline($this->app))
            ->send($request)
            ->through($this->app->shouldSkipMiddleware() ? [] : $this->middleware)
            ->then($this->dispatchToRouter());
    }
    ```

    ```php
    /* Pipeline.php */
    
    /* Set the object being sent through the pipeline. */
    public function send($passable) 
    {
        $this->passable = $passable;
        return $this;
    }
    /* Set the array of pipes. */
    public function through($pipes) 
    {
        $this->pipes = is_array($pipes) ? $pipes : func_get_args();
        return $this;
    }
    /* Run the pipeline with a final destination callback. */
    public function then(Closure $destination) 
    {
        $pipeline = array_reduce(
            array_reverse($this->pipes), $this->carry(), $this->prepareDestination($destination)
        );
        return $pipeline($this->passable);
    }
    /* Get a Closure that represents a slice of the application onion. */
    protected function carry()
    {
        return function ($stack, $pipe) {
            return function ($passable) use ($stack, $pipe) {
                if (is_callable($pipe)) {
                    // If the pipe is an instance of a Closure, we will just call it directly but
                    // otherwise we'll resolve the pipes out of the container and call it with
                    // the appropriate method and arguments, returning the results back out.
                    return $pipe($passable, $stack);
                } elseif (!is_object($pipe)) {
                    list($name, $parameters) = $this->parsePipeString($pipe);
                    // If the pipe is a string we will parse the string and resolve the class out
                    // of the dependency injection container. We can then build a callable and
                    // execute the pipe function giving in the parameters that are required.
                    $pipe = $this->getContainer()->make($name);
                    $parameters = array_merge([$passable, $stack], $parameters);
                } else {
                    // If the pipe is already an object we'll just make a callable and pass it to
                    // the pipe as-is. There is no need to do any extra parsing and formatting
                    // since the object we're given was already a fully instantiated object.
                    $parameters = [$passable, $stack];
                }
                // the method to call on each pipe is declared in Pipeline.php
                // as -- `protected $method='handle';`
                // so each middleware should implement a method called 'handle' with
                // a `Closure` as parameter(`$next` for example), and finally
                // `return $next($request);`
                $response = method_exists($pipe, $this->method)
                                ? $pipe->{$this->method}(...$parameters) 
                                : $pipe(...$parameters);
                return $response instanceof Responsable
                            ? $response->toResponse($this->container->make(Request::class))
                            : $response;
            };
        };
    }
    /* Parse full pipe string to get name and parameters. */
    protected function parsePipeString($pipe)
    {
        list($name, $parameters) = array_pad(explode(':', $pipe, 2), 2, []);
        if (is_string($parameters)) {
            $parameters = explode(',', $parameters);
        }
        return [$name, $parameters];
    }
    /* Get the final piece of the Closure onion. */
    protected function prepareDestination(Closure $destination)
    {
        return function ($passable) use ($destination) {
            return $destination($passable);
        };
    }
    ```

- 2、GIVE IT A SHOT

  - > Usage of API documented as @since 1.8+: https://blog.csdn.net/weixin_42687829/article/details/86751174?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.control

  - `Arrays.stream().reduce()`不保证是串行执行，如何按照middleware中的顺序生成一个集成的闭包（`Function<Request, Request>`）？最后还是用**循环+反射+实例化对象后调用方法**来做了orz

  - 还把Response和Request封成Passable，想着方便Middleware接口定义。。。也不知有无用——用Object应该就可以！



#### 2020.11.30（调度器）

- 1、使用了最简单LinkedList（实现了Queue接口），未考虑多线程等其他情况。因为Scheduler有一个存放Request对象的容器和两个必须实现的方法（插入和取出），所以暂时把它设计成了抽象类
- 2、新的Request加入队列都是在Spider执行的，加入的情况有DownloaderMiddleware返回Request时以及能从Item中解析出的nextUrls时——但**有些问题，是否需要设计成让Request能使用特定的Parser？如果需要如何设计？像那种主页是一个list（智联招聘、百度贴吧、推酷...）的网站比比皆是，似乎需要对特定的Request使用特定的Parser。或者只需要针对主页定制一个Parser就行？这样一个Spider就维持一个SiteParser和ItemParser**



#### 2020.12.01（尝试简单地使用，考虑存在一级列表的详情页爬取）

- 1、建立了SpiderPipeline，能够让Spider向另一个Spider的调度器添加Request——以此解决进入详情页时有多级列表的情况。但是**多个Spider线程之间启动顺序、阻塞逻辑、状态管理仍然未理清楚**（目前是先启动解析列表的Spider，再启动解析详情页的Spider）。而且**调度器多线程间共用（生产-消费模型？）**问题也值得考虑

- 2、尝试爬取百度百科的心理学词条，但发现列表是通过POST方式的请求获得JSON格式的数据，框架的Request和Response刚好还没做这块。。。打算以后再以此为切入点进行项目完善。于是转而用了推酷进行测试，在**Tuicool上跑通了存在一级列表的详情页爬取（单页）**



#### 2020.12.02-2020.12.04（完善Request和层次处理）

- 1、Request不单是由一条url决定！！！还有header、method、body等信息（看看Postman可设置字段就可大概知道！以前接触过的要融会贯通啊！）。但是去看了看Webmagic源码，却没看到`setMethod()`方法在具体实例中的调用，不知到这些信息的添加逻辑应该放在哪一个环节orz，果然**实战开发经验&接触深度不足真谈不上什么设计、甚至无法理解别人设计的精妙之处orz**。。。又去看Scrapy的用法，试图思路——`scrapy.Request(*args)`添加请求，`Request`对象直接包含了各种信息，先尝试完善Request吧

- 2、唯一一点进度，总算知道怎么用URLConnection执行POST方法的连接了！原来是`getOutputStream()`再往这个流里面传表单对应`Content-Type`的数据字节流！（`x-www-form-urlencoded`格式传的应该是query-string，那其他类型呢？

> java doc: https://docs.oracle.com/javase/7/docs/api/java/net/URLConnection.html
>
> a very good demo: https://www.edureka.co/community/5406/how-to-send-http-post-requests-on-java

- 3、将昨天的URLConnection发送POST请求流程封装到了Downloader中，也给Request加入了若干属性（headers/cookies/body先用），**重新思考流程中的一些节点**——生成一个入口Request（唤作`index`吧，**但这个`index`应该不需要保留在Spider中，看看怎么解决**），再初始化列表Spider；将下一级的Spider作为参数，用于初始化列表的Parser，所以SpiderPipeline不要了，暂时决定在列表的Parser中生成下一级的Request并传入下一级Spider的Scheduler（**耦合性逐渐上升。。？orz**）
- 4、Successfully redirect using Middleware !





1. 