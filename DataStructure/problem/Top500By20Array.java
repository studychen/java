package problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * 有20个数组，每个数组有500个元素，升序排列，现在在这20*500个数中找出排名前500的数。 本代码给出了 3种方法，并给出了测试程序
 * 
 * @author tomchen
 *
 */
public class Top500By20Array {
	public static void main(String[] args) {
		Random r = new Random();

		int rowSize = 20;
		int columnSize = 500;

		// 注意 java 二维数组其实是一维数组，里面包含的也是一维数组
		Integer[][] data = new Integer[rowSize][columnSize];

		for (int ii = 0; ii < rowSize; ii++) {
			for (int i = 0; i < columnSize; i++) {
				data[ii][i] = r.nextInt(1600);
			}

			// 将500个元素排序，升序
			Arrays.sort(data[ii]);
			// System.out.println(Arrays.toString(data[ii]));
		}

		// 方法1，直接将20个数组合并，排序，然后取前500个
		// 复杂度是 10000log(10000)
		Integer[] allData = new Integer[20 * 500];
		int a = 0;
		for (int ii = 0; ii < rowSize; ii++) {
			for (int i = 0; i < columnSize; i++) {
				allData[a++] = data[ii][i];
			}
		}
		Arrays.sort(allData);
		Integer[] result1 = Arrays.copyOfRange(allData, 0, columnSize);
		System.out.println("method 1 result:        " + Arrays.toString(result1));

		// 方法2，归并，先归并前两个数组，取前500个数据。将得到的结果再与第3个归并，重复
		// 考虑一下,20个数组先两两归并，得到10个；10个再两两归并，得到5个。等等，直到最后1个
		// 但这样归并的次数和从前往后两两归并的次数是一样的？所以还是 上述的从前往后好了
		// 复杂度是 19*500
		Integer[] result2 = data[0];
		for (int i = 1; i < rowSize; i++) {
			result2 = merge(result2, data[i], columnSize);
		}

		System.out.println("method 2 result:        " + Arrays.toString(result2));

		System.out.println("result1 equals result2: " + Arrays.equals(result1, result2));

		// 方法3，保持一个最小堆，这个堆存放来自20个数组的最小数
		// 每次取出一个数，然后将该数所在的数组的后面一个数入堆
		// 重复上面步骤，取出500个数
		// 注意建堆的时候需要保持 数来自哪个数组，用一个内部类实现
		// 复杂度是 500 * log(20)
		Integer[] result3 = new Integer[500];
		MinHeap<DataWithSource> heap = new MinHeap<DataWithSource>();
		for (int i = 0; i < rowSize; i++) {
			// 记录下来源那个数组，以及在数组中的 index
			DataWithSource d = new DataWithSource(data[i][0], i, 0);
			heap.add(d);
		}

		int num = 0;
		while (num < columnSize) {
			// 删除顶点元素
			DataWithSource d = heap.removeTop();
			result3[num++] = d.getValue();

			// 将 value 置为该数原数组里的下一个数
			d.setValue(data[d.getComeFrom()][d.getIndex() + 1]);

			// 将其在数组中的 index +1
			d.setIndex(d.getIndex() + 1);
			heap.add(d);
		}
		
		System.out.println("method 3 result:        " + Arrays.toString(result3));

		System.out.println("result2 equals result3: " + Arrays.equals(result2, result3));


	}

	/**
	 * 
	 * @param first
	 *            数组1
	 * @param second
	 *            数组2
	 * @param n
	 *            数组的长度，假定 first 和 second 的长度均为 n
	 * @return 返回一个长度为 n 的数组
	 */
	private static Integer[] merge(Integer[] first, Integer[] second, int n) {

		// 构建一个数组，这是归并排序的缺点，需要额外空间
		Integer[] temp = new Integer[n];

		int f = 0;
		int s = 0;

		int i = 0;

		while (i != n && f <= n && s <= n) {
			if (first[f] < second[s]) {
				temp[i++] = first[f++];
			} else {
				temp[i++] = second[s++];
			}
		}

		return temp;

	}

}

class DataWithSource implements Comparable<DataWithSource> {
	// 数据
	private Integer value;
	// 来源的数组
	private Integer comeFrom;
	// 在数组中的 index
	private Integer index;

	public DataWithSource(Integer value, Integer comeFrom, Integer index) {
		this.value = value;
		this.comeFrom = comeFrom;
		this.index = index;
	}

	public Integer getComeFrom() {
		return comeFrom;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public int compareTo(DataWithSource o) {
		return this.value.compareTo(o.value);
	}
}

/**
 * 
 * 最小堆和堆排序, 最小堆，顶点的元素是最小值， 根据《Java 语言程序设计 进阶篇》 p83 改写， 书上是最大堆. 堆排序
 * 将元素都存入最小堆中，从最小堆里面每次取出顶点元素
 * 
 * @author tomchen
 *
 * @param <E>
 */
class MinHeap<E extends Comparable> {

	public static <E extends Comparable> void heapSort(E[] array) {
		MinHeap<E> heap = new MinHeap<E>();
		for (int i = 0; i < array.length; i++) {
			heap.add(array[i]);
		}

		System.out.println("Debug: heap is     " + heap);

		for (int i = 0; i < array.length; i++) {
			array[i] = heap.removeTop();
		}
	}

	private ArrayList<E> data = new ArrayList<E>();

	public MinHeap() {
	}

	/**
	 * 增加一个新元素，步骤是 1. 先把元素插入到 list 的末尾 2. 比较末尾元素和它的父元素，若小于，交换两者 3.
	 * 重复上述步骤，直到到顶点位置或者子元素大于父元素 4. 不一定要遍历堆所有的元素，达到堆的性质后会提前结束
	 * 
	 * @param array
	 */
	public void add(E array) {

		data.add(array);

		int child = data.size() - 1;
		int parent = (child - 1) / 2;

		// 判断是否到达顶点
		while (child > 0) {
			// 父元素大于子元素，交换，保持父是小的
			if (data.get(parent).compareTo(array) > 0) {
				data.set(child, data.get(parent));
				data.set(parent, array);

				child = parent;
				parent = (child - 1) / 2;
			} else {
				// 已经是最小堆了，无需再比较
				break;
			}
		}
	}

	/**
	 * 删除顶点处的元素，步骤是： 1. 把末尾的元素复制到顶点处 2. 然后比较此时顶点的值和左右子树，保持最小堆的性质 3.
	 * 交换顶点和左右子树较小的值 4. 重复上述步骤，直到已经成了最小堆或者遍历完 5. 注意可能存在左子树存在，右子树不存在情况 6.
	 * 不一定要遍历堆所有的元素，达到堆的性质后会提前结束
	 * 
	 * @return 返回被删除的元素
	 */
	public E removeTop() {
		if (data.isEmpty())
			return null;

		E removed = data.get(0);

		// 因为一直交换的是最后的元素，这儿将其保存
		E last = data.get(data.size() - 1);
		data.set(0, last);
		data.remove(data.size() - 1);

		int parent = 0;
		int leftChild = parent * 2 + 1;
		int rightChild = parent * 2 + 2;

		while (leftChild <= data.size() - 1) {

			int minIndex = leftChild;
			// 右子树存在，判断左右子树哪个小，保存坐标
			// 如果不存在，那么使用左子树的坐标
			// 保存较小元素的坐标，可以省去考虑左右子树都存在，只有左存在的情况
			if (rightChild <= data.size() - 1) {
				if (data.get(rightChild).compareTo(data.get(leftChild)) < 0) {
					minIndex = rightChild;
				}
			}

			if (data.get(minIndex).compareTo(last) < 0) {
				data.set(parent, data.get(minIndex));
				data.set(minIndex, last);
				parent = minIndex;
				leftChild = parent * 2 + 1;
				rightChild = parent * 2 + 2;
			} else {
				break; // 已经达到了最小堆的性质
			}
		}

		return removed;
	}

	@Override
	public String toString() {
		return data.toString();
	}

}
