package io.nwdaf.eventsubscription.notify;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.nwdaf.eventsubscription.Constants;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NFType;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NfStatus;
import io.nwdaf.eventsubscription.model.Snssai;
import io.nwdaf.eventsubscription.model.NFType.NFTypeEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.service.MetricsService;

@Component
public class DummyDataProducerListener{
    private static Integer no_dummyDataProducerEventListeners = 0;
	private static final Object dummyDataProducerLock = new Object();
	private static Boolean started_saving_data = false;
	private static final Object started_saving_data_lock = new Object();

	@Autowired
	MetricsService metricsService;
	
	@Autowired
	Environment env;
	
    @Async
    @EventListener
    public void onApplicationEvent(final DummyDataProducerEvent event){
        synchronized (dummyDataProducerLock) {
			if(no_dummyDataProducerEventListeners<1) {
				no_dummyDataProducerEventListeners++;
			}
			else {
				return;
			}
		}

        Logger logger = NwdafSubApplication.getLogger();
    	logger.info("producing dummy data...");
        long start;
        while(true){
            start = System.nanoTime();
            synchronized(dummyDataProducerLock){
                if(no_dummyDataProducerEventListeners==0){
                    logger.info("Dummy Data Production stopped!");
                    return;
                }
            }
            for(int j=0;j<NwdafEventEnum.values().length;j++){
                NwdafEventEnum eType = NwdafEventEnum.values()[j];
                if(eType.equals(NwdafEventEnum.NF_LOAD)){
                    List<NfLoadLevelInformation> nfloadinfos=generateDummyNfloadLevelInfo(10);

                    for(int k=0;k<nfloadinfos.size();k++) {
                        try {
                            metricsService.create(nfloadinfos.get(j));
                            synchronized(started_saving_data_lock){
                                started_saving_data = true;
                            }
                        }
                        catch(Exception e) {
                            logger.error("Failed to save dummy nfloadlevelinfo to timescaledb",e);
                            synchronized (dummyDataProducerLock) {
                                no_dummyDataProducerEventListeners--;
                            }
                            synchronized(started_saving_data_lock){
                                started_saving_data = false;
                            }
                            return;
                        }
                    }
                }
            }
            long diff = (System.nanoTime()-start) / 1000000l;
    		long wait_time = (long)Constants.MIN_PERIOD_SECONDS*1000l;
            if(diff<wait_time) {
	    		try {
					Thread.sleep(wait_time-diff);
				} catch (InterruptedException e) {
					e.printStackTrace();
					synchronized (dummyDataProducerLock) {
			    		no_dummyDataProducerEventListeners--;
			    	}
					synchronized(started_saving_data_lock){
						started_saving_data = false;
					}
					return;
				}
    		}
        }
    }

    private List<NfLoadLevelInformation> generateDummyNfloadLevelInfo(int c){
        List<NfLoadLevelInformation> res = new ArrayList<>();
        for(int i=0;i<c;i++){
            res.add(new NfLoadLevelInformation());
        }
        Random r = new Random();
        Instant now = Instant.now();
        for(int i=0;i<res.size();i++){
            int[] nums = {r.nextInt(101),r.nextInt(101),r.nextInt(101)};
            int[] maxs = {r.nextInt(nums[0],101),r.nextInt(nums[1],101),r.nextInt(nums[2],101)};
            res.set(i,res.get(i).nfCpuUsage(nums[0])
            .nfMemoryUsage(nums[1]).nfStorageUsage(nums[2]).nfLoadLevelAverage((nums[0]+nums[1]+nums[2])/3)
            .nfLoadLevelpeak((maxs[0]+maxs[1]+maxs[2])/3).nfLoadAvgInAoi(r.nextInt(101))
            .time(now)
            );
            int aoiIndex = r.nextInt(6);
            NFTypeEnum nfType = NFTypeEnum.values()[r.nextInt(NFTypeEnum.values().length)];
            switch(aoiIndex){
                case 0:
                    res.set(i,res.get(i).areaOfInterestId(Constants.AreaOfInterestExample1.getId()));
                    break;
                case 1:
                    res.set(i,res.get(i).areaOfInterestId(Constants.AreaOfInterestExample2.getId()));
                    break;
                case 2:
                    res.set(i,res.get(i).areaOfInterestId(Constants.AreaOfInterestExample3.getId()));
                    break;
                case 3:
                    res.set(i,res.get(i).nfSetId("set"+(char)(r.nextInt(26) + 'a')+(char)(r.nextInt(26) + 'a')+(char)(r.nextInt(26) + 'a')+
                    nfType.toString()+"set.5gc."+
                    r.nextInt(10)+r.nextInt(10)+r.nextInt(10)+"mnc."+
                    r.nextInt(10)+r.nextInt(10)+r.nextInt(10)+"mcc"
                    ));
                case 4:
                    res.set(i, res.get(i).snssai(new Snssai().sd(Integer.toHexString(r.nextInt(0,16777216))).sst(r.nextInt(0,256))));
                default:
                    res.set(i,res.get(i).nfInstanceId(UUID.randomUUID()));
                    break;
            }
            int reg = r.nextInt(1,51);
            int undisc = r.nextInt(1,50);
            res.set(i,res.get(i).nfType(new NFType().nfType(nfType))
                .nfStatus(new NfStatus().statusRegistered(reg).statusUndiscoverable(undisc).statusUnregistered(100-reg-undisc))
                .confidence(r.nextInt(101))
            );
        }
        
        return res;
    }
    public static Object getDummydataproducerlock() {
        return dummyDataProducerLock;
    }
    public static Integer getNo_dummyDataProducerEventListeners() {
        return no_dummyDataProducerEventListeners;
    }
    public static Object getStartedSavingDataLock() {
        return started_saving_data_lock;
    }
    public static Boolean getStarted_saving_data() {
        return started_saving_data;
    }
}
