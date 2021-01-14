/*
 * MIT License
 *
 * Copyright (c) 2020 Đình Tài
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.phoenix.common.loader;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XmlLoaderTest {
    private Element getRootElement(String fileName) throws ParserConfigurationException, IOException, SAXException {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(classLoader.getResource(fileName).getFile());

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        return doc.getDocumentElement();
    }//NOPMD

    @Test
    public void testParse() throws IOException, SAXException, ParserConfigurationException {
        Element element = getRootElement("xml/test-xml-01.xml");
        element.normalize();

        Element datasourceElement = (Element) element.getElementsByTagName("data-source").item(0);
        Element connectionPoolElement = (Element) element.getElementsByTagName("connection-pool").item(0);
        Element jpaElement = (Element) element.getElementsByTagName("jpa").item(0);

        NodeList datasourceProps = datasourceElement.getElementsByTagName("property");
        NodeList connectionPoolProps = connectionPoolElement.getElementsByTagName("property");
        NodeList jpaProps = jpaElement.getElementsByTagName("property");

        for (int i = 0; i < datasourceProps.getLength(); i++) {
            Element property = (Element) datasourceProps.item(i);

            System.out.println("Name : "
                    + property.getAttribute("name") + ", value: " + property.getAttribute("value"));
        }
//NOPMD
        System.out.println("============================================================================");

        for (int i = 0; i < connectionPoolProps.getLength(); i++) {
            Element property = (Element) connectionPoolProps.item(i);//NOPMD

            System.out.println("Name : "
                    + property.getAttribute("name") + ", value: " + property.getAttribute("value"));

            NodeList props = property.getElementsByTagName("prop");//NOPMD

            for (int j = 0; j < props.getLength(); j++) {
                Element prop = (Element) props.item(j);

                System.out.println("Name : "
                        + prop.getAttribute("name") + ", value: " + prop.getAttribute("value"));
            }
        }//NOPMD

        System.out.println("============================================================================");

        for (int i = 0; i < jpaProps.getLength(); i++) {
            Element property = (Element) jpaProps.item(i);//NOPMD

            System.out.println("Name : "
                    + property.getAttribute("name") + ", value: " + property.getAttribute("value"));
        }
//NOPMD
    }

}



