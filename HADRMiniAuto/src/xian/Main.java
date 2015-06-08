package xian;

/**
 * Created by I302636 on 8/15/14.
 */
public class Main {
    public static void main(String[] args){
        if(args.length<4){
            throw new RuntimeException("Need args: args[0]->IP,args[1]->PortOfRMA,args[2]->3rdNodeLogicalName," +
                    "args[3]->testName,");
        }

        try{
            Integer portNumber=Integer.parseInt(args[1]);

        }catch(Exception exp){
            exp.printStackTrace();
            System.err.println("The RMA port is invalid");
        }

        HADRSettingMonitor hadrSettingMonitor=new HADRSettingMonitor("DR_admin","Sybase123",args[0],args[1],args[2]);

        if(args.length!=0){
            System.out.println(args[3]);
            String taskName=args[3];
            if(taskName.toLowerCase().equals("ToSync".toLowerCase())){
                hadrSettingMonitor.toSync();
            }else if(taskName.toLowerCase().equals("ToAsync".toLowerCase())){
                hadrSettingMonitor.toAsync();
            }else if(taskName.toLowerCase().equals("ToNearSync".toLowerCase())){
                hadrSettingMonitor.toNearSync();
            }else if(taskName.toLowerCase().equals("ToLocal".toLowerCase())){
                hadrSettingMonitor.toLocal();
            }else if(taskName.toLowerCase().equals("ToRemote".toLowerCase())){
                hadrSettingMonitor.toRemote();
            }else if(taskName.toLowerCase().equals("Failover".toLowerCase())){
                if(args.length<7){
                    throw new RuntimeException("Need args: args[0]->IP,args[1]->PortOfRMA,args[2]->3rdNodeLogicalName,args[3]->Failover," +
                            "args[4]->curPrimary,args[5]->curStandby,args[6]->timeout");
                }else
                {
                    if(args.length==7){
                        hadrSettingMonitor.failover(args[4],args[5],args[6],"");
                    }else if(args.length==8){
                        hadrSettingMonitor.failover(args[4],args[5],args[6],args[7]);
                    }

                }

            }else if(taskName.toLowerCase().equals("sap_host_available".toLowerCase())){
                if(args.length!=5){
                    throw new RuntimeException("Need args: args[0]->IP,args[1]->PortOfRMA,args[2]->3rdNodeLogicalName,args[3]->sap_host_available,args[4]->curPrimary");
                }else
                {
                    hadrSettingMonitor.sapHostRemaining();
                    hadrSettingMonitor.sapHostAvailable(args[4]);
                }

            }else{
                System.out.println("No RMA cmd called!");
            }
        }

    }

    private static void testAsyncToSync(HADRSettingMonitor hadrSettingMonitor) {
        hadrSettingMonitor.toAsync();
        //TODO:Call script to do checksum
        hadrSettingMonitor.toSync();
        //TODO:Call script to do checksum
    }

    private static void SyncToSync(HADRSettingMonitor hadrSettingMonitor) {
        hadrSettingMonitor.toSync();
    }

    private static void SyncToASync(HADRSettingMonitor hadrSettingMonitor) {
        hadrSettingMonitor.toAsync();
    }

    private static void SyncToNearSync(HADRSettingMonitor hadrSettingMonitor) {
        hadrSettingMonitor.toNearSync();
    }

    private static void RemoteToLocal(HADRSettingMonitor hadrSettingMonitor) {
        hadrSettingMonitor.toRemote();
        hadrSettingMonitor.toLocal();
    }
}
