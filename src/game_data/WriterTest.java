package game_data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * JUnit test for the writer
 * @author shichengrao
 *
 */
public class WriterTest {

	private Writer myWriter = new Writer();
	private Logger logger = Logger.getLogger(WriterTest.class.getName());
	public static final int SAMPLE_DATA = 3;
	@Test
	public void testCorrectWriting() {
		List<Object> stuff = new ArrayList<>();
		stuff.add("hi");
		stuff.add(SAMPLE_DATA);
		try {
			myWriter.write("src/game_data/test", stuff);
		} catch (IOException e) {
			logger.log(Level.WARNING, "IOException for writer");
			fail("we fucked up");
		}
		File file = new File("src/game_data/test");
		assertEquals(true,file.exists());
	}
	@Test
	public void testFileNotExist() {
		List<Object> stuff = new ArrayList<>();
		stuff.add("hi");
		stuff.add(SAMPLE_DATA);
		try {
			myWriter.write("\\\\/:*AAAAA?\\\"<>|3*7.pdf", stuff);
			fail("we fucked up");
		} catch (IOException e) {
			logger.log(Level.WARNING, "IOException for writer");
			return;
		}
		fail("we fucked up");
	}

}
