package Models;

public class Tuple<L,R> {
	
	  private L left;
	  private R right;
	
	  public Tuple(L left, R right) {
	    this.left = left;
	    this.right = right;
	  }
	
	  public L getLeft() { return left; }
	  public R getRight() { return right; }
	  public void setLeft(L left) { this.left = left; }
	  public void setRight(R right) { this.right = right; }
	
	  @Override
	  public int hashCode() { return left.hashCode() ^ right.hashCode(); }
	
	  @Override
	  public boolean equals(Object o) {
	    if (!(o instanceof Tuple)) return false;
	    Tuple tuple = (Tuple) o;
	    if (this.left.equals(tuple.getLeft()) &&
	           this.right.equals(tuple.getRight()) )
	    	return true;
	    if(this.left.equals(tuple.getRight()) &&
	           this.right.equals(tuple.getLeft()))
	    	return true;
	    return false;
	  }
}
