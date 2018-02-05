package org.stathry.commons.concurrent;

import java.util.concurrent.RecursiveAction;

import org.stathry.commons.data.ByteList;

/**
 * 递归拆分数据列表以执行指定任务
 * 
 * @author dongdaiming
 */
public class ListSplitAction<E> extends RecursiveAction {

    private static final long serialVersionUID = -8903627052794658254L;

    private ByteList<E> list;

    private long threshodDataSize;

    public ListSplitAction(ByteList<E> list, long threshodDataSize) {
        this.list = list;
        this.threshodDataSize = threshodDataSize;
    }
    
    /**
     * 数据列表总字节数小于阈值时执行
     */
    public void doAction() {
        for (E e : list) {
            System.out.println(e);
        }
//        System.out.println(list.dataSize());
    }

    /**
     * 根据数据列表总字节数和阈值递归拆分数据列表
     */
    @Override
    protected void compute() {
        // 数据列表总字节数小于阈值则执行指定操作
        if (list.dataSize() <= threshodDataSize) {
            doAction();
        } 
        // 否则递归拆分数据列表
        else {
            int size = list.size();
            int middle = size >> 1;
            ByteList<E> lDatas = list.subDataList(0, middle);
            ByteList<E> rDatas = list.subDataList(middle, size);
            ListSplitAction<E> lact = new ListSplitAction<>(lDatas, threshodDataSize);
            ListSplitAction<E> ract = new ListSplitAction<>(rDatas, threshodDataSize);
            lact.fork();
            ract.fork();
        }
    }

    public ByteList<E> getList() {
        return list;
    }

    public long getThreshodDataSize() {
        return threshodDataSize;
    }

}
