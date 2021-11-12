import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Math;

public class driver {

/**
 * 1. Develop an algorithm that given preferences for N programmers and N companies will find satisfactory pairings
 * 2. Implement your algorithm and test it on several cases of preferences (Write a method to do this isSatisfactory )
 * 3. Explain why algorithm is correct (ie meaning that your program stops and outputs a satisfactory pairing)
 * 4. Find the efficiency of your algorithm in the worst case
 * @throws IOException
 * @throws FileNotFoundException
 */

// Possibly use a while loop that once it is complete would call isSatifactory or matchSatisfactory

// Write method to determine if our matches are satifactory or not
//General idea---------------------
/**
 * Each programmer will ask their top prefrence to work for them
 *      The company will then respond with maybe or no:
 *              maybe if the canidate is sutible 
 *              no if the canidate is at the bottom of their prefrences
 *       If a canidate ask to work for a company that has already responded with maybe. one of the following to outcomes will occur:
 *          1. The company says maybe to them if the new worker appears higher on their list
 *          2. the company says no if they appear lower
 */
public static void main(String[] args0) throws FileNotFoundException{
    String fileName = "";
    if(args0.length >0){
        fileName = args0[0];
    }
    ArrayList unfiltered = readFile(fileName);
    ArrayList<String> companies = new  ArrayList<>();
    ArrayList<String> programmers = new  ArrayList<>();
    ArrayList<String> noAnswers;
    filterCompanies(unfiltered, companies);
    filterWorkers(unfiltered, programmers);
    int amount = findAmount(programmers);

    noAnswers = getNos(companies);
   /* System.out.println(unfiltered);
    System.out.println(companies);
    System.out.println(programmers);
    System.out.println(noAnswers);*/
    /// We now have the company prefrences in an array and we have the programmer prefrences in another array
    //we also have the answers of no from companies that do not want a specific client
    char [] programmer = new char[amount];
    for(int i = 0; i < amount;i++){
        programmer[i] = programmers.get(i).charAt(0);
    }
    char [] company = new char[amount];
    for(int i = 0; i < amount;i++){
        company[i] = companies.get(i).charAt(0);

    }
    String [] finalOffers = findOffers(companies, programmers, noAnswers, programmer, company);
    System.out.println("Final Offers!!!!!!");

    for(int i = 0; i < amount;i++){
        System.out.println(finalOffers[i]);
    }
}

private static ArrayList<String> readFile(String fileName) throws FileNotFoundException{
    ArrayList<String> list = new ArrayList<>();
    File file = new File(fileName);
    Scanner scan = new Scanner(file);
    while(scan.hasNext())
    {
        list.add(scan.next());
    }
    scan.close();
    return list;
}
private static void filterCompanies(ArrayList<String> unfiltered, ArrayList<String> companies){
    for(int i = 0; i < unfiltered.size();i++){
        char test = unfiltered.get(i).charAt(0);
        if(test > 64 && test <91){ //Ascii values for uppercase letters
            companies.add(unfiltered.get(i));
        }
    }
    
}
private static void filterWorkers(ArrayList<String> unfiltered, ArrayList<String> programmers){
    for(int i = 0; i < unfiltered.size();i++){
        char test = unfiltered.get(i).charAt(0);
        if(test > 47 && test <58){ //Ascii values for numbers
            programmers.add(unfiltered.get(i));
        }
    }
}
private static ArrayList<String> getNos(ArrayList<String> companies){
    ArrayList<String> nos = new ArrayList<>();
    int indexToEndAt = companies.size() - findAmount(companies);
    for(int i = companies.size()-1;i >= indexToEndAt; i--){
        nos.add(companies.get(i));
    }
    return nos;
}
private static int findAmount(ArrayList<String> list){// each prefrence list will be the length squared
    
    return (int) Math.sqrt((double)list.size());/// this is  a hideous line, but there shouldnt be any problems with the assumption that each list will be a perfect sqaure
}

///------------------------------------------------------------------------------------------------------------

private static String [] findOffers(ArrayList<String> c, ArrayList<String> p, ArrayList<String> no, char [] prog, char [] company){
    String [] offers  = new String[findAmount(c)];
for(int i = 0; i <p.size(); i++){
    boolean toSkip = false;
    if(offersFull(offers)){
        return offers;
    }
    String toTest = flipString(p.get(i));
    for(int j = 0; j< offers.length;j++){
        if(offers[j] != null){
        if(toTest.charAt(1) == offers[j].charAt(1) && !no.contains(toTest)){ // programmer has already taken an offer
            System.out.println("Exsisting offer for programmer found");
            int indexOfExisiting= p.indexOf(flipString(offers[j]));
            int index = findbestPossible(flipString(toTest), p) ; /// need toTest to be in the programmers format
            int cExsiting = c.indexOf(offers[j]);
            int cIndex = findbestPossible(toTest, c);
            if(index < indexOfExisiting && cIndex < cExsiting){
                offers[j] = null;

                int indexToAddinto = (int)toTest.charAt(0) - 64 - 1;
                offers[indexToAddinto] = p.get(index);
            }
        }
    }
}
    if(no.contains(toTest)){
        System.out.println("No found");
        continue;
        }
    if(offerContainsTest(toTest, offers)){
        System.out.println("Contains test: here");
     int index = findbestPossible(toTest, c); 
     int pIndex = findbestPossible(flipString(toTest),p);
     int pExsiting = p.indexOf(flipString(toTest));
    checkCompanyPrefrence(c,p, offers, toTest, index, pIndex, pExsiting);
    }
    else{
        System.out.println("Offers did not contain test: " + toTest);
        int index = findbestPossible(toTest, c) ; 
        int pIndex = findbestPossible(flipString(toTest),p);
        int pExsiting = p.indexOf(flipString(toTest));

        int indexToAddinto = (int)toTest.charAt(0) - 64 - 1;
        if(pIndex< pExsiting){
         offers[indexToAddinto] = c.get(index);
        }
    }
   }
return offers;
}

private static void checkCompanyPrefrence(ArrayList<String> c, ArrayList<String> p, String [] offers, String toTest, int index, int pIndex, int pExsiting) {
    for(int j = 0; j < offers.length;j++){
           if(offers[j] == null){
               continue;
           }
           if(offers[j].charAt(0) == toTest.charAt(0)){// indicates a company is taken
              int indexOfExisiting= c.indexOf(offers[j]);
              if(index < indexOfExisiting && pIndex < pExsiting){// do not need to change programmer if programmer does not desire change
                offers[j] = c.get(index);
              }
        }
    }
}
private static int findbestPossible(String test, ArrayList<String> companies){
    for(int i = 0; i < companies.size(); i++){
        if(companies.get(i).equals(test)){
            System.out.println("Best offer is: " + companies.get(i));
            return i;
        }
    }
    return -1;// indicates something went incredibly wrong. I dont think this is possible

}
private static String flipString(String str){
    String strToReturn = "";
    for(int i = str.length()-1;i>=0;i--){
        strToReturn += str.charAt(i);
    }
    return strToReturn;
}
private static boolean offersFull(String[] offers){
    for(int i = 0; i < offers.length; i++){
        if(offers[i] == null)
            return false;
    }
    return true;
}
private static boolean offerContainsTest(String test, String [] offers){
    boolean result = false;
    for(int i = 0; i < offers.length;i++)
    {
        if(offers[i] != null){
            if(offers[i].charAt(1) == test.charAt(1)){
                return true;
            }
        }
    }
    return result;
}
}