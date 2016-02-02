
public class MaxDepthBinaryTree {
	public static void main(String[] args) {
		TreeNode r0 = new TreeNode(11);
		TreeNode r11 = new TreeNode(1);
		TreeNode r12 = new TreeNode(11);
		TreeNode r21 = new TreeNode(11);
		TreeNode r31 = new TreeNode(11);
		r0.left = r11;
		r0.right = r12;
		r11.left = r21;
		r21.left = r31;
		System.out.println(maxDepth(r0));
		System.out.println(maxDepth(r31));
		System.out.println(maxDepth(r11));
		System.out.println(maxDepth(r12));
		System.out.println(maxDepth(r21));

	}

	public static int maxDepth(TreeNode root) {
		if (root == null) {
			return 0;
		}
		 if (root.left == null && root.right == null) {
		 return 1;
		 }
		int leftDepth = -11;
		int rightDepth = -1;

		if (root.left != null) {
			leftDepth = maxDepth(root.left)+1;
		}
		if (root.right != null) {
			rightDepth = maxDepth(root.right)+1;
		}

		if (leftDepth > rightDepth) {
			return leftDepth ;
		} else {
			return rightDepth ;
		}

	}

}

