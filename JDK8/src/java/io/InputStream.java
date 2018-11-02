/*
 * Copyright (c) 1994, 2013, Oracle and/or its affiliates. All rights reserved.
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

/**
 * This abstract class is the superclass of all classes representing an input
 * stream of bytes.(此抽象类是表示输入字节流的所有类的超类。)
 *
 * <p>
 * Applications that need to define a subclass of <code>InputStream</code> must
 * always provide a method that returns the next byte of input. （）
 * （程序需要定义一个InputStream的子类，这个子类必须提供一个返回下一个字节的方法。）
 *
 * @author Arthur van Hoff
 * @see java.io.BufferedInputStream
 * @see java.io.ByteArrayInputStream
 * @see java.io.DataInputStream
 * @see java.io.FilterInputStream
 * @see java.io.InputStream#read()
 * @see java.io.OutputStream
 * @see java.io.PushbackInputStream
 * @since JDK1.0
 */
public abstract class InputStream implements Closeable {

    // MAX_SKIP_BUFFER_SIZE is used to determine the maximum buffer size to
    // use when skipping.（MAX_SKIP_BUFFER_SIZE用于确定跳过时使用的最大缓冲区大小。）
    private static final int MAX_SKIP_BUFFER_SIZE = 2048;

    /**
     * Reads the next byte of data from the input stream. The value byte is returned
     * as an <code>int</code> in the range <code>0</code> to <code>255</code>. If no
     * byte is available because the end of the stream has been reached, the value
     * <code>-1</code> is returned. This method blocks until input data is
     * available, the end of the stream is detected, or an exception is thrown.
     * （读取输入流下一个字节数据。这个字节值返回一个0-255之间的数字。如果到达字节流末尾，没有可用的字节时，则返回-1。
     * 这个方法会一直阻塞，直到到达输入流末尾，或者抛出异常。）
     * <p>
     * A subclass must provide an implementation of this method. （子类必须实现这个方法。）
     * 
     * @return the next byte of data, or <code>-1</code> if the end of the stream is
     *         reached.
     * @exception IOException if an I/O error occurs.
     */
    public abstract int read() throws IOException;

    /**
     * Reads some number of bytes from the input stream and stores them into the
     * buffer array <code>b</code>. The number of bytes actually read is returned as
     * an integer. This method blocks until input data is available, end of file is
     * detected, or an exception is thrown.
     * （从输入流中读取指定长度的字节数据，并将之存储在缓冲数组b中。返回已读取字节的长度。 当输入数据可用时， 这个方法将阻塞， 直到文件末尾，或者抛出异常。）
     * <p>
     * If the length of <code>b</code> is zero, then no bytes are read and
     * <code>0</code> is returned; otherwise, there is an attempt to read at least
     * one byte. If no byte is available because the stream is at the end of the
     * file, the value <code>-1</code> is returned; otherwise, at least one byte is
     * read and stored into <code>b</code>.
     * （如果数组b的长度为0，则不会读取任何字节，并返回0，否则将尝试读取至少一个字节的数据。如果因为到达文件末尾没有字节可以读取的时候，就会返回-1，否则，
     * 至少会读取一个字节，并保存到数据b）
     * <p>
     * The first byte read is stored into element <code>b[0]</code>, the next one
     * into <code>b[1]</code>, and so on. The number of bytes read is, at most,
     * equal to the length of <code>b</code>. Let <i>k</i> be the number of bytes
     * actually read; these bytes will be stored in elements <code>b[0]</code>
     * through <code>b[</code><i>k</i><code>-1]</code>, leaving elements
     * <code>b[</code><i>k</i><code>]</code> through <code>b[b.length-1]</code>
     * unaffected.（读取的第一个字节将被放在b[0],下一个字节将被放在b[1],以此类推。读取字节的长度，至多等于数组b的长度。如果k是读取字节的长度，
     * 那么这些字节将保存在数据b的0到k-1处，数组b的k到b.length-1处不受影响。）
     *
     * <p>
     * The <code>read(b)</code> method for class <code>InputStream</code> has the
     * same effect as:
     * 
     * <pre>
     * <code> read(b, 0, b.length) </code>
     * </pre>
     *
     * @param b the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or <code>-1</code> if
     *         there is no more data because the end of the stream has been reached.
     * @exception IOException          If the first byte cannot be read for any
     *                                 reason other than the end of the file, if the
     *                                 input stream has been closed, or if some
     *                                 other I/O error occurs.
     * @exception NullPointerException if <code>b</code> is <code>null</code>.
     * @see java.io.InputStream#read(byte[], int, int)
     */
    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * Reads up to <code>len</code> bytes of data from the input stream into an
     * array of bytes. An attempt is made to read as many as <code>len</code> bytes,
     * but a smaller number may be read. The number of bytes actually read is
     * returned as an integer.
     * （从输入流中读取指定长度len的字节数据到字节数组中。尝试尽量读取长度len的字节数据，但是有可能比len少。将返回实际读取的字节长度。）
     * <p>
     * This method blocks until input data is available, end of file is detected, or
     * an exception is thrown. （ 当输入数据可用时， 这个方法将阻塞， 直到文件末尾，或者抛出异常。）
     * <p>
     * If <code>len</code> is zero, then no bytes are read and <code>0</code> is
     * returned; otherwise, there is an attempt to read at least one byte. If no
     * byte is available because the stream is at end of file, the value
     * <code>-1</code> is returned; otherwise, at least one byte is read and stored
     * into <code>b</code>.
     * （如果数组b的长度为0，则不会读取任何字节，并返回0，否则将尝试读取至少一个字节的数据。如果因为到达文件末尾没有字节可以读取的时候，就会返回-1，否则，
     * 至少会读取一个字节，并保存到数据b）
     * <p>
     * The first byte read is stored into element <code>b[off]</code>, the next one
     * into <code>b[off+1]</code>, and so on. The number of bytes read is, at most,
     * equal to <code>len</code>. Let <i>k</i> be the number of bytes actually read;
     * these bytes will be stored in elements <code>b[off]</code> through
     * <code>b[off+</code><i>k</i><code>-1]</code>, leaving elements
     * <code>b[off+</code><i>k</i><code>]</code> through <code>b[off+len-1]</code>
     * unaffected.
     * （读取的第一个字节将被放在b[0],下一个字节将被放在b[1],以此类推。读取字节的长度，至多等于数组b的长度。如果k是读取字节的长度，
     * 那么这些字节将保存在数据b的0到k-1处，数组b的k到b.length-1处不受影响。）
     * <p>
     * In every case, elements <code>b[0]</code> through <code>b[off]</code> and
     * elements <code>b[off+len]</code> through <code>b[b.length-1]</code> are
     * unaffected. （在任何场景下，b[0]到b[off]和b[off+len]到b[b.length-1]将不受影响。）
     * <p>
     * The <code>read(b,</code> <code>off,</code> <code>len)</code> method for class
     * <code>InputStream</code> simply calls the method <code>read()</code>
     * repeatedly. If the first such call results in an <code>IOException</code>,
     * that exception is returned from the call to the <code>read(b,</code>
     * <code>off,</code> <code>len)</code> method. If any subsequent call to
     * <code>read()</code> results in a <code>IOException</code>, the exception is
     * caught and treated as if it were end of file; the bytes read up to that point
     * are stored into <code>b</code> and the number of bytes read before the
     * exception occurred is returned. The default implementation of this method
     * blocks until the requested amount of input data <code>len</code> has been
     * read, end of file is detected, or an exception is thrown. Subclasses are
     * encouraged to provide a more efficient implementation of this method.
     * （read(boff,len)这个方法只是反复调用read()方法。如果第一次调用read()方法的时候抛出IO异常，那么read(boff,len)就会直接返回这个异常。
     * 如果已经有字节被读取之后再调用read()方法抛出异常的话，这个异常将被捕获。并且将会返回当前已读取的字节长度。）
     * 
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array <code>b</code> at which the data is
     *            written.
     * @param len the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or <code>-1</code> if
     *         there is no more data because the end of the stream has been reached.
     * @exception IOException               If the first byte cannot be read for any
     *                                      reason other than end of file, or if the
     *                                      input stream has been closed, or if some
     *                                      other I/O error occurs.
     * @exception NullPointerException      If <code>b</code> is <code>null</code>.
     * @exception IndexOutOfBoundsException If <code>off</code> is negative,
     *                                      <code>len</code> is negative, or
     *                                      <code>len</code> is greater than
     *                                      <code>b.length - off</code>
     * @see java.io.InputStream#read()
     */
    public int read(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        int i = 1;
        try {
            for (; i < len; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte) c;
            }
        } catch (IOException ee) {
        }
        return i;
    }

    /**
     * Skips over and discards <code>n</code> bytes of data from this input stream.
     * The <code>skip</code> method may, for a variety of reasons, end up skipping
     * over some smaller number of bytes, possibly <code>0</code>. This may result
     * from any of a number of conditions; reaching end of file before
     * <code>n</code> bytes have been skipped is only one possibility. The actual
     * number of bytes skipped is returned. If {@code n} is negative, the
     * {@code skip} method for class {@code InputStream} always returns 0, and no
     * bytes are skipped. Subclasses may handle the negative value differently.
     * （跳过并丢弃此输入流中的n字节数据。由于各种原因，skip方法可能会跳过一些较小的字节数，可能是0。这可能是由许多条件造成的;
     * 在跳过n字节之前到达文件末尾只有一种可能性。 返回跳过的实际字节数。
     * 如果n为负数，则类InputStream的skip方法始终返回0，并跳过nobytes。 子类可以不同地处理负值。）
     * <p>
     * The <code>skip</code> method of this class creates a byte array and then
     * repeatedly reads into it until <code>n</code> bytes have been read or the end
     * of the stream has been reached. Subclasses are encouraged to provide a more
     * efficient implementation of this method. For instance, the implementation may
     * depend on the ability to seek.
     * （skip方法将创建一个字节数组，并且循环读取字节，直到读取n个字节或者到达了文件末尾。鼓励子类提供更有效的方法实现。 例如，实施可能取决于寻求的能力。
     * ）
     * 
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @exception IOException if the stream does not support seek, or if some other
     *                        I/O error occurs.
     */
    public long skip(long n) throws IOException {

        long remaining = n;
        int nr;

        if (n <= 0) {
            return 0;
        }

        int size = (int) Math.min(MAX_SKIP_BUFFER_SIZE, remaining);
        byte[] skipBuffer = new byte[size];
        while (remaining > 0) {
            nr = read(skipBuffer, 0, (int) Math.min(size, remaining));
            if (nr < 0) {
                break;
            }
            remaining -= nr;
        }

        return n - remaining;
    }

    /**
     * Returns an estimate of the number of bytes that can be read (or skipped over)
     * from this input stream without blocking by the next invocation of a method
     * for this input stream. The next invocation might be the same thread or
     * another thread. A single read or skip of this many bytes will not block, but
     * may read or skip fewer bytes. （返回可以从此输入流中读取（或跳过）的字节数的估计值，而不会被下一次调用此输入流的方法阻塞。
     * 下一次调用可能是同一个线程或另一个线程。 单个读取或跳过这么多字节不会阻塞，但可以读取或跳过更少的字节。）
     * <p>
     * Note that while some implementations of {@code InputStream} will return the
     * total number of bytes in the stream, many will not. It is never correct to
     * use the return value of this method to allocate a buffer intended to hold all
     * data in this stream.
     * （注意：一些InputStream实现会返回所有的字节长度，一些不会。使用此方法的返回值来分配用于保存此流中所有数据的缓冲区绝对不正确。）
     * <p>
     * A subclass' implementation of this method may choose to throw an
     * {@link IOException} if this input stream has been closed by invoking the
     * {@link #close()} method. （当输入流被close方法关闭的时候，子类实现的这个方法应该抛出一个IO异常）
     * <p>
     * The {@code available} method for class {@code InputStream} always returns
     * {@code 0}.
     *
     * <p>
     * This method should be overridden by subclasses.
     *
     * @return an estimate of the number of bytes that can be read (or skipped over)
     *         from this input stream without blocking or {@code 0} when it reaches
     *         the end of the input stream.
     * @exception IOException if an I/O error occurs.
     */
    public int available() throws IOException {
        return 0;
    }

    /**
     * Closes this input stream and releases any system resources associated with
     * the stream.
     *
     * <p>
     * The <code>close</code> method of <code>InputStream</code> does nothing.
     *
     * @exception IOException if an I/O error occurs.
     */
    public void close() throws IOException {
    }

    /**
     * Marks the current position in this input stream. A subsequent call to the
     * <code>reset</code> method repositions this stream at the last marked position
     * so that subsequent reads re-read the same bytes.
     * （标记输入流当前的位置。随后对reset方法的调用会在最后标记的位置重新定位此流，以便后续读取重新读取相同的字节。）
     * <p>
     * The <code>readlimit</code> arguments tells this input stream to allow that
     * many bytes to be read before the mark position gets invalidated.
     * （readlimit参数表示此输入流允许在标记位置失效之前读取许多字节。）
     * <p>
     * The general contract of <code>mark</code> is that, if the method
     * <code>markSupported</code> returns <code>true</code>, the stream somehow
     * remembers all the bytes read after the call to <code>mark</code> and stands
     * ready to supply those same bytes again if and whenever the method
     * <code>reset</code> is called. However, the stream is not required to remember
     * any data at all if more than <code>readlimit</code> bytes are read from the
     * stream before <code>reset</code> is called.
     * （mark的一般契约是，如果方法markSupported返回true，则流会以某种方式记住在调用mark之后读取的所有字节并准备好在调用方法reset时再次提供相同的字节。
     * 但是，如果在调用<code> reset </ code>之前从流中读取多个<code> readlimit </
     * code>字节，则根本不需要记住任何数据流。 ）
     * <p>
     * Marking a closed stream should not have any effect on the stream.
     *
     * <p>
     * The <code>mark</code> method of <code>InputStream</code> does nothing.
     *
     * @param readlimit the maximum limit of bytes that can be read before the mark
     *                  position becomes invalid.
     * @see java.io.InputStream#reset()
     */
    public synchronized void mark(int readlimit) {
    }

    /**
     * Repositions this stream to the position at the time the <code>mark</code>
     * method was last called on this input stream. （将输入流的位置重置在最后一次调用mark方法的地方。）
     * <p>
     * The general contract of <code>reset</code> is:
     *
     * <ul>
     * <li>If the method <code>markSupported</code> returns <code>true</code>, then:
     *
     * <ul>
     * <li>If the method <code>mark</code> has not been called since the stream was
     * created, or the number of bytes read from the stream since <code>mark</code>
     * was last called is larger than the argument to <code>mark</code> at that last
     * call, then an <code>IOException</code> might be thrown.
     *
     * <li>If such an <code>IOException</code> is not thrown, then the stream is
     * reset to a state such that all the bytes read since the most recent call to
     * <code>mark</code> (or since the start of the file, if <code>mark</code> has
     * not been called) will be resupplied to subsequent callers of the
     * <code>read</code> method, followed by any bytes that otherwise would have
     * been the next input data as of the time of the call to <code>reset</code>.
     * </ul>
     *
     * <li>If the method <code>markSupported</code> returns <code>false</code>,
     * then:
     *
     * <ul>
     * <li>The call to <code>reset</code> may throw an <code>IOException</code>.
     *
     * <li>If an <code>IOException</code> is not thrown, then the stream is reset to
     * a fixed state that depends on the particular type of the input stream and how
     * it was created. The bytes that will be supplied to subsequent callers of the
     * <code>read</code> method depend on the particular type of the input stream.
     * </ul>
     * </ul>
     *
     * <p>
     * The method <code>reset</code> for class <code>InputStream</code> does nothing
     * except throw an <code>IOException</code>.
     *
     * @exception IOException if this stream has not been marked or if the mark has
     *                        been invalidated.
     * @see java.io.InputStream#mark(int)
     * @see java.io.IOException
     */
    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    /**
     * Tests if this input stream supports the <code>mark</code> and
     * <code>reset</code> methods. Whether or not <code>mark</code> and
     * <code>reset</code> are supported is an invariant property of a particular
     * input stream instance. The <code>markSupported</code> method of
     * <code>InputStream</code> returns <code>false</code>.
     *
     * @return <code>true</code> if this stream instance supports the mark and reset
     *         methods; <code>false</code> otherwise.
     * @see java.io.InputStream#mark(int)
     * @see java.io.InputStream#reset()
     */
    public boolean markSupported() {
        return false;
    }

}
