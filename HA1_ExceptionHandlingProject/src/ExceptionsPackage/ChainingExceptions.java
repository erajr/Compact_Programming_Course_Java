package ExceptionsPackage;

import java.util.Scanner;

public class ChainingExceptions {

    public void chainingException() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter a number greater than 0: ");
            int num = input.nextInt();
            // int num = 0;
            if (num == 0) {
                throw new IllegalArgumentException("Error: Number must be non-zero.");
            } else if (num < 0) {
                throw new IllegalArgumentException("Error: Number is a negative number, it must be non-zero.");
            } else {
                System.out.print("The number is a non-zero number");
                // int result = 100 / num;
            }

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error: Invalid input", e);
        } 
        // catch (ArithmeticException e) {
        //     throw new RuntimeException("Error: division by zero", e);
        // }
    }

}
