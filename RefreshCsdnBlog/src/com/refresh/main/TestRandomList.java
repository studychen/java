package com.refresh.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestRandomList {
	// ���Ը���һ������ļ��ϲ���һ������ļ���
	// Ԫ��Ӧ�ò��ظ��Ҷ������Դ�
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
			// ÿ�ζ�����������͵�ǰ���һ����
			// note ����i����,��ǰ���һ������λ�û���ǰ��
			oriArray[random] = oriArray[size - i - 1];
			oriArray[size - i - 1] = value;
		}
		System.out.println(randomList);
	}

}
