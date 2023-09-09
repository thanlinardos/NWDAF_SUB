package io.nwdaf.eventsubscription.datacollection;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.nwdaf.eventsubscription.model.NFType;
import io.nwdaf.eventsubscription.model.NFType.NFTypeEnum;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NfStatus;
import io.nwdaf.eventsubscription.model.Snssai;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.utilities.Constants;

public class DummyDataGenerator {
    public static List<NfLoadLevelInformation> generateDummyNfloadLevelInfo(int c){
        List<NfLoadLevelInformation> res = new ArrayList<>();
        for(int i=0;i<c;i++){
            res.add(new NfLoadLevelInformation());
        }
        Random r = new Random();
        Instant now = Instant.now();
        for(int i=0;i<res.size();i++){
            int[] nums = {r.nextInt(101),r.nextInt(101),r.nextInt(101)};
            int[] maxs = {r.nextInt(nums[0],101),r.nextInt(nums[1],101),r.nextInt(nums[2],101)};
            res.get(i).nfCpuUsage(nums[0])
            .nfMemoryUsage(nums[1]).nfStorageUsage(nums[2]).nfLoadLevelAverage((nums[0]+nums[1]+nums[2])/3)
            .nfLoadLevelpeak((maxs[0]+maxs[1]+maxs[2])/3).nfLoadAvgInAoi(r.nextInt(101))
            .time(now).nfInstanceId(UUID.randomUUID());
            int aoiIndex = r.nextInt(4);
            NFTypeEnum nfType = NFTypeEnum.values()[r.nextInt(NFTypeEnum.values().length)];
            switch(aoiIndex){
                case 0:
                    List<UUID> keys = new ArrayList<>(Constants.ExampleAOIsMap.keySet());
                    keys.remove(Constants.ServingAreaOfInterest.getId());
                    int t = r.nextInt(keys.size());
                    res.get(i).areaOfInterestId(keys.get(t));
                    break;
                case 1:
                    res.get(i).nfSetId("set"+(char)(r.nextInt(26) + 'a')+(char)(r.nextInt(26) + 'a')+(char)(r.nextInt(26) + 'a')+ "." +
                    nfType.toString()+"set.5gc.mnc"+r.nextInt(10)+r.nextInt(10)+r.nextInt(10)+".mcc"+r.nextInt(10)+r.nextInt(10)+r.nextInt(10)
                    );
                    break;
                case 2:
                    res.get(i).snssai(new Snssai().sd(Integer.toHexString(r.nextInt(0,16777216))).sst(r.nextInt(0,256)));
                    break;
                case 3:
                    for(int j=0;j<3;j++){
                        res.get(i).addSupi("imsi-123123"+r.nextInt(100000000,999999999));
                    }
                    res.get(i).addSupi("nai-user@example.com").addSupi("gci-00-00-5E-00-53-00@5gc.mnc123.mcc123.example.com")
                    .addSupi("gli-YXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZg==");
                    break;
            }
            int reg = r.nextInt(1,51);
            int undisc = r.nextInt(1,50);
            res.get(i).nfType(new NFType().nfType(nfType))
                .nfStatus(new NfStatus().statusRegistered(reg).statusUndiscoverable(undisc).statusUnregistered(100-reg-undisc))
                .confidence(r.nextInt(101));
        }
        
        return res;
    }
    public static List<NfLoadLevelInformation> changeNfLoadTimeDependentProperties(List<NfLoadLevelInformation> nfloadinfos){
        Instant now = Instant.now();
        Random r = new Random();
        for(int i=0;i<nfloadinfos.size();i++){
            int[] nums = {r.nextInt(101),r.nextInt(101),r.nextInt(101)};
            int[] maxs = {r.nextInt(nums[0],101),r.nextInt(nums[1],101),r.nextInt(nums[2],101)};
            nfloadinfos.get(i).nfCpuUsage(nums[0])
            .nfMemoryUsage(nums[1]).nfStorageUsage(nums[2]).nfLoadLevelAverage((nums[0]+nums[1]+nums[2])/3)
            .nfLoadLevelpeak((maxs[0]+maxs[1]+maxs[2])/3).nfLoadAvgInAoi(r.nextInt(101)).time(now);
        }
        return nfloadinfos;
    }

    public static List<UeMobility> generateDummyUeMobilities(int c){
        List<UeMobility> res = new ArrayList<>();
        for(int i=0;i<c;i++){
            res.add(new UeMobility());
        }
        Random r = new Random();
        Instant now = Instant.now();
        for(int i=0;i<res.size();i++){
            int[] nums = {r.nextInt(101),r.nextInt(101),r.nextInt(101)};
            int[] maxs = {r.nextInt(nums[0],101),r.nextInt(nums[1],101),r.nextInt(nums[2],101)};
            res.get(i);
        }
        return res;
    }

    public static List<UeMobility> changeUeMobilitiesTimeDependentProperties(List<UeMobility> ueMobilities){

        return ueMobilities;
    }
}
