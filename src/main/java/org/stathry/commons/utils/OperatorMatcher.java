package org.stathry.commons.utils;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.stathry.commons.dao.RedisManager;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 运营商匹配
 *
 * @author dongdaiming
 * @date 2018/5/10
 */
@Component
public class OperatorMatcher implements OperatorMatchable, InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(OperatorMatcher.class);

    @Autowired
    private RedisManager redisManager;

    @Value("${app1.operatorSegment.cacheSeconds}")
    private long operatorCacheTime;

    /**
     * 运营商号段映射redis-key
     */
    public static final String KEY_OPT_SEG_MAP = "APP1:OPT:SEG_MAP_FLAG";

    private Map<String, Map<String, OperatorSegmentInfo>> orgOptMap = new HashMap<>(2);

    /**
     * 匹配运营商
     *
     * @param phone
     * @param org   数据方标识
     * @return 运营商标识(M, T, U)
     */
    @Override
    public String decideOpt(String phone, String org) {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(org) || phone.length() != 11 || !StringUtils.isNumeric(phone)) {
            return "";
        }
        if (StringUtils.isBlank(redisManager.getString(KEY_OPT_SEG_MAP))) {
            refreshOrgOptMap(false);
        }
        Map<String, OperatorSegmentInfo> opts = orgOptMap.get(org);
        if (opts == null || opts.isEmpty()) {
            return "";
        }
        OperatorSegmentInfo optSegInfo;
        Set<String> segments;
        String seg;
        for (Map.Entry<String, OperatorSegmentInfo> e : opts.entrySet()) {
            optSegInfo = e.getValue();
            segments = optSegInfo.getSegments();
            for (Integer len : optSegInfo.getLens()) {
                seg = phone.substring(0, len);
                if (segments.contains(seg)) {
                    LOGGER.info("phone {}, match to operator {}.", phone, optSegInfo.getOpt());
                    return optSegInfo.getOpt();
                }
            }
        }
        return "";
    }


    /**
     * 是否匹配指定运营商
     *
     * @param phone
     * @param org   数据方标识
     * @param opt   运营商标识(如M|T|U)
     * @return 是否匹配指定运营商
     */
    @Override
    public boolean isMatchOpt(String phone, String org, String opt) {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(org) || StringUtils.isBlank(opt) || phone.length() != 11 || !StringUtils.isNumeric(phone)) {
            return false;
        }
        if (StringUtils.isBlank(redisManager.getString(KEY_OPT_SEG_MAP))) {
            refreshOrgOptMap(false);
        }
        Map<String, OperatorSegmentInfo> opts = orgOptMap.get(org);
        if (opts != null && !opts.isEmpty() && isMatchOpt0(phone, opts)) {
            return true;
        }

        for (Map.Entry<String, Map<String, OperatorSegmentInfo>> orgMaps : orgOptMap.entrySet()) {
            opts = orgMaps.getValue();
            if (orgMaps.getKey().equals(org)) {
                continue;
            }
            if (isMatchOpt0(phone, opts)) return true;
        }
        return false;
    }

    private boolean isMatchOpt0(String phone, Map<String, OperatorSegmentInfo> opts) {
        OperatorSegmentInfo optSegInfo;
        Set<String> segments;
        String seg;
        for (Map.Entry<String, OperatorSegmentInfo> e : opts.entrySet()) {
            optSegInfo = e.getValue();
            segments = optSegInfo.getSegments();
            for (Integer len : optSegInfo.getLens()) {
                seg = phone.substring(0, len);
                if (segments.contains(seg)) {
                    LOGGER.info("phone {} matched operator {}.", phone, optSegInfo.getOpt());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 刷新号段映射
     *
     * @param forceRefresh 是否强制更新(首次初始化强制更新号段配置)
     */
    private synchronized void refreshOrgOptMap(boolean forceRefresh) {
        List<OptSegMap> list;
        if (!forceRefresh && StringUtils.isNotBlank(redisManager.getString(KEY_OPT_SEG_MAP))) {
            return;
        }
//        list = dao.findAll();
        list = null;
        Assert.notEmpty(list, "operator segment conf error.");
        LOGGER.info("list operator phone segment from conf, size {}.", list.size());
        orgOptMap.clear();
        initOrgOptMap(orgOptMap, list);
        redisManager.set(KEY_OPT_SEG_MAP, "1", operatorCacheTime);
    }

    /**
     * 将list中的配置初始化到orgOptMap
     *
     * @param orgOptMap
     * @param list
     */
    private void initOrgOptMap(Map<String, Map<String, OperatorSegmentInfo>> orgOptMap, List<OptSegMap> list) {
        OperatorSegmentInfo segInfo;
        Map<String, OperatorSegmentInfo> orgOpt;
        String org;
        String opt;
        String seg;
        for (OptSegMap osm : list) {
            org = osm.getOrgCode().trim();
            opt = osm.getOperator().trim();
            seg = osm.getPhoneSegment().trim();
            orgOpt = orgOptMap.get(org);
            if (orgOpt == null) {
                orgOpt = new HashMap<>();
                orgOptMap.put(org, orgOpt);
            }

            segInfo = orgOpt.get(opt);
            if (segInfo == null) {
                segInfo = new OperatorSegmentInfo();
                segInfo.setSegments(new HashSet<String>());
                segInfo.setLens(new HashSet<Integer>());
                segInfo.setOpt(opt);
                orgOpt.put(opt, segInfo);
            }
            segInfo.getSegments().add(seg);

            segInfo.getLens().add(seg.length());
        }
        LOGGER.info("refresh operator phone segment map success.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        refreshOrgOptMap(true);
    }


    private static class OperatorSegmentInfo {

        private String opt;
        private Set<Integer> lens;
        private Set<String> segments;

        @Override
        public String toString() {
            return "OperatorSegmentInfo{" +
                    "opt='" + opt + '\'' +
                    ", lens=" + lens +
                    ", segments=" + segments +
                    '}';
        }

        public String getOpt() {
            return opt;
        }

        public void setOpt(String opt) {
            this.opt = opt;
        }

        public Set<String> getSegments() {
            return segments;
        }

        public void setSegments(Set<String> segments) {
            this.segments = segments;
        }

        public Set<Integer> getLens() {
            return lens;
        }

        public void setLens(Set<Integer> lens) {
            this.lens = lens;
        }
    }


    /**
     * 运营商手机号段映射
     *
     * @author Auto-generated by FreeMarker
     * @date 2018-11-23 10:49
     */

    private static class OptSegMap {

        private Long id;
        /**
         * 机构标识
         */
        private String orgCode;
        /**
         * 手机号段
         */
        private String phoneSegment;
        /**
         * 归属运营商(M:移动, T:电信, U:联通)
         */
        private String operator;
        /**
         * 创建时间
         */
        private Date createTime;
        /**
         * 更新时间
         */
        private Date updateTime;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getPhoneSegment() {
            return phoneSegment;
        }

        public void setPhoneSegment(String phoneSegment) {
            this.phoneSegment = phoneSegment;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public String toString() {
            return "OptSegMap[" + "id = " + id
                    + ", orgCode = " + orgCode
                    + ", phoneSegment = " + phoneSegment
                    + ", operator = " + operator
                    + ", createTime = " + createTime
                    + ", updateTime = " + updateTime
                    + "]";
        }
    }
}
