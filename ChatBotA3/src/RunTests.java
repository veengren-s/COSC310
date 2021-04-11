import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class RunTests {

	public static void main(String[] args) {

		// Test each test class, print failures and display whether test is successful. 
		
		Result resultWindowTest = JUnitCore.runClasses(WindowTest.class);
		Result resultPipelineTest = JUnitCore.runClasses(PipelineTest.class);

		for (Failure failure : resultWindowTest.getFailures()) {
			System.out.println(failure.toString());
		}

		for (Failure failure : resultPipelineTest.getFailures()) {
			System.out.println(failure.toString());
		}

		if (resultWindowTest.wasSuccessful())
			System.out.println("Window test was successful!");
		else
			System.out.println("Window test was unsuccessful.");

		if (resultPipelineTest.wasSuccessful())
			System.out.println("Pipeline test was successful!");
		else
			System.out.println("Pipeline test was unsuccessful.");
		
		//close the chatbot, otherwise the program will continue running
		WindowTest.window.dispose();
		
		
	}
	}


