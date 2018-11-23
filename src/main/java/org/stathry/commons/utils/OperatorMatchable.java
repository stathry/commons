package org.stathry.commons.utils;

/**
 * 运营商匹配接口
 *
 * @author dongdaiming
 * @date 2018/5/10
 */
public interface OperatorMatchable {

    /**
     * 运营商标识-中国移动
     */
    public static final String OPT_MOBILE = "M";
    /**
     * 运营商标识-中国电信
     */
    public static final String OPT_TELECOM = "T";
    /**
     * 运营商标识-中国联通
     */
    public static final String OPT_UNICOM = "U";

    /**
     * 判断归属运营商
     *
     * @param phone
     * @param org   数据方标识
     * @return 运营商标识(M, T, U)
     */
    public String decideOpt(String phone, String org);

    /**
     * 是否匹配指定运营商
     *
     * @param phone
     * @param org   数据方标识
     * @param opt   运营商标识(如M|T|U)
     * @return 是否匹配指定运营商
     */
    boolean isMatchOpt(String phone, String org, String opt);

}
