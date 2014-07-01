/*
	Serializer.java

    Assignment #3 - CS151 - SJSU
	By Luca Severini, Omari Straker, Syed Sarmad, Matt Szikley
	June-24-2014
*/

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

public class Serializer 
{
	/*
	 * Save the SlideShow object into a file whose path is provided
	 * @param SlideShow of object to save, path of the written file.
	 */
	public static int saveSlideToFile(SlideShow sShow, String filePath)
	{		
		if(sShow instanceof java.io.Serializable)
		{
			try
			{
				FileOutputStream fileOut = new FileOutputStream(filePath);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				
				out.writeObject(sShow);
				
				out.close();
				fileOut.close();
			}
			catch(IOException ex)
			{
				System.out.println("openSlideFromFile Exception: " + ex.getMessage());
				return 1;
			}
		}
		else
		{
			ArrayList<SlideImage> slides = sShow.getImages();

			try
			{
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
/*
					// Just for debug
					System.out.println("Slide " + slideIdx);
					System.out.println(imageData);
					System.out.println(slide.getCaption());
					System.out.println("");
*/
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(filePath));
				transformer.transform(source, result);
			}
			catch(Exception ex)
			{
				System.out.println("saveSlideToFile Exception: " + ex.getMessage());			
				return 1;
			}
		}
		
		System.out.println("SlideShow " + sShow.getName() + " saved in " + filePath);

		return 0;
	}
	
	/*
	 * Read a file whose path is provided and returns a SlideShow object if read/conversion is successfull
	 * @param path of the file to read from.
	 */
	public static SlideShow openSlideFromFile(String filePath)
	{
		File file = new File(filePath);
		if(!file.exists() || !file.isFile())
		{
			return null;
		}
		
		SlideShow sShow = new SlideShow();

		if(sShow instanceof java.io.Serializable)
		{
			try
			{
				FileInputStream fileIn = new FileInputStream(filePath);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				
				sShow = (SlideShow)in.readObject();
				
				in.close();
				fileIn.close();
			}
			catch(IOException | ClassNotFoundException ex)
			{
				System.out.println(ex.getMessage());
				return null;
			}
		}
		else
		{
			try
			{
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
							if(childNode.getNodeName().equals("name"))
							{
								sShow.setName(childNode.getTextContent());
							}
							else if(childNode.getNodeName().equals("slides"))
							{
								NodeList slides = childNode.getChildNodes();
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
												NamedNodeMap attribs = slideNode.getAttributes();											
												Node typeAttrib = attribs.getNamedItem("type");

												if(typeAttrib != null && typeAttrib.getNodeValue().equals("png"))
												{
													byte[] imageData = Base64.decodeBase64(slideNode.getTextContent());												
													ByteArrayInputStream inStream = new ByteArrayInputStream(imageData);
													BufferedImage image = ImageIO.read(inStream);
													if(image != null)
													{
														slide.setImage(image);
													}
													else
													{
														return null;
													}
												}
												else
												{
													return null;
												}
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
			}
			catch(Exception ex)
			{
				System.out.println("openSlideFromFile Exception: " + ex.getMessage());

				return null;
			}
		}
		
		return sShow;
	}
}
