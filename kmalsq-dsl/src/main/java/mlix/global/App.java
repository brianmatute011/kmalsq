package mlix.global;

import mlix.global.kmal.utils.KmalUtils;

public class App {
    public static void main( String[] args ) {
        String dslPrompt = "NOT [a] == 15;";
        System.out.println("DSL Prompt: " + dslPrompt);
        System.out.println("Parsed Query: " + KmalUtils.parseQuery(dslPrompt));
    }
}
