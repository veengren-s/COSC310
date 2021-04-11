import static org.junit.Assert.*;

//import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

//make sure we run in order
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class PipelineTest {
	static Pipeline pipeline;

	//Run before all test methods to setup a pipeline to be used by other methods
	@BeforeClass
	public static void PipelineTest() {
		try {
		//test constructor and there are no exceptions on creation
		pipeline = new Pipeline("tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote, sentiment",
				"coref.algorithm", "neural");
		}
		catch(Exception e) {
			fail("Exception creating pipeline: " + e.toString());
		}
		
	}

	//test the getPipe method
	@Test
	public void getPipeTest() {
		//make sure something is returned
        assertNotNull("Pipeline returns a standfordCoreObject", pipeline.getPipe());
	}

}
