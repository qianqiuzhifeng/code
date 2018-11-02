/*
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

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent.atomic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

/**
 * A reflection-based utility that enables atomic updates to designated
 * {@code volatile} reference fields of designated
 * classes.（基于反射的实用程序，可以对指定类的指定volatile引用字段进行原子更新。） This class is designed for
 * use in atomic data structures in which several reference fields of the same
 * node are independently subject to atomic
 * updates.（此类设计用于原子数据结构，其中同一节点的多个引用字段独立地受原子更新的影响。） For example, a tree node
 * might be declared as
 *
 * <pre>
 * {
 *     &#64;code
 *     class Node {
 *         private volatile Node left, right;
 *
 *         private static final AtomicReferenceFieldUpdater<Node, Node> leftUpdater = AtomicReferenceFieldUpdater
 *                 .newUpdater(Node.class, Node.class, "left");
 *         private static AtomicReferenceFieldUpdater<Node, Node> rightUpdater = AtomicReferenceFieldUpdater
 *                 .newUpdater(Node.class, Node.class, "right");
 *
 *         Node getLeft() {
 *             return left;
 *         }
 * 
 *         boolean compareAndSetLeft(Node expect, Node update) {
 *             return leftUpdater.compareAndSet(this, expect, update);
 *         }
 *         // ... and so on
 *     }
 * }
 * </pre>
 *
 * <p>
 * Note that the guarantees of the {@code compareAndSet} method in this class
 * are weaker than in other atomic classes.（请注意，此类中compareAndSet方法的保证比其他原子类弱。）
 * Because this class cannot ensure that all uses of the field are appropriate
 * for purposes of atomic access, it can guarantee atomicity only with respect
 * to other invocations of {@code compareAndSet} and {@code set} on the same
 * updater.（因为这个类不能保证属性调用的原子性，它只能保证同一更新程序中compareAndSet和set的其他调用的原子性）
 *
 * @since 1.5
 * @author Doug Lea
 * @param <T> The type of the object holding the updatable field
 * @param <V> The type of the field
 */
public abstract class AtomicReferenceFieldUpdater<T, V> {

    /**
     * Creates and returns an updater for objects with the given
     * field.（基于给定属性创建并返回一个updater） The Class arguments are needed to check that
     * reflective types and generic types match.（需要Class参数来检查反射类型和泛型类型是否匹配）
     *
     * @param tclass    the class of the objects holding the field
     * @param vclass    the class of the field
     * @param fieldName the name of the field to be updated
     * @param           <U> the type of instances of tclass
     * @param           <W> the type of instances of vclass
     * @return the updater
     * @throws ClassCastException       if the field is of the wrong type
     * @throws IllegalArgumentException if the field is not volatile
     * @throws RuntimeException         with a nested reflection-based exception if
     *                                  the class does not hold field or is the
     *                                  wrong type, or the field is inaccessible to
     *                                  the caller according to Java language access
     *                                  control
     */
    @CallerSensitive
    public static <U, W> AtomicReferenceFieldUpdater<U, W> newUpdater(Class<U> tclass, Class<W> vclass,
            String fieldName) {
        return new AtomicReferenceFieldUpdaterImpl<U, W>(tclass, vclass, fieldName, Reflection.getCallerClass());
    }

    /**
     * Protected do-nothing constructor for use by subclasses.
     */
    protected AtomicReferenceFieldUpdater() {
    }

    /**
     * Atomically sets the field of the given object managed by this updater to the
     * given updated value if the current value {@code ==} the expected
     * value.（当前值和预期值相等的时候，由updater原子性的设置属性值） This method is guaranteed to be atomic
     * with respect to other calls to {@code compareAndSet} and {@code set}, but not
     * necessarily with respect to other changes in the
     * field.（这个方法保证在其他compareAndSet和set调用时的原子性，但不一定与该属性的其他更改相关）
     *
     * @param obj    An object whose field to conditionally set
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful
     */
    public abstract boolean compareAndSet(T obj, V expect, V update);

    /**
     * Atomically sets the field of the given object managed by this updater to the
     * given updated value if the current value {@code ==} the expected value. This
     * method is guaranteed to be atomic with respect to other calls to
     * {@code compareAndSet} and {@code set}, but not necessarily with respect to
     * other changes in the field.
     *
     * <p>
     * <a href="package-summary.html#weakCompareAndSet">May fail spuriously and does
     * not provide ordering guarantees</a>, so is only rarely an appropriate
     * alternative to {@code compareAndSet}.
     *
     * @param obj    An object whose field to conditionally set
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful
     */
    public abstract boolean weakCompareAndSet(T obj, V expect, V update);

    /**
     * Sets the field of the given object managed by this updater to the given
     * updated value. This operation is guaranteed to act as a volatile store with
     * respect to subsequent invocations of
     * {@code compareAndSet}.（保证此操作在compareAndSet的后续调用中充当volatile存储。）
     *
     * @param obj      An object whose field to set
     * @param newValue the new value
     */
    public abstract void set(T obj, V newValue);

    /**
     * Eventually sets the field of the given object managed by this updater to the
     * given updated value.（最终将此更新程序管理的给定对象的字段设置为给定的更新值。）
     *
     * @param obj      An object whose field to set
     * @param newValue the new value
     * @since 1.6
     */
    public abstract void lazySet(T obj, V newValue);

    /**
     * Gets the current value held in the field of the given object managed by this
     * updater.
     *
     * @param obj An object whose field to get
     * @return the current value
     */
    public abstract V get(T obj);

    /**
     * Atomically sets the field of the given object managed by this updater to the
     * given value and returns the old value.
     *
     * @param obj      An object whose field to get and set
     * @param newValue the new value
     * @return the previous value
     */
    public V getAndSet(T obj, V newValue) {
        V prev;
        do {
            prev = get(obj);
        } while (!compareAndSet(obj, prev, newValue));
        return prev;
    }

    /**
     * Atomically updates the field of the given object managed by this updater with
     * the results of applying the given function, returning the previous
     * value.（基于给定方法的返回结果原子性的更新给定对象的属性） The function should be side-effect-free,
     * since it may be re-applied when attempted updates fail due to contention
     * among threads.（该函数应该是无副作用的，因为当尝试的更新由于线程之间的争用而失败时，它可能会被重新应用。）
     *
     * @param obj            An object whose field to get and set
     * @param updateFunction a side-effect-free function
     * @return the previous value
     * @since 1.8
     */
    public final V getAndUpdate(T obj, UnaryOperator<V> updateFunction) {
        V prev, next;
        do {
            prev = get(obj);
            next = updateFunction.apply(prev);
        } while (!compareAndSet(obj, prev, next));
        return prev;
    }

    /**
     * Atomically updates the field of the given object managed by this updater with
     * the results of applying the given function, returning the updated value. The
     * function should be side-effect-free, since it may be re-applied when
     * attempted updates fail due to contention among threads.
     *
     * @param obj            An object whose field to get and set
     * @param updateFunction a side-effect-free function
     * @return the updated value
     * @since 1.8
     */
    public final V updateAndGet(T obj, UnaryOperator<V> updateFunction) {
        V prev, next;
        do {
            prev = get(obj);
            next = updateFunction.apply(prev);
        } while (!compareAndSet(obj, prev, next));
        return next;
    }

    /**
     * Atomically updates the field of the given object managed by this updater with
     * the results of applying the given function to the current and given values,
     * returning the previous value. The function should be side-effect-free, since
     * it may be re-applied when attempted updates fail due to contention among
     * threads. The function is applied with the current value as its first
     * argument, and the given update as the second
     * argument.（这个方法的第一个参数是属性当前值，第二个参数是变更后的属性值）
     *
     * @param obj                 An object whose field to get and set
     * @param x                   the update value
     * @param accumulatorFunction a side-effect-free function of two arguments
     * @return the previous value
     * @since 1.8
     */
    public final V getAndAccumulate(T obj, V x, BinaryOperator<V> accumulatorFunction) {
        V prev, next;
        do {
            prev = get(obj);
            next = accumulatorFunction.apply(prev, x);
        } while (!compareAndSet(obj, prev, next));
        return prev;
    }

    /**
     * Atomically updates the field of the given object managed by this updater with
     * the results of applying the given function to the current and given values,
     * returning the updated value. The function should be side-effect-free, since
     * it may be re-applied when attempted updates fail due to contention among
     * threads. The function is applied with the current value as its first
     * argument, and the given update as the second argument.
     *
     * @param obj                 An object whose field to get and set
     * @param x                   the update value
     * @param accumulatorFunction a side-effect-free function of two arguments
     * @return the updated value
     * @since 1.8
     */
    public final V accumulateAndGet(T obj, V x, BinaryOperator<V> accumulatorFunction) {
        V prev, next;
        do {
            prev = get(obj);
            next = accumulatorFunction.apply(prev, x);
        } while (!compareAndSet(obj, prev, next));
        return next;
    }

    private static final class AtomicReferenceFieldUpdaterImpl<T, V> extends AtomicReferenceFieldUpdater<T, V> {
        private static final sun.misc.Unsafe U = sun.misc.Unsafe.getUnsafe();
        private final long offset;
        /**
         * if field is protected, the subclass constructing updater, else the same as
         * tclass
         */
        private final Class<?> cclass;
        /** class holding the field */
        private final Class<T> tclass;
        /** field value type */
        private final Class<V> vclass;

        /*
         * Internal type checks within all update methods contain internal inlined
         * optimizations checking for the common cases where the class is final (in
         * which case a simple getClass comparison suffices) or is of type Object (in
         * which case no check is needed because all objects are instances of Object).
         * The Object case is handled simply by setting vclass to null in constructor.
         * The targetCheck and updateCheck methods are invoked when these faster
         * screenings fail.
         */

        AtomicReferenceFieldUpdaterImpl(final Class<T> tclass, final Class<V> vclass, final String fieldName,
                final Class<?> caller) {
            final Field field;
            final Class<?> fieldClass;
            final int modifiers;
            try {
                field = AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() {
                    public Field run() throws NoSuchFieldException {
                        return tclass.getDeclaredField(fieldName);
                    }
                });
                modifiers = field.getModifiers();
                sun.reflect.misc.ReflectUtil.ensureMemberAccess(caller, tclass, null, modifiers);
                ClassLoader cl = tclass.getClassLoader();
                ClassLoader ccl = caller.getClassLoader();
                if ((ccl != null) && (ccl != cl) && ((cl == null) || !isAncestor(cl, ccl))) {
                    sun.reflect.misc.ReflectUtil.checkPackageAccess(tclass);
                }
                fieldClass = field.getType();
            } catch (PrivilegedActionException pae) {
                throw new RuntimeException(pae.getException());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            if (vclass != fieldClass)
                throw new ClassCastException();
            if (vclass.isPrimitive())
                throw new IllegalArgumentException("Must be reference type");

            if (!Modifier.isVolatile(modifiers))
                throw new IllegalArgumentException("Must be volatile type");

            // Access to protected field members is restricted to receivers only
            // of the accessing class, or one of its subclasses, and the
            // accessing class must in turn be a subclass (or package sibling)
            // of the protected member's defining class.
            // If the updater refers to a protected field of a declaring class
            // outside the current package, the receiver argument will be
            // narrowed to the type of the accessing class.
            this.cclass = (Modifier.isProtected(modifiers) && tclass.isAssignableFrom(caller)
                    && !isSamePackage(tclass, caller)) ? caller : tclass;
            this.tclass = tclass;
            this.vclass = vclass;
            this.offset = U.objectFieldOffset(field);
        }

        /**
         * Returns true if the second classloader can be found in the first
         * classloader's delegation chain. Equivalent to the inaccessible:
         * first.isAncestor(second).
         */
        private static boolean isAncestor(ClassLoader first, ClassLoader second) {
            ClassLoader acl = first;
            do {
                acl = acl.getParent();
                if (second == acl) {
                    return true;
                }
            } while (acl != null);
            return false;
        }

        /**
         * Returns true if the two classes have the same class loader and package
         * qualifier
         */
        private static boolean isSamePackage(Class<?> class1, Class<?> class2) {
            return class1.getClassLoader() == class2.getClassLoader()
                    && Objects.equals(getPackageName(class1), getPackageName(class2));
        }

        private static String getPackageName(Class<?> cls) {
            String cn = cls.getName();
            int dot = cn.lastIndexOf('.');
            return (dot != -1) ? cn.substring(0, dot) : "";
        }

        /**
         * Checks that target argument is instance of cclass. On failure, throws cause.
         */
        private final void accessCheck(T obj) {
            if (!cclass.isInstance(obj))
                throwAccessCheckException(obj);
        }

        /**
         * Throws access exception if accessCheck failed due to protected access, else
         * ClassCastException.
         */
        private final void throwAccessCheckException(T obj) {
            if (cclass == tclass)
                throw new ClassCastException();
            else
                throw new RuntimeException(new IllegalAccessException(
                        "Class " + cclass.getName() + " can not access a protected member of class " + tclass.getName()
                                + " using an instance of " + obj.getClass().getName()));
        }

        private final void valueCheck(V v) {
            if (v != null && !(vclass.isInstance(v)))
                throwCCE();
        }

        static void throwCCE() {
            throw new ClassCastException();
        }

        public final boolean compareAndSet(T obj, V expect, V update) {
            accessCheck(obj);
            valueCheck(update);
            return U.compareAndSwapObject(obj, offset, expect, update);
        }

        public final boolean weakCompareAndSet(T obj, V expect, V update) {
            // same implementation as strong form for now
            accessCheck(obj);
            valueCheck(update);
            return U.compareAndSwapObject(obj, offset, expect, update);
        }

        public final void set(T obj, V newValue) {
            accessCheck(obj);
            valueCheck(newValue);
            U.putObjectVolatile(obj, offset, newValue);
        }

        public final void lazySet(T obj, V newValue) {
            accessCheck(obj);
            valueCheck(newValue);
            U.putOrderedObject(obj, offset, newValue);
        }

        @SuppressWarnings("unchecked")
        public final V get(T obj) {
            accessCheck(obj);
            return (V) U.getObjectVolatile(obj, offset);
        }

        @SuppressWarnings("unchecked")
        public final V getAndSet(T obj, V newValue) {
            accessCheck(obj);
            valueCheck(newValue);
            return (V) U.getAndSetObject(obj, offset, newValue);
        }
    }
}
