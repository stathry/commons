package org.free.commons.components.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class XMLUtils {
	
	private static Logger LOGGER = LoggerFactory.getLogger(XMLUtils.class);
	
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String DEFAULT_INDENT = "  ";

	private static DocumentFactory getDocumentFactory() {
		return DocumentFactory.getInstance();
	}


	public static Document parse(File file)  {
		SAXReader reader = new SAXReader();
		Document doc = null;
		 try {
			 doc = reader.read(file);
		} catch (DocumentException e) {
			LOGGER.warn("an error occurs during parsing.", e);
		}
		 return doc;
	}

	public static Document parse(InputStream in)  {
		SAXReader reader = new SAXReader();
		Document doc = null;
		 try {
			 doc = reader.read(in);
		} catch (DocumentException e) {
			LOGGER.warn("an error occurs during parsing.", e);
		}
		 return doc;
	}

	public static Document parse(String text)  {
		Document doc = null;
		 try {
			 doc = DocumentHelper.parseText(text);
		} catch (DocumentException e) {
			LOGGER.warn("an error occurs during parsing.", e);
		}
		 return doc;
	}
	
	@SuppressWarnings("rawtypes")
	public static List selectNodes(Document doc , String xpath) {
		return doc.selectNodes(xpath);
	}
	
	public static Object selectObject(Document doc , String xpath) {
		return doc.selectObject(xpath);
	}
	
	@SuppressWarnings("rawtypes")
	public static List selectNodes(String filePath , String xpath) {
		Document doc = parse(new File(filePath));
		return doc.selectNodes(xpath);
	}
	
	public static Object selectObject(File file , String xpath) {
		Document doc = parse(file);
		return doc.selectObject(xpath);
	}

	public static String toString(Document doc) {
		return doc.asXML();
	}
	
	public static String toString(File file, String encoding)   {
		if(StringUtils.isBlank(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		String content = null;
		try {
			content = FileUtils.readFileToString(file, encoding);
		} catch (IOException e) {
			LOGGER.warn("in case of an I/O error.", e);
			return null;
		}
		
		try {
			if(DocumentHelper.parseText(content) != null) {
				return content;
			}
		} catch (DocumentException e) {
			LOGGER.warn("the document could not be parsed.", e);
			return null;
		}
		return content;
	}

	public static String toString(InputStream in, String encoding)   {
		if(StringUtils.isBlank(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		String content = null;
		try {
			content = IOUtils.toString(in, encoding);
		} catch (IOException e) {
			LOGGER.warn(" I/O error", e);
			return null;
		}
		
		try {
			if(DocumentHelper.parseText(content) != null) {
				return content;
			}
		} catch (DocumentException e) {
			LOGGER.warn("the document could not be parsed.", e);
			return null;
		}
		return content;
	}
	
	public static void write(String xml, String path) {
		write(parse(xml), path);
	}
	
	public static void write(Document root, String path) {
		XMLWriter writer = null;
		File file = new File(path);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LOGGER.warn("I/O error.", e);
				return;
			}
		}
		try {
			writer = new XMLWriter(new FileOutputStream(file),new OutputFormat(DEFAULT_INDENT, false, DEFAULT_ENCODING));
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("unsupported encoding exception.", e);
		} catch (FileNotFoundException e) {
			LOGGER.warn("file not found exception.", e);
		}
		
		if(writer == null) {
			return;
		}
		try {
			writer.write(root);
			writer.close();
		} catch (IOException e) {
			LOGGER.warn("there's any problem writing.", e);
		}
	}

	public static Document createDocument() {
		return getDocumentFactory().createDocument();
	}

	public static Document createDocument(Element rootElement) {
		return getDocumentFactory().createDocument(rootElement);
	}

	public static Element createElement(QName qname) {
		return getDocumentFactory().createElement(qname);
	}

	public static Element createElement(String name) {
		return getDocumentFactory().createElement(name);
	}

	public static Attribute createAttribute(Element owner, QName qname,
			String value) {
		return getDocumentFactory().createAttribute(owner, qname, value);
	}

	public static Attribute createAttribute(Element owner, String name,
			String value) {
		return getDocumentFactory().createAttribute(owner, name, value);
	}

	public static CDATA createCDATA(String text) {
		return DocumentFactory.getInstance().createCDATA(text);
	}

	public static Comment createComment(String text) {
		return DocumentFactory.getInstance().createComment(text);
	}

	public static Text createText(String text) {
		return DocumentFactory.getInstance().createText(text);
	}

	public static Entity createEntity(String name, String text) {
		return DocumentFactory.getInstance().createEntity(name, text);
	}

	public static Namespace createNamespace(String prefix, String uri) {
		return DocumentFactory.getInstance().createNamespace(prefix, uri);
	}
	
	
	
}
