import java.util.Random;

//A FiniteSet is either:
// -a Leaf  (which is empty) or
// -a Tree (which is a constructed FiniteSet containing
// a left node (type FiniteSet), right node (type FiniteSet), and self value (type int) 
  
interface FiniteSet {

    public int cardinality();
    public boolean isEmptyHuh();
    public boolean member(int elt);
    public FiniteSet add(int elt);
    public FiniteSet remove(int elt);
    public FiniteSet union(FiniteSet u);
    public FiniteSet inter(FiniteSet u);
    public FiniteSet diff(FiniteSet u);
    public boolean equal(FiniteSet u);
    public boolean subset(FiniteSet u);
}


class Leaf implements FiniteSet{

    //constructor: takes in no parameters
    //Leaf is the empty set and contains no values or other FiniteSets
    Leaf() { }

    // empty() -> FiniteSet
    // does not take in any data parameters
    // returns a new empty set (i.e. a new instance of the Leaf class)
    public static Leaf empty( ) { return new Leaf(); }

    // cardinality(FiniteSet) -> int
    // because t contains no elements, the size of t will always be 0
    public int cardinality() { return 0; }

    // isEmptyHuh(FiniteSet) -> boolean
    // the empty set it always empty by definition, therefore will always return true
    public boolean isEmptyHuh () { return true; }

    // member(FiniteSet, int) -> boolean
    // because this set is the empty set, member will always return false since it will never contain any int
    public boolean member(int elt) { return false; }

    // add(FiniteSet, int) -> FiniteSet
    // the empty set plus an int returns a new set containing the int as the set's value and two empty sets as left & right   
    public FiniteSet add(int elt) { return new Tree( this, elt, empty()); }

    // remove(FiniteSet, int) -> FiniteSet
    // because int is not a member of the set (because the set is empty), remove does nothing and returns the original set
    public FiniteSet remove(int elt) { return this; }

    // union(FiniteSet, FiniteSet) -> FiniteSet
    // since the empty set is already contained in the second FiniteSet, the second FiniteSet is returned unchanged
    public FiniteSet union(FiniteSet u) { return u; }

    // inter(FiniteSet, FiniteSet) -> FiniteSet
    // the two sets do not have ints in common so just the empty set (the first set, self-referrential) is returned 
    public FiniteSet inter(FiniteSet u) { return this; }

    // diff(FiniteSet FiniteSet) -> FiniteSet
    // returns the second set minus all the elements in the first (which is empty) so the second set is unchanged
    public FiniteSet diff(FiniteSet u) { return u; }

    // equal(FiniteSet FiniteSet) -> FiniteSet
    // returns true if the given set is also empty, false otherwise
    public boolean equal (FiniteSet u) { return u.isEmptyHuh(); }

    // subset(FiniteSet FiniteSet) -> FiniteSet
    // always returns true because the empty set is a subset of every set
    public boolean subset (FiniteSet u) { return true; }

}


class Tree implements FiniteSet {

    FiniteSet left, right;
    int value;

    // constructor: creates a FiniteSet containing an integer value and a Finite Set on either side of the value
    // REQUIRES: left.value is less than this.value; right.value is greater than this.value, OR
    // left and right can be either or both be empty if no such value exists in the Tree
    Tree(FiniteSet left, int value, FiniteSet right){
	this.left = left;
	this.value = value;
	this.right = right; }

    // cardinality(FiniteSet) -> int
    // returns 1 (size of value) plus the size of left (recursive call to method) and the size of right (recursive call of method)
    public int cardinality () { return (left.cardinality() + right.cardinality() + 1); }
 
    // isEmptyHuh(FiniteSet) -> boolean
    // because a Tree by definition contains an integer value, will always return false
    public boolean isEmptyHuh () { return false; }

    // member(FiniteSet, int) -> boolean
    // REQUIRES: Tree adheres to constructor parameters, with value.right > this.value (or empty) and
    // left.value < this.value (or empty)
    // returns true if int equal to this.value or is a member of either left or right, else will terminate
    // by a call member on left or right that is empty (which as defined in the Leaf() class returns false)
    public boolean member(int elt) {
	if (value == elt) { return true; } 
	else if (elt > value) { return right.member(elt);
	} else { return left.member(elt); }
    }

    // add(FiniteSet, int) -> FiniteSet
    // if the int is already equal to a value on the Tree, returns the Tree unchanged; else, places value in tree 
    // according to left.value < this.value < right.value comparisons
    public FiniteSet add(int elt) {
	if (value == elt) { return this;
	} else if (elt > value) { return new Tree(left, value, right.add(elt));
	} else { return new Tree(left.add(elt), value, right); }
    }

    // remove(FiniteSet int) -> FiniteSet
    // REQUIRES: union(FiniteSet) is a defined method for Tree which combines two Finite Sets into one set,
    // which contain at most only one instance of any given int;
    // there are no duplicates on the original FiniteSet (as per the definition of a FiniteSet)
    // if the int is on the Tree it is removed and the two Trees on left and right are combined into one tree
    // according to the behavior of FiniteSet construction and union
    public FiniteSet remove(int elt) {
	if (value == elt) { return left.union(right);
	} else if (elt > value) { return new Tree (left, value, right.remove(elt));
	} else { return new Tree (left.remove(elt), value, right); }
    }

    // union(FiniteSet, FiniteSet) -> FiniteSet
    // REQUIRES: add(int) is a defined method for Trees and obeys the definition of Trees and order of values
    // returns a FiniteSet of one instance of every value on the first and second set, organized according to the 
    // definition of Trees
    public FiniteSet union(FiniteSet u) { return right.union(left.union(u)).add(value); }

    // inter (FiniteSet, FiniteSet) -> FiniteSet
    // returns a FiniteSet of the ints that are a member of both sets; if no values are shared, returns empty set
    public FiniteSet inter(FiniteSet u) {
	if (u.member(value)) { return new Tree (left.inter(u), value, right.inter(u));
	} else { return left.inter(u).union(right.inter(u)); }
    }

    // diff(FiniteSet, FiniteSet) -> FiniteSet
    // returns the second set minus the ints on the first set by creating a new smaller tree using this.left and this.right,
    // removing this.value from the second set (if it's there), otherwise the second set is unchanged
    // and then calling diff again using the smaller tree and the second set until this is the empty set
    public FiniteSet diff(FiniteSet u) {
	FiniteSet TreeBranches = left.union(right);
	//	if (u.member(value)) { 
	return TreeBranches.diff(u.remove(value));
	//} else { return TreeBranches.diff(u); }
    }

    // equal(FiniteSet, FiniteSet) -> boolean
    // REQUIRES: subset(FiniteSet) is defined and checks to see if this is contained in the second set
    // returns true both sets contain exactly the same elements by checking if they are subsets of each other;
    // false if their elements are not the exact same 
    public boolean equal(FiniteSet u) { return this.subset(u) && u.subset(this); }

    // subset(FiniteSet, FiniteSet) -> boolean
    // returns false if this.value is not a member of the second set, otherwise checks if the second set is a subset of 
    // the union of this.left and this.right
    public boolean subset(FiniteSet u) {
	if(!u.member(value)) { return false;
	} else { return this.left.union(this.right).subset(u); } 

    }

}

public class Data1 {

    public static void main(String[] args){

	//instance of using empty() method to return a new empty set (rather than using the constructor)
	Leaf emptyBranch = new Leaf();
	emptyBranch = emptyBranch.empty();
	Leaf emptyBranch2 = new Leaf();

	//checks whether emptyBranch and emptyBranch2 are empty or not and are they both equal. All results should be true
	System.out.println ("Are emptyBranch and emptyBranch2 both empty and are they equal to each other? \n All should be TRUE." + "\n Answers: \n Empty? emptyBranch1: " 
			    + emptyBranch.isEmptyHuh() + "\n emptyBranch2: " + emptyBranch2.isEmptyHuh() + "\n Equal? " + emptyBranch.equal(emptyBranch2));

	Tree branch1 = new Tree(emptyBranch, 1, emptyBranch); 
	Tree branch7 = new Tree(emptyBranch, 7, emptyBranch);
	Tree branch3 = new Tree(emptyBranch, 3, emptyBranch);
	Tree branch5 = new Tree(branch3, 5, branch7);

	//Cardinality Tests: using finite values first to show that the desired value is given, then uses random tests to show 
	//that it works for any FiniteSet created, not just the ones chosen
	//checks to see if Leaf.cardinality() runs correctly
	System.out.println("The size of emptyBranch should be 0: the actual size is " + emptyBranch.cardinality());
	//Tests Tree.cardinality()
	System.out.println("The size of branch5 should be 3: the actual size is " + branch5.cardinality());
	System.out.println("The size of branch5.union(branch1) should be 4: the actual size is " + branch5.union(branch1).cardinality());

	Testing t = new Testing();

	//Checking Random Finite Sets to see if they are equal to each other using their union and intersection
	System.out.println("out of 100 tests, if the intersection and the union of two sets are equal to one another, then the two sets are equal... here are the results ( . = pass) \n");
	for ( int i = 0; i < 100; i ++ ) {
            int size = t.randomInt(0, 5);
	    int size2 = t.randomInt(0,5);
            FiniteSet tester = t.randomFiniteSet(0, 5, size);
	    FiniteSet tester2 = t.randomFiniteSet(0, 5, size2);
            t.testingEquality( tester, tester2);
	}}

    static class Testing {
	    
	//creates a random FiniteSet of given size with values between min and max, using the add method will 
	//prevent duplicates from being added (obeying the property of sets

	public FiniteSet randomFiniteSet( int lower, int upper, int size ) {
	    if (size == 0) { return new Leaf();}
	    else { return randomFiniteSet(lower, upper, (size - 1)).add(randomInt(lower, upper));}
	}

	Random rand = new Random();

	public int randomInt( int min, int max ) {
	    return rand.nextInt((max - min) + 1) + min; }

	public void testingEquality(FiniteSet tester, FiniteSet tester2) {
	    //if the union of tester and tester2 equals the intersection of tester and tester2, tester must equal tester 2,
	    //either because the union and intersection are both empty (meaning both sets are empty) or because tester and tester2
	    //contain the same elements and therefore the intersection is equal to the union  
	    if ( (tester.union(tester2) == tester.inter(tester2)) == tester.equal(tester2) ) {
		System.out.println(".");
	    } else {
		System.out.println("Fail!");
	    }
	}}

}



