package lab;


import java.io.*;
import java.util.HashSet;

	public class Lab2q {
	    //task1
	    static int keywordnum = 0;
	    //task2
	    /*the length of the array = the number of switch,
	      the element of the array i= the number of case, 
	    */
	    static int switchnum = 0;
	    static int [] casenum = new int[500];
	    //task3 and task4
	    
	    static boolean commentflag = false;//comments flag
	    static boolean stringFlag = false;
	    static int [] ifelseStack = new int [5000];
	    static int stackLen = 0;
	    static int elseifnum = 0;
	    static int elsenum = 0;
	   
	    //hash table begin
	    public static void InitKeyword(HashSet<String> list){
	        list.add("auto");
	        list.add("break");
	        list.add("char");
	        list.add("const");
	        list.add("continue");
	        list.add("default");
	        list.add("do");
	        list.add("double");
	        list.add("enum");
	        list.add("extern");
	        list.add("float");
	        list.add("for");
	        list.add("goto");
	        list.add("int");
	        list.add("long");
	        list.add("register");
	        list.add("return");
	        list.add("short");
	        list.add("signed");
	        list.add("sizeof");
	        list.add("static");
	        list.add("struct");
	        list.add("typedef");
	        list.add("union");
	        list.add("unsigned");
	        list.add("void");
	        list.add("volatile");
	        list.add("while");
	    }
	  
	  
	    public static boolean IsChar(char c){
	        return c >= 'a' && c <= 'z';
	    }
	   
	    //stack about curly braces and semicolon
	    public static void Stack(){
	    	 int [] currentStack = new int [4000];
	         int pre=0;
	         for(int i = 1 ; i <= stackLen ; i++){
	             switch (ifelseStack[i]){
	                 case 3:
	                     if(currentStack[pre-1] == 1){
	                         // if a "else if" before this else, change the topping "if;" to ";"
	                         pre -= 2;
	                         if(currentStack[pre] != 0){
	                             currentStack[++pre] = 0;
	                         }
	                         elsenum++;
	                     }else if(currentStack[pre-1] == 2){
	                        
	                         pre -= 4;
	                         if(currentStack[pre] != 0){
	                             currentStack[++pre] = 0;
	                         }
	                         elseifnum++;
	                     }
	                 case 2:
	                   
	                     if(currentStack[pre] != 0 || currentStack[pre-1] != 2){
	                         currentStack[++pre] = 2;
	                     }
	                     break;
	                 case 9:
	                     // {}  => ;
	                     if(currentStack[pre] == 6){
	                         if(currentStack[pre-1] != 0){
	                             currentStack[pre] = 0;
	                         }else{
	                             pre--;
	                         }
	                     // {;} => ;
	                     }else if(currentStack[pre] == 0 && currentStack[pre-1] == 6){
	                         if(currentStack[pre-2] != 0){
	                             currentStack[pre-1] = 0;
	                         }else{
	                             pre -= 2;
	                         }
	                     }
	                     break;
	                 case 0:
	                     // won't stack in if the top is already ";"
	                     if(currentStack[pre] != 0){
	                         currentStack[++pre] = 0;
	                     }
	                     break;
	                 case 1:
	                     currentStack[++pre] = ifelseStack[i];
	                     break;
	                 case 6:
	                     currentStack[++pre] = ifelseStack[i];
	                     break;
	             }
	         }
	     }
	    // delete one line comment
	    public static String DeleteSingleLineComment(String str){
	        for(int i = 0; i < str.length() ;i++){
	            if(str.charAt(i) == '/' && str.charAt(i) == '/'){
	                return str.substring(0,i);
	            }
	        }
	        return str;
	    }
	    // mark a annotation flag if "/*" appears, delete it until find "*/"
	    public static String DeleteMultiLineComment(String str){
	        StringBuilder sb = new StringBuilder(200);
	        for(int i = 0; i < str.length() ;i++){
	            if(!commentflag){
	                if(str.charAt(i) == '/' && str.charAt(i+1) == '*'){
	                    commentflag = true;
	                }else{
	                    sb.append(str.charAt(i));
	                }
	            }else{
	                if(str.charAt(i) == '*' && str.charAt(i+1) == '/'){
	                    i++;
	                    commentflag = false;
	                }
	            }
	        }
	        return sb.toString();
	    }
	    //delete string
	    public static String DeleteInsideString(String str){
	        StringBuilder sb = new StringBuilder(200);
	        for(int i = 0; i < str.length() ;i++){
	            if(!stringFlag){
	                if(str.charAt(i) == '"'){
	                    stringFlag = true;
	                }else{
	                    sb.append(str.charAt(i));
	                }
	            }else{

	                if(str.charAt(i) == '"'){
	                    stringFlag = false;
	                }
	            }
	        }
	        return sb.toString();
	    }
	    // traverse every line
	    public static void Traverse(String line, HashSet <String> keywords){
	        // delete useless content
	        line = DeleteSingleLineComment(line);
	        line = DeleteInsideString(line);
	        line = DeleteMultiLineComment(line);
	        int lineLen = line.length();
	        int head = 0;
	        int end = 0;
	        boolean contain = false;
	        for (int i = 0; i < lineLen; i++){
	            if(IsChar(line.charAt(i))){
	                if(!contain){
	                    head = i;
	                    contain = true;
	                }
	            }else{
	                if(contain){
	                    end = i;
	                    String currentWord = line.substring(head, end);
	                    switch (currentWord){
	                        case "if":
	                            keywordnum++;
	                            ifelseStack[++stackLen] = 1;
	                            i = end;
	                            break;
	                        case "else":
	                            if (end+3 <= line.length() &&line.substring(head, end + 3).equals("else if")) {
	                                keywordnum +=2;
	                                ifelseStack[++stackLen] = 2;
	                                i = end+3;
	                            } else {
	                                keywordnum++;
	                                ifelseStack[++stackLen] = 3;
	                                i = end;
	                            }
	                            break;
	                        case "switch":
	                            keywordnum++;
	                            switchnum++;
	                            i = end;
	                            break;
	                        case "case":
	                            keywordnum++;
	                            casenum[switchnum]++;
	                            i = end;
	                            break;
	                        default:
	                            for(String str:keywords){
	                                if(currentWord.equals(str)){
	                                    keywordnum++;
	                                    break;
	                                }
	                            }
	                            i = end;
	                    }
	                    contain = false;
	                }
	            }
	            switch (line.charAt(i)) {
	                case '{':
	                    ifelseStack[++stackLen] = 6;
	                    break;
	                case '}':
	                    ifelseStack[++stackLen] = 9;
	                    break;
	                case ';':
	                    if (ifelseStack[stackLen] != 0) {
	                        ifelseStack[++stackLen] = 0;
	                    }
	                    break;
	            }
	        }
	    }
	    public static void main(String[] args) throws IOException {
	        HashSet <String> keywords = new HashSet<String>();
	        InitKeyword(keywords);
	        BufferedReader bufIn = new BufferedReader(new InputStreamReader(System.in));
	        String path;
	        System.out.println("Enter the path of testing file:");
	        path = bufIn.readLine();
	        int level;
	        System.out.println("Enter the level of requirement (from 1 to 4):");
	        level = bufIn.read()-'0';
	        while(level != 1 && level != 2 && level != 3 && level != 4){
	            System.out.println("Error, please enter again:");
	            level = bufIn.read();
	        }
	        // read line by line from ordered file
	        FileReader fin = new FileReader(path);
	        BufferedReader bufferedReader = new BufferedReader(fin);
	        String line = bufferedReader.readLine();
	        while(line != null){
	            Traverse(line,keywords);
	            line = bufferedReader.readLine();
	        }
	        bufferedReader.close();
	        fin.close();
	        // task1
	        System.out.println("total num:"+keywordnum);
	        // task2
	        if(level >= 2){
	            System.out.println("switch num:"+switchnum);
	            System.out.print("case num:");
	            for(int i = 1; i <= switchnum; i++){
	                System.out.print(casenum[i]+" ");
	            }
	        }
	        // task3
	        if(level >= 3){
	            Stack();
	            System.out.println("\nif-else num:"+elsenum);
	        }
	        //task4
	        if(level == 4){
	            System.out.println("if-elseif-else num:"+elseifnum);
	        }
	    }
	}
