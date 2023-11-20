package ExceptionsPackage;

public class RethrowingExceptions {
	 public void divide() 
     { 
         int x,y,z; 
         try 
         { 
            x = 6 ; 
            y = 0 ; 
            z = x/y ; 
            System.out.println(x + "/"+ y +" = " + z); 
          } 
          catch(ArithmeticException e) 
          { 
           System.out.println("Exception Caught in Divide()"); 
           System.out.println("Cannot Divide by Zero in Integer Division"); 
           throw e; 
          } 
     }
}
