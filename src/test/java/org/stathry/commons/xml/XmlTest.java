package org.stathry.commons.xml;

import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 * Created by dongdaiming on 2018-07-11 15:34
 */
public class XmlTest {

    @Test
    public void testParseStrXml() throws DocumentException {
        Header h = new Header("i1", "p1", "o1");
        String xml = new XStream().toXML(h);
        System.out.println(DocumentHelper.parseText(xml));
    }

    @Test
    public void testParseCrifParams() throws Exception {
        Document doc = new SAXReader().read(ResourceUtils.getFile("classpath:CrifParamsConfig.xml"));
        Map<String, String> SERVICES_START_ACT = new HashMap<>();
        Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> CRIF_STRATEGY_PARAMS_MAPPING = new HashMap<>();
        Map<String, String> CRIF_BASIC_PARAMS_MAPPING = new HashMap<>();

        Element root = doc.getRootElement();

        Element se = root.element("CrifServiceActionMap");
        Element e;
        for (Object o : se.elements()) {
            e = (Element) o;
            SERVICES_START_ACT.put(e.attributeValue("service"), e.attributeValue("action"));
        }

        Element be = root.element("CrifBasicParamsMap");
        for (Object o : be.elements()) {
            e = (Element) o;
            CRIF_BASIC_PARAMS_MAPPING.put(e.attributeValue("crifParam"), e.attributeValue("reqParam"));
        }

        Element strategyE = root.element("CrifStragegyParamsMap");
        String strategy = "";
        String org = "";
        String prod = "";
        String p = "";
        Map<String, Map<String, Map<String, Map<String, String>>>> orgMap;
        Map<String, Map<String, Map<String, String>>> prodMap;
        Map<String, Map<String, String>> pMap;
        Map<String, String> kMap;
        Element orgE;
        Element prodE;
        Element pE;
        // 遍历策略列表
        for (Object o : strategyE.elements()) {
            e = (Element) o;
            strategy = e.attributeValue("code");
            if((orgMap = CRIF_STRATEGY_PARAMS_MAPPING.get(strategy)) == null) {
                orgMap = new HashMap<>(2);
                CRIF_STRATEGY_PARAMS_MAPPING.put(strategy, orgMap);
            }

            // 遍历机构列表
            for (Object orgO : e.elements()) {
                orgE = (Element)orgO;
                org = orgE.attributeValue("code");
                if((prodMap = orgMap.get(org)) == null) {
                    prodMap = new HashMap<>(2);
                    orgMap.put(org, prodMap);
                }

                // 遍历产品列表
                for (Object prodO : orgE.elements()) {
                    prodE = (Element)prodO;
                    prod = prodE.attributeValue("code");
                    if((pMap = prodMap.get(prod)) == null) {
                        pMap = new HashMap<>(2);
                        prodMap.put(prod, pMap);
                    }

                    // 遍历参数列表
                    for(Object pO : prodE.elements()) {
                        pE = (Element)pO;
                        p = pE.attributeValue("crifParam");
                        kMap = new HashMap<>(2);
                        kMap.put("realParam", pE.attributeValue("realParam"));
                        kMap.put("defaultValue", pE.attributeValue("defaultValue"));
                        pMap.put(p, kMap);
                    }
                }
            }
//            CRIF_BASIC_PARAMS_MAPPING.put(e.attributeValue("crifParam"), e.attributeValue("reqParam"));
        }

//        System.out.println(SERVICES_START_ACT);
//        System.out.println(CRIF_BASIC_PARAMS_MAPPING);
        System.out.println(CRIF_STRATEGY_PARAMS_MAPPING);
    }

    @Test
    public void testBeanToXmlByXStream() {
        Header h = new Header("i1", "p1", "o1");
        System.out.println("testBeanToXmlByXStream:\n" + new XStream().toXML(h));
    }

    @Test
    public void testContactXmlByDom4j() {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("RootConfig");
        Element h = root.addElement("Header");
        h.addElement("h1").setText("hv1");
        h.addElement("h2").setText("hv2");
        Element b = root.addElement("Body");
        b.addElement("K1").setText("V1");
        b.addElement("K2").setText("V2");
        System.out.println("testContactXmlByDom4j:\n" + doc.asXML());
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

        @Override
        public String toString() {
            return "Header{" +
                    "InquiryCode='" + InquiryCode + '\'' +
                    ", ProcessCode='" + ProcessCode + '\'' +
                    ", OrganizationCode='" + OrganizationCode + '\'' +
                    '}';
        }
    }

}
