package com.refresh.main;

public class HomePage {

	private int page;

	private int amount;
	
	/**
	 * ҳ��
	 * @return
	 */
	public int getPage() {
		return page;
	}

	/**
	 * ������
	 * @return
	 */
	public int getAmount() {
		return amount;
	}

	public HomePage(int page, int amount) {
		this.page = page;
		this.amount = amount;
	}
	
	public HomePage() {
		this.page = 1;
		this.amount = 1;
	}

}
