#这是一个ProGuard(6.0)的配置文件，你可以在这里添加项目特定的ProGuard规则，更多规则请百度……

#-ignorewarning #忽略警告

#-optimizationpasses 5 #代码混淆的压缩比例，在0~7之间，默认为5，一般不需要修改

#-obfuscationdictionary 字典.txt #指定一个随机字符字典(字典.txt) 改为混淆文件名

#-classobfuscationdictionary 字典.txt #指定一个混淆类名的字典

#-packageobfuscationdictionary 字典.txt #指定一个混淆包名的字典

#-printconfiguration 文件名.txt #将已解析过的配置输出到指定的文件。该选项可用于调试配置。

#-forceprocessing #尽管输出文件已经是最新的，还是强制进行处理一次。

#-dontshrink #声明不压缩输入文件。默认情况下，除了-keep相关配置指定的类，所有其它没有被引用到的类都会被删除。

#-printusage 被删除的元素.txt #输出出那些被删除的元素到文件。

#-printseeds 保留.txt #列出匹配-keep选项的类和成员并输出到指定的文件。

#-allowaccessmodification #优化时允许访问并修改有修饰符的类和类的成员，这可以提高优化效果。如果你的代码是一个库的话，最好不要配置这个选项，因为它可能会导致一些private变量被改成public

#-useuniqueclassmembernames #指定相同的混淆名对应相同的方法名，不同的混淆名对应不同的方法名。如果不设置这个选项，同一个类中将会有很多方法映射到相同的方法名。这项配置会稍微增加输出文件中的代码，但是它能够保证保存下来的mapping文件能够在随后的增量混淆中继续被遵守，避免重新命名。比如说，两个接口拥有同名方法和相同的签名。如果没有这个配置，在第一次打包混淆之后，他们两个方法可能会被赋予不同的混淆名。如果说下一次添加代码的时候有一个类同时实现了两个接口，那么混淆的时候必然会将两个混淆后的方法名统一起来。这样就必须要改混淆文件其中一处的配置，也就不能保证前后两次混淆的mapping文件一致了。（如果你只想保留一份mapping文件迭代更新的话，这项配置比较有用）

#-whyareyoukeeping class 类名 #输出压缩过程中保留这些类文件和类成员的具体原因，也适用了使用了压缩的情况

-dontoptimize #不优化输入的class文件

#-assumenosideeffects class android.util.Log {#指定一些方法被删除也没有影响，如果确定这些方法没有使用，那么就会删除这些方法的调用。proguard会自动的分析你的代码，但不会分析处理类库中的代码。例如，可以指定System.currentTimeMillis(),这样在optimize阶段就会删除所有的它的调用。还可以用它来删除打印Log的调用。这条配置选项只在optimizate阶段有用，删除代码中Log相关的代码
#public static boolean isLoggable(java.lang.String, int);
# public static int v(...);}

#-mergeinterfacesaggressively #指定接口可以合并，即使实现类没实现所有的方法。该选项可以通过减少类的总数减少输出文件的大小。只有开启优化时可用

#-dontobfuscate #指定不混淆类。默认情况下，混淆是开启的。除了keep配置中声明的类，其它的类或者类的成员混淆后会改成简短随机的名字。

#-printmapping 映射文件.txt #输出类和类成员新旧名字之间的映射到指定文件中。只有开启混淆时可用。

#-applymapping 映射文件.txt #指定重用一个已经写好了的映射文件作为新旧元素名的映射。映射文件未列出的类和类成员会使用随机的名称。如果代码结构从根本上发生变化，ProGuard 可能会输出映射会引起冲突警告。你可以通过添加-useuniqueclassmembernames选项来降低风险。只能指定一个映射文件。只有开启混淆时可用。

#-keeppackagenames cc.yudao.main #不混淆指定的包名。过滤器是由逗号分隔的包名列表。包名可以包含 ？、、* 通配符，并且可以在包名前加上 ! 否定符。只有开启混淆时可用。


#-flattenpackagehierarchy #cc.yudao #重新打包所有重命名的类到给定的包名中。如果没参数或字符串为空，类会移动到根目录包名下。该选项是进一步混淆包名的例子，可以使处理后的代码更小更难阅读。只有开启混淆时可用。

#-repackageclasses cc.yudao #重新打包所有重命名的类到给定的包名中。如果没参数或字符串为空，类的包名会被完全移除。该选项覆盖-flattenpackagehierarchy，是进一步混淆包名的另一个例子，可以使处理后的代码更小更难阅读。曾用名为-defaultpackage(已废弃)。只有开启混淆时可用。

#-overloadaggressively #开启侵入性重载混淆。多个字段及方法允许同名，只要它们的参数及返回值类型不同。该选项可使处理后的代码更小（及更难阅读）。只有开启混淆时可用。注：Dalvik 不能处理重载的静态字段

-dontusemixedcaseclassnames #指定不使用大小写混用的类名。默认情况下，混淆后的类名可能同时包含大写字母和小写字母。这样生成jar包并没有什么问题。只有在大小写不敏感的系统（例如windows）上解压时，才会涉及到这个问题。因为大小写不区分，可能会导致部分文件在解压的时候相互覆盖。如果有在windows系统上解压输出包的需求的话，可以加上这个配置

#-keepattributes Exceptions, InnerClasses, Signature, Deprecated,SourceFile, LineNumberTable, *Annotation*, EnclosingMethod #指定受保护的属性，过滤器是由逗号分隔的 JVM 及 ProGuard 支持的属性列表。属性名可以包含 ？、、* 通配符，也可以加!做前导符，将某个属性排除在外。当混淆一个类库的时候，至少要保持InnerClasses, Exceptions, Signature属性。为了跟踪异常信息，需要保留SourceFile, LineNumberTable两个属性。如果代码中有用到注解，需要把Annotion的属性保留下来。
-keepattributes SourceFile, LineNumberTable, *Annotation*

#-renamesourcefileattribute 名称 #指定一个字符串常量设置到源文件的类的属性(SourceFile和SourceDir)当中（和SourceDir）。需要被-keepattributes选项指定保留。

#-keepparameternames #指定被保护的方法的参数类型和参数名不被混淆。

#-adaptclassstrings 类名 #混淆与完整类名一致的字符串。没指定过滤器时，所有符合现有类的完整类名的字符串常量均会混淆。

#-adaptresourcefilenames 过滤器参数 #以混淆后的类文件作为样本重命名指定的源文件。不加f过滤器的情况下，所有资源文件都受此影响；加了过滤器的情况下，只有匹配到的类受此影响

#-adaptresourcefilecontents 过滤器参数 #以混淆后的类文件作为样本混淆指定的源文件中与完整类名一致的内容。没指定过滤器时，所有源文件中与完整类名一致的内容均会混淆。只有开启混淆时可用。

-dontpreverify #指定不对处理后的类文件进行预校验。默认情况下如果类文件的目标平台是 Java Micro Edition 或 Java 6 或更高时会进行预校验，目标平台是 Android 时没必要开启，关闭可减少处理时间。目标为Java Micro版本的情况下，预校验是必须的。如果你声明了这项配置，你还需要加上下面一条配置。

-microedition #声明目标平台是java micro版本。预校验会根据这项配置加载合适的StackMap，而不是用标准的StackMap。

-verbose #声明在处理过程中输出更多信息。添加这项配置之后，如果处理过程中出现异常，会输出整个StackTrace而不是一条简单的异常说明。

#-dontnote com.* #声明不输出那些潜在的错误和缺失，比如说错别字或者重要的配置缺失。配置是一串正则表达式，混淆过程中不会输出被匹配到的类相关的内容。

#-dontwarn com.* #声明不输出那些未找到的引用和一些错误并继续混淆。配置是一串正则表达式，被匹配到的类名相关的警告都不会被输出出来。

#-ignorewarnings #输出所有找不到引用和其它错误的警告并继续混淆

#-dump 结构.txt #声明输出整个处理之后的jar文件的类结构到文件

#-skipnonpubliclibraryclasses #指定读取引用库文件的时候跳过非public类。这样做可以提高处理速度并节省内存。一般情况下非public在应用内是引用不到的，跳过它们也没什么关系。但是，在一些java类库中中出现了public类继承非public类的情况，这样就不能用这个选项了。这种情况下，会打印一个警告出来，提示找不到类。

-dontskipnonpubliclibraryclasses #跟上面的参数相对。版本4.5以上，这个是默认的选项。

#-dontskipnonpubliclibraryclassmembers 指定不忽略库类库中的非public成员（成员变量和方法）。默认情况下，proguard在读取库文件的时候会自动忽略这些类的成员，因为这些非public成员不会被源代码引用到。但有时候他们是可以被引用到的。比如说，源代码中与库文件用同一个包名，那么源代码就可以访问包作用域的变量。在这些情况下，为了引用一致，不被混淆，就需要指定不跳过这些类。

#-target 1.6 #指定处理的class文件中java的目标版本。版本号是1.0, 1.1, 1.2, 1.3, 1.4, 1.5(或者5)， 1.6（或者6）, 1.7（或者7）,1.8（或者8）之中的一个。默认情况下，class文件的版本号是不会变的。比如，你可能想更新class文件到Java 6,通过改变他们的版本让他们预编译。

#-keepdirectories 目录名 #指定输出jar包中需要保留的目录名。默认情况下所有的目录都会被删除。这项配置后面不加过滤器的时候，所有目录都会被保留。加了过滤器之后，只有过滤器匹配的目录才会被保留。

-keepclasseswithmembernames,includedescriptorclasses class * { native <methods>;}#保护符合条件的含有native方法的类。附加的includedescriptorclasses是保证参数和返回类型的类同样不被混淆。这样就可以做到这些类的方法签名与调试时完全一致

#-keepclassmembernames 成员名称 #指定要保留的类成员，前提是在压缩阶段未被删除,仅用于模糊处理

#-keepnames class * implements java.io.Serializable #指定一些类名受到保护，前提是他们在shrink这一阶段没有被去掉。也就是说没有被入口节点直接或间接引用的类还是会被删除。仅在obfuscate阶段有效。这指定了继承Serizalizable的类的如下成员不被移除混淆

#-keepclasseswithmembers public class * {public static void main(java.lang.String[]);} #指定成员的类将被保护，根据类成员确定一些将要被保护的类。例如保护所有含有main方法的类。
#-keepclassmembers class * { *** b(...); } #指定不混淆方法b
#-keepclassmembers class * implements java.io.Serializable {
#  static final long serialVersionUID;
# private static final java.io.ObjectStreamField[] serialPersistentFields;
# !static !transient <fields>;
#!private <fields>;
#  !private <methods>;
# private void writeObject(java.io.ObjectOutputStream);} #保护的指定的成员变量不被移除、优化、混淆。例如，保护所有序列化的类的成员变量。
#-keepclassmembers class **.R$* { *; } #不混淆资源类
#-keepclassmembers class fqcn.of.javascript.interface.for.Webview { public *;}#不混淆WebView
#-keepclassmembers class * extends android.webkit.WebViewClient {public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap); public boolean *(android.webkit.WebView, java.lang.String);}
#-keepclassmembers class * extends android.webkit.WebViewClient {  public void *(android.webkit.WebView, jav.lang.String);}

#-keep class cc.yudao { <init>(...); } #指定不混淆当前类
#-keep class cc.yudao.**{*;} #保留实体类和成员不被混淆
#-keep class cc.yudao.配置 { *** *(...); } #指定不混淆当前类和所有方法
#-keep public class * extends android.app.Fragment #保留继承Fragment类的子类
#-keep public class * extends android.app.Activity #保留继承Activity类的子类
#-keep public class * extends android.app.Application #保留继承Application类的子类
#-keep public class * extends android.app.Service #保留继承Service类的子类
#-keep public class * extends android.content.BroadcastReceiver #保留继承BroadcastReceiver类的子类
#-keep public class * extends android.content.ContentProvider #保留继承ContentProvider类的子类
#-keep public class * extends android.preference.Preference #保留继承Preference类的子类
#-keep public class * extends android.view.View #保留继承View类的子类
