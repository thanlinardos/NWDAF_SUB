package io.nwdaf.eventsubscription;

public class Regex {
	public static String nf_set_id_plmn = "set[A-Za-z0-9\\-]+[A-Za-z0-9]\\.[A-Za-z]+set\\.5gc\\.mnc([0-9]){3}\\.mcc([0-9]){3}";
	public static String nf_set_id_snpn = "set[A-Za-z0-9\\-]+[A-Za-z0-9]\\.[A-Za-z]+set\\.5gc\\.nid([A-Za-z0-9]){11}\\.mnc([0-9]){3}\\.mcc([0-9]){3}";
	public static String uuid = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89aAbB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";
	public static String tac = "(^[A-Fa-f0-9]{4}$)|(^[A-Fa-f0-9]{6}$)";
	public static String nid = "^[A-Fa-f0-9]{11}$";
	public static String mcc = "^\\d{3}$";
	public static String mnc = "^\\d{2,3}$";
	public static String supi = "^(imsi-[0-9]{5,15}|nai-.+|gci-.+|gli-.+|.+)$";
	public static String correlation_id = "[0-9a-fA-F]{2}\\b-[0-9a-fA-F]{2}\\b-[0-9a-fA-F]{2}\\b-[0-9a-fA-F]{2}";
	public static String group_id = "^[A-Fa-f0-9]{8}-[0-9]{3}-[0-9]{2,3}-([A-Fa-f0-9][A-Fa-f0-9]){1,10}$";
	public static String dnn = "\\.nid([A-Za-z0-9]){11}\\.mnc([0-9]){3}\\.mcc([0-9]){3}\\.gprs";
	public static String nr_cell_id = "^[A-Fa-f0-9]{9}$";
	public static String n3IwfId = "^[A-Fa-f0-9]+$";
	public static String wagfId = "^[A-Fa-f0-9]+$";
	public static String tngfId = "^[A-Fa-f0-9]+$";
	public static String ngeNbId = "^('MacroNGeNB-[A-Fa-f0-9]{5}|LMacroNGeNB-[A-Fa-f0-9]{6}|SMacroNGeNB-[A-Fa-f0-9]{5})$";
	public static String eNbId = "^(MacroeNB-[A-Fa-f0-9]{5}|LMacroeNB-[A-Fa-f0-9]{6}|SMacroeNB-[A-Fa-f0-9]{5}|HomeeNB-[A-Fa-f0-9]{7})$";
	public static String gNbId_bitLength = "2[2-9]|3[0-2]";
	public static String gNBValue = "^[A-Fa-f0-9]{6,8}$";
	public static String sst = "[0-9]|[0-9]{2}|100|19[0-9]|18[0-9]|17[0-9]|16[0-9]|15[0-9]|14[0-9]|13[0-9]|12[0-9]|11[0-9]|10[1-9]|200|24[0-9]|23[0-9]|22[0-9]|21[0-9]|20[1-9]|25[0-5]";
	public static String bitRate = "^\\d+(\\.\\d+)?(bps|Kbps|Mbps|Gbps|Tbps)$";
	public static String ipv4Addr = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$";
	public static String ipv6Addr = "^((:|(0?|([1-9a-f][0-9a-f]{0,3}))):)((0?|([1-9a-f][0-9af]{0,3})):){0,6}(:|(0?|([1-9a-f][0-9a-f]{0,3})))|((([^:]+:){7}([^:]+))|((([^:]+:)*[^:]+)?::(([^:]+:)*[^:]+)?))$";
	public static String ipv6Prefix = "^((:|(0?|([1-9a-f][0-9a-f]{0,3}))):)((0?|([1-9a-f][0-9af]{0,3})):){0,6}(:|(0?|([1-9a-f][0-9a-f]{0,3})))(\\/(([0-9])|([0-9]{2})|(1[0-1][0-9])|(12[0-8])))|((([^:]+:){7}([^:]+))|((([^:]+:)*[^:]+)?::(([^:]+:)*[^:]+)?))(\\/.+)$";
	public static String fqdn = "^([0-9A-Za-z]([-0-9A-Za-z]{0,61}[0-9A-Za-z])?\\.)+[A-Zaz]{2,63}\\.?$";
	public static String packetErrorRate = "^([0-9]E-[0-9])$";
	
	//	String text = "setxyz.amfset.5gc.mnc012.mcc345";
//	String text2 = "setxyz.smfset.5gc.nid000007ed9d5.mnc012.mcc345";
}
