package me.lumpchen.afp.test;

import java.io.StringWriter;

import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MetaUtil {

	private static final Log LOG = LogFactory.getLog(MetaUtil.class);

	static final String SUN_TIFF_FORMAT = "com_sun_media_imageio_plugins_tiff_image_1.0";
	static final String JPEG_NATIVE_FORMAT = "javax_imageio_jpeg_image_1.0";
	static final String STANDARD_METADATA_FORMAT = "javax_imageio_1.0";

	private MetaUtil() {
	}

	// logs metadata as an XML tree if debug is enabled
	static void debugLogMetadata(IIOMetadata metadata, String format) {
		if (!LOG.isDebugEnabled()) {
			return;
		}

		// see http://docs.oracle.com/javase/7/docs/api/javax/imageio/
		// metadata/doc-files/standard_metadata.html
		IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(format);
		try {
			StringWriter xmlStringWriter = new StringWriter();
			StreamResult streamResult = new StreamResult(xmlStringWriter);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			// see http://stackoverflow.com/a/1264872/535646
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource domSource = new DOMSource(root);
			transformer.transform(domSource, streamResult);
			LOG.debug("\n" + xmlStringWriter);
		} catch (IllegalArgumentException ex) {
			LOG.error(ex, ex);
		} catch (TransformerException ex) {
			LOG.error(ex, ex);
		}
	}

}
