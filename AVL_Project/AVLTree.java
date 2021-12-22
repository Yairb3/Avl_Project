import java.util.*;

/**
 *
 * AVLTree
 *
 * An implementation of a׳³ן¿½ AVL Tree with
 * distinct integer keys and info.
 *
 */

public class AVLTree {

	 public  IAVLNode root = null; // Three basic fields for each Tree
	 public IAVLNode maxNode = new VirtualAVLNode();
	 public IAVLNode minNode = new VirtualAVLNode();



	/**
   * public boolean empty()
   *
   * Returns true if and only if the tree is empty.
   *
   */
  public boolean empty() { // Time Complexity O(1)

	  if (root==null) { // if root can be a pointer toward "null" or to "Node"

		  return true;
	  }
      return false;
  }

 /**
   * public String search(int k)
   *
   * Returns the info of an item with key k if it exists in the tree.
   * otherwise, returns null.
   */
  public String search(int k)
  {
	  if (root == null) { // edge Case - the Tree is empty

		  return null;

	  }else {

		  return (recSearch(root,k)); // using recursion method - Time complexity O(log(n))

	  }

  }

  public String recSearch (IAVLNode node,int k) { // each time we call the function for a 1/2 a Tree, Time Complexity O(log(n))
	  if (!node.isRealNode()) { // stop condition
		  return null;
	  }

	  if (node.getKey() == k) { // return the value if the key is founded
		  return node.getValue();

	  }else if(node.getKey() > k) {
		  return recSearch(node.getLeft(),k);

	  } else {
			  return recSearch(node.getRight(),k);
	  }

  }

  /**
   * public int insert(int k, String i)
   *
   * Inserts an item with key k and info i to the AVL tree.
   * The tree must remain valid, i.e. keep its invariants.
   * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
   * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
   * Returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {

	   if (root == null) {  // Spacial case, if the tree is empty, insert the new element as the tree root, size = 1
		   root = new AVLNode(k,i);
		   this.maxNode = this.getMaxNode(); // using those methods to update the max pointer and the min pointer
		   this.minNode = this.getMinNode();
		   return 0; // 0 rebalanced action is needed
	   }


	   IAVLNode relevatParent = getInsertionParent(root,k); /* 1st step - use this function to find the right node which the new element should be added below to.
	   might be his left son or right son*/

	   if (relevatParent == null) { // means the key is already in the tree
		   return -1;
	   }

	   Insertion(relevatParent,k,i); // 2nd step - use this function to make the actual Insertion not including the balance operation

	   this.maxNode = this.getMaxNode(); // using those methods to update the max pointer and the min pointer
	   this.minNode = this.getMinNode();

	  return getMoves(relevatParent); // 3rd step - using this function to count the balanced moves, including making the balance operations
   }

   public  IAVLNode getInsertionParent (IAVLNode node, int k) { // finding the right node which the new element should be added below to. O(log(n))

	   while (node.isRealNode()) {

		  if (node.getKey() == k) { // means the key is already in the Tree
			  return null;

		  }else if(node.getKey() > k) {
			  node=node.getLeft();

		  }else if(node.getKey() < k) {
			  node=node.getRight();
		  }

	  }
	   return node.getParent();
   }

   private void Insertion (IAVLNode node, int k, String i) { // make the actual Insertion not including the balance operation and also update the sizes - O(log(n))

	   IAVLNode newNode = new AVLNode(k,i); // creating a new node for the new key

	   if (k < node.getKey()) {

		   node.setLeft(newNode);
	   }
	   if (k > node.getKey()) {

		   node.setRight(newNode);
	   }

	   newNode.setParent(node);

	   while(node != null){ // using this while loop to update the sizes of the relevant nodes - this part of the code is O(log(n))

		   node.setSize(node.getSize() + 1);

		   node = node.getParent();
	   }

   }

   public int getMoves(IAVLNode parent) { // using this function to count the balanced moves, including making the balance operations - O(log(n))

	   int move = 0;

	   while (parent != null) {
		   int deltaRight =parent.getHeight() - parent.getRight().getHeight() ; // the right edge of current node
		   int deltaLeft = parent.getHeight() -parent.getLeft().getHeight(); // the left edge of the current node

		   // case 1
		   if (deltaRight == 1 && deltaLeft == 1)  {
			   return move; // no more balance steps are required
		   }
		   // case 2

		   if ((deltaRight == 1 && deltaLeft == 0 ) || (deltaRight == 0 && deltaLeft == 1 )){
			   parent.setHeight(parent.getHeight()+1); // promote the current node
			   move++;
			   parent=parent.getParent(); // problem may moved up
		   }

		   // case 3A

		   if ((deltaLeft == 0 && deltaRight == 2 )) {


			   int sDeltaL = parent.getLeft().getHeight()-parent.getLeft().getLeft().getHeight(); // we need to observe the status of the leftSon
			   int sDeltaR = parent.getLeft().getHeight()-parent.getLeft().getRight().getHeight();

			   if (sDeltaL == 1 && sDeltaR == 2) { // one rotation Right is needed
				   rotationRight( parent , parent.getLeft());
				   move ++;
				   return move; // then problem solved

			   }else if (sDeltaL == 2 && sDeltaR == 1) { // double rotation is needed
				   rotationLeft( parent.getLeft() , parent.getLeft().getRight());
				   rotationRight( parent , parent.getLeft());
				   parent.getParent().setHeight(parent.getParent().getHeight()+1);
				   move +=2;
				   return move; // then problem solved

			   }else if (sDeltaL == 1 && sDeltaR == 1) { // spacial case for Join function
				   rotationRight(parent, parent.getLeft());
				   parent.getParent().setHeight(parent.getParent().getHeight() + 1);
				   parent.setHeight(parent.getHeight() + 1);

				   move += 1;
				   return move; // then problem solved
			   }




		   	}

		   //case 3B - up to symmetry

		   		else if((deltaLeft == 2 && deltaRight == 0 )) {

			   int sDeltaL = parent.getRight().getHeight()-parent.getRight().getLeft().getHeight();
				   int sDeltaR = parent.getRight().getHeight()-parent.getRight().getRight().getHeight();

				   if (sDeltaL == 2 && sDeltaR == 1) { // one rotation Left is needed
					   rotationLeft( parent , parent.getRight());
					   move ++;
					   return move;

				   }else if (sDeltaL == 1 && sDeltaR == 2) { // double rotation is needed

					   rotationRight( parent.getRight() , parent.getRight().getLeft());
					   rotationLeft( parent , parent.getRight());
					   parent.getParent().setHeight(parent.getParent().getHeight()+1);
					   move +=2;
					   return move;

				   }else if (sDeltaL == 1 && sDeltaR == 1) { // spacial case for Join function
					   rotationLeft(parent, parent.getRight());
					   parent.getParent().setHeight(parent.getParent().getHeight() + 1);
					   parent.setHeight(parent.getHeight() + 1);


					   move += 1;
					   return move;
				   }



		   }

	   }

	   return move;
   }



   public  void rotationRight(IAVLNode father , IAVLNode son) { // O(1)

	   father.setLeft(son.getRight());
	   son.getRight().setParent(father);

	   if (father.getParent() != null ) {

	   if (father.getParent().getLeft() == father) {
		   father.getParent().setLeft(son);
	   }
	   else {
		   father.getParent().setRight(son);
	   }
	   son.setParent(father.getParent());

	  }

	   else { // father is the root
		   son.setParent(null);
		   this.root = son;
	   }

	   son.setRight(father);
	   father.setParent(son);
	   father.setHeight(father.getHeight()-1);

	   father.setSize(father.getLeft().getSize() + father.getRight().getSize() + 1);
	   son.setSize(son.getLeft().getSize() + son.getRight().getSize() + 1);

   }

   public  void rotationLeft(IAVLNode father , IAVLNode son) { // O(1)
	   father.setRight(son.getLeft());
	   son.getLeft().setParent(father);
	   if (father.getParent() != null ) {
		   if (father.getParent().getRight() == father) {
		   father.getParent().setRight(son);
	   }
	   else {
		   father.getParent().setLeft(son);
	   }
	   son.setParent(father.getParent());
	  }
	   else {
		   son.setParent(null);
		   this.root = son;
	   }
	   son.setLeft(father);
	   father.setParent(son);
	   father.setHeight(father.getHeight()-1);

	   father.setSize(father.getLeft().getSize() + father.getRight().getSize() + 1);
	   son.setSize(son.getLeft().getSize() + son.getRight().getSize() + 1);

   }


  /**
   * public int delete(int k)
   *
   * Deletes an item with key k from the binary tree, if it is there.
   * The tree must remain valid, i.e. keep its invariants.
   * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
   * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
   * Returns -1 if an item with key k was not found in the tree.
   */
  public int delete(int k)
  {

	  if (root == null){ // edge case 1 - empty tree

		  return -1;

	  } // edge case 1 - tree is empty

	  IAVLNode toBeDeleted = SearchNode(root,k); // step 1- find the node with key == k  , TC = (O(log(n))

	  if (toBeDeleted == null){ // means 'toBeDeleted' was not found

		  return -1;
	  }

	  IAVLNode parent = deletion(toBeDeleted); // parent is the node which the correction should be start from

	  if (parent == null){ // edge case 2 - tree size == 1
		  return -1;
	  }

	  this.maxNode = this.getMaxNode(); // using those methods to update the max pointer and the min pointer
	  this.minNode = this.getMinNode();

	  return getMovesdelete(parent);
  }

	public IAVLNode deletion(IAVLNode toBeDeleted){ // executing the deletion, and return the node which the correction should be start from

		IAVLNode parent = toBeDeleted.getParent();

		if (toBeDeleted == root && !root.getLeft().isRealNode() && !root.getRight().isRealNode()){ // edge case 2 - tree size == 1
			root = null;
			return null;
		}

		if (!toBeDeleted.getLeft().isRealNode() && !toBeDeleted.getRight().isRealNode()){ // case 1 - node is a leaf

			if (parent.getRight() == toBeDeleted){ // "toBeDeleted" is the right son

				parent.setRight(toBeDeleted.getRight());
				parent.getRight().setParent(parent);


			}else{ // "toBeDeleted" is the left son

				parent.setLeft(toBeDeleted.getLeft());

			}

			toBeDeleted.setParent(null);

		} // case 1 - node is a leaf

		else if(!toBeDeleted.getLeft().isRealNode() || !toBeDeleted.getRight().isRealNode()){ // case 2 - "toBeDeleted" is an unary node

			IAVLNode temp;

			temp = toBeDeleted.getRight().isRealNode() ? toBeDeleted.getRight() : toBeDeleted.getLeft();

			if (parent != null) {

				if (parent.getRight() == toBeDeleted) { // "toBeDeleted" is the right son

					parent.setRight(temp);


				} else { // "toBeDeleted" is the left son

					parent.setLeft(temp);

				}
			} else { // parent == null - root is an unary node

				root = root.getRight().isRealNode() ? root.getRight() : root.getLeft();

			}

			temp.setParent(parent);

			toBeDeleted.setParent(null);

		}else{ // case 3 -  "toBeDeleted" has two sons

			IAVLNode successor = getSuccessor(toBeDeleted); // get the Successor - TC - O(log(n))

			swapNodes(toBeDeleted,successor); // O(1)

			 parent = deletion(toBeDeleted);

		}

		IAVLNode tempparent = parent;

		while (tempparent != null){ // going up to the root and updating the size field the relevant node

			tempparent.setSize(tempparent.getLeft().getSize() + tempparent.getRight().getSize() + 1);

			tempparent = tempparent.getParent();
		}

		return parent;

	}


	//@pre - node has two sons

	public IAVLNode getSuccessor (IAVLNode node){ // return node's successor

		IAVLNode successor = node.getRight();

		while (successor.getLeft().isRealNode()){

			successor = successor.getLeft();
		}

		return successor;

	}


	public IAVLNode SearchNode (IAVLNode node,int k) { // using this method to return an IAVLNode with key == k - out of subtree with root "node, O(logn(n))
		if (!node.isRealNode()) {
			return null;
		}
		if (node.getKey() == k) {
			return node;
		}else if(node.getKey() > k) {
			return SearchNode(node.getLeft(),k);
		}else  {
			return SearchNode(node.getRight(),k);
		}
	}

	public  void swapNodes (IAVLNode toBeDeleted, IAVLNode successor){ // switch places of Node1 and Node2 in a tree - O(1)

		IAVLNode tempSLeftSon = successor.getLeft();
		IAVLNode temoSRightSon = successor.getRight();
		IAVLNode tempSParent = successor.getParent();


		if (toBeDeleted == this.root){

			this.root = successor;
			successor.setParent(null);

		}else{

			successor.setParent(toBeDeleted.getParent());

			if(toBeDeleted.getParent().getRight() == toBeDeleted){
				toBeDeleted.getParent().setRight(successor);
			}else{
				toBeDeleted.getParent().setLeft(successor);
			}

		}

		if(toBeDeleted.getRight() == successor){
			successor.setRight(toBeDeleted);
			toBeDeleted.setParent(successor);
		}else{
			toBeDeleted.setParent(tempSParent);
			tempSParent.setLeft(toBeDeleted);
			successor.setRight(toBeDeleted.getRight());
		}

		successor.getRight().setParent(successor);
		successor.setLeft(toBeDeleted.getLeft());
		successor.getLeft().setParent(successor);

		toBeDeleted.setRight(temoSRightSon);
		toBeDeleted.setLeft(tempSLeftSon);
		toBeDeleted.getLeft().setParent(toBeDeleted);
		toBeDeleted.getRight().setParent(toBeDeleted);
		successor.setHeight(toBeDeleted.getHeight());

	}



	public int getMovesdelete (IAVLNode parent){ // using this method to return the sum of rebalance action after deleting a node - O(log(n))

	  int moves = 0;

	  while (parent != null){

		  if(correctRanks(parent)){ // no rebalance is needed
			  return moves;
		  }

		  int dL = parent. getHeight() - parent.getLeft().getHeight();
		  int dR = parent. getHeight() - parent.getRight().getHeight();


		  if (dL == 2 && dR ==2){ // case 1 - dL == 2 & dR == 2

			  parent.setHeight(parent.getHeight()-1); // demote parent
			  moves += 1;


		  } // case 1 - dL == 2 & dR == 2

		 else  if(dL == 3 && dR == 1){ // case 2A - dL == 3 &&  dR == 1

			  int sDeltaL = parent.getRight().getHeight()-parent.getRight().getLeft().getHeight(); // we need to observe the status of the leftSon
			  int sDeltaR = parent.getRight().getHeight()-parent.getRight().getRight().getHeight();

			   if (sDeltaL == 1 && sDeltaR == 1){ // case 2Aa sDeltaL == SDeltaR == 1

				   rotationLeft(parent,parent.getRight()); // z , y
				   parent.getParent().setHeight(parent.getParent().getHeight() + 1); // after the rotation we need also to promote y, z demote included in the rotation method
				   moves ++;

			   } // case 2Aa sDeltaL == SDeltaR == 1

			   if (sDeltaL == 1 && sDeltaR == 2){ //  case 2Ab sDeltaL == 1 SDeltaR == 2 - double rotation is needed

				   rotationRight(parent.getRight(),parent.getRight().getLeft());
				   rotationLeft(parent,parent.getRight());
				   parent.setHeight(parent.getHeight()-1);
				   parent.getParent().setHeight(parent.getParent().getHeight()+1);
				   moves += 2;

			   }

			  if (sDeltaL == 2 && sDeltaR == 1){ //  case 2Ac sDeltaL == 2 SDeltaR == 1 - single rotation is needed


				  rotationLeft(parent,parent.getRight());
				  parent.setHeight(parent.getHeight()-1);

				  moves += 1;

			  }

			  parent = parent.getParent();


		  }else{ // case 2B - dL == 1 & dR == 3

			  int sDeltaL = parent.getLeft().getHeight()-parent.getLeft().getLeft().getHeight(); // we need to observe the status of the leftSon
			  int sDeltaR = parent.getLeft().getHeight()-parent.getLeft().getRight().getHeight();

			  if (sDeltaL == 1 && sDeltaR == 1){ // case 2Ba sDeltaL == SDeltaR == 1

				  rotationRight(parent,parent.getLeft()); // z , y
				  parent.getParent().setHeight(parent.getParent().getHeight() + 1); // after the rotation we need also to promote y, z demote included in the rotation method
				  moves ++;

			  } // case 2Ba sDeltaL == SDeltaR == 1

			  if (sDeltaL == 2 && sDeltaR == 1){ //  case 2Bb sDeltaL == 2 SDeltaR == 1 - double rotation is needed

				  rotationLeft(parent.getLeft(),parent.getLeft().getRight());
				  rotationRight(parent,parent.getLeft());
				  parent.setHeight(parent.getHeight()-1);
				  parent.getParent().setHeight(parent.getParent().getHeight()+1);
				  moves += 2;

			  }

			  if (sDeltaL == 1 && sDeltaR == 2){ //  case 2Bc sDeltaL == 1 SDeltaR == 2 - single rotation is needed

				  rotationRight(parent,parent.getLeft());
				  parent.setHeight(parent.getHeight()-1);
				  moves += 1;

			  }

			  parent = parent.getParent();

		  }

		  parent = parent.getParent();

	  }



	  return moves;
	}

	public boolean correctRanks (IAVLNode parent){

	  int dL = parent. getHeight() - parent.getLeft().getHeight();
	  int dR = parent. getHeight() - parent.getRight().getHeight();


	  if ((dL == 1 && dR == 2) || (dL == 2 && dR == 1) || (dL == 1 && dR == 1)){
		  return true;
	  }
	  return false;
	}

	/**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty.
    */
   public String min()
   {
	   if (this.root == null){

		   return null;
	   }

	  return this.minNode.getValue();

   }

   public IAVLNode getMinNode (){

	   if (root == null)
		   return null;
	   IAVLNode node = root;
	   while (node.getLeft().isRealNode() ) {
		   node=node.getLeft();
	   }
	   return node;


   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty.
    */
   public String max() {

	   if (this.root == null){

		   return null;
	   }

	   return this.maxNode.getValue();
   }

	public IAVLNode getMaxNode (){

		if (root == null)
			return null;
		IAVLNode node = root;
		while (node.getRight().isRealNode() ) {
			node=node.getRight();
		}
		return node;


	}

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  if (this.empty()){ // case that the tree is empty
		  return new int [0];
	  }

	  int n = this.size();
	  int [] keyArray = new int [n];

	  IAVLNode current = root;
	  Stack tem = new Stack();
	  int j = 0;


	  while (current.isRealNode() || !tem.empty()){

		  while (current.isRealNode()){

			  tem.push(current);
			  current = current.getLeft();

		  }

		  current = (IAVLNode) tem.pop();
		  keyArray[j] = current.getKey();
		  j++;
		  current = current.getRight();

	  }

	  return keyArray;

  }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray() {

	  if (this.empty()){ // case that the tree is empty
		  return new String [0];
	  }

	  int n = this.size();
	  String [] infoArray = new String [n];
	  IAVLNode tempnode;

	  IAVLNode current = root;
	  Stack tem = new Stack();
	  int j = 0;


	  while (current.isRealNode() || !tem.empty()){

		  while (current.isRealNode()){

			  tem.push(current);
			  current = current.getLeft();

		  }

		 current = (IAVLNode) tem.pop();
		  infoArray[j] = current.getValue();
		  j++;
		  current = current.getRight();

	  }

	  return infoArray;





  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    */
   public int size()
   {
	   if (this.empty()){
		   return 0;
	   }else{

		   return root.getSize();
	   }
   }

   /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    */
   public IAVLNode getRoot()
   {
	   return root;
   }

   /**
    * public AVLTree[] split(int x)
    *
    * splits the tree into 2 trees according to the key x.
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
    *
	* precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
    */
   public AVLTree[] split(int x) {
	   IAVLNode candidate = SearchNode(root, x);
	   AVLTree [] splited = new AVLTree[2];

	   if( candidate.equals(root) && !candidate.getLeft().isRealNode() && !candidate.getRight().isRealNode()){ // only have one node

		   return splited;
	   }

	   int[] sizes = ArraysSizes(candidate);

	   int sN = 0;
	   int sT = 0;
	   int gN = 0;
	   int gT = 0;

	   IAVLNode[] smallerNode = new IAVLNode[sizes[0]];
	   AVLTree[] smallerTrees = new AVLTree[sizes[1]];
	   IAVLNode[] greaterNode = new IAVLNode[sizes[2]];
	   AVLTree[] greaterTrees = new AVLTree[sizes[3]];

	   smallerTrees[0] = new AVLTree();

	   if (candidate.getLeft().isRealNode()){

		   smallerTrees[0].root = candidate.getLeft();

	   }
	   //smallerTrees[0].root = candidate.getLeft().isRealNode() ? candidate.getLeft() : null;

	   if (smallerTrees[0].root != null){
		   smallerTrees[0].root.setParent(null);
	   }

	   candidate.setLeft(new VirtualAVLNode());
	   candidate.getLeft().setParent(candidate);

	   greaterTrees[0] = new AVLTree();

	   if (candidate.getRight().isRealNode()){

		   greaterTrees[0].root = candidate.getRight();

	   }

	   //greaterTrees[0].root = candidate.getRight().isRealNode() ? candidate.getRight() : null;
	   if (greaterTrees[0].root != null){
		   greaterTrees[0].root.setParent(null);
	   }
	   candidate.setRight(new VirtualAVLNode());
	   candidate.getRight().setParent(candidate);

	   sT += 1;
	   gT += 1;

	   while (candidate.getParent() != null) {

		   if (candidate.getParent().getRight() == candidate) {

			   smallerTrees[sT] = new AVLTree();

			   if (candidate.getParent().getLeft().isRealNode()){

				   smallerTrees[sT].root = candidate.getParent().getLeft();
				   smallerTrees[sT].root.setParent(null);

			   }

			   candidate.getParent().setLeft(new VirtualAVLNode());
			   candidate.getParent().getLeft().setParent(candidate.getParent());

			   sT += 1;

			   smallerNode[sN] = candidate.getParent();
			   candidate = candidate.getParent();
			   candidate.setRight(new VirtualAVLNode());
			   candidate.getRight().setParent(candidate);

			   sN += 1;

		   }else{

			   greaterTrees[gT] = new AVLTree();

			   if (candidate.getParent().getRight().isRealNode()){

				   greaterTrees[gT].root = candidate.getParent().getRight();
				   greaterTrees[gT].root.setParent(null);

			   }

			   candidate.getParent().setRight(new VirtualAVLNode());
			   candidate.getParent().getRight().setParent(candidate.getParent());

			   gT += 1;

			   greaterNode[gN] = candidate.getParent();
			   candidate = candidate.getParent();
			   candidate.setLeft(new VirtualAVLNode());
			   candidate.getLeft().setParent(candidate);

			   gN += 1;

		   }

	   }


	   AVLTree smaller = smallerTrees[0];
	   AVLTree greater = greaterTrees [0];

	   for (int i = 0; i < smallerNode.length; i++){

		   smaller.join(smallerNode[i],smallerTrees[i+1]);

	   }

	   for (int i = 0; i < greaterNode.length; i++){

		   greater.join(greaterNode[i],greaterTrees[i+1]);
	   }

	   splited [0] = smaller;
	   splited [1] = greater;

	   splited[0].maxNode = splited[0].getMaxNode(); // using those methods to update the max pointer and the min pointer
	   splited[0].minNode = splited[0].getMinNode();
	   splited[1].maxNode = splited[1].getMaxNode(); // using those methods to update the max pointer and the min pointer
	   splited[1].minNode = splited[1].getMinNode();

	   return splited;

   }

   public int [] ArraysSizes (IAVLNode candidate){

	  int [] sizes = new int [4];
	   int smallerNodes = 0;
	   int smallerTrees = 1;
	   int greaterNodes = 0;
	   int greaterTress = 1;


	   while (candidate.getParent() != null){

		   if (candidate.getParent().getRight() == candidate){

			   smallerNodes +=1;
			   smallerTrees +=1;

		   }else{

			   greaterNodes+=1;
			   greaterTress+=1;

		   }
		   candidate = candidate.getParent();
	   }

	   sizes [0] = smallerNodes;
	   sizes [1] = smallerTrees;
	   sizes [2] =greaterNodes;
	   sizes [3] = greaterTress;

	   return sizes;

   }

   /**
    * public int join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree.
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	*
	* precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
    */
   public int join(IAVLNode x, AVLTree t)
   {

	   if (this.empty() && t.empty()){ // edge case - joining two empty trees
		   this.root = x;
		   this.root.setSize(1);
		   return 0;
	   }

	   if(t.root == null){ // t is an empty Tree

		   this.insert(x.getKey(),x.getValue());

		   return (this.root.getHeight());

	   }

	   if(this.root == null){ // 'this' is an empty Tree

		   t.insert(x.getKey(),x.getValue());

		   this.root = t.root;
		   t.root = null;


		   return (this.root.getHeight());

	   }

	   int timeComplexity = Math.abs(this.getRoot().getHeight() - t.getRoot().getHeight()) + 1;

	   if (this.root.getHeight() == t.root.getHeight()){ // Case 1 - this & t have the same rank

		   if (this.root.getKey() > t.root.getKey()){


			   x.setRight(this.getRoot());
			   this.getRoot().setParent(x);

			   x.setLeft(t.getRoot());
			   t.getRoot().setParent(x);

			   x.setParent(null);
			   this.root = x;
			   t.root = null;

			   x.setHeight(x.getRight().getHeight() + 1);
		   }

		   else if (this.root.getKey() < t.root.getKey()){

			   x.setRight(t.getRoot());
			   t.getRoot().setParent(x);

			   x.setLeft(this.getRoot());
			   this.getRoot().setParent(x);

			   x.setParent(null);
			   this.root = x;
			   t.root = null;

			   x.setHeight(x.getRight().getHeight() + 1);

		   }

	   }

	   else if(this.getRoot().getHeight() > t.getRoot().getHeight()){ // this.rank > t.rank

		   IAVLNode relevantNode = this.root;

		   if (x.getKey() < this.getRoot().getKey()) {

			   while(relevantNode.getHeight() > t.getRoot().getHeight() && relevantNode.getLeft().isRealNode() && relevantNode.getHeight() > 0){ // going down left

				   relevantNode = relevantNode.getLeft();
			   }

				   x.setLeft(t.getRoot());
				   t.getRoot().setParent(x);
				   x.setRight(relevantNode);
				   if (relevantNode.getParent()!= null){
					   x.setParent(relevantNode.getParent());
					   x.getParent().setLeft(x);
				   }else{
					   x.setParent(null);
					   this.root = x;
				   }
				   relevantNode.setParent(x);
				   x.setHeight(Math.max(relevantNode.getHeight(),t.root.getHeight()) + 1);
				   t.root = null;

		   }else{ //x.getKey() > this.root.getkey

			   while(relevantNode.getHeight() > t.getRoot().getHeight() && relevantNode.getRight().isRealNode() && relevantNode.getHeight() > 0){

				   relevantNode = relevantNode.getRight();

			   }

			   x.setRight(t.getRoot());
			   t.getRoot().setParent(x);
			   x.setLeft(relevantNode);
			   if (relevantNode.getParent()!= null){
				   x.setParent(relevantNode.getParent());
				   x.getParent().setRight(x);
			   }else{
				   x.setParent(null);
				   this.root = x;
			   }
			   relevantNode.setParent(x);
			   x.setHeight(Math.max(relevantNode.getHeight(),t.root.getHeight()) + 1);
			   t.root = null;

		   }

	   }
	   else  { // this.rank < t.rank

		   IAVLNode relevantNode = t.root;

		   if (x.getKey() < t.getRoot().getKey()) {

			   while (relevantNode.getHeight() > this.getRoot().getHeight() && relevantNode.getLeft().isRealNode() && relevantNode.getHeight() > 0) {

				   relevantNode = relevantNode.getLeft();
			   }

			   x.setLeft(this.getRoot());
			   this.getRoot().setParent(x);
			   x.setRight(relevantNode);

			   if (relevantNode.getParent()!= null){
				   x.setParent(relevantNode.getParent());
				   x.getParent().setLeft(x);
				   x.setHeight(Math.max(relevantNode.getHeight(),this.root.getHeight()) + 1);
				   this.root = t.getRoot();

			   }else{
				   x.setParent(null);
				   x.setHeight(Math.max(relevantNode.getHeight(),this.root.getHeight()) + 1);
				   this.root = x;
			   }
			   relevantNode.setParent(x);

			   t.root = null;

		   } else { //x.getKey() > t.root.getkey

			   while (relevantNode.getHeight() > this.getRoot().getHeight() && relevantNode.getRight().isRealNode() && relevantNode.getHeight() > 0) {

				   relevantNode = relevantNode.getRight();
			   }

			   x.setRight(this.getRoot());
			   this.getRoot().setParent(x);
			   x.setLeft(relevantNode);

			   if (relevantNode.getParent()!= null){
				   x.setParent(relevantNode.getParent());
				   x.getParent().setRight(x);
				   x.setHeight(Math.max(relevantNode.getHeight(),this.root.getHeight()) + 1);
				   this.root = t.getRoot();

			   }else{
				   x.setParent(null);
				   x.setHeight(Math.max(relevantNode.getHeight(),this.root.getHeight()) + 1);
				   this.root = x;
			   }


			   relevantNode.setParent(x);

			   t.root = null;

		   }
	   }

	   x.setSize(x.getLeft().getSize() + x.getRight().getSize() + 1);

	   IAVLNode xTemp = x;

	   while (xTemp != null){

		   xTemp.setSize(xTemp.getLeft().getSize() + xTemp.getRight().getSize() + 1);

		   xTemp = xTemp.getParent();

	   }


	   getMoves(x.getParent()); // reballancing according to insert function + spacial case

	   this.maxNode = this.getMaxNode(); // using those methods to update the max pointer and the min pointer
	   this.minNode = this.getMinNode();

	   return timeComplexity;

   }





	/** 
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode{	
		public int getKey(); // Returns node's key (for virtual node return -1).
		public String getValue(); // Returns node's value [info], for virtual node returns null.
		public void setLeft(IAVLNode node); // Sets left child.
		public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
		public void setRight(IAVLNode node); // Sets right child.
		public IAVLNode getRight(); // Returns right child, if there is no right child return null.
		public void setParent(IAVLNode node); // Sets parent.
		public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
    	public void setHeight(int height); // Sets the height of the node.
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
		public int getSize();
		public void setSize(int size);
	}

   /** 
    * public class AVLNode
    *
    * If you wish to implement classes other than AVLTree
    * (for example AVLNode), do it in this file, not in another file. 
    * 
    * This class can and MUST be modified (It must implement IAVLNode).
    */
	
  public static class VirtualAVLNode implements IAVLNode	 {
	  
	  IAVLNode parent;
	  
	  public VirtualAVLNode (AVLNode node) { // #1 Constructor - used when creating a new AVLNode
		  
		   this.parent = node;
		   
	   }

	   public VirtualAVLNode () { // used while separating node from left or right Tree - during the Split function

		   this.parent = null;

	   }

	  public int getKey()
		{
			
			return -1; 
		}
		public String getValue()
		{
			return null; 
		}
		public void setLeft(IAVLNode node)
		{
		}
		public VirtualAVLNode getLeft()
		{
			return null; 
		}
		public void setRight(IAVLNode node)
		{
		}
		public VirtualAVLNode getRight()
		{
			return null; 
		}
		public void setParent(IAVLNode node)
		{
			this.parent =   node;
		}
		public IAVLNode getParent()
		{
			return this.parent; 
		}
		public boolean isRealNode()
		{
			return false; 
		}
	    public void setHeight(int height)
	    {
	    }
	    public int getHeight()
	    {
	      return -1; 
	    }

	   public int getSize(){
		  return 0;
	   }
	   public void setSize(int size){

	   }
	  
	  
	  
  }
	
  public class AVLNode implements IAVLNode{
	  
	   int key;
	   int height;
	   int size;
	   String info;
	   IAVLNode left;
	   IAVLNode right;
	   IAVLNode parent;
	   
	   public AVLNode (int k, String i) {
		   this.key=k;
		   this.info=i;
		   this.size = 1;
		   this.left  =  new VirtualAVLNode(this) ;
		   this.right = new VirtualAVLNode(this) ;
	   }
	   
	   
		public int getKey()
		{
			
			return this.key; 
		}
		public String getValue()
		{
			return this.info; 
		}
		public void setLeft(IAVLNode node)
		{
			this.left =  node;
		}
		public IAVLNode getLeft()
		{
			return this.left; 
		}
		public void setRight(IAVLNode node)
		{
			this.right =  node;
		}
		public IAVLNode getRight()
		{
			return this.right; 
		}
		public void setParent(IAVLNode node)
		{
			this.parent =  node;
		}
		public IAVLNode getParent()
		{
			return this.parent; 
		}
		public boolean isRealNode()
		{
			return true;
		}
	    public void setHeight(int height)
	    {
	      this.height = height;
	    }
	    public int getHeight()
	    {
	      return this.height; // to be replaced by student code
	    }

	  public int getSize(){
		  return this.size;
	  }
	  public void setSize(int size){

		   this.size = size;

	  }
  }

	public static void printBinaryTree(IAVLNode root, int space, int height) {
		// Base case
		if (!root.isRealNode()) {
			return;
		}

		// increase distance between levels
		space +=  height;

		// print right child first
		printBinaryTree(root.getRight(), space, height);
		System.out.println();

		// print the current node after padding with spaces
		for (int i = height; i < space; i++) {
			System.out.print(' ');
		}

		System.out.print(root.getKey());
		System.out.print(' ');
		System.out.print(root.getHeight());


		// print left child
		System.out.println();
		printBinaryTree(root.getLeft(), space, height);
	}

	

	

}
  
