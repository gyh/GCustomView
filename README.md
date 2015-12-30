自定义View
=========

##test1

##test2

##test3

##test4  --下拉放大顶部图片

###源代码是 [PullToZoomInListVie](https://github.com/matrixxun/PullToZoomInListVie)
###里面的代码估计是反编译过来的，我自己添加了一些注释，并且完善了部分功能
####基本用法如下
<pre><code>
listView.getHeaderView().setImageResource(R.mipmap.splash);
listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
</code></pre>


