package utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.peptide.AminoAcid;
import model.peptide.Atom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Friederike Hanssen, 01.2018
 */

public final class PDBParser {

    private static final List<List<AminoAcid>> chainList = new ArrayList<>();
    private static final Set<Integer> helix = new HashSet<>();
    private static final Set<Integer> sheet = new HashSet<>();
    private static final List<Atom> atomTemp = new ArrayList<>();
    private static final List<AminoAcid> chain = new ArrayList<>();
    public static final StringProperty header = new SimpleStringProperty();

    public static List<List<AminoAcid>> readFromFile(String inputFile) throws IOException {

        reset();

        char chainIDTracker = ' ';
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String line = br.readLine();
        while (line != null) {
            addHeader(line);
            chainIDTracker = handleAtom(line, chainIDTracker);
            handleHelix(line);
            handleSheet(line);
            line = br.readLine();
        }
        br.close();

        //add last amino acid
        addFinalAminoAcid();
        setSecStructure();

        return chainList;
    }

    public static List<List<AminoAcid>> readFromList(List<String> input) {

        reset();

        char chainIDTracker = ' ';
        for (String line : input) {
            addHeader(line);
            chainIDTracker = handleAtom(line, chainIDTracker);
            handleHelix(line);
            handleSheet(line);
        }

        //add last amino acid
        addFinalAminoAcid();
        setSecStructure();

        return chainList;
    }

    private static void addHeader(String line){
        if(line.startsWith("HEADER")){
            header.setValue(header.getValue().concat(line.replace("HEADER","").trim()));
        }

    }
    private static char handleAtom(String line, char chainIDTracker) {
        if (line.startsWith("ATOM")) {
            if (chainIDTracker != line.charAt(21) && chainIDTracker != ' ') {
                addFinalAminoAcid();
                atomTemp.clear();
            }
            int serial = Integer.parseInt(line.substring(6, 11).trim());
            String atom = line.substring(12, 16).trim();
            String resName = line.substring(17, 20).trim();
            char chainID = line.charAt(21);
            int resSeq = Integer.parseInt(line.substring(22, 26).trim());
            double x = Double.parseDouble(line.substring(30, 38).trim());
            double y = Double.parseDouble(line.substring(38, 46).trim());
            double z = Double.parseDouble(line.substring(46, 54).trim());
            double occupancy = Double.parseDouble(line.substring(54, 60).trim());
            String element = line.substring(76, 78).trim();
            if (atom.equals("N") && !atomTemp.isEmpty()) {
                AminoAcid am = new AminoAcid(atomTemp);
                chain.add(am);
                atomTemp.clear();
            }

            atomTemp.add(new Atom(serial, atom, resName, chainID, resSeq, x, y, z, occupancy, element));
            chainIDTracker = chainID;

        }
        return chainIDTracker;
    }

    private static void addFinalAminoAcid() {
        AminoAcid am = new AminoAcid(atomTemp);
        chain.add(am);
        chainList.add(new ArrayList<>(chain));
        chain.clear();
    }

    private static void reset(){
        chainList.clear();
        helix.clear();
        sheet.clear();
        chain.clear();
        atomTemp.clear();
        header.setValue(" ");
    }

    private static void handleHelix(String line) {
        if (line.startsWith("HELIX")) {
            Integer start = Integer.parseInt(line.substring(21, 25).trim());
            Integer stop = Integer.parseInt(line.substring(33, 37).trim());
            for (int i = start; i <= stop; i++) {
                helix.add(i);
            }
        }
    }

    private static void handleSheet(String line) {
        if (line.startsWith("SHEET")) {
            Integer start = Integer.parseInt(line.substring(22, 26).trim());
            Integer stop = Integer.parseInt(line.substring(33, 37).trim());
            for (int i = start; i <= stop; i++) {
                sheet.add(i);
            }
        }
    }

    private static void setSecStructure() {
        for (List<AminoAcid> currSeq : chainList) {
            for (AminoAcid a : currSeq) {
                if (helix.contains(a.getPosition())) {
                    a.setSecStructure('H');
                } else if (sheet.contains(a.getPosition())) {
                    a.setSecStructure('E');
                } else {
                    a.setSecStructure('-');
                }
            }
        }
    }


}
