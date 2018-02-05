package org.stathry.commons.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 可计算数据列表总字节数(数据项E必须可以获取字节大小，暂仅支持String|File)
 * @author dongdaiming
 */
public class DataList<E> extends ArrayList<E> implements ByteList<E> {

    private static final long serialVersionUID = -6981156571990363347L;

    private long dataSize;

    public DataList() {
        super();
    }
    
    public DataList(int initialCapacity) {
        super(initialCapacity);
    }

    public DataList(Collection<E> c) {
        super(c);
    }
    
    @Override
    public ByteList<E> subDataList(int fromIndex, int toIndex) {
        DataList<E> list = new DataList<>(toIndex - fromIndex);
        for(int i = fromIndex; i < toIndex; i++) {
            list.addData(get(i));
        }
        return list;
    }
    
    @Override
    public boolean addData(E e) {
        if(e instanceof String) {
            String s = (String) e;
            dataSize += s.getBytes().length;
        } else if(e instanceof File) {
            File f = (File) e;
            dataSize += f.length();
        } else {
            throw new IllegalArgumentException(e == null ? "null" : e.getClass().getName());
        }
        return add(e);
    }
    
    @Override
    public long dataSize() {
        return dataSize;
    }

}
