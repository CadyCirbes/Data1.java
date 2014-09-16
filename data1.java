//A BST (binary search tree) is either:
// -an instance of the class Empty_BST (which is empty) or
// -an instance of the class Cons_BST, which contains:
// a left node (type BST), right node (type BST), and self value (type int) 
  
interface BST {
    //size() BST --> int 
    public int size();
}


class Empty_BST implements BST{

    Empty_BST() { }
}

class Cons_BST implements BST{}


 //construct

//        (empty) → finite-set
// (cardinality t) → integer
// (isEmptyHuh t) → boolean
// (member t elt) → boolean
// (add t elt) → finite-set
// (remove t elt) → finite-set
// (union t u) → finite-set
// (inter t u) → finite-set
// (diff t u) → finite-set
//(equal t u) → boolean
//(subset t u) → boolean
