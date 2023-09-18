package io.nwdaf.eventsubscription.datacollection;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import io.nwdaf.eventsubscription.model.CellGlobalId;
import io.nwdaf.eventsubscription.model.EutraLocation;
import io.nwdaf.eventsubscription.model.GeraLocation;
import io.nwdaf.eventsubscription.model.Gli;
import io.nwdaf.eventsubscription.model.HfcNodeId;
import io.nwdaf.eventsubscription.model.LineType;
import io.nwdaf.eventsubscription.model.LocationAreaId;
import io.nwdaf.eventsubscription.model.LocationInfo;
import io.nwdaf.eventsubscription.model.N3gaLocation;
import io.nwdaf.eventsubscription.model.NFType;
import io.nwdaf.eventsubscription.model.NFType.NFTypeEnum;
import io.nwdaf.eventsubscription.model.TransportProtocol.TransportProtocolEnum;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NfStatus;
import io.nwdaf.eventsubscription.model.NrLocation;
import io.nwdaf.eventsubscription.model.RoutingAreaId;
import io.nwdaf.eventsubscription.model.ServiceAreaId;
import io.nwdaf.eventsubscription.model.Snssai;
import io.nwdaf.eventsubscription.model.TnapId;
import io.nwdaf.eventsubscription.model.TransportProtocol;
import io.nwdaf.eventsubscription.model.TwapId;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.model.UserLocation;
import io.nwdaf.eventsubscription.model.UtraLocation;
import io.nwdaf.eventsubscription.model.LineType.LineTypeEnum;
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
        OffsetDateTime now = OffsetDateTime.now();
        for(int i=0;i<res.size();i++){
            int locNum = r.nextInt(5);
            UserLocation userLocation = new UserLocation();
            switch(locNum){
                case(0):
                    userLocation.eutraLocation(new EutraLocation()
                    .ecgi(Constants.AreaOfInterestExample3.getEcgis().get(0))
                    .tai(Constants.AreaOfInterestExample3.getTais().get(0))
                    .globalNgenbId(Constants.AreaOfInterestExample3.getGRanNodeIds().get(0))
                    .globalENbId(Constants.AreaOfInterestExample3.getGRanNodeIds().get(1))
                    .ageOfLocationInformation(0).ueLocationTimestamp(now)
                    .geodeticInformation(null).geographicalInformation(null));
                    break;
                case(1):
                    userLocation.nrLocation(new NrLocation()
                    .ncgi(Constants.AreaOfInterestExample1.getNcgis().get(0))
                    .tai(Constants.AreaOfInterestExample1.getTais().get(0))
                    .ageOfLocationInformation(0).ueLocationTimestamp(now)
                    .geodeticInformation(null).geographicalInformation(null));
                    break;
                case(2):
                    userLocation.n3gaLocation(new N3gaLocation()
                    .gci("00-00-5E-00-53-00@5gc.mnc123.mcc123.example.com")
                    .gli(new Gli().data("gli-YXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZg=="))
                    .hfcNodeId(new HfcNodeId().hfcNId("YXNkZm"))
                    .n3IwfId(Constants.AreaOfInterestExample3.getGRanNodeIds().get(2).getN3IwfId())
                    .n3gppTai(Constants.AreaOfInterestExample3.getTais().get(0))
                    .portNumber(8080).protocol(new TransportProtocol().transportProtocol(TransportProtocolEnum.TCP))
                    .tnapId(new TnapId().bssId(UUID.randomUUID().toString()).civicAddress("agioukonst12"))
                    .twapId(new TwapId().bssId(UUID.randomUUID().toString()).ssId(UUID.randomUUID().toString()).civicAddress("agioukonst12"))
                    .ueIpv4Addr("61.166.76.219").ueIpv6Addr("1702:b2fc:b695:2e32:8541:4020:5abe:bc46")
                    .w5gbanLineType(new LineType().lType(LineTypeEnum.DSL)));
                    break;
                case(3):
                    userLocation.geraLocation(new GeraLocation()
                    .cgi(new CellGlobalId().plmnId(Constants.plmnId).cellId("FFFF").lac("FFFF"))
                    .lai(new LocationAreaId().plmnId(Constants.plmnId).lac("FFFF"))
                    .rai(new RoutingAreaId().plmnId(Constants.plmnId).lac("FFFF").rac("00"))
                    .sai(new ServiceAreaId().plmnId(Constants.plmnId).lac("FFFF").sac("FFFF"))
                    .vlrNumber(Constants.plmnId.toFormattedString()+".vlrYXNkZmFzZGZh")
                    .ageOfLocationInformation(0).ueLocationTimestamp(now)
                    .geodeticInformation(null).geographicalInformation(null));
                    break;
                case(4):
                    userLocation.utraLocation(new UtraLocation()
                    .cgi(new CellGlobalId().plmnId(Constants.plmnId).cellId("0000").lac("FFFF"))
                    .lai(new LocationAreaId().plmnId(Constants.plmnId).lac("FFFF"))
                    .rai(new RoutingAreaId().plmnId(Constants.plmnId).lac("FFFF").rac("FF"))
                    .sai(new ServiceAreaId().plmnId(Constants.plmnId).lac("FFFF").sac("0000"))
                    .ageOfLocationInformation(0).ueLocationTimestamp(now)
                    .geodeticInformation(null).geographicalInformation(null));
                    break;
                default:
                    break;
            }
            res.get(i).ts(now).duration(1).durationVariance(0.0f);
            res.get(i).addLocInfosItem(new LocationInfo().ratio(r.nextInt(1,101)).confidence(r.nextInt(101))
            .loc(userLocation));
        }
        return res;
    }

    public static List<UeMobility> changeUeMobilitiesTimeDependentProperties(List<UeMobility> ueMobilities){
        Instant now = Instant.now();
        OffsetDateTime date = OffsetDateTime.ofInstant(now, TimeZone.getDefault().toZoneId());
        for(int i=0;i<ueMobilities.size();i++){
            ueMobilities.get(i).time(now);
            for(int j=0;j<ueMobilities.get(i).getLocInfos().size();j++){
                UserLocation userLocation = ueMobilities.get(i).getLocInfos().get(j).getLoc();
                if(userLocation.getEutraLocation()!=null){
                    userLocation.getEutraLocation().ueLocationTimestamp(date);
                }
                if(userLocation.getNrLocation()!=null){
                    userLocation.getNrLocation().ueLocationTimestamp(date);
                }
                if(userLocation.getGeraLocation()!=null){
                    userLocation.getGeraLocation().ueLocationTimestamp(date);
                }
                if(userLocation.getUtraLocation()!=null){
                    userLocation.getUtraLocation().ueLocationTimestamp(date);
                }
            }
        }
        return ueMobilities;
    }
}
