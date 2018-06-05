package org.stathry.commons.concurrent;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import org.stathry.commons.utils.DatetimeUtils;

/**
 * 网信数据同步服务
 *
 * @author dongdaiming
 * @date 2017年12月11日
 */
//@Service
public class WangXinDataSyncServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(WangXinDataSyncServiceImpl.class);

    /*
    @Autowired
    private WangXinDataSyncDAO1 wangXinDataSyncDAO1;

    @Autowired
    private WangXinDataSyncDAO2 wangXinDataSyncDAO2;

    @Autowired
    private WangXinDataSyncDAO3 wangXinDataSyncDAO3;

    @Autowired
    private WangXinDataDocDAO wangXinDataDocDAO;
    */

    /** 文件密码 */
    @Value("${wangxin.sync.file.password}")
    private String FILE_PASS;
    /** 文件路径 */
    @Value("${wangxin.sync.file.path}")
    private String FILE_PATH;
    /** 数据查询开始时间 */
    @Value("${wangxin.sync.startTime}")
    private String START_TIME;
    /** 数据查询截止时间 */
    @Value("${wangxin.sync.endTime}")
    private String END_TIME;

    /** 文件标识-客户信息 */
    private static final String CUST = "CUST";
    /** 文件标识-订单信息 */
    private static final String ORDER = "ORDER";
    /** 文件标识-放款记录 */
    private static final String LOAN = "LOAN";
    /** 文件标识-还款计划 */
    private static final String PAY_SCH = "PAY_SCH";
    /** 文件标识-代扣流水 */
    private static final String PAY_HIS = "PAY_HIS";
    /** 文件标识-同盾 */
    private static final String TD = "TD";
    /** 文件标识-维氏盾 */
    private static final String WSD = "WSD";
    /** 文件标识-前海 */
    private static final String QH = "QH";
    /** 文件标识-小视 */
    private static final String XS = "XS";
    /** 文件标识-压缩文件 */
    private static final String ZIP_DATA = "ZIP_DATA";

    /** 任务超时时间(小时) */
    private static final int TIMEOUT_HOURS = 12;
    /** 子任务数 */
    private static final int TASKS = 9;
    /** 文件编码 */
    private static final String ENCODING = "UTF-8";
    /** 文件后缀日期格式 */
    private static final String DATE_PATTERN = "yyyyMMdd";
    /** 数据文件夹前缀 */
    private static final String DIR_DATA = "urlson-data-";
    /** 主键文件夹前缀 */
    private static final String DIR_KEY = "urlson-key-";
    /** 数据查询日期格式 */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /** 剔除产品列表 */
    private static final List<String> EXCL_PRODUCTS = Arrays.asList("S9999", "S8881");
    /** 剔除产品列表 */
    private static final String OFFLINE_PRODUCT = "offline";
    /** 数据文件后缀 */
    private static final String SUFFIX = ".txt";
    /** 代扣成功状态 */
    private static final String STATUS_PAY_SUCCESS = "050";
    /** 空闲线程存活时间 */
    private static final long THREAD_ALIVE_SEC = 60L;
    /** 换行符 */
    private static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;
    /** 数据格式统一转换为string */
//    private static final StringConvertUtils STRING_CONVERT = new StringConvertUtils(DEFAULT_DATE_PATTERN, 2);
    /** 各表主键 */
    private static final Map<String, String> KEY_MAP = new HashMap<>(TASKS);
    /** 各表起始时间 */
    private static Map<String, Date> startTimeMap = new HashMap<>(TASKS);

    static {
        KEY_MAP.put(CUST, "customerid");
        KEY_MAP.put(ORDER, "serialno");
        KEY_MAP.put(LOAN, "serialno");
        KEY_MAP.put(PAY_SCH, "id");
        KEY_MAP.put(PAY_HIS, "collectiontaskid");
        KEY_MAP.put(TD, "id");
        KEY_MAP.put(WSD, "id");
        KEY_MAP.put(QH, "id");
        KEY_MAP.put(XS, "id");
    }
/**
 @Override
    public void generateDataFile() {
        Date endTime = new Date();
        // 初始化数据查询时间范围
        initTimeInterval(endTime);
        File dataDir = mkdir(DIR_DATA, endTime);
        File keyDir = mkdir(DIR_KEY, endTime);
        LOGGER.info("wangxin sync data task started, list data endTime {}.", DatetimeUtils.format(endTime, DEFAULT_DATE_PATTERN));
        ExecutorService exec = createThreadPool(TASKS, new CustomizableThreadFactory("exec-wxsync-"));
        List<Future<Long>> futures = new ArrayList<>(TASKS);

        // 提交任务到线程池-生成JSON文件
        submitTask(exec, futures, CUST, dataDir, keyDir, startTimeMap.get(CUST), endTime);
        submitTask(exec, futures, ORDER, dataDir, keyDir, startTimeMap.get(ORDER), endTime);
        submitTask(exec, futures, LOAN, dataDir, keyDir, startTimeMap.get(LOAN), endTime);
        submitTask(exec, futures, PAY_SCH, dataDir, keyDir, startTimeMap.get(PAY_SCH), endTime);
        submitTask(exec, futures, PAY_HIS, dataDir, keyDir, startTimeMap.get(PAY_HIS), endTime);
        submitTask(exec, futures, TD, dataDir, keyDir, startTimeMap.get(TD), endTime);
        submitTask(exec, futures, WSD, dataDir, keyDir, startTimeMap.get(WSD), endTime);
        submitTask(exec, futures, QH, dataDir, keyDir, startTimeMap.get(QH), endTime);
        submitTask(exec, futures, XS, dataDir, keyDir, startTimeMap.get(XS), endTime);
        exec.shutdown();

        // 等待各子任务执行完成
        try {
            exec.awaitTermination(TIMEOUT_HOURS, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            LOGGER.error("generate wangxin data file error.", e);
        }

        // 合并各子任务数据总量
        long records = countRecord(futures);
        LOGGER.info("wangxin generate data file fishned.");

        // 加密压缩并保存数据文件
        File zipFile = new File(dataDir.getPath() + ".zip");
        boolean zipSuccess = Zip4jUtils.zipDir(zipFile, dataDir, FILE_PASS);
        LOGGER.info("wangxin zip data file result {}, records {}, filepath {}.", zipSuccess, records, zipFile);
        if(zipSuccess) {
            saveDoc(startTimeMap.get(ZIP_DATA), endTime, ZIP_DATA, zipFile, records, ProcessStatus.TODO);
        }
    }

 **/
    /** 合并各子任务数据总量
     * @param futures
     * @return
     */
    private long countRecord(List<Future<Long>> futures) {
        long records = 0;
        try {
            for(Future<Long> f : futures) {
                records += f.get();
            }
        } catch (Exception e) {
            LOGGER.error("exec generate wangxin file task error.", e);
        }
        return records;
    }
    /** 创建线程池（指定存活时间，最大线程数 ）
     * @return exec
     */
    private ExecutorService createThreadPool(int corePoolSize, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(corePoolSize, corePoolSize, THREAD_ALIVE_SEC, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), threadFactory);
    }
    /** 根据配置初始化数据查询时间范围（默认起始时间：1970-01-01,默认截止时间：当前时间）
     * @param startTime
     * @param endTime
     */
    /*
    private void initTimeInterval(Date endTime) {
        Date startTime;
        for (Entry<String, String> e : KEY_MAP.entrySet()) {
            switch (e.getKey()) {
                case CUST:
                    startTime = wangXinDataSyncDAO2.queryCustStartTime();
                    break;
                case ORDER:
                    startTime = wangXinDataSyncDAO2.queryOrderStartTime();
                    break;
                case LOAN:
                    startTime = wangXinDataSyncDAO2.queryLoanStartTime();
                    break;
                case PAY_SCH:
                    startTime = wangXinDataSyncDAO3.queryPaySchStartTime();
                    break;
                case PAY_HIS:
                    startTime = wangXinDataSyncDAO2.queryPayHisStartTime();
                    break;
                case TD:
                    startTime = wangXinDataSyncDAO1.queryTDStartTime();
                    break;
                case WSD:
                    startTime = wangXinDataSyncDAO1.queryWSDStartTime();
                    break;
                case QH:
                    startTime = wangXinDataSyncDAO1.queryQHStartTime();
                    break;
                case XS:
                    startTime = wangXinDataSyncDAO1.queryXSStartTime();
                    break;
                default:
                    startTime = null;
                    break;
            }
            startTimeMap.put(e.getKey(), startTime);
        }
        Date minTime = new Date();
        for (Date t : startTimeMap.values()) {
            if(t.compareTo(minTime) < 0) {
                minTime = t;
            }
        }
        startTimeMap.put(ZIP_DATA, minTime);

//		if(StringUtils.isBlank(START_TIME)) {
//			startDate.setTime(0);
//		}else {
//			try {
//				Date confStartDate = DatetimeUtils.parse(START_TIME, DEFAULT_DATE_PATTERN);
//				startDate.setTime(confStartDate.getTime());
//			} catch (ParseException e) {
//				LOGGER.error("parse wangxin startTime error.", e);
//				startDate.setTime(0);
//			}
//		}
        if(StringUtils.isNotBlank(END_TIME)) {
            try {
                Date confEndDate = DatetimeUtils.parse(END_TIME, DEFAULT_DATE_PATTERN);
                endTime.setTime(confEndDate.getTime());
            } catch (ParseException e) {
                LOGGER.error("parse wangxin endTime error.", e);
            }
        } else {
            endTime.setTime(System.currentTimeMillis());
        }
    }
    */

    /**
     * 提交任务到线程池
     * @param exec
     * @param futures
     * @param flag
     * @param dataDir
     * @param keyDir
     * @param startTime
     * @param endTime
     */
    private void submitTask(ExecutorService exec, List<Future<Long>> futures, final String flag,
                            final File dataDir, final File keyDir, final Date startTime, final Date endTime) {
        try {
            futures.add(exec.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    long records = 0;
//                    try {
//                        // 生成数据文件（根据时间范围等条件按天分页查询数据并追加写入数据）
//                        records = genDoc(dataDir, keyDir, startTime, endTime, flag);
//                    } catch (IOException e) {
//                        LOGGER.error("generate wangxin file error.", e);
//                    }
                    return records;
                }

            }));
        } catch (Exception e) {
            LOGGER.error("submit generate wangxin file task error.", e);
        }
    }

    /**
     * 删除原始文件夹，创建新文件夹
     * @param dirName
     * @param endDate
     * @return
     */
    private File mkdir(String dirName, Date endDate) {
        String date = DateFormatUtils.format(endDate, DATE_PATTERN);
        File dir = new File(FILE_PATH + dirName + date);
        if(dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                LOGGER.error("delete wangxin file error.", e);
            }
        }
        dir.mkdirs();
        return dir;
    }

    /**
     * 生成数据文件（根据时间范围等条件按天分页查询数据并追加写入数据）
     * @param dataDir
     * @param keyDir
     * @param startTime
     * @param endTime
     * @param flag
     * @throws IOException
     */
    /*
    private long genDoc(File dataDir, File keyDir, Date startTime, Date endTime, String flag) throws IOException {
        String date = DateFormatUtils.format(endTime, DATE_PATTERN);
        File dataFile = new File(dataDir.getPath() + "/" + flag + date + SUFFIX);
        File keyFile = new File(keyDir.getPath() + "/" + flag + date + SUFFIX);
        dataFile.createNewFile();
        keyFile.createNewFile();

        // 文件总记录数
        long records = 0;
        int size = 0;
        List<Map<String, Object>> list;
        List<String> keys;
        List<String> datas;
        Date startDate = startTime;
        Date endDate = startTime;
        StringBuilder dataText;
        StringBuilder keyText;
        // 按天分页查询数据并追加写入数据
        while (true) {
            startDate = endDate;
            endDate = DateUtils.truncate(DateUtils.addDays(startDate, 1), Calendar.DAY_OF_MONTH);
            if(endDate.compareTo(endTime) > 0) {
                break;
            }
            // 根据时间范围等条件查询数据
            list = listData(flag, startDate, endDate);
            size = list == null ? 0 : list.size();
            LOGGER.info("wangxin list data size {}, flag {}, between {} and {}.", size, flag,
                    DateFormatUtils.format(startDate, DEFAULT_DATE_PATTERN), DateFormatUtils.format(endDate, DEFAULT_DATE_PATTERN));
            if(size == 0) {
                continue;
            }
            datas = new ArrayList<>(size);
            keys = new ArrayList<>(size);
            for (Map<String, Object> map : list) {
                // 数据序列化成JSON格式
                datas.add(JSON.toJSONString(toStrMap(map)));
                keys.add(String.valueOf(map.get(KEY_MAP.get(flag))));
            }
            dataText = new StringBuilder();
            keyText = new StringBuilder();
            if(records > 0) {
                keyText.append(',');
                dataText.append(LINE_SEPARATOR);
            }
            // 以换行符连接JSON数据
            dataText.append(StringUtils.join(datas, LINE_SEPARATOR));
            keyText.append(StringUtils.join(keys, ','));
            FileUtils.write(dataFile, dataText, ENCODING, true);
            FileUtils.write(keyFile, keyText, ENCODING, true);
            records += size;
        }

//        if(records == 0) {
//            LOGGER.warn("wangxin data is empty, flag {}, startTime {}, endTime {}.", flag,
//                    DatetimeUtils.format(startTime, DEFAULT_DATE_PATTERN), DatetimeUtils.format(endTime, DEFAULT_DATE_PATTERN));
//            dataFile.delete();
//            keyFile.delete();
//        } else {
//            LOGGER.info("wangxin data file records {}, flag {}, startTime {}, endTime {}.", records, flag,
//                    DatetimeUtils.format(startTime, DEFAULT_DATE_PATTERN), DatetimeUtils.format(endTime, DEFAULT_DATE_PATTERN));
//            saveDoc(startTime, endTime, flag, dataFile, records, ProcessStatus.DOING);
//        }
        return records;
    }
    */

    /**
     * 文件信息入库
     * @param startTime
     * @param endTime
     * @param flag
     * @param dataFile
     * @param records
     */
    /*
    private void saveDoc(Date startTime, Date endTime, String flag, File dataFile, long records, ProcessStatus status) {
        WangxinDataDoc doc = new WangxinDataDoc();
        initDoc(startTime, endTime, dataFile, records, doc, status);
        WangxinDataDoc newDoc = wangXinDataDocDAO.save(doc);
        LOGGER.info("save wangxin doc result {}, flag {}.",newDoc != null && newDoc.getId() != null, flag);
    }
    */
    /**
     * 初始化文件信息
     * @param startTime
     * @param endTime
     * @param dataFile
     * @param records
     * @param doc
     */
    /*
    private void initDoc(Date startTime, Date endTime, File dataFile, long records, WangxinDataDoc doc, ProcessStatus status) {
        doc.setDocName(dataFile.getName());
        doc.setDocPath(dataFile.getPath());
        doc.setProcessStatus(status.status());
        doc.setDocSize(dataFile.length());
        doc.setRecords(records);
        doc.setStartTime(startTime);
        doc.setEndTime(endTime);
        Date now = new Date();
        doc.setAddTime(now);
        doc.setUpdateTime(now);
    }
    */

    /** 数据格式统一转换为string
     * @param map
     * @return
     */
    /*
    private Map<String, String> toStrMap(Map<String, Object> map) {
        Map<String, String> strMap = new HashMap<>(map.size());
        for (Entry<String, Object> e : map.entrySet()) {
            strMap.put(e.getKey(), STRING_CONVERT.convert(e.getValue()));
        }
        return strMap;
    }
    */

    /** 根据时间范围等条件查询数据
     * @param startDate
     * @param flag
     * @param endDate
     * @return
     */
    /*
    private List<Map<String, Object>> listData(String flag, Date startDate, Date endDate) {
        List<Map<String, Object>> list;
        switch (flag) {
            case CUST:
                list = wangXinDataSyncDAO2.listCust(startDate, endDate);
                break;
            case ORDER:
                list = wangXinDataSyncDAO2.listOrder(startDate, endDate, EXCL_PRODUCTS);
                break;
            case LOAN:
                list = wangXinDataSyncDAO2.listLoan(startDate, endDate, EXCL_PRODUCTS);
                break;
            case PAY_SCH:
                list = wangXinDataSyncDAO3.listPaySch(startDate, endDate, OFFLINE_PRODUCT);
                break;
            case PAY_HIS:
                list = wangXinDataSyncDAO2.listPayHis(startDate, endDate, EXCL_PRODUCTS, STATUS_PAY_SUCCESS);
                break;
            case TD:
                list = wangXinDataSyncDAO1.listTD(startDate, endDate, EXCL_PRODUCTS);
                break;
            case WSD:
                list = wangXinDataSyncDAO1.listWSD(startDate, endDate, EXCL_PRODUCTS);
                break;
            case QH:
                list = wangXinDataSyncDAO1.listQH(startDate, endDate, EXCL_PRODUCTS);
                break;
            case XS:
                list = wangXinDataSyncDAO1.listXS(startDate, endDate, EXCL_PRODUCTS);
                break;
            default:
                list = Collections.emptyList();
                break;
        }
        return list;
    }
    */
}