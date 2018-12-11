package org.stathry.commons.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 * 资信数据段配置
 * 
 * @author dongdaiming
 * @date 2018年1月3日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DataMapConfig")
public class DataMapConfig {

	@XmlElementWrapper(name = "DataMaps")
	@XmlElement(name = "DataMap")
	private List<DataMap> dataMaps;

	private Map<String, DataMap> dataMapMap;

    @Override
    public String toString() {
        return "DataMapConfig{" +
                "dataMaps=" + dataMaps +
                '}';
    }

    public List<DataMap> getDataMaps() {
        return dataMaps;
    }

    public void setDataMaps(List<DataMap> dataMaps) {
        this.dataMaps = dataMaps;
    }

    public Map<String, DataMap> getDataMapMap() {
        return dataMapMap;
    }

    public void setDataMapMap(Map<String, DataMap> dataMapMap) {
        this.dataMapMap = dataMapMap;
    }
}
