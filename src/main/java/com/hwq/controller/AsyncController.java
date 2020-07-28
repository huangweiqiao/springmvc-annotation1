package com.hwq.controller;

import com.hwq.services.DeferredResultQueue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;
import java.util.concurrent.Callable;

@Controller
public class AsyncController {

    /**
     * 这个请求 /async02  和  /create 这个请求配合演示
     * 1、从浏览器上访问 /async02 ，接收到创建订单的请求，但是创建订单需要耗时，所有异步执行
     * 2、立即在流量器的新标签页上访问 /create ,进行创建订单并且将结果设置给 DeferredResult
     * 3、这时可以看到第一个请求的页面上显示结果了
     * @return
     */
    @ResponseBody
    @RequestMapping("/async02")
    public DeferredResult<Object> createOrder(){
        //5秒超时，超时则返回消息 create fail
        DeferredResult<Object> deferredResult = new DeferredResult<Object>((long)5000,"create fail");

        DeferredResultQueue.save(deferredResult);

        return deferredResult;
    }

    @ResponseBody
    @RequestMapping("/create")
    public String create(){
        //创建订单
        String order = UUID.randomUUID().toString();
        DeferredResult<Object> deferredResult = DeferredResultQueue.get();
        deferredResult.setResult(order);
        return "success ==>"+order;
    }


    /**
     * 该方法是实现异步处理客户端请求 （tomcat线程将请求交个该方法后，线程马上就释放到线程次中）
     * 1、控制器返回 Callable
     * 2、springmvc异步处理：将callable 提交到 TaskExecutor,使用一个叫隔离的线程进行执行
     * 3、DispatcherServlet和所有的Filter退出web容器的线程，但是response保持打开状态
     * 4、Callable返回结果，springmvc将请求重新派发给容器，恢复之前的处理
     * 5、根据Callable返回的结果，springmvc继续进行视图渲染流程等（从收请求--视图渲染）
     *
     *
     * 从日志上看处理过程
     * ===================请求/async01， 进入容器=====================
     * preHandle....
     * main thread start...Thread[http-nio-8080-exec-4,5,main]===1595951470239
     * main thread end...Thread[http-nio-8080-exec-4,5,main]===1595951470243
     * ===================DispatcherServlet及所有的Filter退出线程====================
     *
     * ===================等待Callable执行=============
     * sub thread start...Thread[MvcAsync1,5,main]===1595951470257
     * sub thread end...Thread[MvcAsync1,5,main]===1595951472258
     * ===================Callable执行完成=============
     *
     * ===================请求再次派发给容器，但是这次不会再去执行目标方法（/async01）=============
     * preHandle....
     * postHandle.... （Callable的之前的返回值就是目标方法的方法值，所有目标方法不执行了）
     * afterCompletion....
     *
     *
     *
     * 异步拦截器：
     *      1、原生api的 AsyncListener
     *      2、springmvc的  实现 AsyncHandlerInterceptor
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/async01")
    public Callable<String> async01(){
        System.out.println("main thread start..."+Thread.currentThread()+"==="+System.currentTimeMillis());
        Callable<String> callable = new Callable<String>(){
            @Override
            public String call() throws Exception {
                System.out.println("sub thread start..."+Thread.currentThread()+"==="+System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("sub thread end..."+Thread.currentThread()+"==="+System.currentTimeMillis());
                return "Callable<String> async01";
            }
        };
        System.out.println("main thread end..."+Thread.currentThread()+"==="+System.currentTimeMillis());
        return callable;
    }
}
