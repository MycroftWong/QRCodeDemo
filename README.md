# 二维码扫描Demo

使用了[QRCodeReaderView](https://github.com/dlazaro66/QRCodeReaderView)库进行扫描，但是提供的效果并不好，需要和QQ、微信一样，有扫描动画，所以又找到了[基于google Zxing实现二维码、条形码扫描，仿微信二维码扫描效果 ](http://download.csdn.net/download/xiaanming/5990219)，其中提供的ViewfinderView有动画雏形，进行了一些修改之后，大致满足了需求。

其中因为文字居中的问题，参考了文章[Android Canvas drawText实现中文垂直居中](http://blog.csdn.net/hursing/article/details/18703599)

修改：
## 修改

1. 2016年12月15日21:13:24

看了微信和QQ的apk包，发现他们实现二维码都是使用的图片来实现，并没有进行绘制。既然有现成的资源，那就使用这种方式来实现吧。

修改完成：2016年12月15日23:15:05

使用```QRCodeOverlayView```代替```ViewfinderView```, 不过还缺少在二维码扫描框下方添加文字。


## 绘制.9图片

拿到QQ里面的二维码扫描框时，想直接使用绘制图片的方式将其绘制在屏幕上，即调用```Canvas.drawBitmap```方法，但是实际上，它没有呈现.9图片特有的效果，而是当成给一张普通的图片来绘制。

参考文章[Android手动绘制ninepath(.9.png)图片](http://ntop.iteye.com/blog/1159714), 将获取的```Bitmap```用于构造一个```NinePatch```对象，然后调用```NinePatch.draw(Canvas, Rect)```方法进行绘制，就可以获得.9特有的效果。
```java
mPaint.setAntiAlias(true);

mSquareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.square_qrcode);
mSquareNinePatch = new NinePatch(mSquareBitmap, mSquareBitmap.getNinePatchChunk(), null);

mSquareNinePatch.draw(canvas, mSquareRect, mPaint);
```

# TODO

// 总结二维码扫描，同时将ViewFinderView再进一步修改。

在这个Demo中，[QRCodeReaderView](https://github.com/dlazaro66/QRCodeReaderView)库很大几率会抛出```java.lang.RuntimeException: startPreview failed```异常，而下载该源码sample运行也会出现出现同样的问题。需要进行优化或者换一个扫描库。

