**Collection Studying 1 ---  ArrayList**

```java
package java.util;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import sun.misc.SharedSecrets;

public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 8683452581122892189L;

private static final int DEFAULT_CAPACITY = 10;  //*默认初始空间大小10

private static final Object[] EMPTY_ELEMENTDATA = {}; //*容量为0的空数组 标记0数组

private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    //*容量为10的空数组 标记容量为10的空数组

transient Object[] elementData; // non-private to simplify nested class access
     //*动态数组，难以界定有效元素，默认设定不可序列化；数组实现，支持随机访问

private int size; //*数组实际保存元素大小

//*指定容量构造函数
public ArrayList(int initialCapacity) { //*初始化数组和容量
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) { //*容量为0，则指向默认容量0空数组
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    }
}

//*无参构造函数，将数组标记到10容量的空数组，添加第一个元素后，开容量10的数组
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA; 
    //*使用无参初始化，则默认容量为10,当前空数组指向默认容量10的空数组(相当于标记)
}

//*将集合类型实例初始化ArrayList,注意保持toArray()返回类型控制为object
public ArrayList(Collection<? extends E> c) {
    elementData = c.toArray(); //*指向初始化数组
    if ((size = elementData.length) != 0) {
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        //*集合类toArray的返回类型不一定是Object(例如重载toArray返回String)，
        //如果将非object类型赋值给数组，则数组实际类型变化，就不能在赋值object类型对象
        //因此，需要判断返回类型，非object类型则将其转为object类型
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, size, Object[].class);
    } else {
        // replace with empty array.
        this.elementData = EMPTY_ELEMENTDATA;
    }
}


/***   Test the error of 6260652
/*声明Object类型数组，存入String类型值*/
		String s[] = {"1","2"};
		Object tmp[] = new Object[2];
		tmp[0] = s[0];
		tmp[1] = s[1];
		
		/*用Object类型数组变量赋值*/
		Object o[] = tmp;
		System.out.println(o.getClass());   //class [Ljava.lang.Object; //执行过程中数组变量o解析为Object类型
		System.out.println(o[1].getClass()); //class java.lang.String
		o[1] = new Object();
		System.out.println(o[1].getClass()); //class java.lang.Object
		System.out.println();
		
		/*直接用子类型Sting数组变量赋值*/
		o = s;
		System.out.println(o.getClass()); //class [Ljava.lang.String; //执行过程中数组变量o解析为String类型
		System.out.println(o[0].getClass()); //class java.lang.String
		o[1] = new Object();
		System.out.println(o[1].getClass()); //Exception in thread "main"  java.lang.ArrayStoreException: java.lang.Object//此时数组o存储的是String类型，因此赋值Object报错
***/
    

public void trimToSize() { //将动态数组实际元素拷贝到新数组，以缩减容量到实际大小
    modCount++;
    if (size < elementData.length) {
        elementData = (size == 0)
          ? EMPTY_ELEMENTDATA
          : Arrays.copyOf(elementData, size); //数组拷贝
    }
}


    
/*先判断当前数组是否容量为10(是否为标记数组)，判断容量是否足够，不够则以1.5倍数扩容，还不够则直接扩充到minCapacity,minCapacity太大则以扩展到数组最大容量MAX_ARRAY_SIZE,还不够则扩大到最大整数MAX_VALUE*/
    
public void ensureCapacity(int minCapacity) {
    int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
        // any size if not default element table
        ? 0
        // larger than default for default empty table. It's already
        // supposed to be at default size.
        : DEFAULT_CAPACITY;

    if (minCapacity > minExpand) {
        ensureExplicitCapacity(minCapacity);
    }
}

//*计算数组所需最小容量    
private static int calculateCapacity(Object[] elementData, int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        return Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    return minCapacity;
}

//*确保当前容量大于所需容量
private void ensureCapacityInternal(int minCapacity) {
    ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}


private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

//*扩容
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1); //扩容为原容量的1.5倍
    if (newCapacity - minCapacity < 0) //不够，则直接用当前扩容为需求的容量
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)//扩容后容量超过最大值
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);//拷贝
}

private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
        MAX_ARRAY_SIZE;
}


public int size() {
    return size;
}

    
public boolean isEmpty() {
    return size == 0;
}


public boolean contains(Object o) {
    return indexOf(o) >= 0;
}

public int indexOf(Object o) {
    if (o == null) {
        for (int i = 0; i < size; i++)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i]))
                return i;
    }
    return -1;
}

public int lastIndexOf(Object o) {
    if (o == null) {
        for (int i = size-1; i >= 0; i--)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = size-1; i >= 0; i--)
            if (o.equals(elementData[i]))
                return i;
    }
    return -1;
}

    
//*Arrays.copyOf浅拷贝，新建了一个数组(变量数组，开辟了变量空间)，每个变量指向原来的引用，并没有重新开辟对象空间    
//*浅克隆,返回ArrayList的复制引用
public Object clone() {
    try {
        ArrayList<?> v = (ArrayList<?>) super.clone();
        v.elementData = Arrays.copyOf(elementData, size);//浅克隆，elementData拷贝了引用
        v.modCount = 0;
        return v;
    } catch (CloneNotSupportedException e) {
        // this shouldn't happen, since we are Cloneable
        throw new InternalError(e);
    }
}

//*返回原数组的拷贝，返回对象是Object数组
public Object[] toArray() {
    return Arrays.copyOf(elementData, size);
}


//*返回数组拷贝到指定数组，返回类型为运行时数组类型，指定数组容量不够则copyOf新建原数组大小数组，否则直接将原数组元素拷贝到指定数组，多余位置空
@SuppressWarnings("unchecked")
public <T> T[] toArray(T[] a) {
    if (a.length < size)
        // Make a new array of a's runtime type, but my contents:
        return (T[]) Arrays.copyOf(elementData, size, a.getClass());
    System.arraycopy(elementData, 0, a, 0, size);
    if (a.length > size)
        a[size] = null;
    return a;
}


@SuppressWarnings("unchecked")
E elementData(int index) {
    return (E) elementData[index];
}


public E get(int index) {
    rangeCheck(index);

    return elementData(index);
}


public E set(int index, E element) {
    rangeCheck(index);

    E oldValue = elementData(index);
    elementData[index] = element;
    return oldValue;
}

//*添加元素操作，需先判断是否需要扩容
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}


public void add(int index, E element) {
    rangeCheckForAdd(index);

    ensureCapacityInternal(size + 1);  // Increments modCount!!
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);
    elementData[index] = element;
    size++;
}

//*删除指定位置元素，并将其前面的元素前移拷贝
public E remove(int index) {
    rangeCheck(index);

    modCount++;
    E oldValue = elementData(index);

    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    elementData[--size] = null; // clear to let GC do its work

    return oldValue;
}


public boolean remove(Object o) {
    if (o == null) {
        for (int index = 0; index < size; index++)
            if (elementData[index] == null) {
                fastRemove(index);
                return true;
            }
    } else {
        for (int index = 0; index < size; index++)
            if (o.equals(elementData[index])) {
                fastRemove(index);
                return true;
            }
    }
    return false;
}

/*
 * Private remove method that skips bounds checking and does not
 * return the value removed.
 */
private void fastRemove(int index) {
    modCount++;
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    elementData[--size] = null; // clear to let GC do its work
}

/**
 * Removes all of the elements from this list.  The list will
 * be empty after this call returns.
 */
public void clear() {
    modCount++;

    // clear to let GC do its work
    for (int i = 0; i < size; i++)
        elementData[i] = null;

    size = 0;
}
    
//*自定义序列化，只序列化了实际元素部分的数组/
private void writeObject(java.io.ObjectOutputStream s)
    throws java.io.IOException{
    // Write out element count, and any hidden stuff
    int expectedModCount = modCount;
    s.defaultWriteObject(); //*序列化非trainset瞬时变量和非静态变量

    // Write out size as capacity for behavioural compatibility with clone()
    s.writeInt(size); 
    //此处的size重复写入，是为了保持版本兼容，jdk1.6此处是写入elementData.length

    // Write out all elements in the proper order.
    for (int i=0; i<size; i++) {
        s.writeObject(elementData[i]);
    }

    if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
    }
}


private void readObject(java.io.ObjectInputStream s)
    throws java.io.IOException, ClassNotFoundException {
    elementData = EMPTY_ELEMENTDATA;

    // Read in size, and any hidden stuff
    s.defaultReadObject();

    // Read in capacity
    s.readInt(); // ignored

    if (size > 0) {
        // be like clone(), allocate array based upon size not capacity
        int capacity = calculateCapacity(elementData, size);
        SharedSecrets.getJavaOISAccess().checkArray(s, Object[].class, capacity);
        ensureCapacityInternal(size);

        Object[] a = elementData;
        // Read in all elements in the proper order.
        for (int i=0; i<size; i++) {
            a[i] = s.readObject();
        }
    }
}
    
    public int size() {
        checkForComodification();
        return this.size;
    }

    public void add(int index, E e) {
        rangeCheckForAdd(index);
        checkForComodification();
        parent.add(parentOffset + index, e);
        this.modCount = parent.modCount;
        this.size++;
    }

    public E remove(int index) {
        rangeCheck(index);
        checkForComodification();
        E result = parent.remove(parentOffset + index);
        this.modCount = parent.modCount;
        this.size--;
        return result;
    }
}
```
