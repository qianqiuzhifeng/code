/*
 * Copyright (c) 2009, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang;

/**
 * An object that may hold resources (such as file or socket handles) until it
 * is closed.(一个对象将持有资源（例如文件或者socket连接）直到它关闭) The {@link #close()} method of an
 * {@code AutoCloseable} object is called automatically when exiting a {@code
 * try}-with-resources block for which the object has been declared in the
 * resource specification header.（当退出已在资源规范头中声明了对象的try
 * -with-resources块时这个对象的close方法将被自动调用） This construction ensures prompt
 * release, avoiding resource exhaustion exceptions and errors that may
 * otherwise occur.（这种结构可确保快速释放，避免资源耗尽异常和可能发生的错误。）
 *
 * @apiNote
 *          <p>
 *          It is possible, and in fact common, for a base class to implement
 *          AutoCloseable even though not all of its subclasses or instances
 *          will hold releasable resources. For code that must operate in
 *          complete generality, or when it is known that the
 *          {@code AutoCloseable} instance requires resource release, it is
 *          recommended to use {@code
 * try}  -with-resources constructions. However, when using facilities such
 *          as {@link java.util.stream.Stream} that support both I/O-based and
 *          non-I/O-based forms, {@code try}-with-resources blocks are in
 *          general unnecessary when using non-I/O-based forms.
 *
 * @author Josh Bloch
 * @since 1.7
 */
public interface AutoCloseable {
    /**
     * Closes this resource, relinquishing any underlying resources. This method is
     * invoked automatically on objects managed by the {@code try}-with-resources
     * statement.（关闭资源，放弃任何潜在资源。在try-with-resources语句管理的对象上自动调用此方法。）
     *
     * <p>
     * While this interface method is declared to throw {@code
     * Exception}, implementers are <em>strongly</em> encouraged to declare concrete
     * implementations of the {@code close} method to throw more specific
     * exceptions, or to throw no exception at all if the close operation cannot
     * fail.(虽然声明此接口方法抛出Exception，但强烈建议实现者声明在close方法的具体实现中抛出更具体的异常，或者不抛出异常
     * 所有如果关闭操作不能失败。)
     *
     * <p>
     * Cases where the close operation may fail require careful attention by
     * implementers. It is strongly advised to relinquish the underlying resources
     * and to internally <em>mark</em> the resource as closed, prior to throwing the
     * exception. The {@code
     * close} method is unlikely to be invoked more than once and so this ensures
     * that the resources are released in a timely manner. Furthermore it reduces
     * problems that could arise when the resource wraps, or is wrapped, by another
     * resource.
     *
     * <p>
     * <em>Implementers of this interface are also strongly advised to not have the
     * {@code close} method throw {@link InterruptedException}.</em>
     *
     * This exception interacts with a thread's interrupted status, and runtime
     * misbehavior is likely to occur if an {@code
     * InterruptedException} is {@linkplain Throwable#addSuppressed suppressed}.
     *
     * More generally, if it would cause problems for an exception to be suppressed,
     * the {@code AutoCloseable.close} method should not throw it.
     *
     * <p>
     * Note that unlike the {@link java.io.Closeable#close close} method of
     * {@link java.io.Closeable}, this {@code close} method is <em>not</em> required
     * to be idempotent. In other words, calling this {@code close} method more than
     * once may have some visible side effect, unlike {@code Closeable.close} which
     * is required to have no effect if called more than once.
     *
     * However, implementers of this interface are strongly encouraged to make their
     * {@code close} methods idempotent.
     *
     * @throws Exception if this resource cannot be closed
     */
    void close() throws Exception;
}
