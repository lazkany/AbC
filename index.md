### AbaCuS: A Run-time Environment of the AbC Calculus
In most distributed systems, named connections (i.e., channels) are used as means for programming interaction between communicating partners. These kinds of connections are low level and usually totally independent of the knowledge, the status, the capabilities, ..., in one word, of the attributes of the interacting partners. We have recently introduced a calculus, called [AbC](https://dl.dropboxusercontent.com/s/eikm8lldw00int9/AbCPaper.pdf?dl=0), in which interactions among agents are dynamically established by taking into account “connection” as determined by predicates over agent attributes. Here, we present AbaCuS, a Java run-time environment that has been developed to support modelling and programming of collective adaptive systems by relying on the communication primitives of the AbC calculus. Systems are described as sets of parallel components, each component is equipped with a set of attributes and communications among components take place in an implicit multicast fashion. By means of a number of examples, we also show how opportunistic behaviors, achieved by run-time attribute updates, can be exploited to express different communication and interaction patterns and to program challenging case studies.

### Getting Started
AbaCuS provides a Java API that allows programmers to use the linguistic primitives of the AbC calculus in Java programs. 
The implementation of AbaCuS fully relies on the formal semantics of the AbC calculus. There is a one-to-one correspondence between the AbC primitives and the programming constructs in AbaCuS. This close correspondence enhances confidence on the behavior of AbaCuS programs once they have been analyzed via formal methods, which is possible by relying on the operational semantics of the AbC calculus.

AbC's operational semantics abstracts from a specific communication infrastructure. An AbC model consists of a set of parallel components that cooperate in a highly dynamic environment where the underlying communication infrastructure can change dynamically. 
The current implementation  AbaCuS is however a centralized one, in the sense that it relies on a message broker that mediates the interactions. In essence, the broker accepts messages from sending components, and delivers them to all registered components with the exception of the sending ones. This central component plays the role of a forwarder and does not contribute in any way to message filtering. The decision about accepting or ignoring a message is decreed when the message is delivered to the receiving components. 

We would like to stress that, although the current AbaCuS implementation is centralized, components interact anonymously  and combine their behaviors to achieve the required goals. Components are unaware of the existence of each other, they only interact with the message broker.
To facilitate interoperability with other tools and programming frameworks, AbaCuS relies on JSON, a standard data exchange technology that simplifies the interactions between heterogenous network components and provides the basis for allowing AbaCuS programs to cooperate with external services or devices. 

To do network programming with AbaCuS you always have to start a forwarding server/message broker so that your program functions in the expected way. This task is very easy, all you need to do is to create an instance of the class "AbCServer". This class is responsible for forwarding messages. By default, It accepts messages at port number 9998 and it accepts registration at port number 9999. In the code snippet below we show how to instantiate this class and the required classes to be imported.   

<pre><code>
import java.io.IOException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.topology.AbCServer;
public class MainServer {
	public static void main(String[] args) throws IOException, DuplicateNameException {
		AbCServer srvr=new AbCServer();
		srvr.start();
	}
}
</code></pre>

In this way, every AbC component should communicate only with the message broker/main server, this server is responsible for forwarding the messages to the other components. This means that every component should provide a mean of communication and in our implementation this can be done by instantiating the class "AbCClient" and specify the local address and the server address. Every node should also register itself to the message broker to be considered for future incoming messages. All addresses are socket addresses (i.e., IP address + port number). As mentioned above, the message broker with default settings accepts messages at port 9998 and registration at port 9999. In our tutorial here, we let the user to enter the port address at run time and because we run the examples on one machine, we consider the IP address as a wildcard as shown below.

<pre><code>
    System.out.println("Enter port number : ");
	int port = 0;
	try {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		port = Integer.parseInt(bufferRead.readLine());

	} catch (IOException e) {
		e.printStackTrace();
	}
	AbCClient cPortClient = new AbCClient(InetAddress.getLoopbackAddress(), port);
	cPortClient.RemoteRegister(InetAddress.getLoopbackAddress(), 9999);
</code></pre>

Now everything is in place to start programming with AbC linguistic primitives as we will see in the following examples. Now we list the basic steps to write an AbC program:

* Create a Java class with a main method.
* Create a Java class for each process type in your model (This class should extend the AbCProcess class and override the doRun() method). The doRun() method should contain the behaviour of your process.
* In the main method, instantiate your processes, instantiate the classes AbCEnvironment, Attribute, and AbCComponent, assign attributes to the environment, the environment to the component, and processes to the component. Notice that the method addProcess() puts processes in parallel. For instant, if you have component C1, and then you add processes P1 and P2 to component C1, this is equivalent of having P1 and P2 in parallel within component C1.
* Add a client port to your component (C1.setPort(cPortClient)).
* Start the execution of your component with Start() method.
* This is it!

### Attribute-based Interaction
Now we show the basic idea of attribute-based communication through a swarm robot scenario. We have four kinds of robots: Explorer, Helper, Rescuer, and Charger. The explorer robot is searching for victims in a disaster arena; the helper robot is helping other rescuers to rescue a victim; the rescuer robot already found the victim and waits until sufficient number of robots arrives to start rescuing; The charger robot is waiting for requests for charging from explorers. They all exchange information through the message broker as shown below.

  ![](https://dl.dropboxusercontent.com/s/e1w8f2qxge2m3rt/attribute.png?dl=0) 

The behaviour of the explorer robot is simple, it sends queries for information about victims to nearby rescuers or helpers. It sends its Id, a qry request, and its role. In our implementation values are sent as a tuple of elements. The predicate should be as follows: 

`GroupPredicate orPrd = new Or(new HasValue("role", "rescuer"), new HasValue("role", "helper"));`

We provide a set of predicates to be used to filter messages and you can find them here [AbC Predicates](https://github.com/lazkany/AbC/tree/master/bundles/org.sysma.abc.core/src/org/sysma/abc/core/predicates). If we take the predicate HasValue("role", "rescuer") for example,this corresponds to the AbC predicate (role=rescuer) where the left hand side denotes an attribute identifier and the right hand side corresponds to a value. The behaviour of the explorer robot is shown bellow.

<pre><code>
public class Explorer {
	public static AbCPredicate orPrd = new Or(new HasValue("role", "rescuer"), new HasValue("role", "helper"));
	public static class Process_1 extends AbCProcess {
		public Process_1(String name) throws AbCAttributeTypeException {
			super(name);
		}
		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			send(orPrd, new Tuple(this.getComponent().getStore().getValue("id"), "qry",
					this.getComponent().getStore().getValue("role")));
		}
	}
</code></pre>


The Send method takes two parameters (predicate,values), the filter predicate and the tuple of values. Below you see the the definitions in the main method. 

<pre><code>
    AbCClient cPortClient = new AbCClient(InetAddress.getLoopbackAddress(), port);
		cPortClient.register(InetAddress.getLoopbackAddress(), 9999);
		Process_1 explorer = new Process_1("explorer_1");
		AbCEnvironment store1 = new AbCEnvironment();
		Attribute<Object> a1 = new Attribute<Object>("role", Object.class);
		Attribute<Object> a2 = new Attribute<Object>("id", Object.class);
		store1.setValue(a1, "explorer");
		store1.setValue(a2, "1");
		AbCComponent c1 = new AbCComponent("C1", store1);
		c1.addProcess(explorer);
		c1.setPort(cPortClient);
		cPortClient.start();
		c1.start();
</code></pre>




As we mentioned in the previous section, here we define the component, its environment, and its attributes. we attach the process we defined to the component and start its execution. 

The behaviour of processes (i.e., Helper, Rescuer, and Charger) is specified in the same way. The only differences are those regarding their attributes, attribute values and their predicate. They also use the other construct for communication which is the receive method. The receive method passes the received message to a function (msg->Function(msg)), This function checks if the received message satisfies the receiving predicate.

Below we only show the behaviour of the Helper robot. Processes Rescuer and charger follow in a similar way. The source code for the whole example can be found be the end of this section and we also include a video to show how to run AbC programs.


<pre><code>
public class Helper {
	public class Helper {

	public static class Process_1 extends AbCProcess {
		public Process_1(String name) throws AbCAttributeTypeException {
			super(name);
		}
		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			System.out.println(this.name + " => received: " + receive(o -> andPrd(o)));
		}
		public AbCPredicate andPrd(Object msg) {
			if (msg instanceof Tuple) {
				Tuple t = (Tuple) msg;
				if (t.get(1).equals("qry") && t.get(2).equals("explorer")) {
					return new TruePredicate();
				}
			}
			return new FalsePredicate();
		}
	}
</code></pre>



Putting it all together 

* To run your AbC program, you'll need AbC .JAR and its dependencies (i.e.,[gson-2.5.jar](http://search.maven.org/#artifactdetails%7Ccom.google.code.gson%7Cgson%7C2.5%7Cjar)) placed in the same folder. 

* To create AbC .JAR, after you complete writing your AbC program, select the main directory in the project explorer in your eclipse (org.sysma.abc.core). From file menu, select "Export". the Select JAR File from JAVA folder and finally save it in the same folder of "gson-2.5.jar".

* Assume that you saved the AbC .JAR with "test.jar" name, so in the terminal you write the following to run for instance the code of the Helper robot. The AbC .JAR file for the examples in this tutorial can be found in [test.jar](https://www.dropbox.com/s/w1nqfasckxi1x4j/test.jar?dl=0)

`java -cp test.jar:gson-2.5.jar org.sysma.abc.core.ex.AttributeBased.Helper`

* DO NOT FORGET to run the main server in the beginning, otherwise it will not work.

* The following video demonstrates the way of how you run AbC programs. 


[Source Code](https://github.com/lazkany/AbC/tree/master/bundles/org.sysma.abc.examples/src/org/sysma/abc/core/ex/AttributeBased)            
[Video Demo](https://www.youtube.com/watch?v=QfbCYoRgE3s&feature=youtu.be)

### Modelling group-based Interaction
In this section, we show how to model group-based framework in AbC in an easy way. We consider a scenario where we have three groups: groupA, groupB, and groupC. We defines two members of groupB, one member of groupA, and one member of groupC. The groupC member stays for a certain amount of time in groupC and then leaves to join groupB. The groupB members are always ready to receive messages from groupA members. On the other hand, groupA members are always sending messages only to groupB. The figure below shows the conceptual scenario.   

![](https://dl.dropboxusercontent.com/s/hrlfdvqs27f8kl5/group.png?dl=0)

The groupA behaviour is shown below. Basically, GroupA members always send messages with the predicate "TOgrpB". This predicate filters the message only to GroupB. In the same time, groupA members attach their group name in the message. 
`this.getComponent().getStore().getValue("group")` this corresponds the the AbC attribute reference "this.group".

<pre><code>
public class GroupA {
	public static AbCPredicate TOgrpB = new HasValue("group", "B");
	public static class Process_1 extends AbCProcess {
		public Process_1(String name) throws AbCAttributeTypeException {
			super(name);
		}
		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			while(true){
			send(TOgrpB, new Tuple("MSG",this.getComponent().getStore().getValue("group")) );
			Thread.sleep(2000);
			}
		}
	}
</code></pre>

On the other hand, the behaviour of groupB and groupC members is nearly similar in the sense that both of them are waiting to receive messages from groupA as shown below.

<pre><code>
public class GroupB {
	public static class Process_1 extends AbCProcess {
		public Process_1(String name) throws AbCAttributeTypeException {
			super(name);
		}
		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			while (true) {
				System.out.println(this.name + " => received: " + receive(o -> fromGroupA(o)));
			}
		}
		public AbCPredicate fromGroupA(Object msg) {
			if (msg instanceof Tuple) {
				Tuple t = (Tuple) msg;
				if (t.get(1).equals("A")) {
					return new TruePredicate();
				}
			}
			return new FalsePredicate();
		}
	}
</code></pre>

So, both of groupB and groupC members have a process that is waiting to receive a message with a predicate "FromgrpA". This predicate inspects the second element of the received values and checks its equality with "A" where "A" denotes groupA. if this predicate holds true then the message can be received otherwise it will be discarded. In addition to this behaviour, the groupC members contain another process running in parallel with previous process (Remember: putting two processes in parallel is done using the method "addProcess()"). This process waits for a while and then leaves groupC to join groupB as shown below.

<pre><code>
public static class Process_2 extends AbCProcess {
		public Process_2(String name) throws AbCAttributeTypeException {
			super(name);
		}
		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			Thread.sleep(20000);
			setValue(a1, "B");
			send(new FalsePredicate(), " ");	
			System.out.println("joined group B");
		}
	}
<code></pre>

We model the group name as an attribute and leaving/joining the group by an attribute update. The attribute update can be achieved by updating the attribute followed with a silent move (i.e., send on a false predicate ()@ff ). This action is not observable and indicates a silent move. The above code corresponds to the AbC code `[this.group := B]()@ff`. 

So now, the behaviour of a groupC member is equivalent to the AbC component `Gamma:( (y=B)(x,y) | [this.group := B]()@ff)`. This is interesting and models what we call opportunistic behaviour in the sense that the behaviour of two co-located processes running in parallel can influence each other. For instance, once the attribute update is performed with a silent move, this component leaves groupC and joins groupB and thus is eligible to receive messages from groupA. This is why modelling adaptation and failure in AbC is quite natural. In some sense, processes can influence the behaviour of each other indirectly through changing the shared working space/environment. In many cases, this behaviour is highly recommended where message passing becomes impractical. For instance, in wireless sensor networks where a massive number of components are distributed in a relatively small space, message passing doesn't scale well in this situation. This kind of behaviour is hardly possible in other approaches. Below you find the full source code for the scenario and a video of showing how to run the code.

[Source Code](https://github.com/lazkany/AbC/tree/master/bundles/org.sysma.abc.examples/src/org/sysma/abc/core/ex/GroupBased)      
[Video Demo](https://www.youtube.com/watch?v=3xlWubbXjTM&feature=youtu.be)

### Modelling Publish/Subscribe framework
In this section, we show how to model Topic-based publish/subscribe framework in AbC in a natural way. We consider a scenario where a publisher is sending messages tagged with two different topics: topic_1=movies and topic_2=news. There are three subscribers, one is subscribed to "movies", one to "news", and the last one is subscribed to songs as shown below:

![](https://dl.dropboxusercontent.com/s/72r0wd6ot3ujeuh/pubsub.png?dl=0)

Obviously, only the subscribers with "movies" or "news" will get the message, the subscriber who is subscribed to "songs" will not receive the message. This scenario can be modelled in AbC in an obvious way in the sense that the publisher will send a message with true predicate (i.e., satisfied by all) and tag these messages with topics. The Publisher has two attributes named topic_1 and topic_2 respectively. The publisher code is shown below:

<pre><code>
public class Publisher {
	public static AbCPredicate any = new TruePredicate();
	public static class Process_1 extends AbCProcess {
		public Process_1(String name) throws AbCAttributeTypeException {
			super(name);
		}

		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			send( new TruePredicate() , new Tuple( "msg" , this.getComponent().getStore().getValue("topic_1"), this.getComponent().getStore().getValue("topic_2")));
		}
	}

<code></pre>

Simply, the publisher attaches its attributes "topic_1" and "topic_2" into the message and sends it to all possible subscribers. Note that the following code corresponds to the AbC attribute reference "this.topic_1"

`this.getComponent().getStore().getValue("topic_1")`
 
The subscriber is responsible to check if the message matches its subscriptions or not. This can be done by using the predicate "subscribe" as shown below. This predicate will check if the attached topics match the subscriber subscriptions or not. Again the line `this.getComponent().getStore().getValue("subscription")` corresponds to the AbC attribute reference "this.subscription".

<pre><code>
public class Subscriber_1 {
	public static class Process_1 extends AbCProcess {
		public Process_1(String name) throws AbCAttributeTypeException {
			super(name);	
		}
		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			System.out.println(this.name + " => received: " + receive(o -> subscribe(o)));
		}
		public AbCPredicate subscribe( Object msg ) {
			if (msg instanceof Tuple) {
				Tuple t = (Tuple) msg;
				if (t.get(1).equals(this.getComponent().getStore().getValue("subscription"))||t.get(2).equals(this.getComponent().getStore().getValue("subscription"))) {
					return new TruePredicate();
				}
			}
			return new FalsePredicate();
		}
	}
</code></pre>

The full source code for the whole scenario can be found below.

[Source Code]
(https://github.com/lazkany/AbC/tree/master/bundles/org.sysma.abc.examples/src/org/sysma/abc/core/ex/PublishSubscribe)                 
[Video Demo](https://www.youtube.com/watch?v=Eiil8DkPoqM&feature=youtu.be)

### Encoding Channel-based Interaction
In this example, we show how we can model channel-based communication in AbC smoothly and easily. In essence, we will show how to encode the basic constructs of the [Broadcast Pi-calculus](http://dl.acm.org/citation.cfm?id=662485) into AbC in a natural way. We will consider the send and receive actions and the underlying communication paradigm. The encoding of a broadcast Pi-calculus process P, is rendered as an AbC component with empty environment. The channel is encoded as a special value in the sent message. For instance, the broadcast action in the broadcast Pi-calculus is encoded as follows:

<pre><code>
public class Sender {
	public static AbCPredicate any = new TruePredicate();
	public static class Process_1 extends AbCProcess {
		public Process_1(String name) {
			super(name);
		}
		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			send(any, new Tuple("a","v"));
		}
	}
</code></pre>

as you see, the send action is encoded as a send method in AbC, where the predicate any corresponds to a true predicate in AbC (i.e., satisfied by all). The sent values is a pair, the first element is the name of the channel and the second element is the actual communicated value. As shown below, the receiver decides whether to accept the message or not.  

<pre><code>
public class Receiver_1 {
	public static class Process_1 extends AbCProcess {
		public Process_1(String name) {
			super(name);
		}
		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			System.out.println(this.name + " => received: " + receive(o -> channel(o)));
		}
		public AbCPredicate channel(Object msg) {
			if (msg instanceof Tuple) {
				Tuple t = (Tuple) msg;
				if (t.get(0).equals("a")) {
					return new TruePredicate();
				}
			}
			return new FalsePredicate();
		}
	}
</code></pre>

Basically, the receiver accept only the message that satisfies the predicate "channel". This predicate checks the first element in the message and returns true if it is equal to "a" otherwise the message is discarded. As shown in the figure below, we model a scenario, where there are two receiver waiting to receive messages on channel "a" , one receiver is waiting to receive message on channel "c", and one sender that wants to send a message "v" on channel "a".

![](https://dl.dropboxusercontent.com/s/v8w7a1umxjne2w1/channel.png?dl=0)

Only the receivers which are waiting to receive a message on channel "a" will receive the message, the other receiver will discard any sent message on a channel that is different from "c". The source code for all processes can be found below and we also include a video to show how to run these processes.

[Source Code](https://github.com/lazkany/AbC/tree/master/bundles/org.sysma.abc.examples/src/org/sysma/abc/core/ex/broadcast)      
[Video Demo](https://www.youtube.com/watch?v=VfFOpDV-JSw&feature=youtu.be)

### The Stable Marriage problem in AbaCuS
We consider the classical stable marriage problem (SMP), a problem of finding a stable matching between two equally sized sets of elements given an ordering of preferences for each element. 

In our example, we consider $n$ men and $n$ women, where each person has ranked all members of the opposite sex in order of preferences, we have to engage the men and women together such that there are no two people of opposite sex who would both rather have each other than their current partners. When there are no such pairs of people, the set of marriages is deemed stable. For convenience we assume there are no ties; thus, if a person is indifferent between two or more possible partners he/she is nevertheless required to rank them in some order. 
In our implementation we assume that the man initiates the interaction. This is done by removing his first best from his list of preferences and assuming it to be his partner. The man proposes to this possible partner and waits for any invalidation messages from this woman. If this message is received, the man starts over again and removes the next item from his preferences and soon. The behaviour of a man can be implemented in AbaCuS as follows:
<pre><code>
public class ManAgent extends AbCProcess {
	public LinkedList<Integer> preferences;	
	public ManAgent( LinkedList<Integer> preferences ) {
		super("ManAgent");
		this.preferences = preferences;
	}	
	@Override
	protected void doRun() throws Exception {
		System.out.println("Man started...");
		while ( !preferences.isEmpty() ) {
			Integer partner = preferences.poll();
			System.out.println(getValue(Environment.idAttribute)+"> Selected partner: "+partner);
			setValue(Environment.partnerAttribute, partner);
			System.out.println(getValue(Environment.idAttribute)+"> Sendig request to "+partner);
			send( new HasValue("ID", partner) , new Tuple( "PROPOSE" , getValue(Environment.idAttribute)) );
			receive(o -> isAnInvalidatingMessage(o) );
			System.out.println(getValue(Environment.idAttribute)+"> Matching changed!");
		}
		System.out.println(getValue(Environment.idAttribute)+"> I am alone...");
	}
	public AbCPredicate isAnInvalidatingMessage( Object msg ) {
		if (msg instanceof Tuple) {
			Tuple t = (Tuple) msg;
			if ((t.size() == 1)&&(t.get(0).equals("INVALID"))) {
				return new TruePredicate();
			}
		}
		return new FalsePredicate();
	}
}
</pre></code>

On the other hand, a woman waits for proposals all the time. In the beginning, when she is not engaged, she accepts any proposal. Once she is engaged and another man proposes, she looks at her list of preferences and compare her current man with the new man and decides if she will be better off with the new man or not. If yes, she says sorry to her current man and get engaged to the new one, otherwise she just says sorry to the new proposed man. 
The full implementation can be found below.
[Source Code](https://github.com/lazkany/AbC/tree/master/bundles/org.sysma.abc.examples.sm/src/org/sysma/abc/examples/sm)

### The Smart Conference Case study 
Please refer to this paper [ISOLA](https://dl.dropboxusercontent.com/s/bfbp31mzl57i190/ISOLA.pdf?dl=0) for problem statement and formal specification. You only need to execute the file SmartConference.Java, this file provide a GUI that can be used to generate a random conference venue where each room is assigned a random topic. Then you can add participants to the system. You have to initially provide the identity for the first participant and the number of participants that you want to add to the system. The system will randomly assign topic to these participant and the model is ready for execution. at anytime you can change the schedule of any room and you will see how this affect other participants or rooms where a swap should happen. At any point of time you can still add participants and this will not affect the overall behaviour. The source code can be found here
[Source Code](https://github.com/lazkany/AbC/tree/master/bundles/org.sysma.abc.examples.smartconference/src/org/sysma/abc/examples/smartconference)
### Contributors to the Implementation
Yehia Abd Alrahman                                                                                                     
yehia.abdalrahman@imtlucca.it                                                                                            
IMT Lucca for Advanced Studies                                                                                         
@lazkany


***


Michele Loreti                                                                                                              
michele.loreti@unifi.it                                                                                                   
Universita degli Studi di Firenze                                                                                      
@michele-loreti
### Support or Contact
Having trouble with with installation or programming? Please contact either @lazkany or @michele-loreti
