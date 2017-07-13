反人类音量设计比赛第一名的 Android 控件实现
--
先上效果图：  

![](https://github.com/shunfayang/PumpVolumeView/blob/master/gif/pump.gif)


### 前言 ###

前段时间看到一个反人类音量设计比赛，直接笑出了声。然后过了几天，gank.io竟然推了这么一篇文章[Android实现反人类音量滑块](https://github.com/shellljx/FuckingVolumeSlider)。作者挑了一个设计直接给实现了。然后我又笑出了声。于是我又找到比赛的那些设计，重新笑了一遍。

看到排名第一的打气筒设计，觉得并不难实现，琢磨着周末有空给写出来。周末累（lan）得要死，就没写。某个周四的中午，精神头好着，就想找点乐子玩儿下（是的我很闲），就顺手创建了这么个项目。

### 实现 ###

说实在话，这玩意实在太简单，总结为以下几个步骤：
1. 计算外部柱体位置并绘制深灰色矩形；
2. 计算内部浅灰色柱体并绘制矩形；
3. 计算音量（数字）以及位置并绘制；
4. 计算音量进度及位置并绘制矩形；
5. 计算横向即竖直的手柄并绘制；
6. 重写 onTouchEvent()方法处理手势；
7. 开启一个线程不断递减音量；

![](https://github.com/shunfayang/PumpVolumeView/blob/master/gif/%E7%BB%98%E5%88%B6%E6%AD%A5%E9%AA%A4.jpg)

几乎没什么可以继续写的，有兴趣的小伙伴就看源码吧。



