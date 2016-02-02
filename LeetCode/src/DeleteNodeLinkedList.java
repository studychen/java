
public class DeleteNodeLinkedList {
	public static void main(String[] args) {
		ListNode n0 = new ListNode(10);
		ListNode n1 = new ListNode(11);
		ListNode n2 = new ListNode(12);
		ListNode n3 = new ListNode(13);

		n0.next = n1;
		n1.next = n2;
		n2.next = n3;

		System.out.println("val before delete in main " + n2.val);

		deleteNode(n2);

		System.out.println("val after delete in main " + n2.val);

	}

	public static void deleteNode(ListNode node) {

		if (node == null) {
			return;
		}
		// the last node
		if (node.next == null) {
			node = null;
		}

		System.out.println("val before assignment in deleteNode " + node.val);

		node.val = node.next.val;

		System.out.println("val after assignment in deleteNode " + node.val);

		// node.val = node.next.val;
		// node.next = node.next.next;
	}

}

class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
	}
	// @Override
	// public String toString() {
	// return " -->ListNode " + val + " -->";
	// }
}
