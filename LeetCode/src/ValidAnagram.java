import java.util.Arrays;

public class ValidAnagram {
	public static void main(String[] args) {
		System.out.println(isAnagram("anagram", "anagram"));
		System.out.println(isAnagram("anagram", "nagaram"));
		System.out.println(isAnagram("car", "rat"));
		System.out.println(isAnagram("car", "rac"));
	}

	public static boolean isAnagram(String s, String t) {
		if (s.length() != t.length()) {
			return false;
		}

		char[] sChar = s.toCharArray();
		char[] tChar = t.toCharArray();
		
		Arrays.sort(sChar);
		Arrays.sort(tChar);
		
		return String.valueOf(sChar).equals(String.valueOf(tChar));

	}

}
