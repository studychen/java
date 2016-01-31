package auto;

/**
 * 工程入口类
 * @author tomchen
 *
 */
public class Main {
	/**
	 * 
	 * @param args
	 * ip, RMA 端口, 第3个节点的主机名, 测试任务 
	 * 参数至少是4个,failover 参数是7个
	 * sap_host_available 参数是5个
	 */
	public static void main(String[] args) {

		if (args.length < 4) {
			throw new RuntimeException(
					"Need args: args[0]->IP,args[1]->PortOfRMA,args[2]->3rdNodeLogicalName," + "args[3]->testName,");
		}

		/**
		 * 判断输入的端口号是否是整数
		 */
		try {
			Integer.parseInt(args[1]);
		} catch (Exception exp) {
			exp.printStackTrace();
			System.err.println("The RMA port is invalid");
		}

		HADRThreeNodes HADRThirdUser = new HADRThreeNodes("DR_admin", "**passwd**", args[0], args[1], args[2]);

		if (args.length != 0) {
			// 最后一个参数是任务名称
			System.out.println(args[3]);
			String taskName = args[3];

			// 根据相应的任务名称执行对应的任务
			// if else 设计可以优化
			if (taskName.equalsIgnoreCase("ToSync")) {
				HADRThirdUser.toSync();
			} else if (taskName.equalsIgnoreCase("ToAsync")) {
				HADRThirdUser.toAsync();
			} else if (taskName.equalsIgnoreCase("ToNearSync")) {
				HADRThirdUser.toNearSync();
			} else if (taskName.equalsIgnoreCase("ToLocal")) {
				HADRThirdUser.toLocal();
			} else if (taskName.equalsIgnoreCase("ToRemote")) {
				HADRThirdUser.toRemote();
			} else if (taskName.equalsIgnoreCase("Failover")) {
				if (args.length < 7) {
					throw new RuntimeException(
							"Need args: args[0]->IP,args[1]->PortOfRMA,args[2]->3rdNodeLogicalName,args[3]->Failover,"
									+ "args[4]->curPrimary,args[5]->curStandby,args[6]->timeout");
				} else {
					if (args.length == 7) {
						// sap_failover PRI, STA, 600
						HADRThirdUser.failover(args[4], args[5], args[6], "");
					} else if (args.length == 8) {
						HADRThirdUser.failover(args[4], args[5], args[6], args[7]);
					}

				}

			} else if (taskName.equalsIgnoreCase("sap_host_available")) {
				if (args.length != 5) {
					throw new RuntimeException(
							"Need args: args[0]->IP,args[1]->PortOfRMA,args[2]->3rdNodeLogicalName,args[3]->sap_host_available,args[4]->curPrimary");
				} else {
					// 执行sap_failover_remaining
					HADRThirdUser.sapHostRemaining();
					// 执行sap_host_available
					HADRThirdUser.sapHostAvailable(args[4]);
				}

			} else {
				System.out.println("No RMA cmd called!");
			}
		}

	}

	/**
	 * 这儿的设计不会检查初始状态是 Async 还是 Sync
	 * 先切换到Async，再切换到Sync
	 * @param HADRThirdUser
	 */
	private static void testAsyncToSync(HADRThreeNodes HADRThirdUser) {
		HADRThirdUser.toAsync();
		HADRThirdUser.toSync();
	}

	private static void RemoteToLocal(HADRThreeNodes HADRThirdUser) {
		HADRThirdUser.toRemote();
		HADRThirdUser.toLocal();
	}
}
