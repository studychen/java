
public class LowestCommonAncestorBinarySearchTree {
	public static void main(String[] args) {
		TreeNode r0 = new TreeNode(8);
		TreeNode r11 = new TreeNode(4);
		TreeNode r12 = new TreeNode(10);
		TreeNode r21 = new TreeNode(2);
		TreeNode r31 = new TreeNode(1);
		TreeNode r23 = new TreeNode(9);
		TreeNode r24 = new TreeNode(12);
		r0.left = r11;
		r0.right = r12;
		r11.left = r21;
		r12.left = r23;
		r12.right = r24;
		r21.left = r31;

		System.out.println(isReach(r0, r11));
		System.out.println(isReach(r11, r12));
		System.out.println(isReach(r11, r31));
		System.out.println(isReach(r11, r11));

		System.out.println(lowestCommonAncestor(r0, r11, r31));
		System.out.println(lowestCommonAncestor(r0, r21, r31));
		System.out.println(lowestCommonAncestor(r0, r11, r31));
		System.out.println(lowestCommonAncestor(r0, r11, r23));
		System.out.println(lowestCommonAncestor(r0, r11, null));
		System.out.println(lowestCommonAncestor(r0, null, r21));
		System.out.println(lowestCommonAncestor(r0, null, null));
		System.out.println(lowestCommonAncestor(null, null, null));

	}

	public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null)
			return null;
		if (p == null && q == null) {
			return root;
		}
		if (p == null) {
			return q;
		}
		if (q == null) {
			return p;
		}

		if (Math.max(p.val, q.val) < root.val) {
			return lowestCommonAncestor(root.left, p, q);
		}
		if (Math.min(p.val, q.val) > root.val) {
			return lowestCommonAncestor(root.right, p, q);
		}
		return root;
		// if (isReach(root, p) && isReach(root, q) && (!isReach(root.left, p)
		// || !isReach(root.left, q))
		// && (!isReach(root.right, p) || !isReach(root.right, q))) {
		// return root;
		// }
		//
		// if (isReach(root.left, p) && isReach(root.left, q)) {
		// return lowestCommonAncestor(root.left, p, q);
		// } else {
		// return lowestCommonAncestor(root.right, p, q);
		// }

	}

	public static boolean isReach(TreeNode root, TreeNode p) {
		if (root == null) {
			return false;
		}
		if (root == p) {
			return true;
		} else {
			return isReach(root.left, p) || isReach(root.right, p);
		}

	}

}
