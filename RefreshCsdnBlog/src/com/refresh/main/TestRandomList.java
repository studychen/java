package com.refresh.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestRandomList {
	// 测试根据一个有序的集合产生一个乱序的集合
	// 元素应该不重复且都出现以此
	public static void main(String[] args) {
		int size = 10;
		List<String> oriListUrl = new ArrayList<String>();

		for (int i = 0; i < size; i++) {
			oriListUrl.add("test" + new Random().nextInt(100) + "haha");
		}
		System.out.println(oriListUrl);
		
		List<String> randomList = new ArrayList<String>(10);
		int[] oriArray = new int[10];

		for (int i = 0; i < size; i++) {
			oriArray[i] = i;
		}

		for (int i = 0; i < size; i++) {
			int random = new Random().nextInt(size - i);
			int value = oriArray[random];
			randomList.add(oriListUrl.get(value));
			// 每次都交换随机数和当前最后一个数
			// note 随着i递增,当前最后一个数的位置会往前移
			oriArray[random] = oriArray[size - i - 1];
			oriArray[size - i - 1] = value;
		}
		System.out.println(randomList);
	}

}
