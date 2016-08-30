# DrawerLayout
Learn how to use ViewDragHelper

这个DrawerLayout内部主要是运用了一个ViewDragViewHelper来实现拖拽的一个效果

在xml中可以写成

```xml
<highwin.zgs.drawerlayout.view.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:zgs="http://schemas.android.com/apk/res-auto"
        android:id="@+id/drawer_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@mipmap/bg"
        zgs:endLeftAlphaValue="0.5"
        zgs:endLeftValue="1"
        zgs:endMainValue="0.5"
        zgs:leftDistance="300"
        zgs:mode="MODE_SCALE"
        zgs:startLeftAlphaValue="0"
        zgs:startLeftValue="0"
        zgs:startMainValue="1">

        <!--左子View|left View-->
        <include layout="@layout/left_layout" />

        <!--右子View|right view-->
        <include layout="@layout/main_layout" />

</highwin.zgs.drawerlayout.view.DrawerLayout>
```

***
对应的attr的文件为

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="DrawerLayout">
    
        <!--距离左边的距离，默认是屏幕宽度的3/5，也可以自行设置-->
        <attr name="leftDistance" format="integer|dimension"/>
        
        <!--以下两个是左view的缩放的值从开始到结束-->
        <attr name="startLeftValue" format="float|dimension"/>
        <attr name="endLeftValue" format="float|dimension"/>
        
        <!--以下两个是主view的缩放的值从开始到结束-->
        <attr name="startMainValue" format="float|dimension"/>
        <attr name="endMainValue" format="float|dimension"/>
        
        <!--以下两个是包含两个View的背景的的alpha值-->
        <attr name="startLeftAlphaValue" format="float|dimension"/>
        <attr name="endLeftAlphaValue" format="float|dimension"/>
        
        <!--主要是设置拖动的模式，拖拽不是向右平移-->
        <attr name="mode" format="enum">
            <enum name="MODE_SCALE" value="0x000004"/>
            <enum name="MODE_TRANSLATE" value="0x000005"/>
        </attr>
    </declare-styleable>
</resources>

```

> 需要注意的是，子View的最外层的容器最好写成match_parent，如果不写match_parent
> 会报异常,可以在源码中删除掉报异常的代码即可


