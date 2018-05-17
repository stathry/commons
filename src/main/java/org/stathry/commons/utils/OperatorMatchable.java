package org.stathry.commons.utils;

/**
 * 运营商匹配接口
 *
 * @author dongdaiming
 * @date 2018/5/10
 */
public interface OperatorMatchable {

    /** 运营商标识-中国移动 */
    public static final String OPT_MOBILE = "M";
    /** 运营商标识-中国电信 */
    public static final String OPT_TELECOM = "T";
    /** 运营商标识-中国联通 */
    public static final String OPT_UNICOM = "U";

    /** 数据方标识-小视科技 */
    public static final String ORG_XS = "XS";

    /**
     * 匹配运营商
     * @param phone
     * @return 运营商标识(M,T,U)
     */
    public String matchToOpt(String phone);

    /**
     * 匹配运营商
     * @param phone
     * @param org 数据方标识(如小视科技为XS)
     * @return 运营商标识(M,T,U)
     */
    public String matchToOpt(String phone, String org);

    /**
     * 是否匹配指定运营商
     * @param phone
     * @param org 数据方标识(如小视科技为XS)
     * @param opt 运营商标识(如M|T|U)
     * @return 是否匹配指定运营商
     */
    boolean isMatchOpt(String phone, String org, String opt);

    /**
     * 是否为移动运营商
     * @param phone
     * @return
     */
    public boolean matchChinaMobile(String phone);

    /**
     * 是否为移动运营商
     * @param phone
     * @param org 数据方标识(如小视科技为XS)
     * @return
     */
    public  boolean matchChinaMobile(String phone, String org);

    /**
     * 是否为电信运营商
     * @param phone
     * @return
     */
    public boolean matchChinaTelecom(String phone);

    /**
     * 是否为电信运营商
     * @param phone
     * @param org 数据方标识(如小视科技为XS)
     * @return
     */
    public boolean matchChinaTelecom(String phone, String org);

    /**
     * 是否为联通运营商
     * @param phone
     * @return
     */
    public boolean matchChinaUnicom(String phone);

    /**
     * 是否为联通运营商
     * @param phone
     * @param org 数据方标识(如小视科技为XS)
     * @return
     */
    public boolean matchChinaUnicom(String phone, String org);

}
