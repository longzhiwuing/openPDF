#ionic打开pdf的插件（android） 内置pdf查看器

这个插件内置了[pdf查看器](https://github.com/JoanZapata/android-pdfview)，不是调用机器自带的pdf阅读软件打开，而是在app内部查看pdf。因为是项目需要，我只做了打开远程pdf文件url的功能，没做本地打开pdf，但是看源码的话应该很好改，大家可以按照自己的需求修改代码。

首先需要在android项目的gradle中添加
```
compile 'com.joanzapata.pdfview:android-pdfview:1.0.4@aar'
```

##调用方法
```javascript
 window.plugins.OpenPDF.openWithUrl($scope.pdfUrl,function(data){
  console.log('success data:'+data);
},function(data){
  console.log('error data:'+data);
});
 ```

只有一个参数，就是pdf的远程url地址，这个做了一个缓存，他在第一次打开url的时候，会先下载该pdf文件到本地，之后在调用该方法传入该地址，就只打开本地的pdf，不会再去服务器下载该pdf文件了。

欢迎反馈bug


