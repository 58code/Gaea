package Serializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.*;
import com.bj58.spat.gaea.serializer.serializer.Serializer;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class OtherTest {

	/**
	 * Test of WriteObject method, of class Int32Serializer.
	 */
	@org.junit.Test
	public void testWriteObject() throws Exception {
		Map<Object, Integer> map = new HashMap<Object, Integer>();
		List l1 = new ArrayList();
		List l2 = new ArrayList();
		map.put(l1, 1);
		int aaa = map.get(l2);
		boolean cc = map.keySet().contains(l2);

		assertTrue(l1 == l2);

	}

	public void getRes() throws IOException, URISyntaxException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> resourceUrls = cl.getResources("com/bj58/");
		Set<URL> result = new LinkedHashSet<URL>(16);
		while (resourceUrls.hasMoreElements()) {
			URL url = (URL) resourceUrls.nextElement();
			// result.add(convertClassLoaderURL(url));
			result.add(url);

		}
		List<File> classes = new ArrayList<File>();
		for (URL url : result) {
			if (url == null) {
				continue;
			}
			String filePath = url.toURI().getPath();
			if (filePath == null) {
				continue;
			}
			File f = new File(filePath);
			String path = f.getAbsolutePath();
			path = path.replace(File.separator, "/");

			getClassFile(f, classes);

			String str = "";
		}
	}

	public void getClassFile(File f, List<File> classes)
			throws MalformedURLException, URISyntaxException {
		if (f.isDirectory()) {
			File[] cfs = f.listFiles();
			for (File cf : cfs) {
				getClassFile(cf, classes);
			}
		} else {
			if (f.getName().endsWith(".class")) {
				classes.add(f);
			}
		}
	}
}
