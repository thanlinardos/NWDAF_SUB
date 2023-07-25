package io.nwdaf.eventsubscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;

public class Constants {
	public static Integer MIN_PERIOD_SECONDS = 1;
	public static Integer MAX_PERIOD_SECONDS = 600;
	public static List<NwdafEventEnum> supportedEvents = new ArrayList<>(Arrays.asList(NwdafEventEnum.NF_LOAD));
	//check 5.1.8-1 table (135) from 3gpp 29520-h80 and for encoding : 29571-h80 (17) table 5.2.2-3
	//Events supported: 2(UeMobility), 7(NfLoad), 11(EneNA), 17(NfLoadExt), 22(UeMobilityExt)
	public static String supportedFeatures = "210442";  //all 24 features: FFFFFF
	public static List<Integer> supportedFeaturesList = new ArrayList<>(Arrays.asList(2,7,11,17,22));
	public static String cpuQuerry = "sum by (name,id,nfType) (rate(container_cpu_usage_seconds_total{image!=\"\",container_label_org_label_schema_group=\"\",nfType!=\"\"}[1m])) / scalar(count(node_cpu_seconds_total{mode=\"user\"})) * 100";
	public static String memQuerry = "sum by (name,id,nfType)(container_memory_usage_bytes{image!=\"\",container_label_org_label_schema_group=\"\",nfType!=\"\"}) / scalar(sum(node_memory_MemTotal_bytes))*100";
	public static String storageQuerry = "sum by (name,id,nfType)(container_fs_usage_bytes{image!=\"\",container_label_org_label_schema_group=\"\",nfType!=\"\"}) / scalar(sum(node_filesystem_size_bytes{fstype=\"tmpfs\"})) * 100";
	public static String maxCpuQuerry = "max by (name,id,nfType) (rate(container_cpu_usage_seconds_total{image!=\"\",container_label_org_label_schema_group=\"\"}[1m])) * 100";
	public static String maxMemQuerry = "sum by (name,id,nfType)(max_over_time(container_memory_usage_bytes{image!=\"\",container_label_org_label_schema_group=\"\",nfType!=\"\"}[1m])) / scalar(sum(node_memory_MemTotal_bytes))*100";
	public static String maxStorageQuerry = "sum by (name,id,nfType)(max_over_time(container_fs_usage_bytes{image!=\"\",container_label_org_label_schema_group=\"\",nfType!=\"\"}[1m])) / scalar(sum(node_filesystem_size_bytes{fstype=\"tmpfs\"})) * 100";
}
