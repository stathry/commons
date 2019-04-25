package org.jeecgframework.poi.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.junit.Test;
import org.stathry.commons.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * ExcelDataAligningTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-04-22 12:12
 */
public class ExcelDataAligningTest {

    @Test
    public void testImport() throws Exception {
        ImportParams importParams = new ImportParams();
        importParams.setStartRows(1);
        List<CityCreditOrderCounter> list = ExcelImportUtil.importExcel(new File("/doc/数据统计/cityC.xlsx"), CityCreditOrderCounter.class, importParams);

        Set<Integer> cities = mergeCityId(list);

        Map<Integer, Map<Integer, Map<Integer, Integer>>> typeCityMap = initTypeProductCityMap(Arrays.asList(1, 2, 3), Arrays.asList(3, 1));

        loadCityMap(list, typeCityMap);

        mergeCityCountMap(cities, typeCityMap);

        exportCityCountMap(cities, typeCityMap, "/doc/数据统计/全国城市信用单统计-src-" + LocalDate.now().minusDays(1).toString() + ".xls");

        copyResultFile();
    }

    private void copyResultFile() throws IOException {
        LocalDate t = LocalDate.now().minusDays(1);
        String newName = "/doc/数据统计/全国城市信用单统计" + t.getMonthValue() + "" + t.getDayOfMonth() +".xlsx";
        FileUtils.copyFile(new File("/doc/数据统计/全国城市信用单统计.xlsx"), new File(newName));
    }

    private void exportCityCountMap(Set<Integer> cities, Map<Integer, Map<Integer, Map<Integer, Integer>>> typeCityMap, String expFile) throws IOException {
        List<CityCounter> counters = new ArrayList<>(cities.size());
        CityCounter c;
        for(Integer city : cities) {
            c = new CityCounter();
            c.setC(city);
            c.setO13(typeCityMap.get(1).get(3).get(city));
            c.setO11(typeCityMap.get(1).get(1).get(city));
            c.setO23(typeCityMap.get(2).get(3).get(city));
            c.setO21(typeCityMap.get(2).get(1).get(city));
            c.setO33(typeCityMap.get(3).get(3).get(city));
            c.setO31(typeCityMap.get(3).get(1).get(city));
            counters.add(c);
        }
        ExportParams exportParams = new ExportParams();
        exportParams.setCreateHeadRows(false);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, CityCounter.class, counters);
        File file = new File(expFile);
        workbook.write(new FileOutputStream(file));
        workbook.close();
        System.out.println(expFile);
    }

    private void mergeCityCountMap(Set<Integer> cities, Map<Integer, Map<Integer, Map<Integer, Integer>>> typeCityMap) {
        for(Map<Integer, Map<Integer, Integer>> type : typeCityMap.values()) {
            for(Map<Integer, Integer> citMap : type.values()) {
                for(Integer city : cities) {
                    if(citMap.get(city) == null) {
                        citMap.put(city, 0);
                    }
                }
            }
        }
    }

    private void loadCityMap(List<CityCreditOrderCounter> list, Map<Integer, Map<Integer, Map<Integer, Integer>>> typeCityMap) {
        list.stream().forEach(c -> {
            if (c.getP1() != null && c.getC1() != null && c.getO1() != null && c.getP1() != 2) {
                typeCityMap.get(1).get(c.getP1()).put(c.getC1(), c.getO1());
            }
            if (c.getP2() != null && c.getC2() != null && c.getO2() != null && c.getP2() != 2) {
                typeCityMap.get(2).get(c.getP2()).put(c.getC2(), c.getO2());
            }
            if (c.getP3() != null && c.getC3() != null && c.getO3() != null && c.getP3() != 2) {
                typeCityMap.get(3).get(c.getP3()).put(c.getC3(), c.getO3());
            }
        });
    }

    private Set<Integer> mergeCityId(List<CityCreditOrderCounter> list) {
        Set<Integer> cities = new TreeSet<>();
        list.stream().forEach(c -> {
            if (c.getC1() != null) {
                cities.add(c.getC1());
            }
            if (c.getC2() != null) {
                cities.add(c.getC2());
            }
            if (c.getC3() != null) {
                cities.add(c.getC3());
            }
        });
        System.out.println("citySize: " + cities.size());
        return cities;
    }

    private Map<Integer, Map<Integer, Map<Integer, Integer>>> initTypeProductCityMap(List<Integer> types, List<Integer> prods) {
        Map<Integer, Map<Integer, Map<Integer, Integer>>> typeMap = new HashMap<>(4);

        Map<Integer, Map<Integer, Integer>> productMap;
        for (Integer type : types) {
            productMap = new LinkedHashMap<>(4);
            typeMap.put(type, productMap);
            for (Integer prod : prods) {
                productMap.put(prod, new TreeMap<>());
            }
            typeMap.put(type, productMap);
        }
        return typeMap;
    }
}
