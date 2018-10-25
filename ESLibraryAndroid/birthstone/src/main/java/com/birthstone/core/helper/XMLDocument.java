package com.birthstone.core.helper;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Xml.Encoding;

public class XMLDocument
{
	private String XML;
	private String fileName;
	private DocumentBuilderFactory docBuilderFactory = null;
	private DocumentBuilder docBuilder = null;
	private Document document = null;
	private Element root = null;
	private Node currentNode = null;

	public XMLDocument( )
	{
	}

	public XMLDocument( String xml )
	{
		this.XML = xml;
	}

	public void readXML(String fileName) throws Exception
	{
		this.fileName = fileName;
		try
		{
			java.io.File fileXml = new java.io.File(fileName);
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			document = docBuilder.parse(fileXml);
			// XML=docBuilder.
			root = document.getDocumentElement();
			currentNode = document.getFirstChild();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	/*
	 * xml strxml
	 */
	public void loadXML(String xml) throws Exception
	{
		try
		{
			XML = xml;
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			InputStream source = new ByteArrayInputStream(XML.getBytes(Encoding.UTF_8.toString()));
			document = docBuilder.parse(source);
			// root element
			root = document.getDocumentElement();
			currentNode = document.getFirstChild();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	/*
	 * XMLӽڵб
	 */
	public NodeList getNodeList(Node node) throws Exception
	{
		NodeList nodeList = null;
		try
		{
			nodeList = node.getChildNodes();
		}
		catch(Exception ex)
		{
			throw ex;
		}
		return nodeList;
	}

	/*
	 * XMLӽڵб
	 */
	public Node getNode(NodeList nodeList, String nodeName) throws Exception
	{
		Node node = null;
		try
		{
			String nodeName1 = "";
			int size = nodeList.getLength();
			for(int i = 0; i < size; i++)
			{
				nodeName1 = nodeList.item(i).getNodeName();
				if(nodeName1.equals(nodeName))
				{
					node = nodeList.item(i);
					break;
				}
				if(nodeList.item(i).hasChildNodes())
				{
					node = getNode(nodeList.item(i).getChildNodes(), nodeName);
				}
			}

		}
		catch(Exception ex)
		{
			throw ex;
		}
		return node;
	}

	/*
	 * ָڵֵ
	 */
	public String getValue(Node node) throws Exception
	{
		String obj = null;
		try
		{
			if(node != null)
			{
				obj = node.getFirstChild().getNodeValue();
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		return obj;
	}

	/*
	 * XML filenameƣ·
	 */
	public void save() throws Exception
	{
		try
		{
			String[] name = fileName.split("/");
			String filename = name[name.length - 1];
			String path = fileName.substring(0, fileName.length() - filename.length());
			java.io.File filedir = new java.io.File(path);
			if(!filedir.exists())
			{
				filedir.mkdirs();
			}
			java.io.File file = new java.io.File(fileName);
			if(!file.exists())
			{
				file.createNewFile();
			}
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			FileOutputStream outStream = new FileOutputStream(file);
			StreamResult result = new StreamResult(outStream);
			transformer.transform(source, result);

		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	/*
	 * XML filenameƣ·
	 */
	public void save(String fileName) throws Exception
	{
		try
		{
			String[] name = fileName.split("/");
			String filename = name[name.length - 1];
			String path = fileName.substring(0, fileName.length() - filename.length());
			java.io.File filedir = new java.io.File(path);
			if(!filedir.exists())
			{
				filedir.mkdirs();
			}
			java.io.File file = new java.io.File(fileName);
			if(!file.exists())
			{
				file.createNewFile();
			}
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			FileOutputStream outStream = new FileOutputStream(file);
			StreamResult result = new StreamResult(outStream);
			transformer.transform(source, result);
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	public String getXML()
	{
		return XML;
	}

	public void setXML(String xML)
	{
		XML = xML;
	}

	public Element getRoot()
	{
		return root;
	}

	public Node getCurrentNode()
	{
		return currentNode;
	}

}
