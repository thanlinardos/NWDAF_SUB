package io.nwdaf.eventsubscription.repository.redis.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("nfLoadLevelId")
public class NfLoadLevelId implements Serializable {
		
		private OffsetDateTime time;
		
		private UUID nfInstanceId;

		public NfLoadLevelId() {
			
		}
	    public NfLoadLevelId(OffsetDateTime time, UUID nfInstanceId) {
	        this.setTime(time);
	        this.setNfInstanceId(nfInstanceId);
	    }

		public OffsetDateTime getTime() {
			return time;
		}

		public void setTime(OffsetDateTime time) {
			this.time = time;
		}

		public UUID getNfInstanceId() {
			return nfInstanceId;
		}

		public void setNfInstanceId(UUID nfInstanceId) {
			this.nfInstanceId = nfInstanceId;
		}

		public String toString(){
			return "{\"nfInstanceId\":\""+this.nfInstanceId+"\",\"time\":\""+time+"\"}";
		}
}
