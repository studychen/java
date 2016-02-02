import java.util.ArrayList;
import java.util.List;

public class InvertBinaryTree {
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
		 outputTree(r0);
		 invertTree(r0);
		 outputTree(r0);
		
		TreeNode rr0 = new TreeNode(4);

		TreeNode rr11 = new TreeNode(2);
		TreeNode rr12 = new TreeNode(7);

		TreeNode rr21 = new TreeNode(1);
		TreeNode rr22 = new TreeNode(3);
		TreeNode rr23 = new TreeNode(6);
		TreeNode rr24 = new TreeNode(9);

		rr0.left = rr11;
		rr0.right = rr12;

		rr11.left = rr21;
		rr11.right = rr22;
		rr12.left = rr23;
		rr12.right = rr24;
		// outputTree(rr0);
		// invertTree(rr0);
		// outputTree(rr0);

		TreeNode q0 = new TreeNode(2);
		TreeNode q11 = new TreeNode(3);
		TreeNode q21 = new TreeNode(1);

		q0.left = q11;
		q11.left = q21;

		outputTree(q0);
		invertTree(q0);
		outputTree(q0);

	}

	public static TreeNode invertTree(TreeNode root) {
		if (root == null) {
			return null;
		}
		if (root.left != null && root.right != null) {
			int temp = root.left.val;
			root.left.val = root.right.val;
			root.right.val = temp;

			TreeNode tempLeft = root.left.left;
			root.left.left = root.right.left;
			root.right.left = tempLeft;

			TreeNode tempRight = root.left.right;
			root.left.right = root.right.right;
			root.right.right = tempRight;

			invertTree(root.left);
			invertTree(root.right);
		}
		if (root.left == null && root.right == null) {
			return root;
		}
		if (root.left == null) {
			root.left = new TreeNode(root.right.val);
			root.left.left = root.right.left;
			root.left.right = root.right.right;
			root.right = null;
			invertTree(root.left);
			return root;
		}

		if (root.right == null) {
			root.right = new TreeNode(root.left.val);
			root.right.left = root.left.left;
			root.right.right = root.left.right;
			root.left = null;
			invertTree(root.right);
			return root;
		}

		return root;

	}

public static void outputTree(TreeNode root) {
	if (root == null) {
		return;
	}
	System.out.println(root);
	List<TreeNode> former = new ArrayList<TreeNode>();
	former.add(root);
	while (former.size() != 0) {

		List<TreeNode> current = new ArrayList<TreeNode>();

		for (TreeNode node : former) {
			System.out.print(node.left + ", ");
			System.out.print(node.right + ", ");
			if (node.left != null) {
				current.add(node.left);
			}
			if (node.right != null) {
				current.add(node.right);
			}
		}
		System.out.println();
		former.clear();
		former.addAll(current);
	}
}
}
