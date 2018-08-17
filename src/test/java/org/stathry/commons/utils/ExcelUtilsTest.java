/*
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.stathry.commons.enums.ExcelTypeEnums;
import org.stathry.commons.pojo.config.ExcelParams;
import org.stathry.commons.pojo.config.StyleParams;
import org.stathry.commons.utils.DatetimeFormatUtils;
import org.stathry.commons.utils.ExcelUtils;

/**
 * @author stathry@126.com
 * @date 2017年5月21日
 */
public class ExcelUtilsTest {

	@Test
	public void testExport() {
		String[] header = new String[] { "借款人", "收款人" };
		String[][] data = new String[][] { { "ITeye文章版权属于作者，受法律保护。没有作者书面许可不得转载。若作者同意转载，必须以超链接形式标明文章原始", "董永强" },
				{ "董代明", "东方冰" } };
		ExcelParams params = new ExcelParams();
		params.setTitle("个人账单");
		params.setSheetName("汇总");
		params.setType(ExcelTypeEnums.xlsx);

		StyleParams titleStyleParam = new StyleParams();
		titleStyleParam.setHasBorder(false);
		titleStyleParam.setFontBold(true);
		titleStyleParam.setStartRow(2);
		titleStyleParam.setStartColumn(3);
		params.setTitleStyle(titleStyleParam);

		ExcelUtils.export("d:/temp/excel/test.xlsx", header, data, params);
	}

	@Test
	public void testExport2() {
		List<String> header = new ArrayList<String>();
		List<List<String>> data = new ArrayList<List<String>>();
		List<String> data1 = new ArrayList<String>();
		List<String> data2 = new ArrayList<String>();

		header.add("门派");
		header.add("美女");
		header.add("时间");
		data1.add("青云门");
		data1.add("陆雪琪");
		data1.add(DatetimeFormatUtils.format(new Date()));
		data2.add("合欢派");
		data2.add("妙妙");
		data2.add(DatetimeFormatUtils.format(new Date()));

		data.add(data1);
		data.add(data2);

		ExcelParams params = new ExcelParams();
		params.setTitle("青云合欢美女大合集");
		params.setSheetName("美女大合集");
		params.setType(ExcelTypeEnums.xlsx);

		StyleParams titleStyleParam = new StyleParams();
		titleStyleParam.setHasBorder(false);
		titleStyleParam.setFontBold(true);
		titleStyleParam.setStartRow(2);
		titleStyleParam.setStartColumn(3);
		params.setTitleStyle(titleStyleParam);

		ExcelUtils.export("d:/temp/excel/test2.xlsx", header, data, params);

	}

	@Test
	public void testExport3() {
		String[] keyIndex = new String[]{"COMPANY_NAME", "COMPANY_TYPE", "COMPANY_DATE"};
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("COMPANY_NAME", "公司名称");
		header.put("COMPANY_TYPE", "业务类型");
		header.put("COMPANY_DATE", "成立时间");
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("COMPANY_NAME", "阿里巴巴");
		data1.put("COMPANY_TYPE", "电子商务");
		data1.put("COMPANY_DATE", 2016);
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("COMPANY_NAME", "蚂蚁金服");
		data2.put("COMPANY_TYPE", "金融科技");
		data2.put("COMPANY_DATE", new Date());
		data.add(data1);
		data.add(data2);

		ExcelParams params = new ExcelParams();
		params.setTitle("中国科技巨擘");
		params.setSheetName("科技巨擎");
		params.setType(ExcelTypeEnums.xlsx);
		params.setKeyIndex2(keyIndex);

		StyleParams titleStyleParam = new StyleParams();
		titleStyleParam.setHasBorder(false);
		titleStyleParam.setFontBold(true);
		titleStyleParam.setStartRow(2);
		titleStyleParam.setStartColumn(3);
		params.setTitleStyle(titleStyleParam);

		ExcelUtils.export("d:/temp/excel/test3.xlsx", header, data, params);

	}

}
