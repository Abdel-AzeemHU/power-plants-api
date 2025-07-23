package com.AIQ.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TestUtils {
	public static final long WAIT = 50;

	// Method to parse XML and return key-value pairs
	public HashMap<String, String> parseStringXML(InputStream file) throws Exception {
		HashMap<String, String> stringMap = new HashMap<>();
		// Get Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Build Document
		Document document = builder.parse(file);

		// Normalize the XML Structure
		document.getDocumentElement().normalize();

		// Root node
		Element root = document.getDocumentElement();

		// Get all elements
		NodeList nList = document.getElementsByTagName("string");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				// Store each element key-value pair in map
				stringMap.put(eElement.getAttribute("name"), eElement.getTextContent());
			}
		}
		return stringMap;
	}

	// Method to get current date-time in a specific format
	public String dateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	// Log method without platform/device details
	public void log(String txt) {
		String msg = Thread.currentThread().getId() + ":" + Thread.currentThread().getStackTrace()[2].getClassName() + ":" + txt;

		System.out.println(msg);

		// Create a directory to store logs
		String strFile = "logs" + File.separator + dateTime();
		File logFile = new File(strFile);

		if (!logFile.exists()) {
			logFile.mkdirs();
		}

		try (FileWriter fileWriter = new FileWriter(logFile + File.separator + "log.txt", true);
			 PrintWriter printWriter = new PrintWriter(fileWriter)) {
			printWriter.println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Logger method for more advanced logging
	public Logger log() {
		return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
	}
}
