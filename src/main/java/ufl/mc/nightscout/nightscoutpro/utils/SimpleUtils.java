package ufl.mc.nightscout.nightscoutpro.utils;



import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.EmailValidator;

public class SimpleUtils {

	public static String listToSingleString(List<String> specialities) {
		String finalString = specialities.get(0);
		for (int i = 1; i < specialities.size(); i++) {
			finalString = finalString + "," + specialities.get(i);
		}
		return finalString;
	}

	public static List<String> stringToList(String specialities) {
		String[] finalList = specialities.split(",");
		return Arrays.asList(finalList);
	}

	public static boolean areAllFieldsEmpty(Object obj) throws Exception {
		for (Field f : obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (f.get(obj).getClass() == String.class) {
				String field = (String) f.get(obj);
				if (StringUtils.isNotBlank(field))
					return false;
			}
		}

		return true;
	}

	public static boolean isValidSession(HttpSession httpSession) {
		if (httpSession != null
				&& StringUtils.isNotBlank((String) httpSession
						.getAttribute("User")))
			return true;
		else
			return false;
	}

	public static boolean isValidEmail(String email) {
		EmailValidator emailValidator = EmailValidator.getInstance();
		return emailValidator.isValid(email);
	}

	public static boolean isValidDomain(String domain) {
		DomainValidator validator = DomainValidator.getInstance();
		return validator.isValid(domain);
	}

	public static boolean isValidUsername(String username) {
		Pattern pattern = Pattern.compile("^[a-z0-9_-]{6,14}$");
		Matcher match = pattern.matcher(username);
		if (match.matches())
			return true;
		return false;
	}

	public static void main(String[] args) throws Exception {
		
		System.out.println("nazeem n -"+isValidUsername("nazeem n"));
		System.out.println("nazee -"+isValidUsername("nazee"));
		System.out.println("naaz -"+isValidUsername("naaz"));
		System.out.println("12345678901234 - "+isValidUsername("12345678901234"));
		System.out.println("123456789012345 - "+isValidUsername("123456789012345"));
		System.out.println("6_ - "+isValidUsername("________"));
		System.out.println("1----- - "+isValidUsername("---------"));
		
		List<String> sample1 = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			sample1.add(String.valueOf(i));
			System.out.println(sample1);
		}
		String final1 = listToSingleString(sample1);
		System.out.println("list to String..." + final1);
		System.out.println("String to List..." + stringToList(final1));

		/*Home home1 = new Home("212", "121", 0, "asa");
		System.out.println(SimpleUtils.areAllFieldsEmpty(home1));*/
	}

}
