/*
	Serializer.java

    Assignment #3 - CS151 - SJSU
	By Luca Severini, Omari Straker, Syed Sarmad, Matt Szikley
	June-24-2014
*/

//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Serializer -----------------------------------------------------
public class Serializer 
{
	public static int saveSlideToFile(Object obj, String filePath) throws Exception
	{
		if(!(obj instanceof SlideShow))
		{
			throw new Exception();
		}
		
		SlideShow sShow = (SlideShow)obj;
		ArrayList<SlideImage> slides = sShow.getImages();
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("slideshow");
		doc.appendChild(rootElement);
		
		Element nameElem = doc.createElement("name");
		nameElem.setTextContent(sShow.getName());
		rootElement.appendChild(nameElem);
	
		Element slidesElem = doc.createElement("slides");
		rootElement.appendChild(slidesElem);

		int slideIdx = 0;
		for(SlideImage slide : slides)
		{
			slideIdx++;
				
			Element slideElem = doc.createElement("slide");
			slidesElem.appendChild(slideElem);
			
			Attr indexAttr = doc.createAttribute("index");
			indexAttr.setValue(String.valueOf(slideIdx));
			slideElem.setAttributeNode(indexAttr);

			BufferedImage image = slide.getImage();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outStream);		
			String imageData = Base64.encodeBase64String(outStream.toByteArray());

			Element imageElem = doc.createElement("image");
			slideElem.appendChild(imageElem);

			Attr typeAttr = doc.createAttribute("type");
			typeAttr.setValue("png");
			imageElem.setAttributeNode(typeAttr);

			Element dataElem = doc.createElement("data");
			dataElem.setTextContent(imageData);
			imageElem.appendChild(dataElem);

			Element captionElem = doc.createElement("caption");
			captionElem.setTextContent(slide.getCaption());
			imageElem.appendChild(captionElem);

			// Just for debug
			System.out.println("Slide " + slideIdx);
			System.out.println(imageData);
			System.out.println(slide.getCaption());
			System.out.println("");
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filePath));
		transformer.transform(source, result);

		// Output to console for testing
		result = new StreamResult(System.out);
		transformer.transform(source, result);
		
		System.out.println("SlideShow " + sShow.getName() + " saved in " + filePath);

		return 1;
	}
	
	public static Object openSlideFromFile(String filePath) throws Exception
	{
		File file = new File(filePath);
		if(!file.exists() || !file.isFile())
		{
			return null;
		}
		
		SlideShow sShow = new SlideShow();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
 
		doc.getDocumentElement().normalize();
 
		System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
 
		NodeList nodes = doc.getElementsByTagName("slideshow");
		
		for (int nodeIdx = 0; nodeIdx < nodes.getLength(); nodeIdx++)
		{
			Node node = nodes.item(nodeIdx);
			if(node.getNodeName().equals("slideshow"))
			{
				NodeList children = node.getChildNodes();
				for (int childIdx = 0; childIdx < children.getLength(); childIdx++)
				{
					Node childNode = children.item(childIdx);
					if(node.getNodeName().equals("name"))
					{
						sShow.setName(node.getTextContent());
					}
					else if(node.getNodeName().equals("slides"))
					{
						NodeList slides = node.getChildNodes();
						for (int slideIdx = 0; slideIdx < slides.getLength(); slideIdx++)
						{
							Node slidesNodes = slides.item(slideIdx);
							if(slidesNodes.getNodeName().equals("slide"))
							{
								NodeList slideNodes = slidesNodes.getChildNodes();
								for (int slideNodeIdx = 0; slideNodeIdx < slideNodes.getLength(); slideNodeIdx++)
								{
									SlideImage slide = new SlideImage();
									
									Node slideNode = slideNodes.item(slideNodeIdx);
									if(slideNode.getNodeName().equals("image"))
									{
										/*
										NamedNodeMap attribs = slideNode.getAttributes();
										if(attribs.)
										{
										}
										
										byte[] imageData = Base64.decodeBase64(slideNode.getTextContent());
										
										BufferedImage image = new BufferedImage();
										*/
										//slide.setImage(image);
									}
									else if(slideNode.getNodeName().equals("caption"))
									{
										slide.setCaption(slideNode.getTextContent());
									}
									
									sShow.addSlide(slide);
								}
							}
						}
					}
				}
			}
 		}

		return sShow;
	}
}
