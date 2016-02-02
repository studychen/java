public class SameTree {
	public static void main(String[] args) {
		TreeNode r0 = new TreeNode(00);
		TreeNode r11 = new TreeNode(11);
		TreeNode r12 = new TreeNode(12);
		TreeNode r21 = new TreeNode(21);
		TreeNode r31 = new TreeNode(31);
		r0.left = r11;
		r0.right = r12;
		r11.left = r21;
		r21.left = r31;

		TreeNode rr0 = new TreeNode(00);
		TreeNode rr11 = new TreeNode(11);
		TreeNode rr12 = new TreeNode(12);
		TreeNode rr21 = new TreeNode(21);
		TreeNode rr31 = new TreeNode(31);
		rr0.left = rr11;
		rr0.right = rr12;
		rr11.left = r21;
		rr21.left = rr31;

		System.out.println(isSameTree(rr31, r31));

	}

	public static boolean isSameTree(TreeNode p, TreeNode q) {
		if (p == null && q == null)
			return true;
		if (p == null)
			return false;
		if (q == null)
			return false;

		if (p.val != q.val) {
			return false;
		}
		return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);

	}

}

class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode(int x) {
		val = x;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return val+"";
	}
}
