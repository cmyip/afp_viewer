package me.lumpchen.afp.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ServiceRegistry;

import junit.framework.TestCase;
import me.lumpchen.xafp.render.RenderParameter;
import me.lumpchen.xafp.tool.AFPTool;

public class TestCase0 extends TestCase {

	private static Logger logger = Logger.getLogger(TestCase0.class.getName());
	
	private static final int TIMEOUT = 10000;
	private static final int CPU_CORE_NUM = Runtime.getRuntime().availableProcessors();
//	File root = new File("C:/dev/xdiff/testcases/xafp");
	
	private static final File root = new File("src/test/resources/testcases/xafp");
	
	private static <T> T lookupProviderByName(final ServiceRegistry registry, final String providerClassName) {
	    try {
	        return (T) registry.getServiceProviderByClass(Class.forName(providerClassName));
	    } catch (ClassNotFoundException ignore) {
	        return null;
	    }
	}
	
	protected void setUp() throws Exception {
		Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
		while (readers.hasNext()) {
		    System.out.println("reader: " + readers.next());
		}
		
		IIORegistry registry = IIORegistry.getDefaultInstance();
		ImageReaderSpi sunProvider = lookupProviderByName(registry, "com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi");
		ImageReaderSpi twelvemonkeysProvider = lookupProviderByName(registry, "com.twelvemonkeys.imageio.plugins.jpeg.JPEGImageReaderSpi");
		
		registry.setOrdering(ImageReaderSpi.class, twelvemonkeysProvider, sunProvider);
		
		readers = ImageIO.getImageReadersByFormatName("JPEG");
		System.out.println("reader: " + readers.next());
	}
	
	public void test_img() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = false;
		para.resolution = 96f;
		
		compare("img.afp", para);
	}
	
	public void test_fillet() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = false;
		para.resolution = 96f;
		
		compare("fillet.afp", para);
	}
	
	public void test_X2() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = false;
		para.resolution = 96f;
		
		compare("x2.afp", para);
	}
	
	public void test_X80_2C() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = true;
		
		compare("X80_2C.afp", para, 1, 10);
	}
	
	public void test__provini_1() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = true;
		
		compare("_provini (1).afp", para, 1, 15);
	}
	
	public void test_provini() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = true;
		
		compare("_provini.afp", para, 1, 15);
	}
	
	public void test_ttf() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = false;
		para.resolution = 96f;
		
		compare("/font/ttf.afp", para);
	}
	
	public void test_ttc() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = false;
		para.resolution = 96f;
		
		compare("/font/ttc.afp", para);
	}
	
	public void test_ttf_courier() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = false;
		para.resolution = 96f;
		
		compare("/font/ttf_courier.afp", para);
	}
	
	public void test_97376() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = true;
		
		compare("97376.afp", para);
	}
	
	public void test_Letter_Ref() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = true;
		
		compare("/oc_samples/Letter_Ref.afp", para);
	}
	
	public void test_Bank_Statement_REF() {
		RenderParameter para = new RenderParameter();
		para.usePageResolution = true;
		
		compare("/oc_samples/Bank_Statement_REF.afp", para, 1, 10);
	}
	
	private boolean compare(String afpName, RenderParameter para) {
		return this.compare(afpName, para, -1, -1);
	}
	
	private boolean compare(String afpName, RenderParameter para, int from, int to) {
		String s = root.getAbsolutePath() + "/" + afpName.substring(0, afpName.length() - 4);
		File caseFolder = new File(s);
		File testFolder = new File(caseFolder, "_test");
		
		if (!testFolder.exists()) {
			testFolder.mkdirs();
		} else {
			File[] files = testFolder.listFiles();
			for (File f : files) {
				if (f.isFile()) {
					f.delete();
				}
			}
		}
		
		File afpFile = new File(root, afpName);
		try {
			logger.info("Start rendering " + afpFile.getAbsolutePath());
			AFPTool.renderQuick(afpFile, testFolder, para, "jpg", from, to);
			logger.info("Complete rendering: " + afpFile.getAbsolutePath());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Render fail: " + afpFile.getAbsolutePath(), e);
			fail("Render fail: " + afpFile.getAbsolutePath());
			return false;
		}
		
		boolean result = true;
		try {
			logger.info("Start comparing bitmaps: " + caseFolder.getAbsolutePath());
			result &= compare(caseFolder, "jpg");
			logger.info("Complete comparing bitmaps: " + caseFolder.getAbsolutePath());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Bitmap compare fail: " + afpFile.getAbsolutePath(), e);
			fail("Bitmap compare fail: " + afpFile.getAbsolutePath());
			result = false;
		}
		
		assertTrue(afpFile.getAbsolutePath(), result);
		return result;
	}
	
	private boolean compare(final File caseFolder, final String imageFomat) throws InterruptedException, ExecutionException {
		final File baseFolder = new File(caseFolder, "baseline");
		if (!baseFolder.exists()) {
			logger.severe("Not find baseline folder: " + baseFolder.getAbsolutePath());
			return false;
		}
		
		File testFolder = new File(caseFolder, "_test");
		final File[] images = testFolder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith("." + imageFomat)) {
					return true;
				}
				return false;
			}
			
		});
		
		final List<Callable<Map<String, Boolean>>> partitions = new ArrayList<Callable<Map<String, Boolean>>>();
		int total = images.length;
    	int partition = CPU_CORE_NUM * 2;
    	int pagesPerPartition = total / partition;
    	if (total <= partition) {
    		pagesPerPartition = 1;
    		partition = total;
    	} else {
    		pagesPerPartition = total / partition + 1;
    	}
    	for (int i = 0; i < partition; i++) {
    		final int begin = i * pagesPerPartition;
    		if (begin >= total) {
    			break;
    		}
    		int nend = begin + pagesPerPartition - 1;
    		final int end = nend > (total - 1) ? (total - 1) : nend;
    		
    		partitions.add(new Callable<Map<String, Boolean>>() {
				@Override
				public Map<String, Boolean> call() throws Exception {
					Map<String, Boolean> ret = compare(begin, end, images, caseFolder);
					return ret;
				}
    		});
    	}
		
    	final ExecutorService executorPool = Executors.newFixedThreadPool(CPU_CORE_NUM);
		final List<Future<Map<String, Boolean>>> resultFromParts = executorPool.invokeAll(partitions, TIMEOUT, TimeUnit.SECONDS);
		executorPool.shutdown();
		
		boolean result = true;
		for (final Future<Map<String, Boolean>> ret : resultFromParts) {
			Map<String, Boolean> map = ret.get();
			Iterator<Entry<String, Boolean>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Boolean> entry = it.next();
				String name = entry.getKey();
				boolean value = entry.getValue();
				result &= value;
				System.out.println(name + " : " + (value ? "identity" : "different"));
			}
		}
		
		return result;
	}
	
	private Map<String, Boolean> compare(int begin, int end, File[] images, File caseFolder) {
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>(end - begin + 1);
		for (int i = begin; i <= end; i++) {
			File img = images[i];
			String name = img.getName();
			
			File baseFolder = new File(caseFolder, "baseline");
			File baseline = new File(baseFolder, name);
			if (!baseline.exists()) {
				logger.severe("Not find baseline image: " + baseline.getAbsolutePath());
				resultMap.put(name, false);
				continue;
			}
			
			File testFolder = new File(caseFolder, "_test");
			BufferedImage bimg1;
			BufferedImage bimg2;
			BufferedImage diff;
			try {
				bimg1 = ImageIO.read(new FileInputStream(baseline));
				bimg2 = ImageIO.read(new FileInputStream(img));
				diff = BitmapComparor.diffImages(bimg1, bimg2, "black");
				if (diff != null) {
					logger.log(Level.WARNING, "Different with baseline: " + baseline.getAbsolutePath());
					
					ImageIO.write(diff, "jpg", new File(testFolder, img.getName() + ".diff.jpg"));
					resultMap.put(name, false);
				} else {
					resultMap.put(name, true);
					logger.log(Level.INFO, "Same as baseline: " + baseline.getAbsolutePath());
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "bitmap generation fail: " + img.getAbsolutePath(), e);
				resultMap.put(name, false);
			}
		}
		return resultMap;
	}
	
}
