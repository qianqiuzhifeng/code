/*
 * Copyright (c) 2003, 2013, Oracle and/or its affiliates. All rights reserved.
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

package java.io;

import java.io.IOException;

/**
 * A {@code Closeable} is a source or destination of data that can be closed.
 * The close method is invoked to release resources that the object is holding
 * (such as open files).（Closeable是可以关闭的数据的来源或目的地。
 * 调用close方法以释放对象所持有的资源（例如打开的文件））
 *
 * @since 1.5
 */
public interface Closeable extends AutoCloseable {

    /**
     * Closes this stream and releases any system resources associated with it. If
     * the stream is already closed then invoking this method has no effect.
     * (关闭流并且释放与流相关的任何系统资源。如果流已经被关闭，则这个操作没有任何影响（操作幂等性）)
     * <p>
     * As noted in {@link AutoCloseable#close()}, cases where the close may fail
     * require careful attention.（就像AutoCloseable的close方法强调的一样，需要额外注意close方法调用失败的情况）
     * It is strongly advised to relinquish the underlying resources and to
     * internally <em>mark</em> the {@code Closeable} as closed, prior to throwing
     * the {@code IOException}.（在抛出{@code IOException}之前，强烈建议放弃底层资源并在内部<em>将<@
     * em>标记</ em> {@code Closeable}关闭。）
     *
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException;
}
