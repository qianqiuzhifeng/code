# try-with-resources

[try-with-resources](https://javapapers.com/core-java/try-with-resources/)

Java 7 gave us try-with-resources, a nice feature on exception handling. This tutorial is part 3 of the exception handling series. Part I is about basics of exception handling. Part II is about exception hierarchy, stack traces, handling exception and best practices. Now in this part three of exception handling tutorial, we will see about try-with-resources (ARM) introduced in Java version 7.

```text
Java7给我们带来了try-with-resources,一个优秀的异常处理特性。这个教程讲述了异常处理的场景的三部分，第一部分是基础的异常处理，第二部分是关于异常层次结构，堆栈跟踪，处理异常和最佳实践，现在在异常处理教程的第三部分中，我们将看到Java版本7中引入的try-with-resources（ARM）。
```

Close the resources in finally block is a well know rule for all java developers. As part of exception handling mechanism, we should always ensure to close and release the resources that is used inside the block. In a block of code which is enclosed in a try-catch block, if an exception is thrown the normal flow is altered.

```text
所有Java开发者都知道在finally语句块中关闭资源这个规则。作为异常处理机制的一部分，我们必须确保在这个语句块中资源被关闭并且被释放。在try-catch块中包含的代码块中，如果抛出异常，则更改正常流。
```

```java
try {
    //code statements
    //exception thrown here
    //lines not reached if exception thrown
} catch (Exception e) {
    //lines reached only when exception is thrown
} finally {
    //always executed
    //irrespective of an exception thrown or not
}
```

Look at the above code block. The best place to close and release all the resources is finally block. So what is the resource we are talking about here? Resources means database connection, file connections, etc.

```text
看上面的代码块，最好在finally语句块中关闭并释放资源。那么我们在这里讨论的资源是什么？ 像数据库连接，文件连接等。
```

## Poor Structure

As stated above, we are going to close the resources in the finally block. Look at the following code, how ugly it looks like.

```text
如上所述，我们将关闭finally块中的资源。 看看下面的代码，看起来有多难看。
```

```java
...
InputStream is = null;
try {
    is = new FileInputStream("test");
    is.read();
    ...
} catch (Exception e) {
    ...
} finally {
    try {
        is.close();
    } catch (IOException e) {
        e.printStackTrace();
        ...
    }
}
```

Explicitly the programmer has to close the resource and need to surround it with a try-catch block and all inside the finally. This is a standard repeated code in java projects.

```text
程序员必须明确地关闭资源，并且需要使用try-catch块并在finally中包含所有内容。 这是java项目中的标准重复代码。
```

We just need to put a try-catch and we don’t do anything to handle it there. Apache’s IOUtils introduced methods to use inside finally block such as above.

```text
我们只需要设置一个try-catch，我们就不会做任何事情来处理它。 Apache的IOUtils介绍了在上面的finally块中使用的方法。
```

org.apache.commons.io.IOUtils has methods to suppress the exception thrown inside finally block when the close method is executed.

```text
org.apache.commons.io.IOUtils有一些方法可以在执行close方法时抑制finally块内抛出的异常。
```

## Automatic Resource Management (ARM)

In Java 7, we got a nice feature to manage these resources automatically. Manage is really a hype word here, all it does is close the resources. Automatic resource management, helps to close the resources automatically.

```text
Java7中，我们有一个很好的功能来自动管理这些资源。 管理在这里真的是一个炒作词，它所做的就是关闭资源。 自动资源管理，有助于自动关闭资源。
```

Resource instantiation should be done within try(). A parenthesis () is introduced after try statement and the resource instantiation should happen within that paranthesis as below,

```text
资源实例化应该在try（）中完成。 在try语句之后引入括号（），并且资源实例化应该在该paranthesis中发生，如下所示，
```

```java
try (InputStream is = new FileInputStream("test")) {
    is.read();
    ...
} catch(Exception e) {
    ...
} finally {
    //no need to add code to close InputStream
    //it's close method will be internally called
}
```

## try-with-resources and AutoCloseable

* All the classes cannot be used in try-with-resources. AutoCloseable is an interface used as a contract to implement try-with-resources. Classes that implements AutoCloseable must be used as a resource in try-with-resources, else we will get compilation error.

```text
不是所有类都能在try-with-resources中使用。 AutoCloseable是用作实现try-with-resources的契约的接口。 实现AutoCloseable的类必须在try-with-resources中用作资源，否则我们将得到编译错误。
```

* close is the only method in AutoCloseable and it gets automatically invoked at runtime.

```text
close是AutoCloseable中唯一的方法，它在运行时自动调用。
```

* Multiple classes can be declared within the same try as “try (Lion lion = new Lion(); Tiger tiger = new Tiger()) {…”

```text
可以在一个try语句中定义多个类
```

* During initialization of multiple resources in ‘try’, if there is any issue then immediately in the reverse order those resources that are already initialized will be closed.

```text
在'try'中初始化多个资源期间，如果有任何问题，则立即以相反的顺序关闭那些已经初始化的资源。
```

* When multiple classes are used in ‘try’, then the close method is called in the reverse order. To understand this, check the below example.

```text
当在'try'中使用多个类时，则以相反的顺序调用close方法。 要了解这一点，请查看以下示例。
```

* try-with-resources, can have catch and finally. They work as usual and no change in it.

```text
try-with-resources,可以包含catch和finally。 他们像往常一样工作，没有变化。
```

* In try-with-resources on exception, before going to catch the close statements will be executed.

```text
在异常的try-with-resources中，在执行catch之前将执行close语句。
```

* While implementing AutoCloseable, best practice is to throw specific exception and not the highest level ‘Exception’ itself. This needs to highlighted because the signature of ‘close’ in AutoCloseable throws Exception.

```text
在实现AutoCloseable时，最佳做法是抛出特定异常而不是最高级别的“Exception”本身。 这需要突出显示，因为AutoCloseable中的'close'签名会抛出异常。
```

* On implementation, AutoCloseable.close should not throw InterrupedException, because at runtime if this exception is suppressed it will cause issues in thread handling.

```text
在实现时，AutoCloseable.close不应抛出InterrupedException，因为在运行时如果此异常被抑制，则会导致线程处理出现问题。
```

* By specification this close method is not required to be idempotent. But still, it is best practice to implement it as idempotent. That is, even if the close method is invoked multiple times, it should act as same.

```text
根据规范，这种接近的方法不需要是幂等的。 但是，最佳做法是将其实现为幂等。 也就是说，即使多次调用close方法，它也应该是相同的。
```

* Closeable is an interface that extends AutoCloseable and its close method must be idempotent.

```text
Closeable是一个扩展AutoCloseable的接口，它的close方法必须是幂等的。
```

* Resource declared in try gets instantiated just before the start of the try-block.

```text
try中声明的资源在try-block开始之前实例化。
```

* Resource is implicitly declared as final.

```text
资源被隐式声明为final。
```
