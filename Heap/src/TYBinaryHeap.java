import java.util.Comparator;

public class TYBinaryHeap<E> implements TYHeap<E> {

    private E[] elements;
    private int size;
    private Comparator<E> comparator;
    private static final int DEFAULT_CAPCITY = 10;

    public TYBinaryHeap(Comparator<E> comparator) {
        this.comparator = comparator;
        this.elements = (E[])new Object[DEFAULT_CAPCITY];
    }

    public TYBinaryHeap() {
        this(null);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public void add(E element) {
        elementNotNullCheck(element);
        // 扩容
        ensureCapacity(size + 1);
        // 添加元素到最后面
        elements[size++] = element;
        // 上滤操作
        siftUp(size - 1);
    }

    @Override
    public E get() {
        return null;
    }

    @Override
    public E remove() {
        emptyCheck();

        // 取出根节点
        E root = elements[0];
        // 最后一个节点覆盖到根节点
        elements[0] = elements[size - 1];
        // 最后一个节点清空
        elements[size - 1] = null;
        size--;
        // 下滤
        siftDown(0);
        return root;

    }

    @Override
    public E replace(E element) {
        elementNotNullCheck(element);

        E root = null;
        if (size == 0) {
            // 堆顶元素
            elements[0] = element;
            size++;
        } else {
            // 堆顶有元素的情况
            root = elements[0];
            elements[0] = element;
            siftDown(0);
        }
        return root;
    }

    private int compare(E e1, E e2) {
        return comparator != null ? comparator.compare(e1,e2) : ((Comparable<E>)e1).compareTo(e2);
    }

    private void emptyCheck() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Heap is empty");
        }
    }

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
    }

    /**
     * 数组扩容
     * @param capacity 容量
     */
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity) return;

        // 新容量扩容为旧容量的 1.5 倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    private void siftUp(int index) {
//        E e = elements[index];
//        while (index > 0) {
//            // 获取父节点索引, 向下取整
//            int pIndex = (index - 1) >> 1;
//            // 父节点
//            E p = elements[pIndex];
//            if (compare(e, p) <= 0) return;
//
//            // 交换 index, pIndex 位置的内容
//            E tmp = elements[index];
//            elements[index] = elements[pIndex];
//            elements[pIndex] = tmp;
//
//            // 重新赋值 index
//            index = pIndex;
//        }

        // 优化代码

        // 取出新添加元素
        E e = elements[index];
        while (index > 0) {
            // 获取父节点索引
            int pIndex = (index-1) >> 1;
            // 取出父节点元素
            E p = elements[pIndex];
            // 新添加元素如果小于父节点元素, 则退出循环
            if (compare(e, p) <= 0) break;

            // 将父节点元素存错到 index 位置
            elements[index] = p;
            // 重新赋值 index
            index = pIndex;
        }
        // 最后再赋值新添加元素, 此时 index 是找到的最终节点了
        elements[index] = e;

    }

    private void siftDown(int index) {
        E element = elements[index];

        /**
         * 完全二叉树, 假设叶子节点个数为 n0, 度为 1 的节点个数为 n1, 度为 2 的节点个数为 n2
         * 叶子节点个数 n0 = floor((n+1)/2)
         * 非叶子节点个数 n1+n2 = floor(n/2)
         */

        int half = size >> 1;

        // index < 第一个叶子节点的索引, 必须保证 index 位置是非叶子节点
        while (index < half) {

            /**
             * index 节点有 2 种情况
             * 1. 只有左子节点
             * 2. 同时有左右子节点
             */

            // 默认为左子节点跟它进行比较
            int childIndex = (index << 1) + 1;
            E child = elements[childIndex];

            // 右子节点
            int rightIndex = childIndex + 1;

            // 选出左右子节点最大的那个
            if (rightIndex < size && compare(elements[rightIndex], child) > 0) {
                childIndex = rightIndex;
                child = elements[rightIndex];
            }

            if (compare(element, child) >= 0) break;

            // 将子节点存放到 index 位置
            elements[index] = child;
            // 重新设置 index
            index = childIndex;
        }
        elements[index] = element;
    }

}
