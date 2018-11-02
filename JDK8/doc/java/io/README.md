# JAVA IO

## 类图

## 详解

### AutoCloseable 【interface】

详情参见类[AutoCloseable](../../../src/java/lang/AutoCloseable.java)

Java7引入，作为引入try-with-resources特性的接口。
详情见[try-with-resources](../../feature/try-with-resources.md)

### Closeable 【interface】

* 继承AutoCloseable接口

* 实现者需要保证close方法的幂等性。

详情参见类[Closeable](../../../src/java/io/Closeable.java)

### InputStream 【abstract class】

* 实现Closeable接口。

* 所有输入字节流类的超类。

* 定义了一个抽象方法read()。这个方法将一直阻塞，除非到达输入流末尾，或者抛出异常。

详情参见类[InputStream.java](../../../src/java/io/InputStream.java)

### FilterInputStream 【class】

* 继承InputStream抽象类

* 引入其他输入流

详情参见类[FilterInputStream.java](../../../src/java/io/FilterInputStream.java)

### BufferedInputStream 【class】

* 继承FilterInputStream类

* 提供数据缓冲功能

详情参见类[BufferedInputStream.java](../../../src/java/io/BufferedInputStream.java)