import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.pipeline.*;

public class Window extends JFrame implements KeyListener{
	//Here we make a window that will contain our text area box and the input box at the bottom as well as a scroll bar the shows up when needed
	JPanel pane= new JPanel();
	JTextArea talkArea= new JTextArea(25, 73);
	JTextArea input= new JTextArea(2,65);
	JScrollPane sideBar= new JScrollPane(talkArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	//This is to load the image of the bot into an icon form
	ImageIcon icon = new ImageIcon("img/bot.png");
	Detection Source;
	//Variable to keep track how positive vs. negative the conversation is
	int Sentiment=0;

	Pipeline pipeline;
	List<Object[]> temp = CoRef("");




	//This 2D array will contain the bots responses
	String[][] Responses= {
			//greeting
			{"Hello there, my name is Elon, What would you like to ask me today?", "I'm very good, thank you."},
			//Thank you
			{"You're welcome"},
			//Default
			{"Sorry, I didn't understand that. I may not have enough updates to understand what you \n\twere asking.", 
			 	"Hmm... I'm not sure what you're asking me.", 
			 	"Sorry, could you rephrase the question?  I didn't understand that.", 
			 	"A ridiculous question like that doesn't deserve a response.", 
			 	"I wish I knew how to answer that.", 
			 	"I'm afraid I don't know what to say. Can you ask me something else?", 
			 	"Well I'm speechless. Ask me something else.",""},
			//Goodbye
			{"It was a pleasure to talk to you","Have a great day","See you later","Goodbye", "We must colonize Mars!"},
			//Career Facts
			{"My first company was Zip2, which eventually sold to Compaq for $307 million.",
				"I am the founder of Space Exploration technologies, better known as SpaceX",
				"In 2008 I took over as CEO of Tesla.",
				"I was the cofounder of X.com, which later merged with confinity to form paypal and was then \n\tsold to ebay for $1.5 Billion!",
				"In 2015 I co-founded OpenAI, a non profit reasearch company.",
				"In 2016 I founded Neuralink, a company that focuses on bran-computer interactions.",
				"In 2006 I helped create SolarCity.",
			"The main companies I have been involved in are: Zip2,SpaceX,Tesla,OpenAI,Neuralink and SolarCity"},
			//general random interests
			{ "I'm a big fan of dogecoin, and all forms of cryptocurrency!", "Spaceships are cool I guess.",
				"I love cars! I remember when I bought my first McLaren F1.", "I love anime!" },
			// Interests facts
			{ "Probably Parasite, it was definitely the best movie of 2019.",
					"Black Mirror, I really like the concepts it explores.",
					"I really enjoyed Your Name, but I'm also a fan of Studio Ghibli. Princess Mononoke is one of my\n\tfavourite films by them.",
			"My favourite airplane is the SR-71 Blackbird. The A-XII in X AE A-XII is the predecessor to\n\tthis plane." },
			//Life Facts
			{"I was born in Pretoria, South Africa.", "June 28 1971.","Thank you for asking. I'm 49 now and will be 50 this year.", 
				"My mother was Maye and my father was Errol. I am not very fond\n\tof my father.","I have two siblings, my sister Tosca and my brother Kimball",
				"I started university in Pretoria, and I later moved to Canada and went to Queens university. \n\tThen after two years I transferred to the University of Pennsylvania. \n\tAfter That I started my phd at stanford where I dropped out after two days.",
				"I have had two wives but those ended in divorce. I am currently am dating the musician Grimes",
				"My first wife's name was Justine Wilson and we were married from 2000-2008. We had 5 \n\tchildren. One of our kids Nevada unfortunately passed away due to sudden infant death syndrome.",
				"My second wife's name was Talula Riley and we were married from 2010-2016.",
				"I am currently dating the musician Grimes. We have one child together named X AE A-XII.\n\tWe had a fun time naming this one.",
				"When I was 12 I sold my first game Blastar for $500.",
				"I taught myself to code when I was around 10 years old.",
				"I mainly spend my time between SpaceX and Tesla, and I'm heavily involved with the engineering decisions\n\tat those companies. I also spend a lot of my time at OpenAI too.",
				"I own a lot of cars, but mainly drive my Model S. Though I only drive Teslas now, I've owned a \n\t1978 BMW 320i and a 1967 Jaguar (E-type).",
			"She is from Canada originally. She was born and raised in Vancouver."},
			//Appearances/Interviews
			{ "I had a cameo in The Simpsons, The Big Bang theory, South Park, and Rick and Morty. Maybe\n\tyou've seen one of my episodes?",
			"Yes, I was on Joe Rogan's podcast. In 2018 I think. We talked about all sorts of things, but I got\n\tin trouble for that one thing I did..." },
			{"I love to travel though.", "There's so many places to visit aren't there?", "Going to different places changes a man.", "War... war never changes but men do, through the places they've been"},
			//Positive sentiment responses
			{"Glad to hear that you are feeling positive.","You seem to be in a good mood!", "You're annoyingly chipper.", "Wow you're a cheerful one.", "I think this is going really well!", "I appreciate your positivity."},
			//Negative sentiment responses
			{"I am sorry it seems that you are a bit negative.","Well aren't you just a regular bowl of sunshine.","Are you having a bad day?","You seem to be upset.","Did I say something to offend you?","You're not a very pleasant person to converse with."},
			//Bezos rivalry
			{"Yes we have met before. If I recall correctly, we met in 2004 to talk about space. That meeting didn't go \n\tso well",
				"Like him? We have a bit of a rivalry going you know, so things can be a little tense\n\t between us I'd say.",
				"To be honest, I do not like that his company Amazon has become a monopoly, and I also think he is a \n\tcopycat for his self-driving car interests.",
				"Yeah, him and I are not on the greatest terms, and our spaceflight companies are always competing, so I \n\twould say we are rivals.",
				"We have been rivals ever since a bad meeting in 2004, and it's only grown from there.",
				"We have had some disgreements over the years and our spaceflight companies have butted heads a few times \n\tbecause they are always competing. Aside from our general disagreements and competing companies, there \n\tare a some notable events that contributed to our rivalry: a bad meeting in 2004, a disagreement between \n\tour companies in 2013, a patent battle between our companies in 2014. If you'd like me to elaborate on any \n\tof those events just ask me 'what happened between you and Jeff Bezos in (insert year).' I also disagree \n\twith his spaceflight company's hiring practices. On a lighter note, we also enjoy jabbing at each other on \n\tTwitter in recent years.",
				"In 2004, Jeff and I had a meeting that did not go well. We disagreed about our reusable rocket ideas that \n\twe were developing for our spaceflight companies.",
				"In 2013, we had a disagreement about my company, SpaceX, having exclusive use of a NASA launchpad that \n\tJeff thought should be open to all launch companies, including his company, Blue Origin. It was a phony \n\tblocking tactic. Blue origin had not created a reliable suborbital spacecraft that needed launching at that \n\tpoint.",
				"In 2014, our companies got into a patent battle in 2014 over Blue Origin being granted a patent for drone \n\tships used for landing rocket boosters, which my company and I contested with the support of a judge, so \n\tBlue Origin withdrew most of their claims.",
			"I disagree with their hiring practices, and as I have said before, their rate of progress is too slow and the \n\tamount of years Jeff Bezos has left is not enough, but I'm still glad he's doing what he's doing with \n\tBlue Origin"}
	};

	//Constructor to create the window
	public Window() {
		//set the title
		super("Elon Bot");
		//set the size of the window and set it so it can't be resized
		setSize(800,600);
		setResizable(false);
		//set it so it closes when you press the X in the corner
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//set the icon of the program to our image
		setIconImage(icon.getImage());
		//For our window we need to add our scrollbar and our input text box
		pane.add(sideBar);
		pane.add(input);
    
		pipeline = new Pipeline("tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote, sentiment", "coref.algorithm", "neural");

		//Add a GIF as a jLabel based on URL.
		try {
			//GIF: Harrington, D. (2020). Pixel-Robot[GIF]. Retrieved from https://opengameart.org/content/pixel-robot.
			URL url = new URL("https://opengameart.org/sites/default/files/robot-idle.gif");
			JLabel gif = new JLabel(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(78, 78, Image.SCALE_DEFAULT)));
			pane.add(gif);
		}
		catch(MalformedURLException e) {
			System.out.println(e);
		}

		//set the background image of the window
		pane.setBackground(new Color(0,150,200));
		//add the pane
		add(pane);
		//Set the talk area box that it cannot be editable
		talkArea.setEditable(false);
		//adding a keylistener that can listen for events
		input.addKeyListener(this);
		//Set the font
		input.setFont(new javax.swing.plaf.FontUIResource("Comic Sans MS",Font.BOLD,12));
		talkArea.setFont(new javax.swing.plaf.FontUIResource("Comic Sans MS",Font.BOLD,12));
		//set the window to become visible
		setVisible(true);
		//Calling the addText method to add text to the text ares
		addText("\t\t\tPlease type Q to end the conversation\n" );



	}

	//Don't use this method atm
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	//a method that is called when button is pressed
	public void keyPressed(KeyEvent e) {
		//if the key pressed is enter
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			//set the input field so it is not editable
			input.setEditable(false);
			//put the the sentence in the input text field into a String
			String msg=input.getText();
			//set the input field to empty
			input.setText("");
			//Call the method that adds the text to the text are
			addText("\n-->You:\t"+msg+"\n");

			//Check to see if the input is a question.
			Boolean question;
			if(msg.indexOf('?') != -1) 
				question = true;
			else
				question = false;
			
			if(msg.equals("SocketSTUFF")) {
			this.dispose();
			doSocketStuff(55690);
			}
			else {
				//call the response method sending the msg String and boolean question which is true if a question was asked
				try {
					response(msg, question);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	//When a key is released
	public void keyReleased(KeyEvent e) {
		//if the key was enter set the input field back so it is editable
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
			input.setEditable(true);


	}
	//add text method to text area
	public void addText(String s) {
		talkArea.setText(talkArea.getText()+s);
	}


	//A delay method I wanted to implement so the bot seems human and is taking time to reply
	public void delay(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}


	//The method that will get the bots response
	public String response(String s, Boolean question) throws IOException {
		int Sentiment= analyse(s);
		int r = 0; 
		int c = 0;
		String initMsg = Translate(s);
		initMsg=assist(initMsg);
		String response = "";

		initMsg=initMsg.trim();	

		//Replaces "?" and "." and "," with ">" for splitting purposes
		initMsg = initMsg.replace('.', (char)62);
		initMsg = initMsg.replace('?', (char)62);
		initMsg = initMsg.replace(',', (char)62);

		//splits the message at the end of every sentence
		List<String> sentences = Arrays.asList(initMsg.split(">"));

		//for loop for running every sentence separately so that the bot can understand 
		//every sentence sent in as separate statements opposed to as one large statement. 
		//this also allows us to properly demonstrate coreference resolution 
		for (int i=0; i < sentences.size(); i++) {
			//Make msg lower case so that s is intact and case doesn't matter for sent
			String msg = sentences.get(i);
			//A string list of all the named entities detected by corenlp
			List<String> namedEntities = getNameEntityList(msg);
			msg = msg.toLowerCase();
			// Replace all punctuation so it doesn't interfere with responses
			msg = msg.replace(',', (char)32);
			//Redundant trimming just in case
			msg=msg.trim();

			//make a list of every word in the message
			List<String> sent = Arrays.asList(msg.split(" "));
			addText("\n-->Elon:\t");
			
			if(Sentiment==1) {
				addText(Responses[10][(int)(Math.random()*6)]+"\n");
				Sentiment=0;
				addText("\n-->Elon:\t");
			}else if(Sentiment==-1) {
				addText(Responses[11][(int)(Math.random()*6)]+"\n");
				Sentiment=0;
				addText("\n-->Elon:\t");
			}
			
			
			//if it is hello print a greeting
			if(sent.contains("hello")||sent.contains("hi")||sent.contains("hey")) {
				r=0;
				c=0;

				//Saying Hi i'm [name], gives this response. Likely the name is the first entity in the sentence if done correctly, so elon will repeat your name back.
				if(sent.contains("im")||sent.contains("i'm")||(sent.contains("i")&&sent.contains("am"))||(sent.contains("my")&&sent.contains("name"))&&!namedEntities.isEmpty())
				{
					String name = namedEntities.get(0);
					//makes the first letter capital
					name = name.substring(0,1).toUpperCase() + name.substring(1);
					//response
					addText("Nice to meet you " + name + ". You have a nice name.\n");
					addText("\n-->Elon:\t");
				}

			}
			//response to how are you?
			// #2 response bug one -> added sent.contains !old so how old are you does not trigger. 
			else if(sent.contains("how")&&sent.contains("are")&&sent.contains("you")&&!sent.contains("old")) {
				r=0;
				c=1;
				//Thats a great question doesn't really make sense.
				question = false;

			}
			//--------------------------------------------------Life Facts---------------------------------------------------------//	
			// Asking where born
			else if(sent.contains("where")&&sent.contains("born")) {
				r = 7;
				c = 0;
			}//Asking when born
			else if(sent.contains("when")&&sent.contains("born")) {
				r = 7;
				c = 1;
			}//Asking age. Fix #3 Need to change && between you and your to || implemented 
			else if((sent.contains("old")||sent.contains("age"))&&(sent.contains("you")||sent.contains("your"))) {
				r = 7;
				c = 2;
				//Thats a great question doesn't really make sense with response.
				question = false;
			}
			else if(sent.contains("who")&&sent.contains("parents")) {
				r = 7;
				c = 3;
			}
			else if(sent.contains("siblings")||sent.contains("brother")||sent.contains("sister")) {
				r = 7;
				c = 4;
			}
			else if(sent.contains("education")||sent.contains("school")) {
				r = 7;
				c = 5;
			}
			else if((sent.contains("first")||sent.contains("1st"))&&sent.contains("wife")) {
				r = 7;
				c = 7;
			}
			else if((sent.contains("second")||sent.contains("2nd"))&&sent.contains("wife")) {
				r = 7;
				c = 8;
			}
			else if(sent.contains("wife")||sent.contains("married")) {
				r = 7;
				c = 6;
			}
			else if((sent.contains("currently")||sent.contains("dating"))||namedEntities.contains("grimes")) {
				r = 7;
				c = 9;
			}
			//Elon's first software he made or game he created.
			else if(sent.contains("first")&&(sent.contains("software")||sent.contains("game"))) {
				r =7;
				c = 10;
			}//issue #7 can now ask when did you learn programming and when did you learn to program
			else if((sent.contains("when")||sent.contains("how"))&&sent.contains("learn")&&(sent.contains("code")||sent.contains("program")||sent.contains("programming"))) {
				r = 7;
				c = 11;
			}
			else if (sent.contains("spend")&&(sent.contains("time")||sent.contains("freetime"))){
				r = 7;
				c = 12;

			}
			// intended input: what cars do you own? or what car do you drive?
			else if((sent.contains("cars")||sent.contains("car"))&&(sent.contains("drive")||sent.contains("own"))) {
				r = 7;
				c = 13;

			}
			else if((sent.contains("canada")||sent.contains("where"))&&sent.contains("from")&&namedEntities.contains("grimes")) {
				r = 7;
				c = 14;
			}
			//--------------------------------------------------Appearances/interviews---------------------------------------------------------//	
			//Shows he has appeared in.
			else if((sent.contains("shows")||sent.contains("show"))&&sent.contains("appeared")) {
				r = 8;
				c = 0;
			}
			//Joe rogan podcast
			else if(namedEntities.contains("joe rogan")) {
				r= 8;
				c= 1;

			}
			//--------------------------------------------------Interests---------------------------------------------------------//		
			//Asking about specific favorite things
			else if((sent.contains("favourite")||sent.contains("favorite"))&&sent.contains("movie")) {
				r = 6;
				c = 0;
			}
			//Favorite show
			else if((sent.contains("favourite")||sent.contains("favorite"))&&(sent.contains("series")||sent.contains("show"))) {
				r = 6;	
				c = 1;
			}
			else if((sent.contains("favourite")||sent.contains("favorite"))&&(sent.contains("anime"))) {
				r = 6;	
				c = 2;
			}
			else if((sent.contains("favourite")||sent.contains("favorite"))&&(sent.contains("aircraft")||sent.contains("airplane"))) {
				r = 6;
				c = 3;
			}
			// Random favorite thing
			else if((sent.contains("favorite")||sent.contains("favourite"))&&(sent.contains("things")||sent.contains("hobbies")||sent.contains("thing"))) {
				r = 5;
				c=(int)Math.round(Math.random()*3);
			}

			//----------------------------------------------------Career----------------------------------------------------------//

			else if(sent.contains("zip2")||(sent.contains("first")&&(sent.contains("company")||sent.contains("business")))) {
				r = 4;
				c = 0;
			}
			//SpaceX
			else if(sent.contains("spacex")) {
				r = 4;
				c = 1;
			}
			//tesla
			else if(namedEntities.contains("tesla")) {
				r = 4;
				c = 2;
			}
			//paypal
			else if((sent.contains("x")&&sent.contains("com"))||sent.contains("confinity")||namedEntities.contains("ebay")||namedEntities.contains("paypal")) {
				r = 4;
				c = 3;
			}
			//OpenAI
			else if(sent.contains("openai")) {
				r = 4;
				c = 4;
			}
			//Neuralink
			else if(sent.contains("neuralink")) {
				r = 4;
				c = 5;
			}
			//solar city
			else if(sent.contains("solarcity")) {
				r = 4;
				c = 6;
			}
			// list of all major companies
			else if((sent.contains("companies")||sent.contains("businesses"))&&sent.contains("what")) {
				r = 4;
				c = 7;
			}

			//----------------------------------------------------Bezos Rivalry----------------------------------------------------------//

			//0   Have you met Jeff Bezos? Do you know Jeff Bezos?
			else if((sent.contains("know")||sent.contains("met"))&&(namedEntities.contains("jeff bezos")||(sent.contains("jeff")&&sent.contains("bezos")))) {
				r = 12;
				c = 0;
			}

			//1   Do you like Jeff Bezos?
			else if(sent.contains("like")&&(namedEntities.contains("jeff bezos")||(sent.contains("jeff")&&sent.contains("bezos")))) {
				r = 12;
				c = 1;
			}

			//2   What do you think of Jeff Bezos?
			else if(sent.contains("think")&&(namedEntities.contains("jeff bezos")||(sent.contains("jeff")&&sent.contains("bezos")))) {
				r = 12;
				c = 2;
			}

			//3   Do you have a rivalry with Jeff Bezos? Are you and Jeff Bezos Rivals? Is Jeff Bezos your rival?
			else if((sent.contains("do")||(sent.contains("are")&&!sent.contains("why"))||sent.contains("is"))&&(sent.contains("rivalry")||sent.contains("rival")||sent.contains("rivals"))&&(namedEntities.contains("jeff bezos")||(sent.contains("jeff")&&sent.contains("bezos")))) {
				r = 12;
				c = 3;
			}

			//4   How long have you been rivals with Jeff Bezos?
			else if(sent.contains("rivals")&&sent.contains("long")&&(namedEntities.contains("jeff bezos")||(sent.contains("jeff")&&sent.contains("bezos")))) {
				r = 12;
				c = 4;
			}

			//5   Why are you and Jeff Bezos rivals?
			else if(sent.contains("why")&&sent.contains("rivals")&&(namedEntities.contains("jeff bezos")||(sent.contains("jeff")&&sent.contains("bezos")))) {
				r = 12;
				c = 5;
			}

			//6   What happened between you and Jeff Bezos in 2004?
			else if(sent.contains("what")&&sent.contains("happened")&&sent.contains("2004")&&(namedEntities.contains("jeff bezos")||(sent.contains("jeff")&&sent.contains("bezos")))) {
				r = 12;
				c = 6;
			}

			//7   What happened between you and Jeff Bezos in 2013?
			else if(sent.contains("what")&&sent.contains("happened")&&sent.contains("2013")&&(namedEntities.contains("jeff bezos")||(sent.contains("jeff")&&sent.contains("bezos")))) {
				r = 12;
				c = 7;
			}

			//8   What happened between you and Jeff Bezos in 2014?
			else if(sent.contains("what")&&sent.contains("happened")&&sent.contains("2014")&&(namedEntities.contains("jeff bezos")||(sent.contains("jeff")&&sent.contains("bezos")))) {
				r = 12;
				c = 8;
			}
			
			//9   What do you think of Blue Origin
			else if(sent.contains("think")&&sent.contains("blue")&&sent.contains("origin")) {
				r = 12;
				c = 9;
			}

			//----------------------------------------------------Ask Elon about places he has been----------------------------------------------------------//

			else if(sent.contains("have")&&sent.contains("you")&&sent.contains("been")&&sent.contains("to")) {
				// namedEntities will be empty if an recognized location is entered by corenlp
				if(!namedEntities.isEmpty()) {
					int rnd = (int)Math.round(Math.random()*(namedEntities.size()-1));
					//If multiple places are entered elon will randomnly pick one
					String place = namedEntities.get(rnd);

					place = place.substring(0,1).toUpperCase() + place.substring(1);
					addText("I can't remember if i've been to " + place + ", but " + place + " sure seems like it's lovely!\n");
					addText("\n-->Elon:\t");
				}
				else {
					addText("Been to where?\n");
					addText("\n-->Elon:\t");
				}

				r = 9;
				c=(int)Math.round(Math.random()*2);


			}
			//----------------------------------------Easter Egg--------------------------------------------------------//
			else if(s.toLowerCase().equals("the earth king has invited you to lake laogai.")) {
				addText("I am honored to accept his invitation.\n");
				addText("\n-->Elon:\t");
				r = 2;
				c=  0;

			}

			//------------------------------------------------------Random-----------------------------------------------//
			else if(sent.contains("thanks")||(sent.contains("thank")&&sent.contains("you"))) {
				r = 1;
				c = 0;
			}
//----------------------------------------Easter Egg--------------------------------------------------------//
		else if(s.toLowerCase().equals("the earth king has invited you to lake laogai.")) {
			addText("I am honored to accept his invitation.\n");
			addText("\n-->Elon:\t");
			r = 2;
			c=  0;
			
		}
//------------------------------------------------------Random-----------------------------------------------//
		else if(sent.contains("thanks")||(sent.contains("thank")&&sent.contains("you"))) {
			r = 1;
			c = 0;
		}
		//if its q end the chat and disable the input field
		else if(sent.contains("q")) {
			r=3;
			c=(int)Math.round(Math.random()*4);
			input.disable();
		}
		
		//default case
		else {
			r=2;
			c=7;
			addText("I dont know how to awnser that, But let me look it up." + "\n");
			Wolf(sent);
		}
		
	    // If the msg received was a question and the response is not default. There is a 1/5 chance bot responds this.
		if(question&&r!=2&&((int)Math.round(Math.random()*4))==4) {
			addText("That's a great question!\n");
			addText("\n-->Elon:\t");
		}
		
		//again checking if it was q and making a visible message saying the chat has ended across window
		if(sent.contains("q"))
			addText("--------------------------------------------Chat Has Ended--------------------------------------------");
		
		
		response = Responses[r][c];
		response= translate_back(response);
		//add the response to the text Area
		addText(response + "\n");
		
		
	}
		
	return response;
	
}
		

		
		 
	    
	    public int analyse(String txt) {
	    	//document for corenlp
	    	CoreDocument core= new CoreDocument(txt);
	    	//annotate the document
	    	pipeline.getPipe().annotate(core);
	    	//Make a variable which will store the sentiment value
	    	int temp=0;
	    	//get the sentences from the input msg
	    	List<CoreSentence> sentences= core.sentences();
	    	//go through each sentence and get the sentiment
	    	for(CoreSentence sentence: sentences) {
	    		String sentiment = sentence.sentiment();
	    		if(sentiment.equalsIgnoreCase("Positive")||sentiment.equalsIgnoreCase("Very Positive")) {
	    			temp=1;
	    		}
	    		else if(sentiment.equalsIgnoreCase("Negative")||sentiment.equalsIgnoreCase("Very Negative")) {
	    			temp=-1;
	    		}
	    		else
	    			temp=0;
	    		System.out.println(sentiment+","+temp+ "\t"+ txt);

	    	}
	    	
	    	return temp;
	    }

	    
	    public void doSocketStuff(int port) {
	    	System.out.println("Ready to receive....");
	    	try 
	    	(ServerSocket serversocket = new ServerSocket(port);
	    	Socket sock = serversocket.accept();//establishes connection 
	    	DataInputStream dis = new DataInputStream(sock.getInputStream()); 
	    	DataOutputStream dos = new DataOutputStream(sock.getOutputStream()))
	    	{
	    	String inmsg = "";
	    	String outmsg = "";
			while(inmsg!="q") {
		    inmsg = (String)dis.readUTF();  
			//outmsg = response(inmsg, inmsg.indexOf('?') != -1);
			System.out.println(inmsg);
			System.out.println(outmsg);
			dos.writeUTF(outmsg);
			}
			
	    	}
	    	catch(Exception e) {
	    		
	    	}
	    
			
	    }
	    
		


	//Get a list of namedEntitys from the string and then convert those named entities to strings in a new list
	public List<String> getNameEntityList(String s){

		List<String> list = new ArrayList();
		
	    if(s!="") {
		//document for corenlp
		CoreDocument document = new CoreDocument(s);
		// annnotate the document
		pipeline.getPipe().annotate(document);
		// get entities from the document
		List<CoreEntityMention> entityMentions = document.entityMentions();
		//print entities
		System.out.println(entityMentions.toString());
		//Convert entities to their string representations and add to the list
		for(int i = 0; i<entityMentions.size();i++) {
			list.add(entityMentions.get(i).toString().toLowerCase());
		}

	    }
		return list;

	}

	public static String assist(String s) {
		String res = s;
		List<Object[]> list =  CoRef(s);
		for(int i = 0; i < list.size();i++)
			res = replace(res, list.get(i));
		return res;
	}
	//coreference resolution
	/*********************
	 * Determines the coreferences for the sentence, then puts them into an array with
	 * equvalent words, all equivalent words are in the same array. If multiple 
	 * equivalences exist will return a list of arrays
	 * @param sentence that will be solved
	 * @return list of equivalant words
	 */
	public static List<Object[]> CoRef(String s) {
		Pipeline p = new Pipeline("tokenize,ssplit,pos,lemma,ner,parse,coref","","");
		List<Object[]> list = new ArrayList<Object[]>();
		Annotation document = new Annotation(s);
		p.getPipe().annotate(document);
		for (CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values() ) {
			list.add(extract(cc.getMentionMap().values().toArray()));
 
		}
		return list;
	}

	public static Object[] extract(Object[] A) {
		for (int i = 0;i < A.length;i++) {
			A[i]=(A[i].toString().substring(( A[i]).toString().indexOf("\"")+1,( A[i]).toString().lastIndexOf("\"")));
		}
			return A;
	}

	/****************
	 * Takes in A string s then takes the first item in equivalence list, then replaces all appearances of the 
	 * following strings in the list, with the first item in the list. 
	 * NOTE: MUST CAST
	 * OBJECT[] WHEN CALLING METHOD.
	 * @param Sentence that is to be updated
	 * @param Equivalence matrix, that determines what is to be replaced, and by what
	 * @return The updated sentence.
	 */
	public static String replace(String s,Object[] replace) {
		String replacer = replace[0].toString();//value that is going to replace next
		for(int i = 1; i < replace.length;i++) {
			if(s.contains(replace[i].toString()))
				s=s.replace(replace[i].toString(), replacer);
		}
		
		return s;
	}
	/********************************
	 * Takes in the input sentence and uses the wolframalpha short awsner api it returns a 
	 * possible awnser to the question
	 * @param sent
	 * @throws IOException
	 */
	public void Wolf(List<String> sent) throws IOException{
		try {
		String b = "http://api.wolframalpha.com/v1/result?appid=RUUJHL-XTW44JUHW7&i=";
		System.out.println(sent.toString());
		b= b +(sent.get(0));	
		for (int i = 1; i < sent.size();i++) {
				b = b + "+"+sent.get(i);
			}
			System.out.println(b);
			URL u = new URL(b.substring(0, b.length()));
	        BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
	        String s =br.readLine();
	        if(s.length() == 0) {
				addText("hmmmm, cant find anything");
	        }
	        while (s != null) {
	        	if (s.length() >= 100) {
	        		addText(s.substring(0, 99)+'-'+'\n');
	        		addText(s.substring(99) +'\n');
	        	}
	        	else {
		        	addText(s);
	        	}
                s=br.readLine();
            }
		}catch(Exception E) {
			addText("hmmmm, Something went Wrong. Try Again.");
		}
	}
	/************************************
	 * Takes in a string and using google translate api it it will translate from any language 
	 * into english so the question can be awnsered
	 * @param s
	 * @return
	 */
	public String Translate(String s) {
			Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyD2CQN0FIzxb3AY7NcN4oxitdKBfnHPLo8").build().getService();
			Source = translate.detect(s);
 			Translation translation = translate.translate(s,Translate.TranslateOption.targetLanguage("en"));
 			System.out.println(Source.getLanguage());
			return translation.getTranslatedText();
	}
	public String translate_back(String s) {
		Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyD2CQN0FIzxb3AY7NcN4oxitdKBfnHPLo8").build().getService();
		Translation translation = translate.translate(s,Translate.TranslateOption.targetLanguage(Source.getLanguage()));
		return translation.getTranslatedText();
	}

}

