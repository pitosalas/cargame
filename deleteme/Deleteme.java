package com.salas;

public class Deleteme {
	abstract class EntityA<T extends AssocA> {

	    // Basically, this means myA is at least an AssocA but possibly more...
	    T myA;
	    abstract void meet();
	}

	abstract class AssocA {
	    int something;
	    abstract void greet();
	}

	class AssocAConcrete extends AssocA {
	    void greet() {
	        System.out.println("hello");
	    }
	    void salute() {
	        System.out.println("I am saluting.");
	    }
	}

	class EntityAConcrete extends EntityA<AssocAConcrete> {
	    void meet() {
	        System.out.println("I am about to meet someone");
	        myA.salute();
	    }
	}
/*	Aside from avoiding the casting, this also makes it much easier to add different functionality in your AssocA implementations. There should always be a way to do things without using dummy implementations (ie methods that just throw "NotImplementedException") or casting. Even though it isn't always easy or worth the time to do so. In other words, no one is going to blame you for casting (well...maybe some people will but you can't please everyone).

	EDIT (Notes on instantiation):

	From @pitosalas' comments below...

	//Won't work...can't call 'new' on abstract class AssocA
	AssocAConcrete myAssoc = new Assoca();

	//Instead, do this...
	 */
	AssocAConcrete myAssoc = new AssocAConcrete();
/*	And then....

	// Again, won't work.  T is only declaring the type inside your class/method.
	// When using it to declare a variable, you have to say EXACTLY what you're making,
	// or at least something as exact as the methods you're trying to invoke
	EnitityA<T extends AssocA> myEntity = new EntityA<AssocAConcrete>();

	EntityA<AssocAConcrete> myEntity1 = new EntityAConcrete()
	//Instead do this... 
	myAssoc = null;
*/
	// Or this...
//	EntityAConcrete myEntity = new EntityAConcrete();
//	And then this should be good...

	// Assuming this is defined as `public void setAssoc(T newAssoc) {this.myA = newAssoc;}`
//	myEntity1.setAssoc(myAssoc);
//	myAssoc.salute();
}
