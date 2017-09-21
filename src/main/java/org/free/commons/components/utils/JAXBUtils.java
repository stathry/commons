/**
 * Copyright 2016-2100 Deppon Co., Ltd.
 */
package org.free.commons.components.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dongdaiming@deppon.com
 *
 *         2016年8月8日
 */
public class JAXBUtils {
	
	private static Logger LOGGER = LoggerFactory.getLogger(JAXBUtils.class);

	public static String beanToXML(Object obj) {
		JAXBContext context = null;
		Marshaller marshaller = null;
		Writer writer = new StringWriter();
		
		try {
			 context = JAXBContext.newInstance(obj.getClass());
			 marshaller = context.createMarshaller();
			 marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);  
			 marshaller.marshal(obj, writer);
			 return writer.toString();
		} catch (JAXBException e) {
			LOGGER.warn("any unexpected problem occurs during the marshalling", e);
			return null;
		}
	}

	public static void beanToXML(Object obj, String path) {
		JAXBContext context = null;
		Marshaller marshaller = null;
		
		try {
			 context = JAXBContext.newInstance(obj.getClass());
			 marshaller = context.createMarshaller();
			 marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);  
			 marshaller.marshal(obj, new FileOutputStream(path));
		} catch (JAXBException e) {
			LOGGER.warn("any unexpected problem occurs during the marshalling", e);
		} catch (FileNotFoundException e) {
			LOGGER.warn("may be the directory not found", e);
		}
	
	}

	@SuppressWarnings("unchecked")
	public static <T> T xmlToBean(Class<T> clazz, String xml) {
		JAXBContext context = null;
		Unmarshaller unmarshaller = null;

		try {
			context = JAXBContext.newInstance(clazz);
			unmarshaller = context.createUnmarshaller();
			return (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (JAXBException e) {
			LOGGER.warn("any unexpected problem occurs during the marshalling", e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T xmlFileToBean(Class<T> clazz, String xmlPath) {
		JAXBContext context = null;
		Unmarshaller unmarshaller = null;

		try {
			context = JAXBContext.newInstance(clazz);
			unmarshaller = context.createUnmarshaller();
			return (T) unmarshaller.unmarshal(new FileInputStream(xmlPath));
		} catch (JAXBException e) {
			LOGGER.warn("any unexpected problem occurs during the marshalling", e);
		} catch (FileNotFoundException e) {
			LOGGER.warn("may be the directory not found", e);
		}
		return null;

	}
}
