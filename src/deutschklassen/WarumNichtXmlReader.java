/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deutschklassen;

import Interface.IWarumNichtXmlReader;
import dto.DeutschChapterDataDto;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class WarumNichtXmlReader implements IWarumNichtXmlReader {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final String filePath;
    private final File file;

    //an instance of factory that gives a document builder  
    private final DocumentBuilderFactory dbf;
    private DocumentBuilder db;

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public WarumNichtXmlReader(String filePath) {
        this.filePath = filePath;
        this.file = new File(this.filePath);
        this.dbf = DocumentBuilderFactory.newInstance();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public String getRootName() throws Exception {
        try {
            this.db = dbf.newDocumentBuilder();
            Document doc = db.parse(this.file);
            doc.getDocumentElement().normalize();
            String rootName = doc.getDocumentElement().getNodeName();
            return rootName;
        } catch (IOException ex) {
            System.out.println("IOException " + ex.getMessage());
            throw new IOException("Problem while reading the file.. please check the file name, file path again");
        } catch (ParserConfigurationException ex) {
            System.out.println("ParserConfigurationException " + ex.getMessage());
            throw new IOException("Problem while parsing the file..");
        }
    }

    @Override
    public ArrayList<DeutschChapterDataDto> getLessonsLinksWithTag(String tag) throws IOException, SAXException, ParserConfigurationException {
        try {
            ArrayList links = new ArrayList<String>(); // returned array
            this.db = dbf.newDocumentBuilder();
            Document doc = db.parse(this.file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName(tag);
            String rootName = doc.getDocumentElement().getNodeName();

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String classLink = eElement.getElementsByTagName("link").item(0).getTextContent();
                    String classTitle = eElement.getElementsByTagName("title").item(0).getTextContent();
                    String classDescription = eElement.getElementsByTagName("description").item(0).getTextContent();
                    DeutschChapterDataDto data = new DeutschChapterDataDto(classLink, classTitle, classDescription);
                    links.add(data);
                }
            }
            return links;
        } catch (IOException ex) {
            System.out.println("IOException " + ex.getMessage());
            throw new IOException("Problem while reading the file.. please check the file name, file path again");
        } catch (ParserConfigurationException ex) {
            System.out.println("ParserConfigurationException " + ex.getMessage());
            throw new IOException("Problem while parsing the file..");
        } catch (SAXException ex) {
            throw new SAXException("Problem while parsing the file..");
        }
    }

    @Override
    public String gtFilePath() {
        return this.filePath;
    }

//</editor-fold>
}
