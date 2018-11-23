package org.stathry.commons.data;

import java.util.List;

/**
 * 可计算数据列表总字节数
 *
 * @author dongdaiming
 */
public interface ByteList<E> extends List<E> {

    /**
     * 添加数据并计算总字节数
     *
     * @param e
     * @return
     */
    boolean addData(E e);

    /**
     * 根据索引范围获取数据列表子集
     *
     * @param fromIndex
     * @param toIndex
     * @return
     */
    ByteList<E> subDataList(int fromIndex, int toIndex);

    /**
     * 获取总字节数(bytes)
     *
     * @return
     */
    long dataSize();

}
