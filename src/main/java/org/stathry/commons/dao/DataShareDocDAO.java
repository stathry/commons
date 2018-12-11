package org.stathry.commons.dao;

import org.stathry.commons.mapper.GenericMapper;
import org.stathry.commons.model.DataShareDoc;

import java.util.Date;
import java.util.List;

/**
 * 芝麻信用反馈文件访问
 *
 * @author dongdaiming
 */
public interface DataShareDocDAO extends GenericMapper<DataShareDoc, Integer> {

    /**
     * 查询待处理文件列表
     *
     * @param dataGroup
     * @param status
     * @param pageSize
     * @param maxFailCount
     * @return 待处理文件列表
     */
    List<DataShareDoc> listDoc(String dataGroup, Integer status, Integer pageSize, Integer maxFailCount);

    /**
     * 查询待处理文件列表
     *
     * @param dataGroup
     * @param status
     * @param bizDate
     * @param pageSize
     * @param maxFailCount
     * @return 待处理文件列表
     */
    List<DataShareDoc> listDoc(String dataGroup, Integer status, Date bizDate, Integer pageSize, Integer maxFailCount);

    /**
     * 根据id更新文件状态
     *
     * @param id
     * @param sourceStatus
     * @param targetStatus
     * @param failCount
     * @return 影响记录数
     */
    Integer updateStatusById(Long id, Integer sourceStatus, Integer targetStatus, Integer failCount);


}
