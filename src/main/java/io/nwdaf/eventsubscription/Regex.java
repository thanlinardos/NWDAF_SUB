package io.nwdaf.eventsubscription;

public class Regex {
	public static String nf_set_id_plmn = "set[A-Za-z0-9\\-]+[A-Za-z0-9]\\.[A-Za-z]+set\\.5gc\\.mnc([0-9]){3}\\.mcc([0-9]){3}";
	public static String nf_set_id_snpn = "set[A-Za-z0-9\\-]+[A-Za-z0-9]\\.[A-Za-z]+set\\.5gc\\.nid([A-Za-z0-9]){11}\\.mnc([0-9]){3}\\.mcc([0-9]){3}";
	public static String uuid = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
	public static String tac = "(^[A-Fa-f0-9]{4}$)|(^[A-Fa-f0-9]{6}$)";
	public static String nid = "^[A-Fa-f0-9]{11}$";
	public static String mcc = "^\\d{3}$";
	public static String mnc = "^\\d{2,3}$";
	public static String supi = "^(imsi-[0-9]{5,15}|nai-.+|gci-.+|gli-.+|.+)$";
	public static String correlation_id = "[0-9a-fA-F]{2}\\b-[0-9a-fA-F]{2}\\b-[0-9a-fA-F]{2}\\b-[0-9a-fA-F]{2}";
	
	//	String text = "setxyz.amfset.5gc.mnc012.mcc345";
//	String text2 = "setxyz.smfset.5gc.nid000007ed9d5.mnc012.mcc345";
}
