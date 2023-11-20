package ExceptionsPackage;

public class MultipleExceptionsHandler {
	public void handleMultipleExceptions() {
        try {
            int[] numbers = {1, 2, 3};
            System.out.println(numbers[4]); // ArrayIndexOutOfBoundsException

            String str = null;
            System.out.println(str.length()); // NullPointerException

            int result = 10 / 0; // ArithmeticException
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException occurred: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException occurred: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("ArithmeticException occurred: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Some other exception occurred: " + e.getMessage());
        }
    }
}
