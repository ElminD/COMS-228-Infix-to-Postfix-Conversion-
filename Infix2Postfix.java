import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class Infix2Postfix {

    /**
     * Infix-to-Postfix Conversion
     * @Author Elmin Didic
     */


    public static void main(String[] args) {


        //creates output file with the input file converted
        try {
            String output = fileReader("input.txt");
            try {
                FileWriter writer = new FileWriter("output.txt");
                writer.write(output);
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e){
            System.out.println("no output");
        }
    }

    /**
     * reads a file line by line and on each line calls the converter class
     * and then adds that output to the output string and a new line to later
     * be added to the output file
     * @param fileName
     * @return
     */
    static String fileReader(String fileName) throws FileNotFoundException {

        try {
            String postfix = "";
            String output = "";
            File file = new File(fileName);
            Scanner scan = new Scanner(file);

            while(scan.hasNextLine())
            {
                postfix = converter(scan.nextLine());

                output += postfix + "\n";

            }


            scan.close();
            return output;
        }
        catch(FileNotFoundException e)
        {
            throw new FileNotFoundException();
        }

    }

    /**
     * returns the precedence of a given op
     * @param op
     * @return
     */
    static int Precedence(String op, boolean fromStack)
    {
        if(fromStack == false) {
            if (op.equals("+") || op.equals("-")) {
                return 1;
            }
            if (op.equals("*") || op.equals("/") || op.equals("%")) {
                return 2;
            }
            if (op.equals("^")) {
                return 4;
            }
        }
        else
        {
            if (op.equals("+") || op.equals("-")) {
                return 1;
            }
            if (op.equals("*") || op.equals("/") || op.equals("%")) {
                return 2;
            }
            if (op.equals("^")) {
                return 3;
            }
        }

        return 0;
    }

    /**
     * converts a string of infix and makes it into postfix
     * using a stack
     * @param infix
     * @return
     */
    static String converter(String infix)
    {

        Stack<String> stack = new Stack<>();
        String postfix = "";
        int Pcount = 0;
        int Scount = 0;
        int Rank = 0;
        String sub = "";
        Scanner scan = new Scanner(infix);

        while(scan.hasNext())
        {
            if(scan.hasNextInt())
            {
                int operand = scan.nextInt();
                postfix += operand + " ";
                Scount += 1;
                Rank += 1;
                sub += operand;

                if(Rank > 1)
                {
                    postfix = "Error: too many operands (" + operand + ")";
                    stack.clear();
                    break;
                }

            }
            else
            {

                String op = scan.next();
                if(op.equals("("))
                {
                    Rank = 0;
                    Scount = 0;
                    Pcount += 1;
                    sub = "";
                    stack.push(op);
                }
                else if(op.equals(")"))
                {
                    Pcount -= 1;
                    if(Pcount < 0)
                    {
                        postfix = "Error: no opening parenthesis detected";
                        stack.clear();
                        break;
                    }
                    while(!stack.isEmpty() && !(stack.peek().equals("(")))
                    {
                        postfix += stack.pop() + " ";
                    }

                         stack.pop();

                    if(Scount == 0)
                    {
                        postfix = "Error: no subexpression detected ()";
                        stack.clear();
                        break;
                    }
                    else if(Scount == 1)
                    {
                        postfix = "Error: too many operands (" + sub.trim() + ")";
                        stack.clear();
                        break;
                    }

                }
                else
                {
                    Rank -= 1;


                    while(!stack.isEmpty() &&  Precedence(op, false) <= Precedence(stack.peek(), true))
                    {
                        postfix += stack.pop() + " ";

                    }
                    stack.push(op);

                    if(Rank != 0 || (Rank == 0 && !scan.hasNext()))
                    {
                        postfix = "Error: too many operators (" + op + ")";
                        stack.clear();
                        Pcount = 0;
                        break;
                    }
                }

            }

        }

        while(!stack.isEmpty()) {
            postfix += stack.pop() + " ";
        }

        postfix = postfix.trim();


        if(Pcount != 0)
        {

            if(Pcount > 0)
            {
                postfix = "Error: no closing parenthesis detected";
                stack.clear();
            }
        }
        return postfix;




    }



}
