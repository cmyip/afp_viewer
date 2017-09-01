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

import junit.framework.TestCase;
import me.lumpchen.xafp.render.RenderParameter;
import me.lumpchen.xafp.tool.AFPTool;

public class TestCase0 extends TestCase {

	private static Logger logger = Logger.getLogger(TestCase0.class.getName());
	
	private static final int TIMEOUT = 10000;
	private static final int CPU_CORE_NUM = Runtime.getRuntime().availableProcessors();
	File root = new File("C:/dev/xdiff/testcases/xafp");
	
	public void test_img() {
		assertTrue(compare("img.afp"));
	}
	
	public void test_X2() {
		assertTrue(compare("x2.afp"));
	}
	
	public void test_X80_2C() {
		assertTrue(compare("X80_2C.afp"));
	}
	
	public void test__provini_1() {
		assertTrue(compare("_provini (1).afp"));
	}
	
	public void test_provini() {
		assertTrue(compare("_provini.afp"));
	}
	
	public void test_ttf() {
		assertTrue(compare("/font/ttf.afp"));
	}
	
	public void test_ttc() {
		assertTrue(compare("/font/ttc.afp"));
	}
	
	public void test_ttf_courier() {
		assertTrue(compare("/font/ttf_courier.afp"));
	}
	
	public void test_97376() {
		assertTrue(compare("97376.afp"));
	}
	
	public void test_Letter_Ref() {
		assertTrue(compare("/oc_samples/Letter_Ref.afp"));
	}
	
	public void test_Bank_Statement_REF() {
		assertTrue(compare("/oc_samples/Bank_Statement_REF.afp"));
	}
	
	private boolean compare(String afpName) {
		String s = this.root.getAbsolutePath() + "/" + afpName.substring(0, afpName.length() - 4);
		File outputFolder = new File(s);
		
		RenderParameter para = new RenderParameter();
		para.usePageResolution = true;
		
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		} else {
			File[] files = outputFolder.listFiles();
			for (File f : files) {
				if (f.isFile()) {
					f.delete();
				}
			}
		}
		
		File afpFile = new File(this.root, afpName);
		try {
			logger.info("Start rendering " + afpFile.getAbsolutePath());
			AFPTool.render(afpFile, outputFolder, para, "jpg");
			logger.info("Complete rendering: " + afpFile.getAbsolutePath());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Render fail: " + afpFile.getAbsolutePath(), e);
			return false;
		}
		
		boolean result = true;
		try {
			logger.info("Start comparing bitmaps: " + outputFolder.getAbsolutePath());
			result &= compare(outputFolder, "jpg");
			logger.info("Complete comparing bitmaps: " + outputFolder.getAbsolutePath());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Bitmap compare fail: " + afpFile.getAbsolutePath(), e);
			result = false;
		}
		assertTrue(result);
		return result;
	}
	
	private boolean compare(File outputFolder, final String imageFomat) throws InterruptedException, ExecutionException {
		final File baseFolder = new File(outputFolder, "baseline");
		if (!baseFolder.exists()) {
			logger.severe("Not find baseline folder: " + baseFolder.getAbsolutePath());
			return false;
		}
		
		final File[] images = outputFolder.listFiles(new FileFilter() {

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
					Map<String, Boolean> ret = compare(begin, end, images, baseFolder);
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
	
	private Map<String, Boolean> compare(int begin, int end, File[] images, File baseFolder) {
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>(end - begin + 1);
		for (int i = begin; i <= end; i++) {
			File img = images[i];
			String name = img.getName();
			File baseline = new File(baseFolder, name);
			if (!baseline.exists()) {
				logger.severe("Not find baseline image: " + baseline.getAbsolutePath());
				resultMap.put(name, false);
				continue;
			}
			
			BufferedImage bimg1;
			BufferedImage bimg2;
			BufferedImage diff;
			try {
				bimg1 = ImageIO.read(new FileInputStream(baseline));
				bimg2 = ImageIO.read(new FileInputStream(img));
				diff = BitmapComparor.diffImages(bimg1, bimg2, "black");
				if (diff != null) {
					logger.log(Level.WARNING, "Different with baseline: " + baseline.getAbsolutePath());
					
					ImageIO.write(diff, "jpg", new File(img.getParentFile(), img.getName() + ".diff.jpg"));
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
	
	final static class CompareResult {
		public String baseImageName;
		public String testImageName;
		public boolean isSame;
	}
	
}
