package org.stathry.commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * TODO
 * Created by dongdaiming on 2018-07-23 16:59
 */
public class JSONTest {

    @Test
    public void testToStr() {
        JSONObject data = new JSONObject();
        data.put("k1", "v1");
        data.put("k2", null);
        data.put("k3", "");
        System.out.println(data.toJSONString());
        Assert.assertEquals("{\"k1\":\"v1\",\"k3\":\"\"}", data.toJSONString());
    }

    @Test
    public void testGetIntToDouble() throws IOException {
        JSONObject js1 = JSON.parseObject("{\"k1\":1.55, \"k2\":null}");
        JSONObject js2 = JSON.parseObject("{\"k1\":1, \"k2\":null}");
        Double d1 = js1.getDouble("k1");
        Double d2 = js2.getDouble("k1");
        System.out.println(Math.round(d1));
        System.out.println(Math.round(d2));
    }

    @Test
    public void testGetStringInt() throws IOException {
        JSONObject js = JSON.parseObject("{\"k1\":1, \"k2\":null}");
        System.out.println(js.getString("k1"));
        System.out.println(js.getString("k2"));
        Assert.assertEquals("1", js.getString("k1"));
        Assert.assertNull(js.getString("k2"));
    }

    @Test
    public void testEscapeJSONStr() throws IOException {
//        String s = FileUtils.readFileToString(new File("/temp/taobaoOData.txt"), "utf-8");
//        String s = FileUtils.readFileToString(new File("/temp/taobaoOData.txt"), "utf-8");
        String s = FileUtils.readFileToString(new File("/temp/data-report.txt"), "utf-8");
//        String s = FileUtils.readFileToString(new File("/temp/data-original.txt"), "utf-8");
//        System.out.println(s);
        JSONObject j = JSON.parseObject(s);
        System.out.println();
        System.out.println(j.toJSONString());
        System.out.println("escapeJson:\n" + StringEscapeUtils.escapeJson(j.toJSONString()));
    }

    @Test
    public void testWriteNullValue() {
        User u = new User();
        System.out.println(JSON.toJSONString(u));
        System.out.println(JSON.toJSONString(u, SerializerFeature.WriteMapNullValue));
    }

    @Test
    public void testWriteWithDateFormat() {
        User u = new User();
        u.setBirth(new Date());
        System.out.println(JSON.toJSONString(u));
        System.out.println(JSON.toJSONStringWithDateFormat(u, "yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @Test
    public void testWriteWithDefault() {
        User u = new User();
        System.out.println(JSON.toJSONString(u, SerializerFeature.WriteMapNullValue));
        System.out.println(JSON.toJSONString(u, SerializerFeature.WriteNullStringAsEmpty));
        System.out.println(JSON.toJSONString(u, SerializerFeature.WriteNullListAsEmpty));
        System.out.println(JSON.toJSONString(u, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty));
        System.out.println(JSON.toJSONString(u, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero));
        System.out.println(JSON.toJSONString(u, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse));
        System.out.println(JSON.toJSONString(u, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse));
    }

    @Test
    public void testWriteWithAnnotation() {
        User2 u = new User2();
        u.setBirth(new Date());
        System.out.println(JSON.toJSONString(u));
        System.out.println(JSON.toJSONString(u, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty));
    }

    @Test
    public void testWriteWithFilter() {
        User2 u = new User2();
        u.setBirth(new Date());
        System.out.println(JSON.toJSONString(u));
        System.out.println(JSON.toJSONString(u, new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                String r;
                if (value != null && value.getClass().equals(Date.class)) {
                    r = new SimpleDateFormat("yyyy-MM-dd HH:mm").format((Date) value);
                } else {
                    r = value == null ? "" : value.toString();
                }
                return r;
            }
        }, SerializerFeature.WriteNullNumberAsZero));
    }

    static class User {
        private Integer id;
        private String name;
        private Date birth;
        private Boolean sex;
        private List<Object> child;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getBirth() {
            return birth;
        }

        public void setBirth(Date birth) {
            this.birth = birth;
        }

        public Boolean getSex() {
            return sex;
        }

        public void setSex(Boolean sex) {
            this.sex = sex;
        }

        public List<Object> getChild() {
            return child;
        }

        public void setChild(List<Object> child) {
            this.child = child;
        }
    }

    static class User2 {
        @JSONField(name = "uid", ordinal = 3)
        private Integer id;
        private String name;
        @JSONField(format = "yyyy-MM-dd", ordinal = 2)
        private Date birth;
        @JSONField(serialize = false)
        private Boolean sex;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getBirth() {
            return birth;
        }

        public void setBirth(Date birth) {
            this.birth = birth;
        }

        public Boolean getSex() {
            return sex;
        }

        public void setSex(Boolean sex) {
            this.sex = sex;
        }
    }

}
