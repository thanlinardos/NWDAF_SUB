package io.nwdaf.eventsubscription.responsebuilders;

import java.util.ArrayList;
import java.util.List;

public class PrometheusDataModel {
	private String resultType = null;
	private List<PrometheusResultModel> result = new ArrayList<>();
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public List<PrometheusResultModel> getResult() {
		return result;
	}
	public void setResult(List<PrometheusResultModel> result) {
		this.result = result;
	}
}
