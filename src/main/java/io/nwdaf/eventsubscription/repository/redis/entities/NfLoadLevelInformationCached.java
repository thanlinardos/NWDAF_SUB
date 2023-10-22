package io.nwdaf.eventsubscription.repository.redis.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import lombok.Getter;
import lombok.Setter;

@RedisHash("NfLoadLevelInformation")
@Getter
@Setter
public class NfLoadLevelInformationCached  implements Serializable {
    @Id
    private String id;

    private NfLoadLevelId IdObject;

    private UUID nfInstanceId;

    private OffsetDateTime time;

    private NfLoadLevelInformation data;

	private String nfSetId;
	
	private Integer nfCpuUsage;
	
	private Integer nfMemoryUsage;
	
	private Integer nfStorageUsage;
	
	private Integer nfLoadLevelAverage;
	
	private Integer nfLoadLevelpeak;
	
	private Integer nfLoadAvgInAoi;

	private UUID areaOfInterestId;

	public void setTime(OffsetDateTime time) {
		if(this.id==null) {
			this.IdObject = new NfLoadLevelId(time,null);
		}
		else {
			this.IdObject.setTime(time);
		}
        this.id = this.IdObject.toString();
        this.time = time;
	}
	public void setNfInstanceId(UUID nfInstanceId) {
		this.IdObject.setNfInstanceId(nfInstanceId);
        this.id = this.IdObject.toString();
        this.nfInstanceId = nfInstanceId;
	}
    public void setData(NfLoadLevelInformation data){
        this.data = data;
        this.IdObject = new NfLoadLevelId(data.getTimeStamp(), data.getNfInstanceId());
        this.id = this.IdObject.toString();
        this.nfInstanceId = data.getNfInstanceId();
        this.time = data.getTimeStamp();
    }

    public String toString(){
        return "{\"id\":"+id+",\"data\":"+data.toString()+",\"nfSetId\":\""+nfSetId+"\""
        +",\"nfCpuUsage\":\""+nfCpuUsage+"\""
        +",\"nfMemoryUsage\":\""+nfMemoryUsage+"\""
        +",\"nfStorageUsage\":\""+nfStorageUsage+"\""
        +",\"nfLoadLevelAverage\":\""+nfLoadLevelAverage+"\""
        +",\"nfLoadLevelpeak\":\""+nfLoadLevelpeak+"\""
        +",\"nfLoadAvgInAoi\":\""+nfLoadAvgInAoi+"\""
        +",\"areaOfInterestId\":\""+areaOfInterestId+"\""
        +"}";
    }
}
