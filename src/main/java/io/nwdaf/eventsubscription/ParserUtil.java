package io.nwdaf.eventsubscription;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParserUtil {
    public static Integer safeParseInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Float safeParseFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return null;
        }
        catch (NullPointerException e) {
            return null;
        }
    }
    public static Double safeParseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
        catch (NullPointerException e) {
            return null;
        }
    }
    public static OffsetDateTime safeParseOffsetDateTime(String str) {
        try {
            return OffsetDateTime.parse(str);
        } catch (DateTimeParseException e) {
            return null;
        }
        catch (NullPointerException e) {
            return null;
        }
    }
    public static UUID safeParseUUID(String str) {
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            return null;
        } catch(NullPointerException e){
            return null;
        }
    }
    public static Boolean safeParseBoolean(String value) {
        if (value == null) {
            return null;
        }

        return Boolean.parseBoolean(value);
    }
    public static String safeParseString(Object o) {
        if (o == null) {
            return null;
        }

        return o.toString();
    }
    public static List<String> safeParseListString(List<Object> l){
        List<String> res = new ArrayList<>();
        for(int i=0;i<l.size();i++){
            res.add(safeParseString(l.get(i)));
        }
        return res;
    }
    public static Boolean safeParseEquals(String value,String comparisonValue) {
        if (value == null) {
            return false;
        }
        try{
            return value.equalsIgnoreCase(comparisonValue);
        }catch(Exception e){
            return false;
        }
    }
    public static Long safeParseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static <T> List<String> parseListToFilterList (List<T> list,String name){
		List<String> filterList = new ArrayList<>();
		for(int i=0;i<list.size();i++){
            if(list.get(i)==null || list.get(i).getClass()==null){
                continue;
            }
			if(list.get(i).getClass().equals(Integer.class)){
				filterList.add("{\""+name+"\":"+list.get(i)+"}");
			}
			else{
				filterList.add("{\""+name+"\":\""+list.get(i)+"\"}");
			}
		}
		return filterList;
	}

	public static String parseQuerryFilter(List<String> filterList){
		String res = "";
        if(filterList.size()==0){
            return null;
        }
		for(int i=0;i<filterList.size();i++){
			res += "data @> cast('"+filterList.get(i)+"' as jsonb)";
			if(i!=filterList.size()-1){
				res += " or ";
			}
		}

		return res;
	}
}