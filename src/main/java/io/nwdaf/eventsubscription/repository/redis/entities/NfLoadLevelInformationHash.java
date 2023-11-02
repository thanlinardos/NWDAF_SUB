package io.nwdaf.eventsubscription.repository.redis.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RedisHash("NfLoadLevelInformation")
@Getter
@Setter
@NoArgsConstructor
public class NfLoadLevelInformationHash implements Serializable {

    @Id
    private String nfInstanceId;

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

    public NfLoadLevelInformationHash(NfLoadLevelInformation data) {
        this.data = data;
        if(data == null) {return;}
        this.nfInstanceId = data.getNfInstanceId().toString();
        this.time = data.getTimeStamp();
        this.nfSetId = data.getNfSetId();
        this.areaOfInterestId = data.getAreaOfInterestId();
    }

    public String toString(){
        return "{\"nfInstanceId\":"+nfInstanceId+",\"data\":"+data.toString()+",\"nfSetId\":\""+nfSetId+"\""
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
