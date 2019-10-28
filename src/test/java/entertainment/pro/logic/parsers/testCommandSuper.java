package entertainment.pro.logic.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entertainment.pro.commons.enums.COMMANDKEYS;
import entertainment.pro.logic.parsers.commands.SearchCommand;
import entertainment.pro.ui.MovieHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class testCommandSuper {
    @Test
    public void testGetSubRoot(){
        SearchCommand sc = new SearchCommand(null);


        assertEquals(true , sc.subCommand(new String[]{"search" , "movies" , "testing"}));
        assertEquals( COMMANDKEYS.movies , sc.getSubRootCommand());
        assertEquals(true, sc.subCommand(new String[]{"search" , "tvshows" , "testing"}));
        assertEquals(  COMMANDKEYS.tvshows , sc.getSubRootCommand());
        assertEquals(true, sc.subCommand(new String[]{"search" , "mioivies" , "testing"}));
        assertEquals(  COMMANDKEYS.movies , sc.getSubRootCommand());
        assertEquals(true, sc.subCommand(new String[]{"search" , "telvshows" , "testing"}));
        assertEquals(  COMMANDKEYS.tvshows , sc.getSubRootCommand());
        assertEquals(false , sc.subCommand(new String[]{"search"}));
        assertEquals( COMMANDKEYS.none , sc.getSubRootCommand());

    }


    @Test
    public void testprocessFlags() {

        SearchCommand sc = new SearchCommand(null);

        String command = "search movies joker -A albert -a alex , ashley , alan -b benny -c";
        sc.processFlags(command.split(" ") , command);
        assertEquals(true , sc.getFlagMap().containsKey("-A"));
        assertEquals(true , sc.getFlagMap().containsKey("-a"));
        assertEquals(true , sc.getFlagMap().containsKey("-b"));
        ArrayList<String> AFlag = sc.getFlagMap().get("-A");
        AFlag.sort(null);
        ArrayList<String> aFlag = sc.getFlagMap().get("-a");
        aFlag.sort(null);
        ArrayList<String> bFlag = sc.getFlagMap().get("-b");
        bFlag.sort(null);
        ArrayList<String> cFlag = sc.getFlagMap().get("-c");
        ArrayList<String> dFlag = sc.getFlagMap().get("-d");
        assertEquals(true , AFlag.equals(new ArrayList<String>(
            Arrays.asList("albert"))));
        assertEquals(true , aFlag.equals(new ArrayList<String>(
                Arrays.asList("alan" , "alex" , "ashley"))));
        assertEquals(true , bFlag.equals(new ArrayList<String>(
                Arrays.asList("benny"))));
        assertEquals(0 , cFlag.size());
        assertEquals(null , dFlag);


        SearchCommand sc2 = new SearchCommand(null);
        String command2 = "search movies joker";
        sc2.processFlags(command2.split(" ") , command2);
        assertEquals(0 , sc2.getFlagMap().keySet().size());


    }


    @Test
    public void testpayload() {

        SearchCommand sc = new SearchCommand(null);

        String command = "search movies joker -A albert -a alex , ashley , alan -b benny -c";
        sc.processPayload(command.split(" "));
        assertEquals("joker" , sc.getPayload());

        String command2 = "search movies batman and robbin";
        sc.processPayload(command2.split(" "));
        assertEquals("batman and robbin" , sc.getPayload());


        String command3 = "search movies Atlantis";
        sc.processPayload(command3.split(" "));
        assertEquals("Atlantis" , sc.getPayload());

        String command4 = "search movies";
        sc.processPayload(command4.split(" "));
        assertEquals("" , sc.getPayload());

        String command5 = "search movies ";
        sc.processPayload(command5.split(" "));
        assertEquals("" , sc.getPayload());




    }
}
