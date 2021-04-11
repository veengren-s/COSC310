import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WindowTest {
	static Window window;

	@BeforeClass
	public static void setUp() {
		//Initialize the window before all other methods so we can use it
		try {
		window = new Window();
		List<Object[]> temp = window.CoRef("");

		}
		catch(Exception e){
			//If there is an error when creating the window for any reason the test fails
			fail(e.toString());
		}
	}


	@Test
	public void testResponse() throws IOException {
		// Tests for all core coded responses

		// Greetings
		assertEquals("Hello there, my name is Elon, What would you like to ask me today?",
				window.response("Hello", false));
		assertEquals("I'm very good, thank you.", window.response("How are you?", false));

		// Default
		String response = window.response("blam", false);
		assertTrue(response.equals(
				"Sorry, I didn't understand that. I may not have enough updates to understand what you \n\twere asking.")
				|| response.equals("Hmm... I'm not sure what you're asking me.")
				|| response.equals("Sorry, could you rephrase the question?  I didn't understand that.")
				|| response.equals("A ridiculous question like that doesn't deserve a response.")
				|| response.equals("I wish I knew how to answer that.")
				|| response.equals("I'm afraid I don't know what to say. Can you ask me something else?")
				|| response.equals("Well I'm speechless. Ask me something else."));

		// Thank you
		assertEquals("You're welcome", window.response("Thank you", false));

		// Career Facts
		assertEquals("My first company was Zip2, which eventually sold to Compaq for $307 million.",
				window.response("Zip2", false));
		assertEquals("My first company was Zip2, which eventually sold to Compaq for $307 million.",
				window.response("First business?", false));
		assertEquals("My first company was Zip2, which eventually sold to Compaq for $307 million.",
				window.response("First company?", false));
		assertEquals("I am the founder of Space Exploration technologies, better known as SpaceX",
				window.response("SpaceX", false));
		assertEquals("In 2008 I took over as CEO of Tesla.", window.response("Tesla", false));
	
     // Waiting to be resolved
	//	assertEquals(
	//			"I was the cofounder of X.com, which later merged with confinity to form paypal and was then \n\tsold to ebay for $1.5 Billion!",
	//			window.response("X.com", false));
		
		assertEquals(
				"I was the cofounder of X.com, which later merged with confinity to form paypal and was then \n\tsold to ebay for $1.5 Billion!",
				window.response("Confinity", false));
		assertEquals(
				"I was the cofounder of X.com, which later merged with confinity to form paypal and was then \n\tsold to ebay for $1.5 Billion!",
				window.response("Paypal", false));
		assertEquals(
				"I was the cofounder of X.com, which later merged with confinity to form paypal and was then \n\tsold to ebay for $1.5 Billion!",
				window.response("Ebay", false));
		assertEquals("In 2015 I co-founded OpenAI, a non profit reasearch company.", window.response("openAI", false));
		assertEquals("In 2016 I founded Neuralink, a company that focuses on bran-computer interactions.",
				window.response("NeuraLink", false));
		assertEquals("In 2006 I helped create SolarCity.", window.response("SolarCity", false));
		assertEquals("The main companies I have been involved in are: Zip2,SpaceX,Tesla,OpenAI,Neuralink and SolarCity",
				window.response("What companies", false));
		assertEquals("The main companies I have been involved in are: Zip2,SpaceX,Tesla,OpenAI,Neuralink and SolarCity",
				window.response("What companies", false));

		response = window.response("Favourite thing?", false);

		// General Interests
		assertTrue(response.equals("I'm a big fan of dogecoin, and all forms of cryptocurrency!")
				|| response.equals("Spaceships are cool I guess.")
				|| response.equals("I love cars! I remember when I bought my first McLaren F1.")
				|| response.equals("Spaceships are cool I guess.") || response.equals("I love anime!"));

		response = window.response("Favorite things?", false);

		assertTrue(response.equals("I'm a big fan of dogecoin, and all forms of cryptocurrency!")
				|| response.equals("Spaceships are cool I guess.")
				|| response.equals("I love cars! I remember when I bought my first McLaren F1.")
				|| response.equals("Spaceships are cool I guess.") || response.equals("I love anime!"));

		response = window.response("Favorite hobbies?", false);

		assertTrue(response.equals("I'm a big fan of dogecoin, and all forms of cryptocurrency!")
				|| response.equals("Spaceships are cool I guess.")
				|| response.equals("I love cars! I remember when I bought my first McLaren F1.")
				|| response.equals("Spaceships are cool I guess.") || response.equals("I love anime!"));

		// Interests Facts
		assertEquals("Probably Parasite, it was definitely the best movie of 2019.",
				window.response("Favourite movie?", false));
		assertEquals("Black Mirror, I really like the concepts it explores.",
				window.response("Favourite show?", false));
		assertEquals("Black Mirror, I really like the concepts it explores.",
				window.response("Favorite series?", false));
		assertEquals(
				"I really enjoyed Your Name, but I'm also a fan of Studio Ghibli. Princess Mononoke is one of my\n\tfavourite films by them.",
				window.response("Favorite anime?", false));
		assertEquals(
				"My favourite airplane is the SR-71 Blackbird. The A-XII in X AE A-XII is the predecessor to\n\tthis plane.",
				window.response("Favorite airplane?", false));
		assertEquals(
				"My favourite airplane is the SR-71 Blackbird. The A-XII in X AE A-XII is the predecessor to\n\tthis plane.",
				window.response("Favorite aircraft?", false));

		// Life Facts
		assertEquals("I was born in Pretoria, South Africa.", window.response("Where were you born", false));
		assertEquals("June 28 1971.", window.response("When were you born", false));
		assertEquals("Thank you for asking. I'm 49 now and will be 50 this year.",
				window.response("How old are you", false));
		assertEquals("Thank you for asking. I'm 49 now and will be 50 this year.",
				window.response("What is your age", false));
		assertEquals(
				"My mother was Maye and my father was Errol. I am not very fond\n\tof my father.",
				window.response("Who are your parents", false));
		assertEquals("I have two siblings, my sister Tosca and my brother Kimball",
				window.response("siblings", false));
		assertEquals("I have two siblings, my sister Tosca and my brother Kimball",
				window.response("brother", false));
		assertEquals("I have two siblings, my sister Tosca and my brother Kimball",
				window.response("sister", false));
		assertEquals(
				"I started university in Pretoria, and I later moved to Canada and went to Queens university. \n\tThen after two years I transferred to the University of Pennsylvania. \n\tAfter That I started my phd at stanford where I dropped out after two days.",
				window.response("education", false));
		assertEquals(
				"I started university in Pretoria, and I later moved to Canada and went to Queens university. \n\tThen after two years I transferred to the University of Pennsylvania. \n\tAfter That I started my phd at stanford where I dropped out after two days.",
				window.response("school", false));
		assertEquals(
				"My first wife's name was Justine Wilson and we were married from 2000-2008. We had 5 \n\tchildren. One of our kids Nevada unfortunately passed away due to sudden infant death syndrome.",
				window.response("first wife", false));
		assertEquals(
				"My first wife's name was Justine Wilson and we were married from 2000-2008. We had 5 \n\tchildren. One of our kids Nevada unfortunately passed away due to sudden infant death syndrome.",
				window.response("1st wife", false));
		assertEquals("My second wife's name was Talula Riley and we were married from 2010-2016.",
				window.response("2nd wife", false));
		assertEquals("My second wife's name was Talula Riley and we were married from 2010-2016.",
				window.response("second wife", false));
		assertEquals("I have had two wives but those ended in divorce. I am currently am dating the musician Grimes",
				window.response("wife", false));
		assertEquals("I have had two wives but those ended in divorce. I am currently am dating the musician Grimes",
				window.response("married", false));
		assertEquals(
				"I am currently dating the musician Grimes. We have one child together named X AE A-XII.\n\tWe had a fun time naming this one.",
				window.response("currently dating", false));
		assertEquals(
				"I am currently dating the musician Grimes. We have one child together named X AE A-XII.\n\tWe had a fun time naming this one.",
				window.response("Grimes", false));
		assertEquals("When I was 12 I sold my first game Blastar for $500.", window.response("first game", false));
		assertEquals("When I was 12 I sold my first game Blastar for $500.", window.response("first software", false));
		assertEquals("When I was 12 I sold my first game Blastar for $500.", window.response("first software", false));
		assertEquals("I taught myself to code when I was around 10 years old.",
				window.response("how did you learn to code", false));
		assertEquals("I taught myself to code when I was around 10 years old.",
				window.response("how did you learn programming", false));
		assertEquals("I taught myself to code when I was around 10 years old.",
				window.response("when did you learn to code", false));
		assertEquals("I taught myself to code when I was around 10 years old.",
				window.response("when did you learn to program", false));
		assertEquals(
				"I mainly spend my time between SpaceX and Tesla, and I'm heavily involved with the engineering decisions\n\tat those companies. I also spend a lot of my time at OpenAI too.",
				window.response("how do you spend your freetime?", false));
		assertEquals(
				"I mainly spend my time between SpaceX and Tesla, and I'm heavily involved with the engineering decisions\n\tat those companies. I also spend a lot of my time at OpenAI too.",
				window.response("how do you spend your time?", false));
		assertEquals(
				"I own a lot of cars, but mainly drive my Model S. Though I only drive Teslas now, I've owned a \n\t1978 BMW 320i and a 1967 Jaguar (E-type).",
				window.response("what cars do you own?", false));
		assertEquals(
				"I own a lot of cars, but mainly drive my Model S. Though I only drive Teslas now, I've owned a \n\t1978 BMW 320i and a 1967 Jaguar (E-type).",
				window.response("what car do you drive?", false));

		// Appearances/Interviews
		assertEquals(
				"I had a cameo in The Simpsons, The Big Bang theory, South Park, and Rick and Morty. Maybe\n\tyou've seen one of my episodes?",
				window.response("What shows have you appeared in?", false));
		assertEquals(
				"I had a cameo in The Simpsons, The Big Bang theory, South Park, and Rick and Morty. Maybe\n\tyou've seen one of my episodes?",
				window.response("What show have you appeared in?", false));
		assertEquals(
				"Yes, I was on Joe Rogan's podcast. In 2018 I think. We talked about all sorts of things, but I got\n\tin trouble for that one thing I did...",
				window.response("Joe Rogan", false));
   
	}
	// Tests the functionality of the Coreference resolution function
	@Test
	public void testCoref() {
		try {
			// test 1
			List<Object[]> a = window.CoRef("Bob is cool. Be like him.");
			assertNotNull(a);
			assertEquals(a.get(0)[0].toString(),"Bob");
			assertEquals(a.get(0)[1].toString(),"him");
			//test 2
			a = window.CoRef("I think Jane is smart. Dont be like her.");
			assertNotNull(a);
			assertEquals(a.get(0)[0].toString(),"Jane");
			assertEquals(a.get(0)[1].toString(),"her");
			//test 3
			a = window.CoRef("Jane is cool. Bob is smart. be like like him and her.");
			assertNotNull(a);
			assertEquals(a.get(0)[0].toString(),"Jane");
			assertEquals(a.get(0)[1].toString(),"her");
			assertEquals(a.get(1)[0].toString(),"Bob");
			assertEquals(a.get(1)[1].toString(),"him");
			}catch(Exception e) {
				fail(e.toString());
		}
	}

	//tests the functionality of the replace function
	@Test
	public void testReplace() {
		//test 1
		Object[] b = {"Bob","him"};
		String a = window.replace("Bob is cool. Be like him.",b);
		assertEquals("Bob is cool. Be like Bob.",a);
		//test 2
		Object[] c= {"Jeff Bezos","him"};
		a = window.replace("Do you know Jeff Bezos. Do you like him?",c);
		assertEquals("Do you know Jeff Bezos. Do you like Jeff Bezos?",a);
		//test 3
		Object[] d = {"Grimes","she"};
		a = window.replace("Are you dating Grimes. Where was she born.",d);
		assertEquals("Are you dating Grimes. Where was Grimes born.",a);
	}
 
	@Test
	public void testGetNameEntityList() {
		
		try {
			// check for errors when there is no entity inputed
			List<String> namedEntities = window.getNameEntityList("");
			
			// check to see if an actual list is returned and it is not null
			assertNotNull(namedEntities);
			
			// check to see if entering nothing results in an empty list
			assertTrue("Entering nothing results in an empty namedEntitieslist", namedEntities.isEmpty());
			
			// Check if the well known entity tesla is returned in the list of named
			namedEntities = window.getNameEntityList("Tesla");
			assertTrue(namedEntities.contains("tesla"));

			// test for multiple entities
			namedEntities = window.getNameEntityList("Japan, China, Korea, India, United Kingdom, Scotland");
			assertTrue(namedEntities.contains("japan") && namedEntities.contains("china")
					&& namedEntities.contains("korea") && namedEntities.contains("india")
					&& namedEntities.contains("united kingdom") && namedEntities.contains("scotland"));
		
		}
		catch(Exception e){
			//if there is an exception for any reason the test fails
			fail(e.toString());	
		}
	
	}
	
	
	@After 
	public void testDisableOnQ() throws IOException {
		window.response("q", false);
		//check that the window.input is disabled upon entering q
		assertFalse(window.input.isEnabled());
		
	}


}
