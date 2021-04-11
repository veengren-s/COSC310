import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class Pipeline{

	private Properties props;
	private StanfordCoreNLP pipe;

	public Pipeline(String listofannotators, String annotator, String annotatorarguement ) {
		// set up pipeline properties 
		props = new Properties();
		// set the list of annotators to run using constructor arguement
		props.setProperty("annotators", listofannotators);
		// set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
		props.setProperty(annotator, annotatorarguement);
		// build pipeline
		pipe = new StanfordCoreNLP(props);
	}

	public StanfordCoreNLP getPipe() {
		return pipe;
	}
}

