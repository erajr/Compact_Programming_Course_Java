package ExceptionsPackage;

public class ExceptionHandling {
	public static void main(String[] args) {

		//--------------------- Handling Multiple Exceptions -------------------
		System.out.println("Multiple Exceptions Handling starts");
		MultipleExceptionsHandler multipleExceptionsHandler = new MultipleExceptionsHandler();
		multipleExceptionsHandler.handleMultipleExceptions();
		System.out.println("Multiple Exceptions Handling ends");

		//--------------------- Rethrwoing Exceptions -------------------
		System.out.println("\n");
		System.out.println("Rethrwoing Exceptions Handling starts");
		RethrowingExceptions rethrowingExceptions = new RethrowingExceptions();
		try {
			rethrowingExceptions.divide();
		} catch (ArithmeticException e) {
			System.out.println("Rethrown Exception Caught in Main()");
			System.out.println(e);
		}
		System.out.println("Rethrwoing Exceptions Handling ends ");

		//--------------------- Resource Management -------------------
		System.out.println("\n");
		System.out.println("Resource Management Handling starts");
		System.out.println("Reading File");

		ResourceManagement resourceManagement = new ResourceManagement();
		resourceManagement.manageResources();
		System.out.println("\n");
		System.out.println("Resource Management Handling ends");

		//--------------------- Chaining Exceptions -------------------
		System.out.println("\n");
		System.out.println("Chaining Exceptions Handling starts: ");
		ChainingExceptions chainingExceptions = new ChainingExceptions();
		chainingExceptions.chainingException();
		System.out.println("\n");
		System.out.println("Chaining Exceptions Handling ends.");
	}
}
