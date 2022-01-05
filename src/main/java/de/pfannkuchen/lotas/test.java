package de.pfannkuchen.lotas;

public class test {

	public static void main(String[] args) {
		String s1 = "abcdefghihlm28צהü.-,-.§$%&/54334567";
		System.out.println(s1.replaceAll("[^a-z0-9_.-]", ""));
	}
	
}
