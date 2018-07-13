package org.stathry.commons.xml;

import com.thoughtworks.xstream.XStream;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * Created by dongdaiming on 2018-07-11 15:34
 */
public class XmlTest {

    @Test
    public void testBeanToXml() {
        Header h = new Header("i1", "p1", "o1");
        System.out.println(new XStream().toXML(h));
    }

    @Test
    public void test2() throws Exception {
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("BizK1", "BizV1"));
        list.add(new BasicNameValuePair("BizK2", "BizV2"));
        System.out.println(requestJumpStrategy(list, null));
    }

    public static String requestJumpStrategy(List<NameValuePair> list , Map<String,String> map) throws Exception {
        Document doc = DocumentHelper.createDocument();
        // TODO 报文格式待定
        Element root = doc.addElement("StrategyOneRequest");
        Element h = root.addElement("Header");
        h.addElement("AppCd").setText("iqv");

        Element b = root.addElement("Body");
        b.addElement("CurAction").setText("V1");
        b.addElement("ActionSup").setText("V1");
        b.addElement("AppNo").setText("V1");
        b.addElement("AppDt").setText("V2");
        Element v = b.addElement("Vars");
        for (NameValuePair p : list) {
            v.addElement(p.getName()).setText(p.getValue());
        }
        return doc.asXML();
    }

    @Test
    public void test1() {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("StrategyOneRequest");
        Element h = root.addElement("Header");
        h.addElement("InquiryCode").setText("iqv");
        h.addElement("ProcessCode").setText("pcv");
        Element b = root.addElement("Body");
        b.addElement("K1").setText("V1");
        b.addElement("K2").setText("V2");
        System.out.println(doc.asXML());
    }

    private static class Header {
        private String InquiryCode;
        private String ProcessCode;
        private String OrganizationCode;

        public Header(String inquiryCode, String processCode, String organizationCode) {
            InquiryCode = inquiryCode;
            ProcessCode = processCode;
            OrganizationCode = organizationCode;
        }
    }

}
